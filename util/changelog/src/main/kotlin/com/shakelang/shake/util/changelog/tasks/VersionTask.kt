package com.shakelang.shake.util.changelog.tasks

import com.shakelang.shake.util.changelog.*
import org.gradle.api.DefaultTask
import org.gradle.api.Project

open class VersionTask : DefaultTask() {
    init {
        group = "changelog"
        description = "Prints the current version"
        this.dependsOn("initChangelog")
    }

    private var tagFormat: TagCreation = {
        "${it.project.name}@${it.version}"
    }

    fun tagFormat(tagFormat: TagCreation) {
        this.tagFormat = tagFormat
    }

    @org.gradle.api.tasks.TaskAction
    open fun version() {
        var typeValue: String = project.findProperty("type")?.toString() ?: "SNAPSHOT"
        if (typeValue == "RELEASE") typeValue = ""

        applyVersion(typeValue)
    }

    open fun applyVersion(type: String) {
        val structureFile = Changelog.instance.readStructureFile()
        val bumpFile = Changelog.instance.readBumpFile()
        val mapFile = Changelog.instance.readMap()
        val stash = Changelog.instance.readStash()

        val changelog = structureFile.projects.associate { it.path to mutableListOf<String>() }
        val bumpTypes = mutableMapOf<String, BumpType>()

        println("Updating dependencies...")

        val packagesWithUpdatedDependencies = mutableSetOf<Project>()

        val bumpedPackages = bumpFile.bumpedPaths.map {
            val cache = VersionBumpCache()
            cache.projectStructure = structureFile.projects.find { project -> project.path == it }!!
            cache.project = project.project(it)
            cache.autoGenerated = false
            cache
        }.toMutableList()

        var i = 0
        while (i < bumpedPackages.size) {
            val cache = bumpedPackages[i]
            val project = cache.project
            val dependents = project.allDependents
            val newVersion = bumpFile.calculateNewVersion(project.path, cache.autoGenerated)
            cache.version = newVersion
            val bump = Bump(
                BumpType.PATCH,
                "Update ${project.group}:${project.name} to $newVersion",
                dependents.map { it.path }
            )
            bumpFile.add(bump)
            dependents.forEach { dependent ->
                if (!bumpedPackages.any { it.project.path == dependent.path }) {
                    println("Noticed changed dependency for ${dependent.path} (bumping patch)")
                    val newCache = VersionBumpCache()
                    newCache.projectStructure =
                        structureFile.projects.find { project -> project.path == dependent.path }!!
                    newCache.project = project.project(dependent.path)
                    newCache.autoGenerated = true
                    bumpedPackages.add(newCache)
                }
            }
            i++
        }

        // Add new versions to changelog

        val tagFormat = this.tagFormat

        bumpedPackages.forEach { cache ->
            val project = cache.project
            val projectStructure = cache.projectStructure
            val version = cache.version
            val autoGenerated = cache.autoGenerated

            val messages = cache.getChangelogEntries(bumpFile)
            if (messages.isEmpty()) return@forEach
            if (project.private) {
                project.logger.warn("Skipping version bump for private project ${project.path}")
                return@forEach
            }
            println("Bumping ${project.path} to $version")
            val entry = ChangelogVersion(
                version,
                messages
            )

            var it = mapFile.packages.find { it.path == project.path }
            if (it == null) {
                it = PackageChangelog(
                    project.path,
                    project.name,
                    description ?: ""
                )
                mapFile.add(it)
            }

            it.add(entry)

            val tagName = tagFormat(TagCreationInfo(projectStructure, version, messages.joinToString("\n")))
            stash.add(TagStash(tagName))

            projectStructure.version = version
        }

        bumpFile.bumps.clear()

        // save files
        Changelog.instance.writeBumpFile(bumpFile)
        Changelog.instance.writeMap(mapFile)
        Changelog.instance.writeStructure(structureFile)
        Changelog.instance.writeStash(stash)

        // render new changelog files
        Changelog.instance.renderChangelog(mapFile)
    }

    class VersionBumpCache {
        lateinit var version: Version
        lateinit var project: Project
        lateinit var projectStructure: ProjectStructure
        var autoGenerated: Boolean = true

        fun getChangelogEntries(bumpFile: BumpFile): List<String> {
            return bumpFile.bumps.filter { bump ->
                bump.paths.contains(project.path)
            }.map { it.message }
        }
    }
}

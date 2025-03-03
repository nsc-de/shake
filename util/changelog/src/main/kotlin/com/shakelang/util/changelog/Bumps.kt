package com.shakelang.util.changelog

import com.shakelang.util.shason.elements.JsonObject
import com.shakelang.util.shason.json
import org.gradle.api.Project
import kotlin.math.max

enum class BumpType {
    MAJOR,
    MINOR,
    PATCH,
    ;

    val type: String get() = name.toLowerCase()

    fun toInt() = when (this) {
        MAJOR -> 2
        MINOR -> 1
        PATCH -> 0
    }

    companion object {
        fun fromString(string: String): BumpType {
            if (string == "major") return MAJOR
            if (string == "minor") return MINOR
            if (string == "patch") return PATCH
            throw IllegalArgumentException("BumpType is not valid")
        }

        fun fromInt(int: Int): BumpType {
            if (int == 2) return MAJOR
            if (int == 1) return MINOR
            if (int == 0) return PATCH
            throw IllegalArgumentException("BumpType is not valid")
        }
    }
}

class Bump(
    val type: BumpType,
    val message: String,
    val paths: List<String>,
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "type" to type.type,
            "message" to message,
            "paths" to paths,
        )
    }

    companion object {
        fun fromObject(obj: JsonObject): Bump {
            if (!obj.containsKey("type")) throw IllegalArgumentException("Bump object has no type")
            if (!obj.containsKey("message")) throw IllegalArgumentException("Bump object has no message")
            if (!obj.containsKey("paths")) throw IllegalArgumentException("Bump object has no paths")

            val typeElement = obj["type"]!!
            val messageElement = obj["message"]!!
            val pathsElement = obj["paths"]!!

            if (!typeElement.isJsonPrimitive() || !typeElement.toJsonPrimitive()
                    .isString()
            ) {
                throw IllegalArgumentException("Bump type is not a string")
            }

            if (!messageElement.isJsonPrimitive() || !messageElement.toJsonPrimitive()
                    .isString()
            ) {
                throw IllegalArgumentException("Bump message is not a string")
            }

            if (!pathsElement.isJsonArray()) throw IllegalArgumentException("Bump paths is not an array")

            val type = typeElement.toJsonPrimitive().toStringElement().value
            val message = messageElement.toJsonPrimitive().toStringElement().value
            val paths = pathsElement.toJsonArray().map { it.toJsonPrimitive().toStringElement().value }

            if (type == "major") return Bump(BumpType.MAJOR, message, paths)
            if (type == "minor") return Bump(BumpType.MINOR, message, paths)
            if (type == "patch") return Bump(BumpType.PATCH, message, paths)

            throw IllegalArgumentException("Bump type is not valid")
        }
    }
}

class BumpFile(
    val bumps: MutableList<Bump>,
) {
    fun toObject(): Map<String, Any> {
        return mapOf(
            "bumps" to bumps.map { it.toObject() },
        )
    }

    fun calculateUpdateTypeForPackage(path: String): BumpType {
        var bumpType = BumpType.PATCH
        bumps.forEach { bump ->
            if (bump.paths.contains(path)) {
                bumpType = BumpType.fromInt(max(bumpType.toInt(), bump.type.toInt()))
            }
        }
        return bumpType
    }

    fun calculateNewVersion(project: Project, autoGenerated: Boolean = true): Version {
        val bumpType = calculateUpdateTypeForPackage(project.path)
        val version = Version.fromString(project.version.toString())

        when (bumpType) {
            BumpType.MAJOR -> version.incrementMajor()
            BumpType.MINOR -> version.incrementMinor()
            BumpType.PATCH -> version.incrementPatch()
        }

        if (autoGenerated) {
            val latestVersion =
                Changelog.instance.readMap().packages.find { it.path == project.path }?.versions?.firstOrNull()
            if (latestVersion != null) version.suffix = latestVersion.version.suffix
        }

        return version
    }

    fun calculateNewVersion(path: String, autoGenerated: Boolean = true) =
        calculateNewVersion(Changelog.instance.project.project(path), autoGenerated)

    val bumpedPaths: List<String>
        get() {
            val packages = mutableSetOf<String>()
            bumps.forEach { bump ->
                bump.paths.forEach { path ->
                    if (path !in packages) packages.add(path)
                }
            }
            return packages.toList()
        }

    fun addBump(bump: Bump) = bumps.add(bump)
    fun addBump(type: BumpType, message: String, paths: List<String>) = addBump(Bump(type, message, paths))
    fun addBump(type: BumpType, message: String, vararg paths: String) = addBump(Bump(type, message, paths.toList()))

    fun add(bump: Bump) = addBump(bump)
    fun add(type: BumpType, message: String, paths: List<String>) = addBump(type, message, paths)
    fun add(type: BumpType, message: String, vararg paths: String) = addBump(type, message, paths.toList())

    companion object {
        fun fromObject(obj: JsonObject): BumpFile {
            if (!obj.containsKey("bumps")) throw IllegalArgumentException("BumpFile object has no bumps")

            val bumpsElement = obj["bumps"]!!

            if (!bumpsElement.isJsonArray()) throw IllegalArgumentException("BumpFile bumps is not an array")

            val bumps = bumpsElement.toJsonArray().map { Bump.fromObject(it.toJsonObject()) }

            return BumpFile(bumps.toMutableList())
        }

        fun fromString(string: String): BumpFile {
            val parsed = json.parse(string)
            if (!parsed.isJsonObject()) throw IllegalArgumentException("BumpFile is not a json object")
            return fromObject(parsed.toJsonObject())
        }

        fun empty(): BumpFile {
            return BumpFile(mutableListOf())
        }
    }
}

fun Changelog.readBumpFile(): BumpFile {
    val file = project.file(".changelog/bumps.json")
    if (!file.exists()) return BumpFile.empty()
    return BumpFile.fromString(file.readText())
}

fun Changelog.writeBumpFile(bumpFile: BumpFile) {
    val file = project.file(".changelog/bumps.json")
    file.writeText(json.stringify(bumpFile.toObject(), true))
}

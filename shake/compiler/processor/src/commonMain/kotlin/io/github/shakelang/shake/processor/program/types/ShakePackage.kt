package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope

interface ShakePackage {
    val baseProject: ShakeProject
    val name: String
    val parent: ShakePackage?
    val subpackages: List<ShakePackage>
    val classes: List<ShakeClass>
    val functions: List<ShakeFunction>
    val fields: List<ShakeField>
    val qualifiedName: String
    val scope: ShakeScope
    fun getPackage(name: String): ShakePackage
    fun getPackage(name: Array<String>): ShakePackage
    fun toJson(): Map<String, Any?>

    class Impl : ShakePackage {
        override val baseProject: ShakeProject
        override val name: String
        override val parent: ShakePackage?
        override val subpackages: List<ShakePackage>
        override val classes: List<ShakeClass>
        override val functions: List<ShakeFunction>
        override val fields: List<ShakeField>

        override val qualifiedName: String get() = if (parent == null) name else "${parent.qualifiedName}.$name"
        override val scope: ShakeScope = PackageScope()

        constructor(
            baseProject: ShakeProject,
            name: String,
            parent: ShakePackage?,
            subpackages: List<ShakePackage>,
            classes: List<ShakeClass>,
            functions: List<ShakeFunction>,
            fields: List<ShakeField>
        ) {
            this.baseProject = baseProject
            this.name = name
            this.parent = parent
            this.subpackages = subpackages
            this.classes = classes
            this.functions = functions
            this.fields = fields
        }

        internal constructor(
            baseProject: ShakeProject.Impl,
            parent: Impl?,
            it: ShakePackage
        ) {
            this.baseProject = baseProject
            this.name = it.name
            this.parent = parent
            this.subpackages = it.subpackages.map { from(baseProject, this, it) }
            this.classes = it.classes.map { ShakeClass.from(baseProject, this, it) }
            this.functions = it.functions.map { ShakeFunction.from(baseProject, this, it) }
            this.fields = it.fields.map { ShakeField.from(baseProject, this, it) }
        }

        override fun getPackage(name: String): ShakePackage {
            if(name.contains(".")) return getPackage(name.split(".").toTypedArray())
            return subpackages.find { it.name == name } ?: throw Error("Package $name not found")
        }

        override fun getPackage(name: Array<String>): ShakePackage {
            if(name.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            return getPackage(name.first()).getPackage(name.drop(1).toTypedArray())
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "name" to name,
                "subpackages" to subpackages.map { it.name },
                "classes" to classes.map { it.name },
                "functions" to functions.map { it.name },
                "fields" to fields.map { it.name }
            )
        }

        inner class PackageScope: ShakeScope {
            override val parent: ShakeScope get() = baseProject.projectScope

            override fun get(name: String): ShakeAssignable? {
                return fields.find { it.name == name }
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return functions.filter { it.name == name }
            }

            override fun getClass(name: String): ShakeClass? {
                return classes.find { it.name == name }
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return functions.filter { it.name == name }
            }
        }
    }

    companion object {
        fun from(project: ShakeProject.Impl, parent: Impl?, it: ShakePackage): ShakePackage = Impl(project, parent, it)
    }
}
package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.parseutils.Promise
import io.github.shakelang.shake.parser.node.ShakeIdentifierNode
import io.github.shakelang.shake.parser.node.ShakeValuedNode
import io.github.shakelang.shake.parser.node.ShakeVariableType
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shason.json

interface ShakeProject {

    val subpackages: List<ShakePackage>
    val classes: List<ShakeClass>
    val functions: List<ShakeFunction>
    val fields: List<ShakeField>

    val projectScope: ShakeScope

    fun getPackage(name: String): ShakePackage
    fun getPackage(name: Array<String>): ShakePackage
    fun getClass(pkg: Array<String>, name: String): ShakeClass?
    fun getClass(clz: String): ShakeClass?
    fun toJson(): Map<String, Any?>
    fun toJsonString(format: Boolean = false): String

    class Impl : ShakeProject {

        private val classRequirements = mutableListOf<ClassRequirement>()
        private val packageRequirements = mutableListOf<PackageRequirement>()

        override val subpackages: List<ShakePackage>
        override val classes: List<ShakeClass>
        override val functions: List<ShakeFunction>
        override val fields: List<ShakeField>
        override val projectScope: ShakeScope = ProjectScope()

        constructor(
            subpackages: List<ShakePackage>,
            classes: List<ShakeClass>,
            functions: List<ShakeFunction>,
            fields: List<ShakeField>
        ) {
            this.subpackages = subpackages
            this.classes = classes
            this.functions = functions
            this.fields = fields
        }

        internal constructor(
            it: ShakeProject
        ) {
            this.subpackages = it.subpackages.map { ShakePackage.from(this, null, it) }
            this.classes = it.classes.map { ShakeClass.from(this, null, it) }
            this.functions = it.functions.map { ShakeFunction.from(this, null, it) }
            this.fields = it.fields.map { ShakeField.from(this, null, it) }
        }

        fun expectPackage(pkg: String) = Promise<ShakePackage> { resolve, _ -> expectPackage(pkg) { resolve(it) } }
        fun expectClass(clazz: String) = Promise<ShakeClass> { resolve, _ -> expectClass(clazz) { resolve(it) } }


        override fun getPackage(name: String): ShakePackage {
            if(name.contains(".")) return getPackage(name.split(".").toTypedArray())
            return subpackages.find { it.name == name } ?: throw Error("Package $name not found")
        }

        override fun getPackage(name: Array<String>): ShakePackage {
            if(name.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            return getPackage(name.first()).getPackage(name.drop(1).toTypedArray())
        }

        override fun getClass(pkg: Array<String>, name: String): ShakeClass? {
            return this.getPackage(pkg).classes.find { it.name == name }
        }

        override fun getClass(clz: String): ShakeClass? {
            val parts = clz.split(".")
            val name = parts.last()
            val pkg = parts.dropLast(1).toTypedArray()
            return if(pkg.isEmpty()) this.classes.find { it.name == name }
            else this.getPackage(pkg).classes.find { it.name == name }
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "classes" to classes.map { it.toJson() },
                "functions" to functions.map { it.toJson() },
                "fields" to fields.map { it.toJson() },
                "subpackages" to subpackages.map { it.toJson() }
            )
        }

        override fun toJsonString(format: Boolean): String {
            return json.stringify(toJson(), format)
        }

        fun expectPackage(name: String, then: (ShakePackage) -> Unit) {
            this.packageRequirements.add(PackageRequirement(name, then))
        }

        fun expectClass(name: String, then: (ShakeClass) -> Unit) {
            this.classRequirements.add(ClassRequirement(name, then))
        }

        fun getType(type: ShakeVariableType, then: (ShakeType) -> Unit) {
            when (type.type) {
                ShakeVariableType.Type.BYTE -> then(ShakeType.Primitive.BYTE)
                ShakeVariableType.Type.SHORT -> then(ShakeType.Primitive.SHORT)
                ShakeVariableType.Type.INTEGER -> then(ShakeType.Primitive.INT)
                ShakeVariableType.Type.LONG -> then(ShakeType.Primitive.LONG)
                ShakeVariableType.Type.FLOAT -> then(ShakeType.Primitive.FLOAT)
                ShakeVariableType.Type.DOUBLE -> then(ShakeType.Primitive.DOUBLE)
                ShakeVariableType.Type.BOOLEAN -> then(ShakeType.Primitive.BOOLEAN)
                ShakeVariableType.Type.CHAR -> then(ShakeType.Primitive.CHAR)
                ShakeVariableType.Type.OBJECT -> {
                    val clz = mutableListOf<String>()
                    var identifier: ShakeValuedNode? = type.subtype!!
                    while(identifier != null) {
                        if(identifier !is ShakeIdentifierNode) throw IllegalArgumentException("Invalid type ${type.subtype}")
                        clz.add(identifier.name)
                        identifier = identifier.parent
                    }
                    val clzName = clz.reversed().joinToString(".")
                    this.expectClass(clzName) {
                        then(ShakeType.objectType(it))
                    }
                }
            }
        }

        fun finish() {

            classRequirements.forEach {
                val cls = this.getClass(it.name)
                it.then(cls!!)
            }

            classRequirements.clear()

            packageRequirements.forEach {
                val pkg = this.getPackage(it.name)
                it.then(pkg)
            }

            packageRequirements.clear()

        }

        inner class ProjectScope : ShakeScope {
            override val parent: ShakeScope? = null

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

        private class ClassRequirement(val name: String, val then: (ShakeClass) -> Unit)
        private class PackageRequirement(val name: String, val then: (ShakePackage) -> Unit)
    }

    companion object {
        fun from(it: ShakeProject): ShakeProject = Impl(it)
    }
}
package io.github.shakelang.shake.processor.program.types

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

    class Impl(
        override val subpackages: List<ShakePackage>,
        override val classes: List<ShakeClass>,
        override val functions: List<ShakeFunction>,
        override val fields: List<ShakeField>
    ) : ShakeProject {

        override val projectScope: ShakeScope = object : ShakeScope {
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

    }
}
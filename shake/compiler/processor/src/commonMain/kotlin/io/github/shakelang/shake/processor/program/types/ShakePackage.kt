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

    class Impl(
        override val baseProject: ShakeProject,
        override val name: String,
        override val parent: ShakePackage?,
        override val subpackages: List<ShakePackage>,
        override val classes: List<ShakeClass>,
        override val functions: List<ShakeFunction>,
        override val fields: List<ShakeField>
    ) : ShakePackage {

        override val qualifiedName: String = if (parent == null) name else "${parent.qualifiedName}.$name"

        override val scope: ShakeScope = object : ShakeScope {
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
    }
}
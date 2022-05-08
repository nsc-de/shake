package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration


interface ShakeMethod : ShakeFunction {
    val clazz: ShakeClass

    class Impl(
        override val clazz: ShakeClass,
        override val prj: ShakeProject,
        override val pkg: ShakePackage?,
        override val parentScope: ShakeScope,
        override val name: String,
        override val isStatic: Boolean,
        override val isFinal: Boolean,
        override val isAbstract: Boolean,
        override val isSynchronized: Boolean,
        override val isStrict: Boolean,
        override val isPrivate: Boolean,
        override val isProtected: Boolean,
        override val isPublic: Boolean,
        override val returnType: ShakeType,
        override val parameters: List<ShakeParameter>,
        override val body: ShakeCode
    ) : ShakeMethod {
        override val qualifiedName: String get() = "${pkg?.qualifiedName?.plus(".") ?: ""}$name"
        override val scope: ShakeScope = object : ShakeScope {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            override val parent: ShakeScope = parentScope

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return parent.getInvokable(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return parent.getClass(name)
            }
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "name" to name,
                "isStatic" to isStatic,
                "isFinal" to isFinal,
                "isAbstract" to isAbstract,
                "isSynchronized" to isSynchronized,
                "isStrict" to isStrict,
                "isPrivate" to isPrivate,
                "isProtected" to isProtected,
                "isPublic" to isPublic,
                "returnType" to returnType.toJson()
            )
        }
    }
}
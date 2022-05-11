package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration


interface ShakeMethod : ShakeFunction {
    val clazz: ShakeClass

    class Impl : ShakeMethod {
        override val clazz: ShakeClass
        override val prj: ShakeProject
        override val pkg: ShakePackage?
        override val parentScope: ShakeScope
        override val name: String
        override val isStatic: Boolean
        override val isFinal: Boolean
        override val isAbstract: Boolean
        override val isSynchronized: Boolean
        override val isStrict: Boolean
        override val isPrivate: Boolean
        override val isProtected: Boolean
        override val isPublic: Boolean
        override val returnType: ShakeType
        override val parameters: List<ShakeParameter>
        override val body: ShakeCode

        constructor(
            clazz: ShakeClass,
            prj: ShakeProject,
            pkg: ShakePackage?,
            parentScope: ShakeScope,
            name: String,
            isStatic: Boolean,
            isFinal: Boolean,
            isAbstract: Boolean,
            isSynchronized: Boolean,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            returnType: ShakeType,
            parameters: List<ShakeParameter>,
            body: ShakeCode
        ) {
            this.clazz = clazz
            this.prj = prj
            this.pkg = pkg
            this.parentScope = parentScope
            this.name = name
            this.isStatic = isStatic
            this.isFinal = isFinal
            this.isAbstract = isAbstract
            this.isSynchronized = isSynchronized
            this.isStrict = isStrict
            this.isPrivate = isPrivate
            this.isProtected = isProtected
            this.isPublic = isPublic
            this.returnType = returnType
            this.parameters = parameters
            this.body = body
            this.signature = "${clazz.qualifiedName}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"
        }

        constructor(
            clazz: ShakeClass,
            scope: ShakeScope,
            it: ShakeMethod
        ) {
            this.clazz = clazz
            this.prj = clazz.prj
            this.pkg = clazz.pkg
            this.parentScope = scope
            this.name = it.name
            this.isStatic = it.isStatic
            this.isFinal = it.isFinal
            this.isAbstract = it.isAbstract
            this.isSynchronized = it.isSynchronized
            this.isStrict = it.isStrict
            this.isPrivate = it.isPrivate
            this.isProtected = it.isProtected
            this.isPublic = it.isPublic
            this.returnType = ShakeType.from(prj, it.returnType)
            this.parameters = it.parameters // TODO: copy parameters
            this.body = it.body // TODO copy body
            this.signature = "${clazz.qualifiedName}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"
        }

        override val qualifiedName: String get() = "${pkg?.qualifiedName?.plus(".") ?: ""}$name"
        override val scope: ShakeScope = MethodScope()
        override val signature: String

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "class" to clazz.name,
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

        inner class MethodScope : ShakeScope {
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
    }

    companion object {
        fun from(clazz: ShakeClass, it: ShakeMethod): ShakeMethod = TODO()
    }
}
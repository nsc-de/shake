package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope

interface ShakeFunction : ShakeInvokable {
    val prj: ShakeProject
    val pkg: ShakePackage?
    val parentScope: ShakeScope
    val name: String
    val isStatic: Boolean
    val isFinal: Boolean
    val isAbstract: Boolean
    val isSynchronized: Boolean
    val isStrict: Boolean
    val isPrivate: Boolean
    val isProtected: Boolean
    val isPublic: Boolean

    override val qualifiedName: String
    override val returnType: ShakeType
    val scope : ShakeScope
    val signature: String

    override fun toJson(): Map<String, Any?>

    class Impl : ShakeFunction {
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
        override val signature: String

        constructor(
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
            this.signature =  "${pkg?.qualifiedName ?: ""}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"
        }

        internal constructor(
            prj: ShakeProject,
            pkg: ShakePackage?,
            parentScope: ShakeScope,
            it: ShakeFunction
        ) {
            this.prj = prj
            this.pkg = pkg
            this.parentScope = parentScope
            this.name = it.name
            this.isStatic = it.isStatic
            this.isFinal = it.isFinal
            this.isAbstract = it.isAbstract
            this.isSynchronized = it.isSynchronized
            this.isStrict = it.isStrict
            this.isPrivate = it.isPrivate
            this.isProtected = it.isProtected
            this.isPublic = it.isPublic
            this.returnType = it.returnType // TODO: copy return type
            this.parameters = it.parameters // TODO: copy parameters
            this.body = it.body // TODO: copy body
            this.signature =  "${pkg?.qualifiedName ?: ""}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"
        }

        override val qualifiedName: String get() = "${pkg?.qualifiedName?.plus(".") ?: ""}$name"
        override val scope: ShakeScope = ShakeScope.ShakeFunctionScope.from(this)

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

    companion object {
        fun from(project: ShakeProject, pkg: ShakePackage?, it: ShakeFunction): ShakeFunction = Impl(project, pkg, it.parentScope, it)
    }

}
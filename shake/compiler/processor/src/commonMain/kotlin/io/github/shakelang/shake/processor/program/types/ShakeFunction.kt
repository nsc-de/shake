package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

/**
 * Represents a function in the Shake language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeFunction : ShakeFunctionType {

    class Impl : ShakeFunction {
        override val project: ShakeProject.Impl
        override val pkg: ShakePackage.Impl?
        override val parentScope: ShakeScope.ShakeScopeImpl
        override val name: String
        override val isStatic: Boolean
        override val isFinal: Boolean
        override val isAbstract: Boolean
        override val isSynchronized: Boolean
        override val isStrict: Boolean
        override val isPrivate: Boolean
        override val isProtected: Boolean
        override val isPublic: Boolean

        override val returnTypePointer: Pointer<ShakeType>
        override val returnType: ShakeType get () = returnTypePointer.value

        override val parameters: List<ShakeParameter>
        override val bodyPointer: Pointer<ShakeCode>
        override val body: ShakeCode get() = bodyPointer.value
        override val signature: String
            get() = "${pkg?.qualifiedName ?: ""}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"

        constructor(
            prj: ShakeProject.Impl,
            pkg: ShakePackage.Impl?,
            parentScope: ShakeScope.ShakeScopeImpl,
            name: String,
            isStatic: Boolean,
            isFinal: Boolean,
            isAbstract: Boolean,
            isSynchronized: Boolean,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            returnType: Pointer<ShakeType>,
            parameters: List<ShakeParameter>,
            body: ShakeCode
        ) {
            this.project = prj
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
            this.returnTypePointer = returnType
            this.parameters = parameters
            this.bodyPointer = body.point()
        }

        constructor(
            prj: ShakeProject.Impl,
            pkg: ShakePackage.Impl?,
            parentScope: ShakeScope.ShakeScopeImpl,
            name: String,
            isStatic: Boolean,
            isFinal: Boolean,
            isAbstract: Boolean,
            isSynchronized: Boolean,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            returnType: Pointer<ShakeType>,
            parameters: List<ShakeParameter>,
            body: (Impl) -> Pointer<ShakeCode>
        ) {
            this.project = prj
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
            this.returnTypePointer = returnType
            this.parameters = parameters
            this.bodyPointer = body(this)
        }

        internal constructor(
            scope: ShakeScope.ShakeScopeImpl,
            it: ShakeFunction
        ) {
            this.project = scope.project
            this.pkg = scope.pkg
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
            this.returnTypePointer = ShakeType.from(this.project, it.returnType)
            this.parameters = it.parameters.map { ShakeParameter.from(this.project, it) }
            this.bodyPointer = ShakeCode.from(this.scope, it.body).point()
        }

        override val qualifiedName: String get() = "${pkg?.qualifiedName?.plus(".") ?: ""}$name"
        override val scope: ShakeScope.ShakeFunctionScope.Impl = ShakeScope.ShakeFunctionScope.from(this)

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
        fun from(scope: ShakeScope.ShakeScopeImpl, it: ShakeFunction) = Impl(scope, it)
        fun create(
            project: ShakeProject.Impl,
            pkg: ShakePackage.Impl?,
            parentScope: ShakeScope.ShakeScopeImpl,
            name: String,
            isStatic: Boolean,
            isFinal: Boolean,
            isAbstract: Boolean,
            isSynchronized: Boolean,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            returnType: Pointer<ShakeType>,
            parameters: List<ShakeParameter.Impl>,
            body: (Impl) -> Pointer<ShakeCode>
        ) = Impl(project, pkg, parentScope, name, isStatic, isFinal, isAbstract, isSynchronized, isStrict, isPrivate, isProtected, isPublic, returnType, parameters, body)

    }

}
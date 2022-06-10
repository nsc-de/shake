package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point


/**
 * Represents a method in the Shake language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeMethod : ShakeFunctionType {

    /**
     * The class that this method belongs to.
     */
    val clazz: ShakeClass
    class Impl : ShakeMethod {
        override val clazz: ShakeClass
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
        override val returnType: ShakeType get() = returnTypePointer.value
        override val parameters: List<ShakeParameter>
        override val bodyPointer: Pointer<ShakeCode>
        override val body: ShakeCode get() = bodyPointer.value

        constructor(
            clazz: ShakeClass.Impl,
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
            this.clazz = clazz
            this.project = clazz.project
            this.pkg = clazz.pkg
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
            clazz: ShakeClass.Impl,
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
            this.clazz = clazz
            this.project = clazz.project
            this.pkg = clazz.pkg
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

        constructor(
            clazz: ShakeClass.Impl,
            scope: ShakeScope.ShakeScopeImpl,
            it: ShakeMethod
        ) {
            this.clazz = clazz
            this.project = clazz.project
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
            this.returnTypePointer = ShakeType.from(project, it.returnType)
            this.parameters = it.parameters // TODO: copy parameters
            this.bodyPointer = ShakeCode.from(this.scope, it.body).point()
        }

        override val qualifiedName: String get() = "${pkg?.qualifiedName?.plus(".") ?: ""}$name"
        override val scope: ShakeScope.ShakeMethodScope.Impl = ShakeScope.ShakeMethodScope.from(this)
        override val signature: String get() = "${clazz.signature}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"

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
    }

    companion object {

        /**
         * Clones a [ShakeMethod].
         *
         * @param clazz The class that the method belongs to.
         * @param it The method to clone.
         */
        fun from(clazz: ShakeClass.Impl, parentScope: ShakeScope.ShakeScopeImpl, it: ShakeMethod): Impl = Impl(clazz, parentScope, it)

        fun create(
            clazz: ShakeClass.Impl,
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
        ): Impl = Impl(
            clazz,
            parentScope,
            name,
            isStatic,
            isFinal,
            isAbstract,
            isSynchronized,
            isStrict,
            isPrivate,
            isProtected,
            isPublic,
            returnType,
            parameters,
            body
        )
    }
}
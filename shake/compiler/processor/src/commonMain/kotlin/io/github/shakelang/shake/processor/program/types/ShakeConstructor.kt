package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeConstructor {

    /**
     * The project that this constructor belongs to.
     */
    val project: ShakeProject

    /**
     * The class that this constructor belongs to.
     */
    val clazz: ShakeClass

    /**
     * The name of the constructor (if any).
     */
    val name: String?

    /**
     * The pointer to the code the constructor executes.
     */
    val bodyPointer: Pointer<ShakeCode>

    /**
     * The code that this constructor executes.
     */
    val body: ShakeCode

    /**
     * Is this constructor strict?
     */
    val isStrict: Boolean

    /**
     * Is this constructor private?
     */
    val isPrivate: Boolean

    /**
     * Is this constructor protected?
     */
    val isProtected: Boolean

    /**
     * Is this constructor public?
     */
    val isPublic: Boolean

    /**
     * The parameters of this constructor.
     */
    val parameters: List<ShakeParameter>

    /**
     * The parent scope of this constructor.
     */
    val parentScope: ShakeScope

    /**
     * The scope of this constructor.
     */
    val scope: ShakeScope

    /**
     * The signature of this constructor.
     */
    val signature: String

    /**
     * Convert the constructor to json map representation.
     */
    fun toJson(): Map<String, Any?>

    class Impl : ShakeConstructor {

        override val clazz: ShakeClass.Impl
        override val bodyPointer: Pointer<ShakeCode>
        override val body: ShakeCode get() = bodyPointer.value
        override val isStrict: Boolean
        override val isPrivate: Boolean
        override val isProtected: Boolean
        override val isPublic: Boolean
        override val name: String?
        override val parameters: List<ShakeParameter.Impl>
        override val parentScope: ShakeScope
        override val scope: ShakeScope.ShakeScopeImpl

        override val project: ShakeProject
            get() = clazz.project

        constructor(
            clazz: ShakeClass.Impl,
            body: ShakeCode.Impl,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            name: String?,
            parameters: List<ShakeParameter.Impl>,
            parentScope: ShakeScope,
        ) {
            this.clazz = clazz
            this.bodyPointer = body.point()
            this.isStrict = isStrict
            this.isPrivate = isPrivate
            this.isProtected = isProtected
            this.isPublic = isPublic
            this.name = name
            this.parameters = parameters
            this.parentScope = parentScope
            this.scope = ShakeScope.ShakeConstructorScope.from(this)
        }

        constructor(
            clazz: ShakeClass.Impl,
            body: (Impl) -> Pointer<ShakeCode.Impl>,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            name: String?,
            parameters: List<ShakeParameter.Impl>,
            parentScope: ShakeScope,
        ) {
            this.clazz = clazz
            this.isStrict = isStrict
            this.isPrivate = isPrivate
            this.isProtected = isProtected
            this.isPublic = isPublic
            this.name = name
            this.parameters = parameters
            this.parentScope = parentScope
            this.scope = ShakeScope.ShakeConstructorScope.from(this)
            this.bodyPointer = body(this)
        }

        constructor(
            clazz: ShakeClass.Impl,
            it: ShakeConstructor
        ) {
            this.clazz = clazz
            this.isStrict = it.isStrict
            this.isPrivate = it.isPrivate
            this.isProtected = it.isProtected
            this.isPublic = it.isPublic
            this.name = it.name
            this.parameters = it.parameters.map { ShakeParameter.from(clazz.project, it) }
            this.parentScope = it.parentScope
            this.scope = ShakeScope.ShakeConstructorScope.from(this)
            this.bodyPointer = ShakeCode.from(this.scope, it.body).point()
        }

        override val signature: String get() = "${clazz.signature}#${name?.let { "#$it" } ?: ""}(${parameters.joinToString(",") { it.type.signature }})"

        override fun toJson(): Map<String, Any?> = mapOf(
            "clazz" to clazz.toJson(),
            "body" to body.toJson(),
            "isStrict" to isStrict,
            "isPrivate" to isPrivate,
            "isProtected" to isProtected,
            "isPublic" to isPublic,
            "name" to name,
            "parameters" to parameters.map { it.toJson() },
        )
    }

    companion object {
        fun from(clazz: ShakeClass.Impl, it: ShakeConstructor) = Impl(clazz, it)
        fun create(
            clazz: ShakeClass.Impl,
            scope: ShakeScope.ShakeScopeImpl,
            name: String?,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            parameters: List<ShakeParameter.Impl>,
            body: (Impl) -> Pointer<ShakeCode.Impl>
        ) = Impl(clazz, body, isStrict, isPrivate, isProtected, isPublic, name, parameters, scope)
    }

}
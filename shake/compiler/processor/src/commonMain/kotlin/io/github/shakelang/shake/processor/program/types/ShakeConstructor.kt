package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.ShakeScope

interface ShakeConstructor {
    val clazz: ShakeClass
    val body: ShakeCode
    val isStrict: Boolean
    val isPrivate: Boolean
    val isProtected: Boolean
    val isPublic: Boolean
    val name: String?
    val parameters: List<ShakeParameter>
    val parentScope: ShakeScope
    val scope: ShakeScope

    val signature: String

    fun toJson(): Map<String, Any?>

    class Impl : ShakeConstructor {

        override val clazz: ShakeClass
        override val body: ShakeCode
        override val isStrict: Boolean
        override val isPrivate: Boolean
        override val isProtected: Boolean
        override val isPublic: Boolean
        override val name: String?
        override val parameters: List<ShakeParameter>
        override val parentScope: ShakeScope
        override val scope: ShakeScope

        constructor(
            clazz: ShakeClass,
            body: ShakeCode,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            name: String?,
            parameters: List<ShakeParameter>,
            parentScope: ShakeScope,
            scope: ShakeScope
        ) {
            this.clazz = clazz
            this.body = body
            this.isStrict = isStrict
            this.isPrivate = isPrivate
            this.isProtected = isProtected
            this.isPublic = isPublic
            this.name = name
            this.parameters = parameters
            this.parentScope = parentScope
            this.scope = scope
        }

        constructor(
            clazz: ShakeClass,
            it: ShakeConstructor
        ) {
            this.clazz = clazz
            this.body = it.body // TODO: copy
            this.isStrict = it.isStrict
            this.isPrivate = it.isPrivate
            this.isProtected = it.isProtected
            this.isPublic = it.isPublic
            this.name = it.name
            this.parameters = it.parameters.map { ShakeParameter.from(clazz.prj, it) }
            this.parentScope = it.parentScope
            this.scope = it.scope
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
        fun from(clazz: ShakeClass, it: ShakeConstructor): ShakeConstructor = Impl(clazz, it)
    }

}
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
    val scope: ShakeScope

    val signature: String
        get() = "${clazz.qualifiedName}${name?.let { "#$it" } ?: ""}(${parameters.joinToString(",") { it.type.signature }})"

    fun toJson(): Map<String, Any?>

    class Impl(
        override val clazz: ShakeClass,
        override val body: ShakeCode,
        override val isStrict: Boolean,
        override val isPrivate: Boolean,
        override val isProtected: Boolean,
        override val isPublic: Boolean,
        override val name: String?,
        override val parameters: List<ShakeParameter>,
        override val scope: ShakeScope
    ) : ShakeConstructor {
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
        fun from(clazz: ShakeClass, it: ShakeConstructor): ShakeConstructor = TODO()
    }

}
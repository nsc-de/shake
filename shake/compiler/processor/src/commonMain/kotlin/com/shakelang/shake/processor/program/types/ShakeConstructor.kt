package com.shakelang.shake.processor.program.types

import com.shakelang.shake.processor.program.types.code.ShakeCode
import com.shakelang.shake.processor.program.types.code.ShakeScope

interface ShakeConstructor {
    val clazz: ShakeClass
    val body: ShakeCode?
    val isStrict: Boolean
    val isPrivate: Boolean
    val isProtected: Boolean
    val isPublic: Boolean
    val isNative: Boolean
    val name: String?
    val parameters: List<ShakeParameter>
    val scope: ShakeScope

    val qualifiedName: String
        get() = "${clazz.qualifierPrefix}+${name ?: "default"}"

    val parameterTypes: List<ShakeType> get() = parameters.map { it.type }
    val signature: String
        get() = "+${name ?: "default"}(${parameterTypes.joinToString(",") { it.qualifiedName }})N"
    val qualifiedSignature: String
        get() = "$qualifiedName(${parameterTypes.joinToString(",") { it.qualifiedName }})N"

    fun toJson(): Map<String, Any?> {
        return mapOf(
            "class" to clazz.toJson(),
            "body" to body?.toJson(),
            "isStrict" to isStrict,
            "isPrivate" to isPrivate,
            "isProtected" to isProtected,
            "isPublic" to isPublic,
            "name" to name,
            "parameters" to parameters.map { it.toJson() },
        )
    }

    fun phase3()
    fun phase4()
}

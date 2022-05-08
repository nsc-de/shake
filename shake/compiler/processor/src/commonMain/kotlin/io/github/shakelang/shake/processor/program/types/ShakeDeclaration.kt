package io.github.shakelang.shake.processor.program.types

interface ShakeDeclaration {
    val name: String
    val type: ShakeType
    val qualifiedName: String
    fun toJson(): Map<String, Any?>
}
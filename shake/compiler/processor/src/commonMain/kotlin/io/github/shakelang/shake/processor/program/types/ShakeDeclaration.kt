package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.util.Pointer

interface ShakeDeclaration {
    val name: String
    val typePointer: Pointer<ShakeType>
    val type: ShakeType
    val qualifiedName: String
    fun toJson(): Map<String, Any?>
}
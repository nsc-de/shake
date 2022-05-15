package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeParameter
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeInvokable {
    val body: ShakeCode
    val qualifiedName: String
    val parameters: List<ShakeParameter>
    val returnTypePointer: Pointer<ShakeType>
    val returnType: ShakeType
    fun toJson(): Map<String, Any?>
}
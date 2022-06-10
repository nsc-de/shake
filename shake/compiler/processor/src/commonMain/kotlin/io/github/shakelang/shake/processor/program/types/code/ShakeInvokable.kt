package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.*
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeInvokable {
    val bodyPointer: Pointer<ShakeCode>
    val body: ShakeCode
    val qualifiedName: String
    val parameters: List<ShakeParameter>
    val returnTypePointer: Pointer<ShakeType>
    val returnType: ShakeType
    fun toJson(): Map<String, Any?>

    companion object {
        fun from(prj: ShakeProject, it: ShakeInvokable): Pointer<ShakeInvokable> {
            return when (it) {
                is ShakeFunction -> {
                    val signature = it.signature
                    prj.getFunctionBySignature(signature).transform { it ?: throw IllegalStateException("Function not found: $signature") }
                }
                is ShakeMethod -> {
                    val signature = it.signature
                    prj.getMethodBySignature(signature).transform { it ?: throw IllegalStateException("Method not found: $signature") }
                }
                else -> throw IllegalStateException("Unsupported invocation type: $it")
            }
        }
    }
}
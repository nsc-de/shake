package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeInvocation: ShakeValue, ShakeStatement {
    val callable: ShakeInvokable
    val arguments: List<ShakeValue>
    val parent: ShakeValue?
    val name: String
    val isAnonymous: Boolean

    override fun toJson(): Map<String, Any?>

    class Impl(
        override val callable: ShakeInvokable,
        override val arguments: List<ShakeValue>,
        override val parent: ShakeValue?,
        override val name: String,
        override val isAnonymous: Boolean,
        override val typePointer: Pointer<ShakeType>
    ): ShakeInvocation {
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> = mapOf(
            "callable" to callable.toJson(),
            "arguments" to arguments.map { it.toJson() },
            "parent" to parent?.toJson(),
            "name" to name,
            "isAnonymous" to isAnonymous
        )
    }
}
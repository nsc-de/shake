package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeReturn : ShakeStatement {
    val value: ShakeValue?

    class Impl(override val value: ShakeValue?) : ShakeReturn {
        override fun toJson(): Map<String, Any?> = mapOf(
            "type" to "return",
            "value" to value
        )
    }
}
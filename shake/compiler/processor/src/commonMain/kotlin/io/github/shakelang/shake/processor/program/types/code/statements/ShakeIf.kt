package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeIf : ShakeStatement {

    val condition: ShakeValue
    val body: ShakeCode
    val elseBody: ShakeCode?

    class Impl(
        override val condition: ShakeValue,
        override val body: ShakeCode,
        override val elseBody: ShakeCode?
    ) : ShakeIf {
        override fun toJson(): Map<String, Any?> = mapOf(
            "condition" to condition,
            "body" to body,
            "elseBody" to elseBody
        )
    }


}
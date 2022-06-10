package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeIf : ShakeStatement {

    val condition: ShakeValue
    val body: ShakeCode
    val elseBody: ShakeCode?

    class Impl(
        override val scope: ShakeScope,
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

    companion object {
        fun from(scope: ShakeScope.ShakeScopeImpl, it: ShakeIf): ShakeIf {
            return Impl(
                scope,
                ShakeValue.from(scope, it.condition),
                ShakeCode.from(scope, it.body),
                it.elseBody?.let { ShakeCode.from(scope, it) }
            )
        }

        fun create(scope: ShakeScope, condition: ShakeValue, body: ShakeCode, elseBody: ShakeCode? = null): ShakeIf {
            return Impl(scope, condition, body, elseBody)
        }
    }
}
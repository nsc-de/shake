package com.shakelang.shake.processor.program.types.code

import com.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import com.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeInvocation : ShakeValue, ShakeStatement {
    val callable: ShakeInvokable
    val arguments: List<ShakeValue>
    val parent: ShakeValue?
    val name: String
    val isAnonymous: Boolean

    override fun toJson(): Map<String, Any?>
}

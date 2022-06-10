package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.ShakeAssignmentType
import io.github.shakelang.shake.processor.program.types.code.ShakeInvocation
import io.github.shakelang.shake.processor.program.types.code.ShakeMutateType
import io.github.shakelang.shake.processor.program.types.code.ShakeNew

interface ShakeStatement {
    val scope: ShakeScope
    fun toJson(): Map<String, Any?>

    companion object {
        fun from(scope: ShakeScope.ShakeScopeImpl, statement: ShakeStatement): ShakeStatement {
            return when (statement) {
                is ShakeAssignmentType -> ShakeAssignmentType.from(scope, statement)
                is ShakeMutateType -> ShakeMutateType.from(scope, statement)
                is ShakeIf -> ShakeIf.from(scope, statement)
                is ShakeReturn -> ShakeReturn.from(scope, statement)
                is ShakeWhile -> ShakeWhile.from(scope, statement)
                is ShakeNew -> ShakeNew.from(scope, statement)
                is ShakeInvocation -> ShakeInvocation.from(scope, statement)
                else -> throw IllegalStateException("Unknown statement type")
            }
        }
    }
}
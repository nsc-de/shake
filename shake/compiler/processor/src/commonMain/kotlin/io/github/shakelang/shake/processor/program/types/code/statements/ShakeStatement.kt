package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.code.ShakeAssignmentType
import io.github.shakelang.shake.processor.program.types.code.ShakeInvocation
import io.github.shakelang.shake.processor.program.types.code.ShakeMutateType
import io.github.shakelang.shake.processor.program.types.code.ShakeNew

interface ShakeStatement {
    fun toJson(): Map<String, Any?>

    companion object {
        fun from(prj: ShakeProject, statement: ShakeStatement): ShakeStatement {
            return when (statement) {
                is ShakeAssignmentType -> ShakeAssignmentType.from(prj, statement)
                is ShakeMutateType -> ShakeMutateType.from(prj, statement)
                is ShakeIf -> ShakeIf.from(prj, statement)
                is ShakeReturn -> ShakeReturn.from(prj, statement)
                is ShakeWhile -> ShakeWhile.from(prj, statement)
                is ShakeNew -> ShakeNew.from(prj, statement)
                is ShakeInvocation -> ShakeInvocation.from(prj, statement)
                else -> throw IllegalStateException("Unknown statement type")
            }
        }
    }
}
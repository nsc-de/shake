package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.*

interface ShakeValue {
    val type: ShakeType
    fun toJson(): Map<String, Any?>

    companion object {
        fun from(prj: ShakeProject, it: ShakeValue): ShakeValue {
            return when (it) {
                is ShakeDoubleLiteral -> ShakeDoubleLiteral.from(prj, it)
                is ShakeIntLiteral -> ShakeIntLiteral.from(prj, it)
                is ShakeBooleanLiteral -> ShakeBooleanLiteral.from(prj, it)
                is ShakeCharacterLiteral -> ShakeCharacterLiteral.from(prj, it)
                is ShakeAddition -> ShakeAddition.from(prj, it)
                is ShakeSubtraction -> ShakeSubtraction.from(prj, it)
                is ShakeMultiplication -> ShakeMultiplication.from(prj, it)
                is ShakeDivision -> ShakeDivision.from(prj, it)
                is ShakeModulus -> ShakeModulus.from(prj, it)
                is ShakePower -> ShakePower.from(prj, it)
                is ShakeEquals -> ShakeEquals.from(prj, it)
                is ShakeNotEquals -> ShakeNotEquals.from(prj, it)
                is ShakeLessThan -> ShakeLessThan.from(prj, it)
                is ShakeLessThanOrEqual -> ShakeLessThanOrEqual.from(prj, it)
                is ShakeGreaterThan -> ShakeGreaterThan.from(prj, it)
                is ShakeGreaterThanOrEqual -> ShakeGreaterThanOrEqual.from(prj, it)
                is ShakeAnd -> ShakeAnd.from(prj, it)
                is ShakeOr -> ShakeOr.from(prj, it)
                is ShakeNot -> ShakeNot.from(prj, it)
                is ShakeAssignment -> ShakeAssignment.from(prj, it)
                is ShakeAddAssignment -> ShakeAddAssignment.from(prj, it)
                is ShakeSubAssignment -> ShakeSubAssignment.from(prj, it)
                is ShakeMulAssignment -> ShakeMulAssignment.from(prj, it)
                is ShakeDivAssignment -> ShakeDivAssignment.from(prj, it)
                is ShakeModAssignment -> ShakeModAssignment.from(prj, it)
                is ShakePowAssignment -> ShakePowAssignment.from(prj, it)
                is ShakeIncrementBefore -> ShakeIncrementBefore.from(prj, it)
                is ShakeIncrementAfter -> ShakeIncrementAfter.from(prj, it)
                is ShakeDecrementBefore -> ShakeDecrementBefore.from(prj, it)
                is ShakeDecrementAfter -> ShakeDecrementAfter.from(prj, it)
                else -> {
                    throw IllegalArgumentException("Unknown value type: ${it::class.simpleName}")
                }
            }
            TODO()
        }
    }
}
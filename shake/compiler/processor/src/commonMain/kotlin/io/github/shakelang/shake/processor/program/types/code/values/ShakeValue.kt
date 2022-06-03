package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.*
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeValue {
    val scope: ShakeScope
    val project: ShakeProject get() = scope.project
    val typePointer: Pointer<ShakeType>
    val type: ShakeType
    fun toJson(): Map<String, Any?>

    companion object {
        fun from(scope: ShakeScope, it: ShakeValue): ShakeValue {
            return when (it) {
                is ShakeDoubleLiteral -> ShakeDoubleLiteral.from(scope, it)
                is ShakeIntLiteral -> ShakeIntLiteral.from(scope, it)
                is ShakeBooleanLiteral -> ShakeBooleanLiteral.from(scope, it)
                is ShakeCharacterLiteral -> ShakeCharacterLiteral.from(scope, it)
                is ShakeAddition -> ShakeAddition.from(scope, it)
                is ShakeSubtraction -> ShakeSubtraction.from(scope, it)
                is ShakeMultiplication -> ShakeMultiplication.from(scope, it)
                is ShakeDivision -> ShakeDivision.from(scope, it)
                is ShakeModulus -> ShakeModulus.from(scope, it)
                is ShakePower -> ShakePower.from(scope, it)
                is ShakeEquals -> ShakeEquals.from(scope, it)
                is ShakeNotEquals -> ShakeNotEquals.from(scope, it)
                is ShakeLessThan -> ShakeLessThan.from(scope, it)
                is ShakeLessThanOrEqual -> ShakeLessThanOrEqual.from(scope, it)
                is ShakeGreaterThan -> ShakeGreaterThan.from(scope, it)
                is ShakeGreaterThanOrEqual -> ShakeGreaterThanOrEqual.from(scope, it)
                is ShakeAnd -> ShakeAnd.from(scope, it)
                is ShakeOr -> ShakeOr.from(scope, it)
                is ShakeNot -> ShakeNot.from(scope, it)
                is ShakeAssignment -> ShakeAssignment.from(scope, it)
                is ShakeAddAssignment -> ShakeAddAssignment.from(scope, it)
                is ShakeSubAssignment -> ShakeSubAssignment.from(scope, it)
                is ShakeMulAssignment -> ShakeMulAssignment.from(scope, it)
                is ShakeDivAssignment -> ShakeDivAssignment.from(scope, it)
                is ShakeModAssignment -> ShakeModAssignment.from(scope, it)
                is ShakePowAssignment -> ShakePowAssignment.from(scope, it)
                is ShakeIncrementBefore -> ShakeIncrementBefore.from(scope, it)
                is ShakeIncrementAfter -> ShakeIncrementAfter.from(scope, it)
                is ShakeDecrementBefore -> ShakeDecrementBefore.from(scope, it)
                is ShakeDecrementAfter -> ShakeDecrementAfter.from(scope, it)
                else -> {
                    TODO("Unsupported value type: ${it::class.simpleName}")
                    //throw IllegalArgumentException("Unknown value type: ${it::class.simpleName}")
                }
            }
        }
    }
}
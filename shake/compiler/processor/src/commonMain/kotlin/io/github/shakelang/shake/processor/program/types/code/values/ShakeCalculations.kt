package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeConcatenation : ShakeValue {
    val left: ShakeValue
    val right: ShakeValue
    companion object {
        fun from(scope: ShakeScope, it: ShakeConcatenation): ShakeConcatenation {
            return when (it) {
                is ShakeCalculation -> ShakeCalculation.from(scope, it)
                is ShakeLogicalConcatenation -> ShakeLogicalConcatenation.from(scope, it)
                else -> throw IllegalArgumentException("Unknown concatenation type")
            }
        }
    }
}

interface ShakeCalculation : ShakeConcatenation {
    companion object {
        fun from(scope: ShakeScope, it: ShakeCalculation): ShakeCalculation {
            return when(it) {
                is ShakeAddition -> ShakeAddition.from(scope, it)
                is ShakeSubtraction -> ShakeSubtraction.from(scope, it)
                is ShakeMultiplication -> ShakeMultiplication.from(scope, it)
                is ShakeDivision -> ShakeDivision.from(scope, it)
                is ShakeModulus -> ShakeModulus.from(scope, it)
                is ShakePower -> ShakePower.from(scope, it)
                else -> throw IllegalArgumentException("Unknown calculation type")
            }
        }
    }
}
interface ShakeAddition : ShakeCalculation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeAddition {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "addition",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeAddition): ShakeAddition {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeSubtraction : ShakeCalculation {

    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeSubtraction {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "subtraction",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeSubtraction): ShakeSubtraction {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeMultiplication : ShakeCalculation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeMultiplication {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "multiplication",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeMultiplication): ShakeMultiplication {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeDivision : ShakeCalculation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeDivision {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "division",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeDivision): ShakeDivision {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeModulus : ShakeCalculation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeModulus {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "modulus",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeModulus): ShakeModulus {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakePower : ShakeCalculation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakePower {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "power",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakePower): ShakePower {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}

interface ShakeComparison : ShakeConcatenation

interface ShakeEquals : ShakeComparison {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeEquals {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "equals",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeEquals): ShakeEquals {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeNotEquals : ShakeComparison {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeNotEquals {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "notEquals",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeNotEquals): ShakeNotEquals {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeLessThan : ShakeComparison {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeLessThan {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "lessThan",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeLessThan): ShakeLessThan {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeLessThanOrEqual : ShakeComparison {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeLessThanOrEqual {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "lessThanOrEqual",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeLessThanOrEqual): ShakeLessThanOrEqual {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeGreaterThan : ShakeComparison {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeGreaterThan {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "greaterThan",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeGreaterThan): ShakeGreaterThan {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeGreaterThanOrEqual : ShakeComparison {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeGreaterThanOrEqual {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "greaterThanOrEqual",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeGreaterThanOrEqual): ShakeGreaterThanOrEqual {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}

interface ShakeLogical : ShakeValue {
    companion object {
        fun from(scope: ShakeScope, it: ShakeLogical): ShakeLogical {
            return when (it) {
                is ShakeLogicalConcatenation -> ShakeLogicalConcatenation.from(scope, it)
                is ShakeNot -> ShakeNot.from(scope, it)
                else -> throw IllegalArgumentException("Unsupported logical type: ${it::class.simpleName}")
            }
        }
    }
}

interface ShakeLogicalConcatenation : ShakeConcatenation, ShakeLogical {
    companion object {
        fun from(scope: ShakeScope, it: ShakeLogicalConcatenation): ShakeLogicalConcatenation {
            return when (it) {
                is ShakeAnd -> ShakeAnd.from(scope, it)
                is ShakeOr -> ShakeOr.from(scope, it)
                is ShakeXor -> ShakeXor.from(scope, it)
                else -> throw IllegalArgumentException("Unsupported type: ${it::class.simpleName}")
            }
        }
    }
}

interface ShakeAnd : ShakeLogicalConcatenation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeAnd {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "and",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeAnd): ShakeAnd {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeOr : ShakeLogicalConcatenation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeOr {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "or",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeOr): ShakeOr {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeXor : ShakeLogicalConcatenation {
    class Impl(
        override val scope: ShakeScope,
        override val left: ShakeValue,
        override val right: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeXor {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "xor",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeXor): ShakeXor {
            return Impl(
                scope,
                left = ShakeValue.from(scope, it.left),
                right = ShakeValue.from(scope, it.right),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}
interface ShakeNot : ShakeLogical {
    val value: ShakeValue

    class Impl(
        override val scope: ShakeScope,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeNot {

        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "not",
                "value" to value.toJson(),
                "valueType" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeNot): ShakeNot {
            return Impl(
                scope,
                value = ShakeValue.from(scope, it.value),
                typePointer = ShakeType.from(scope, it.type)
            )
        }
    }
}


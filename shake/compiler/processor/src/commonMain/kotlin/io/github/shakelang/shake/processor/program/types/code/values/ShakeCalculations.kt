package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeType

interface ShakeConcatenation : ShakeValue {
    val left: ShakeValue
    val right: ShakeValue
}

interface ShakeCalculation : ShakeConcatenation
interface ShakeAddition : ShakeCalculation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeAddition {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "addition",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeSubtraction : ShakeCalculation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeSubtraction {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "subtraction",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeMultiplication : ShakeCalculation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeMultiplication {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "multiplication",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeDivision : ShakeCalculation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeDivision {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "division",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeModulus : ShakeCalculation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeModulus {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "modulus",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakePower : ShakeCalculation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakePower {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "power",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}

interface ShakeComparison : ShakeConcatenation

interface ShakeEquals : ShakeComparison {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeEquals {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "equals",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeNotEquals : ShakeComparison {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeNotEquals {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "notEquals",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeLessThan : ShakeComparison {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeLessThan {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "lessThan",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeLessThanOrEqual : ShakeComparison {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeLessThanOrEqual {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "lessThanOrEqual",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeGreaterThan : ShakeComparison {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeGreaterThan {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "greaterThan",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeGreaterThanOrEqual : ShakeComparison {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeGreaterThanOrEqual {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "greaterThanOrEqual",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}

interface ShakeLogical : ShakeValue
interface ShakeLogicalConcatenation : ShakeConcatenation, ShakeLogical

interface ShakeAnd : ShakeLogicalConcatenation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeAnd {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "and",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeOr : ShakeLogicalConcatenation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeOr {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "or",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeXor : ShakeLogicalConcatenation {
    class Impl(override val left: ShakeValue, override val right: ShakeValue, override val type: ShakeType) : ShakeXor {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "xor",
                "left" to left.toJson(),
                "right" to right.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}
interface ShakeNot : ShakeLogical {
    val value: ShakeValue

    class Impl(override val value: ShakeValue, override val type: ShakeType) : ShakeNot {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "not",
                "value" to value.toJson(),
                "valueType" to type.toJson()
            )
        }
    }
}


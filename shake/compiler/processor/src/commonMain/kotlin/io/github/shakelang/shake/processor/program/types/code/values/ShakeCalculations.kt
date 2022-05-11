package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeProject
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeAddition): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeSubtraction): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeMultiplication): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeDivision): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeModulus): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakePower): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeEquals): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeNotEquals): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeLessThan): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeLessThanOrEqual): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeGreaterThan): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeGreaterThanOrEqual): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeAnd): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeOr): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeXor): ShakeValue {
            return Impl(
                left = ShakeValue.from(prj, it.left),
                right = ShakeValue.from(prj, it.right),
                type = ShakeType.from(prj, it.type)
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeNot): ShakeValue {
            return Impl(
                value = ShakeValue.from(prj, it.value),
                type = ShakeType.from(prj, it.type)
            )
        }
    }
}


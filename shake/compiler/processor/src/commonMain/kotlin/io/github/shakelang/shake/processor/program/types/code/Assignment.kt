package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeAssignmentType : ShakeValue, ShakeStatement {
    val variablePointer: Pointer<ShakeAssignable>
    val variable: ShakeAssignable
    val value: ShakeValue
}

interface ShakeAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakeAssignment): ShakeAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakeAddAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeAddAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "add_assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakeAddAssignment): ShakeAddAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakeSubAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeSubAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "sub_assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakeSubAssignment): ShakeSubAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakeMulAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeMulAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "mul_assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakeMulAssignment): ShakeMulAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakeDivAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeDivAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "div_assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakeDivAssignment): ShakeDivAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakeModAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeModAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "mod_assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakeModAssignment): ShakeModAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakePowAssignment : ShakeAssignmentType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakePowAssignment {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "pow_assignment",
                "variable" to variable,
                "value" to value.toJson(),
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, assignment: ShakePowAssignment): ShakePowAssignment {
            return Impl(ShakeAssignable.from(prj, assignment.variable), ShakeValue.from(prj, assignment.value), ShakeType.from(prj, assignment.type))
        }
    }
}

interface ShakeMutateType : ShakeValue, ShakeStatement {
    val variablePointer: Pointer<ShakeAssignable>
    val variable: ShakeAssignable
}

interface ShakeIncrementBefore : ShakeMutateType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeIncrementBefore {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "increment_before",
                "variable" to variable,
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, increment: ShakeIncrementBefore): ShakeIncrementBefore {
            return Impl(ShakeAssignable.from(prj, increment.variable), ShakeType.from(prj, increment.type))
        }
    }
}

interface ShakeIncrementAfter : ShakeMutateType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeIncrementAfter {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "increment_after",
                "variable" to variable,
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, increment: ShakeIncrementAfter): ShakeIncrementAfter {
            return Impl(ShakeAssignable.from(prj, increment.variable), ShakeType.from(prj, increment.type))
        }
    }
}

interface ShakeDecrementBefore : ShakeMutateType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeDecrementBefore {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "decrement_before",
                "variable" to variable,
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, decrement: ShakeDecrementBefore): ShakeDecrementBefore {
            return Impl(ShakeAssignable.from(prj, decrement.variable), ShakeType.from(prj, decrement.type))
        }
    }
}

interface ShakeDecrementAfter : ShakeMutateType {
    class Impl(
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
    ) : ShakeDecrementAfter {
        override val variable: ShakeAssignable get() = variablePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "decrement_after",
                "variable" to variable,
                "valueType" to type.toJson(),
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, decrement: ShakeDecrementAfter): ShakeDecrementAfter {
            return Impl(ShakeAssignable.from(prj, decrement.variable), ShakeType.from(prj, decrement.type))
        }
    }
}
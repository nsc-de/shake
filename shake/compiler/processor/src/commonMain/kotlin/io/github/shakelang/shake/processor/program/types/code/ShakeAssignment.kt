package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeAssignmentType : ShakeValue, ShakeStatement {
    val variablePointer: Pointer<ShakeAssignable>
    val variable: ShakeAssignable
    val value: ShakeValue

    companion object {
        fun from(
            scope: ShakeScope,
            assignment: ShakeAssignmentType,
        ): ShakeAssignmentType {
            return when (assignment) {
                is ShakeAssignment -> ShakeAssignment.from(scope, assignment)
                is ShakeAddAssignment -> ShakeAddAssignment.from(scope, assignment)
                is ShakeSubAssignment -> ShakeSubAssignment.from(scope, assignment)
                is ShakeMulAssignment -> ShakeMulAssignment.from(scope, assignment)
                is ShakeDivAssignment -> ShakeDivAssignment.from(scope, assignment)
                is ShakeModAssignment -> ShakeModAssignment.from(scope, assignment)
                is ShakePowAssignment -> ShakePowAssignment.from(scope, assignment)
                else -> {
                    throw IllegalArgumentException("Unknown assignment type: ${assignment::class.simpleName}")
                }
            }
        }
    }
}

interface ShakeAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakeAssignment): ShakeAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakeAddAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakeAddAssignment): ShakeAddAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakeSubAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakeSubAssignment): ShakeSubAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakeMulAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakeMulAssignment): ShakeMulAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakeDivAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakeDivAssignment): ShakeDivAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakeModAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakeModAssignment): ShakeModAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakePowAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, assignment: ShakePowAssignment): ShakePowAssignment {
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type))
        }
    }
}

interface ShakeMutateType : ShakeValue, ShakeStatement {
    val variablePointer: Pointer<ShakeAssignable>
    val variable: ShakeAssignable

    companion object {
        fun from(scope: ShakeScope, mutate: ShakeMutateType): ShakeMutateType {
            return when (mutate) {
                is ShakeIncrementBefore -> ShakeIncrementBefore.from(scope, mutate)
                is ShakeIncrementAfter -> ShakeIncrementAfter.from(scope, mutate)
                is ShakeDecrementBefore -> ShakeDecrementBefore.from(scope, mutate)
                is ShakeDecrementAfter -> ShakeDecrementAfter.from(scope, mutate)
                else -> throw IllegalArgumentException("Unknown mutate type: ${mutate::class.simpleName}")
            }
        }

    }
}

interface ShakeIncrementBefore : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, mutate: ShakeIncrementBefore): ShakeIncrementBefore {
            return Impl(scope, ShakeAssignable.from(scope, mutate.variable), ShakeType.from(scope.project, mutate.type))
        }
    }
}

interface ShakeIncrementAfter : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, mutate: ShakeIncrementAfter): ShakeIncrementAfter {
            return Impl(scope, ShakeAssignable.from(scope, mutate.variable), ShakeType.from(scope.project, mutate.type))
        }
    }
}

interface ShakeDecrementBefore : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, mutate: ShakeDecrementBefore): ShakeDecrementBefore {
            return Impl(scope, ShakeAssignable.from(scope, mutate.variable), ShakeType.from(scope.project, mutate.type))
        }
    }
}

interface ShakeDecrementAfter : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
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
        fun from(scope: ShakeScope, mutate: ShakeDecrementAfter): ShakeDecrementAfter {
            return Impl(scope, ShakeAssignable.from(scope, mutate.variable), ShakeType.from(scope.project, mutate.type))
        }
    }
}
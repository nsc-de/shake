package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeAssignmentType : ShakeValue, ShakeStatement {
    val variablePointer: Pointer<ShakeAssignable>
    val variable: ShakeAssignable
    val value: ShakeValue
    val receiver: ShakeValue?

    companion object {
        fun from(
            scope: ShakeScope.ShakeScopeImpl,
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
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakeAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakeAddAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakeAddAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeAddAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakeSubAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakeSubAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeSubAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakeMulAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakeMulAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeMulAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakeDivAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakeDivAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeDivAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakeModAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakeModAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeModAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakePowAssignment : ShakeAssignmentType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val value: ShakeValue,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, ShakeAssignable.from(scope, assignment.variable), ShakeValue.from(scope, assignment.value), ShakeType.from(scope.project, assignment.type), assignment.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, value: ShakeValue, type: ShakeType, receiver: ShakeValue? = null): ShakePowAssignment {
            return create(scope, variable.point(), value, type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, value: ShakeValue, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakePowAssignment {
            return Impl(scope, variable, value, type, receiver)
        }
    }
}

interface ShakeMutateType : ShakeValue, ShakeStatement {
    val variablePointer: Pointer<ShakeAssignable>
    val variable: ShakeAssignable
    val receiver: ShakeValue?

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
        override val receiver: ShakeValue?
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
            return Impl(scope, mutate.variablePointer, ShakeType.from(scope.project, mutate.type), mutate.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, type: ShakeType, receiver: ShakeValue? = null): ShakeIncrementBefore {
            return create(scope, variable.point(), type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeIncrementBefore {
            return Impl(scope, variable, type, receiver)
        }
    }
}

interface ShakeIncrementAfter : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, mutate.variablePointer, ShakeType.from(scope.project, mutate.type), mutate.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, type: ShakeType, receiver: ShakeValue? = null): ShakeIncrementAfter {
            return create(scope, variable.point(), type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeIncrementAfter {
            return Impl(scope, variable, type, receiver)
        }
    }
}

interface ShakeDecrementBefore : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, mutate.variablePointer, ShakeType.from(scope.project, mutate.type), mutate.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, type: ShakeType, receiver: ShakeValue? = null): ShakeDecrementBefore {
            return create(scope, variable.point(), type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeDecrementBefore {
            return Impl(scope, variable, type, receiver)
        }
    }
}

interface ShakeDecrementAfter : ShakeMutateType {
    class Impl(
        override val scope: ShakeScope,
        override val variablePointer: Pointer<ShakeAssignable>,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue?
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
            return Impl(scope, mutate.variablePointer, ShakeType.from(scope.project, mutate.type), mutate.receiver?.let { ShakeValue.from(scope, it) })
        }

        fun create(scope: ShakeScope, variable: ShakeAssignable, type: ShakeType, receiver: ShakeValue? = null): ShakeDecrementAfter {
            return create(scope, variable.point(), type.point(), receiver)
        }

        fun create(scope: ShakeScope, variable: Pointer<ShakeAssignable>, type: Pointer<ShakeType>, receiver: ShakeValue? = null): ShakeDecrementAfter {
            return Impl(scope, variable, type, receiver)
        }
    }
}
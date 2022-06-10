package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.*
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeUsage : ShakeValue {
    val declaration: ShakeDeclaration
    val receiver: ShakeValue?

    class Impl(
        override val scope: ShakeScope,
        override val declaration: ShakeDeclaration,
        override val typePointer: Pointer<ShakeType>,
        override val receiver: ShakeValue? = null
    ) : ShakeUsage {
        override val type: ShakeType
            get() = typePointer.value
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "usage",
                "declaration" to declaration.toJson(),
                "receiver" to receiver?.toJson()
            )
        }
    }

    companion object {
        fun create(scope: ShakeScope, assignable: ShakeAssignable, receiver: ShakeValue? = null): ShakeValue {
            return Impl(scope, assignable.declaration, assignable.typePointer, receiver)
        }
    }
}
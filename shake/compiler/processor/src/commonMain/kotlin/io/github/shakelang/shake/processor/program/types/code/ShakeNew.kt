package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeConstructor
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.notNull
import io.github.shakelang.shake.processor.util.point

interface ShakeNew : ShakeValue, ShakeStatement {

    val referencePointer: Pointer<ShakeConstructor>
    val reference: ShakeConstructor
    val arguments: List<ShakeValue>
    val parent: ShakeValue?
    val name: String

    class Impl(
        override val scope: ShakeScope,
        override val referencePointer: Pointer<ShakeConstructor>,
        override val arguments: List<ShakeValue>,
        override val parent: ShakeValue?,
        override val name: String,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeNew {
        override val reference: ShakeConstructor get() = referencePointer.value
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "reference" to reference.toJson(),
                "arguments" to arguments.map { it.toJson() },
                "parent" to parent?.toJson(),
                "name" to name,
                "type" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeNew): ShakeNew {
            return Impl(
                scope,
                scope.project.getConstructorBySignature(it.reference.signature).notNull(),
                it.arguments.map { ShakeValue.from(scope, it) },
                it.parent?.let { ShakeValue.from(scope, it) },
                it.name,
                it.typePointer
            )
        }

        fun create(scope: ShakeScope, constructor: ShakeConstructor, args: List<ShakeValue>, type: ShakeType): ShakeNew {
            return create(scope, constructor, args, type.point())
        }

        fun create(scope: ShakeScope, constructor: ShakeConstructor, args: List<ShakeValue>, type: Pointer<ShakeType>): ShakeNew {
            return Impl(
                scope,
                constructor.point(),
                args,
                null,
                "",
                type
            )
        }

        fun create(scope: ShakeScope, constructor: Pointer<ShakeConstructor>, args: List<ShakeValue>, type: ShakeType): ShakeNew {
            return create(scope, constructor, args, type.point())
        }

        fun create(scope: ShakeScope, constructor: Pointer<ShakeConstructor>, args: List<ShakeValue>, type: Pointer<ShakeType>): ShakeNew {
            return Impl(
                scope,
                constructor,
                args,
                null,
                "",
                type
            )
        }
    }
}
package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeCast : ShakeValue {
    val castTargetPointer: Pointer<ShakeType>
    val castTarget: ShakeType
    val value: ShakeValue

    class Impl(
        override val scope: ShakeScope,
        override val value: ShakeValue,
        override val castTargetPointer: Pointer<ShakeType>
    ) : ShakeCast {

        override val castTarget: ShakeType
            get() = castTargetPointer.value

        override val typePointer: Pointer<ShakeType> get() = value.typePointer
        override val type: ShakeType get() = castTarget


        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "value" to value.toJson(),
                "castTarget" to castTarget.toJson(),
                "type" to type.toJson()
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeCast): ShakeCast {
            return Impl(scope, ShakeValue.from(scope, it.value), ShakeType.from(scope, it.castTarget))
        }

        fun create(scope: ShakeScope, value: ShakeValue, target: Pointer<ShakeType>): ShakeCast {
            return Impl(scope, value, target)
        }

        fun create(scope: ShakeScope, value: ShakeValue, target: ShakeType): ShakeCast {
            return create(scope, value, target.point())
        }
    }
}
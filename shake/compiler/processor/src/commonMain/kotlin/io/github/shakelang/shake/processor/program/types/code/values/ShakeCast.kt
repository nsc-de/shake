package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeCast : ShakeValue {
    val castTargetPointer: Pointer<ShakeType>
    val castTarget: ShakeType
    val value: ShakeValue

    class Impl(override val value: ShakeValue, override val castTargetPointer: Pointer<ShakeType>) : ShakeCast {

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
        fun from(prj: ShakeProject, it: ShakeCast): ShakeCast {
            return Impl(ShakeValue.from(prj, it.value), ShakeType.from(prj, it.castTarget))
        }
    }
}
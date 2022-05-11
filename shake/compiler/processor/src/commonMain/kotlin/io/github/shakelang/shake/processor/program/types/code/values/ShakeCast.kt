package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeType

interface ShakeCast : ShakeValue {
    val value: ShakeValue
    val castTarget: ShakeType

    class Impl(override val value: ShakeValue, override val castTarget: ShakeType) : ShakeCast {
        override val type: ShakeType = castTarget
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
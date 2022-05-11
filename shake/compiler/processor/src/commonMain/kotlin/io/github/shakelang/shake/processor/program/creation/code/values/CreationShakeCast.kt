package io.github.shakelang.shake.processor.program.creation.code.values

import io.github.shakelang.shake.processor.program.creation.CreationShakeType
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

class CreationShakeCast(
    val value: CreationShakeValue,
    val castTarget: ShakeType,
) : CreationShakeValue, ShakeValue {
    override val type: ShakeType
        get() = castTarget

    override fun toJson(): Map<String, Any?> {
        return mapOf(
            "type" to "cast",
            "value" to value.toJson(),
            "castTarget" to castTarget.toJson(),
        )
    }
}
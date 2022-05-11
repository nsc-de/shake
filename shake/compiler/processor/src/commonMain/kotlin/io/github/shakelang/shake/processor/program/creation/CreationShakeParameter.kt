package io.github.shakelang.shake.processor.program.creation

import io.github.shakelang.shake.processor.program.types.ShakeParameter
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

class CreationShakeParameter (
    override val name: String,
): ShakeParameter {

    override val qualifiedName: String get() = "parameter $name"

    val actualType: ShakeType
        get() = TODO("Not yet implemented")

    override fun assignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun additionAssignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun subtractionAssignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun multiplicationAssignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun divisionAssignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun modulusAssignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun powerAssignType(other: ShakeType): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun incrementBeforeType(): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun incrementAfterType(): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun decrementBeforeType(): ShakeType? {
        TODO("Not yet implemented")
    }

    override fun decrementAfterType(): ShakeType? {
        TODO("Not yet implemented")
    }

    val actualValue: ShakeValue
        get() = TODO("Not yet implemented")

    constructor(name: String, type: CreationShakeType): this(name) {
        this.type = type
    }

    override lateinit var type: ShakeType
        private set

    fun lateinitType(): (ShakeType) -> ShakeType {
        return {
            type = it
            it
        }
    }

    override fun toJson(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "type" to type.toJson(),
        )
    }
}
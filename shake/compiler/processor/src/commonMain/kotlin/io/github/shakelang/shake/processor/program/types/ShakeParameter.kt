package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeParameter : ShakeAssignable {
    val name: String
    override val type: ShakeType
    fun toJson(): Map<String, Any?>

    class Impl(override val name: String, override val type: ShakeType) : ShakeParameter {

        override val qualifiedName: String get() = "parameter $name"
        override fun toJson(): Map<String, Any?> = mapOf("name" to name, "type" to type.toJson())
        override val actualValue: ShakeValue? get() = null
        override val actualType: ShakeType get() = type
        override fun assignType(other: ShakeType): ShakeType? = type.assignType(other)
        override fun additionAssignType(other: ShakeType): ShakeType? = type.additionAssignType(other)
        override fun subtractionAssignType(other: ShakeType): ShakeType? = type.subtractionAssignType(other)
        override fun multiplicationAssignType(other: ShakeType): ShakeType? = type.multiplicationAssignType(other)
        override fun divisionAssignType(other: ShakeType): ShakeType? = type.divisionAssignType(other)
        override fun modulusAssignType(other: ShakeType): ShakeType? = type.modulusAssignType(other)
        override fun powerAssignType(other: ShakeType): ShakeType? = type.powerAssignType(other)
        override fun incrementBeforeType(): ShakeType? = type.incrementBeforeType()
        override fun incrementAfterType(): ShakeType? = type.incrementAfterType()
        override fun decrementBeforeType(): ShakeType? = type.decrementBeforeType()
        override fun decrementAfterType(): ShakeType? = type.decrementAfterType()
    }
}
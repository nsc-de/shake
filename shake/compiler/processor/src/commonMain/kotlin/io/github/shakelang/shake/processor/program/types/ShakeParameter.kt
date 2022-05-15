package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeParameter : ShakeAssignable {
    val name: String
    fun toJson(): Map<String, Any?>

    class Impl : ShakeParameter {

        override val name: String
        override val typePointer: Pointer<ShakeType>

        constructor(
            name: String,
            type: ShakeType
        ) {
            this.name = name
            this.typePointer = type.point()
        }

        internal constructor(
            prj: ShakeProject,
            parameter: ShakeParameter
        ) {
            this.name = parameter.name
            this.typePointer = ShakeType.from(prj, parameter.type)
        }

        override val type: ShakeType get() = typePointer.value

        override val qualifiedName: String get() = "parameter $name"
        override fun toJson(): Map<String, Any?> = mapOf("name" to name, "type" to type.toJson())
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

    companion object {
        fun from(prj: ShakeProject, it: ShakeParameter): ShakeParameter = Impl(prj, it)
    }
}
package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.values.ShakeUsage
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeParameter : ShakeAssignable, ShakeDeclaration {

    override val name: String
    override fun toJson(): Map<String, Any?>

    override val declaration: ShakeDeclaration get() = this

    class Impl : ShakeParameter {

        override val name: String
        override val typePointer: Pointer<ShakeType>

        constructor(
            name: String,
            type: Pointer<ShakeType>
        ) {
            this.name = name
            this.typePointer = type
        }

        constructor(
            name: String,
            type: ShakeType
        ) : this(name, type.point())

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

        override fun access(scope: ShakeScope, receiver: ShakeValue?): ShakeValue {
            if(receiver != null) throw IllegalArgumentException("Parameter $name cannot be accessed with a receiver")
            return ShakeUsage.create(scope, this)
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeParameter) = Impl(prj, it)

        fun create(name: String, type: Pointer<ShakeType>) = Impl(name, type)
    }
}
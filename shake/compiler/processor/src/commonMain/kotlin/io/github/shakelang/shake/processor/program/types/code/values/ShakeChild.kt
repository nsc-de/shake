package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeChild : ShakeAssignable {
    val scope: ShakeScope
    val parent : ShakeValue
    val name: String
    val access : ShakeValue

    class Impl(
        override val scope: ShakeScope,
        override val parent: ShakeValue,
        override val name: String,
        override val access: ShakeValue,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeChild {
        override val type: ShakeType get() = typePointer.value
        override val qualifiedName: String get() = "${parent.type.name}.$name"

        override fun assignType(other: ShakeType): ShakeType {
            return this.type.assignType(other) ?: other
        }

        override fun additionAssignType(other: ShakeType): ShakeType? {
            return this.type.additionAssignType(other) ?: this.type.additionType(other)
        }

        override fun subtractionAssignType(other: ShakeType): ShakeType? {
            return this.type.subtractionAssignType(other) ?: this.type.subtractionType(other)
        }

        override fun multiplicationAssignType(other: ShakeType): ShakeType? {
            return this.type.multiplicationAssignType(other) ?: this.type.multiplicationType(other)
        }

        override fun divisionAssignType(other: ShakeType): ShakeType? {
            return this.type.divisionAssignType(other) ?: this.type.divisionType(other)
        }

        override fun modulusAssignType(other: ShakeType): ShakeType? {
            return this.type.modulusAssignType(other) ?: this.type.modulusType(other)
        }

        override fun powerAssignType(other: ShakeType): ShakeType? {
            return this.type.powerAssignType(other) ?: this.type.powerType(other)
        }

        override fun incrementBeforeType(): ShakeType? {
            return this.type.incrementBeforeType()
        }

        override fun incrementAfterType(): ShakeType? {
            return this.type.incrementAfterType()
        }

        override fun decrementBeforeType(): ShakeType? {
            return this.type.decrementBeforeType()
        }

        override fun decrementAfterType(): ShakeType? {
            return this.type.decrementAfterType()
        }

    }
}

interface ShakeChildUsage : ShakeUsage {
    val used: ShakeChild
}
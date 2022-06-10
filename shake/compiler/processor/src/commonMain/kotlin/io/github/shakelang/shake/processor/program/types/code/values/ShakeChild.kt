package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeChild : ShakeAssignable {
    val scope: ShakeScope
    val parent : ShakeValue
    val name: String

    override val declaration: ShakeDeclaration

    fun toJson(): Map<String, Any>

    class Impl(
        override val scope: ShakeScope,
        override val parent: ShakeValue,
        override val name: String,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeChild {
        override val type: ShakeType get() = typePointer.value
        override val qualifiedName: String get() = "${parent.type.name}.$name"
        override val declaration: ShakeDeclaration get() {
            val parentType = parent.type
            if(parentType is ShakeType.Object) {
                return parentType.clazz.getFieldByName(name) ?: error("Field $name not found in ${parentType.name}")
            }
            error("Cannot get declaration of child $name of non-object parent ${parentType.name}")
        }

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

        override fun access(scope: ShakeScope, receiver: ShakeValue?): ShakeValue {
            if(receiver != null) throw IllegalArgumentException("Cannot access child $name of ${receiver.type.name}")
            return ShakeUsage.create(scope, this, receiver)
        }

        override fun toJson(): Map<String, Any> {
            return mapOf(
                "type" to "child",
                "name" to name,
                "type" to type.toJson()
            )
        }

    }

    companion object {
        fun create(scope: ShakeScope, parent: ShakeValue, name: String, type: Pointer<ShakeType>): ShakeChild {
            return Impl(scope, parent, name, type)
        }

        fun create(scope: ShakeScope, parent: ShakeValue, name: String, type: ShakeType): ShakeChild {
            return create(scope, parent, name, type.point())
        }
    }
}
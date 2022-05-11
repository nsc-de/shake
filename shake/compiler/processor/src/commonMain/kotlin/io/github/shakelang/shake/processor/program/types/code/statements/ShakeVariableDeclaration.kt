package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeVariableDeclaration : ShakeDeclaration, ShakeAssignable, ShakeStatement {
    val scope: ShakeScope
    val initialValue: ShakeValue?
    val latestValue: ShakeValue?
    val latestType: ShakeType
    val isFinal: Boolean

    fun valueCompatible(value: ShakeValue): Boolean

    class Impl(
        override val scope: ShakeScope,
        override val name: String,
        override val initialValue: ShakeValue?,
        override val isFinal: Boolean,
        override val type: ShakeType,
        override val latestValue: ShakeValue?,
        override val latestType: ShakeType
    ) : ShakeVariableDeclaration {

        override val qualifiedName: String get() = "local $name"

        override fun valueCompatible(value: ShakeValue): Boolean {
            return value.type.compatibleTo(this.type)
        }

        override fun assignType(other: ShakeType): ShakeType {
            return type.assignType(other) ?: this.type
        }

        override fun additionAssignType(other: ShakeType): ShakeType? {
            return type.additionAssignType(other) ?: if(type.additionType(other) != null) type else null
        }

        override fun subtractionAssignType(other: ShakeType): ShakeType? {
            return type.subtractionAssignType(other) ?: if(type.subtractionType(other) != null) type else null
        }

        override fun multiplicationAssignType(other: ShakeType): ShakeType? {
            return type.multiplicationAssignType(other) ?: if(type.multiplicationType(other) != null) type else null
        }

        override fun divisionAssignType(other: ShakeType): ShakeType? {
            return type.divisionAssignType(other) ?: if(type.divisionType(other) != null) type else null
        }

        override fun modulusAssignType(other: ShakeType): ShakeType? {
            return type.modulusAssignType(other) ?: if(type.modulusType(other) != null) type else null
        }

        override fun powerAssignType(other: ShakeType): ShakeType? {
            return type.powerAssignType(other) ?: if(type.powerType(other) != null) type else null
        }

        override fun incrementBeforeType(): ShakeType {
            return type.incrementBeforeType() ?: type
        }

        override fun incrementAfterType(): ShakeType {
            return type.incrementAfterType() ?: this.type
        }

        override fun decrementBeforeType(): ShakeType {
            return type.decrementBeforeType() ?: this.type
        }

        override fun decrementAfterType(): ShakeType {
            return type.decrementAfterType() ?: this.type
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "name" to name,
                "initialValue" to initialValue,
                "isFinal" to isFinal,
                "type" to type,
                "latestValue" to latestValue,
                "latestType" to latestType
            )
        }
    }

}
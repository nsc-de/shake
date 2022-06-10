package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.values.ShakeUsage
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeVariableDeclaration : ShakeDeclaration, ShakeAssignable, ShakeStatement {
    override val scope: ShakeScope
    val initialValue: ShakeValue?
    val latestValue: ShakeValue?
    val latestType: ShakeType
    val isFinal: Boolean

    override val declaration: ShakeDeclaration get() = this

    fun valueCompatible(value: ShakeValue): Boolean

    class Impl : ShakeVariableDeclaration {

        override val scope: ShakeScope
        override val name: String
        override val initialValue: ShakeValue?
        override val isFinal: Boolean
        override val latestValue: ShakeValue?
        override val latestType: ShakeType
        override val typePointer: Pointer<ShakeType>

        override val type: ShakeType get() = typePointer.value
        override val qualifiedName: String get() = "local $name"

        constructor(
            scope: ShakeScope,
            name: String,
            initialValue: ShakeValue?,
            isFinal: Boolean,
            type: ShakeType
        ) {
            this.scope = scope
            this.name = name
            this.initialValue = initialValue
            this.isFinal = isFinal
            this.latestValue = initialValue
            this.latestType = type
            this.typePointer = type.point()
        }

        constructor(
            scope: ShakeScope,
            name: String,
            initialValue: ShakeValue?,
            isFinal: Boolean,
            typePointer: Pointer<ShakeType>
        ) {
            this.scope = scope
            this.name = name
            this.initialValue = initialValue
            this.isFinal = isFinal
            this.latestValue = initialValue
            this.latestType = typePointer.value
            this.typePointer = typePointer
        }

        constructor(
            scope: ShakeScope,
            it: ShakeVariableDeclaration
        ) {
            this.scope = ShakeScope.from(scope, it.scope).value ?: throw IllegalStateException("Scope not found")
            this.name = it.name
            this.initialValue = it.initialValue?.let { it1 -> ShakeValue.from(scope, it1) }
            this.isFinal = it.isFinal
            this.latestValue = it.latestValue
            this.latestType = it.latestType
            this.typePointer = ShakeType.from(scope, it.type)
        }

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

        override fun access(scope: ShakeScope, receiver: ShakeValue?): ShakeValue {
            if(receiver != null) throw IllegalArgumentException("Cannot access variable with receiver")
            return ShakeUsage.create(scope, this)
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

    companion object {
        fun from(scope: ShakeScope, it: ShakeVariableDeclaration): ShakeVariableDeclaration {
            return Impl(scope, it)
        }

        fun create(scope: ShakeScope, name: String, type: ShakeType, value: ShakeValue?, final: Boolean): ShakeVariableDeclaration {
            return Impl(scope, name, value, final, type)
        }
    }
}
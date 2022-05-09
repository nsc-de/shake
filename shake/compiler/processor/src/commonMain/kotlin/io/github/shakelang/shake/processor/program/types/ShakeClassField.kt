package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeClassField : ShakeField {
    val clazz: ShakeClass
    override val qualifiedName: String

    class Impl(
        override val clazz: ShakeClass,
        override val name: String,
        override val actualValue: ShakeValue?,
        override val actualType: ShakeType,
        override val type: ShakeType,
        override val project: ShakeProject,
        override val pkg: ShakePackage?,
        override val parentScope: ShakeScope,
        override val isStatic: Boolean,
        override val isFinal: Boolean,
        override val isAbstract: Boolean,
        override val isPrivate: Boolean,
        override val isProtected: Boolean,
        override val isPublic: Boolean,
        override val initialValue: ShakeValue?,
    ) : ShakeClassField {

        override val qualifiedName: String = "${clazz.qualifiedName}.$name"

        override fun assignType(other: ShakeType): ShakeType? {
            return type.assignType(other) ?: this.type
        }

        override fun additionAssignType(other: ShakeType): ShakeType {
            return type.additionAssignType(other) ?: if(type.additionType(other) != null) this.type else other
        }

        override fun subtractionAssignType(other: ShakeType): ShakeType {
            return type.subtractionAssignType(other) ?: if(type.subtractionType(other) != null) this.type else other
        }

        override fun multiplicationAssignType(other: ShakeType): ShakeType {
            return type.multiplicationAssignType(other) ?: if(type.multiplicationType(other) != null) this.type else other
        }

        override fun divisionAssignType(other: ShakeType): ShakeType {
            return type.divisionAssignType(other) ?: if(type.divisionType(other) != null) this.type else other
        }

        override fun modulusAssignType(other: ShakeType): ShakeType {
            return type.modulusAssignType(other) ?: if(type.modulusType(other) != null) this.type else other
        }

        override fun powerAssignType(other: ShakeType): ShakeType {
            return type.powerAssignType(other) ?: if(type.powerType(other) != null) this.type else other
        }

        override fun incrementBeforeType(): ShakeType {
            return type.incrementBeforeType() ?: this.type // TODO not available cases
        }

        override fun incrementAfterType(): ShakeType {
            return type.incrementAfterType() ?: this.type // TODO not available cases
        }

        override fun decrementBeforeType(): ShakeType {
            return type.decrementBeforeType() ?: this.type // TODO not available cases
        }

        override fun decrementAfterType(): ShakeType {
            return type.decrementAfterType() ?: this.type // TODO not available cases
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "clazz" to clazz.qualifiedName,
                "name" to name,
                "actualValue" to actualValue?.toJson(),
                "actualType" to actualType.toJson(),
                "type" to type.toJson(),
                "isStatic" to isStatic,
                "isFinal" to isFinal,
                "isAbstract" to isAbstract,
                "isPrivate" to isPrivate,
                "isProtected" to isProtected,
                "isPublic" to isPublic,
                "initialValue" to initialValue?.toJson(),
            )
        }
    }

    companion object {
        fun from(clazz: ShakeClass, it: ShakeClassField): ShakeClassField = TODO()
    }
}
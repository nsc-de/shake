package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeClassField : ShakeField {
    val clazz: ShakeClass
    override val qualifiedName: String

    class Impl : ShakeClassField {
        override val clazz: ShakeClass
        override val name: String
        override val typePointer: Pointer<ShakeType>
        override val type: ShakeType get() = typePointer.value
        override val project: ShakeProject
        override val pkg: ShakePackage?
        override val scope: ShakeScope
        override val isStatic: Boolean
        override val isFinal: Boolean
        override val isAbstract: Boolean
        override val isPrivate: Boolean
        override val isProtected: Boolean
        override val isPublic: Boolean
        override val initialValue: ShakeValue?

        override val signature: String
        override val qualifiedName: String

        constructor(
            clazz: ShakeClass,
            name: String,
            actualValue: ShakeValue?,
            actualType: ShakeType,
            type: ShakeType,
            project: ShakeProject,
            pkg: ShakePackage?,
            parentScope: ShakeScope,
            isStatic: Boolean,
            isFinal: Boolean,
            isAbstract: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            initialValue: ShakeValue?
        ) {
            this.clazz = clazz
            this.name = name
            this.typePointer = type.point()
            this.project = project
            this.pkg = pkg
            this.scope = parentScope
            this.isStatic = isStatic
            this.isFinal = isFinal
            this.isAbstract = isAbstract
            this.isPrivate = isPrivate
            this.isProtected = isProtected
            this.isPublic = isPublic
            this.initialValue = initialValue
            this.signature = "${clazz.signature}#$name"
            this.qualifiedName = "${clazz.qualifiedName}.$name"
        }

        constructor(
            clazz: ShakeClass,
            parentScope: ShakeScope,
            it: ShakeClassField
        ) {
            this.clazz = clazz
            this.name = it.name
            this.typePointer = ShakeType.from(clazz.prj, it.type)
            this.project = clazz.prj
            this.pkg = clazz.pkg
            this.scope = parentScope
            this.isStatic = it.isStatic
            this.isFinal = it.isFinal
            this.isAbstract = it.isAbstract
            this.isPrivate = it.isPrivate
            this.isProtected = it.isProtected
            this.isPublic = it.isPublic
            this.initialValue = it.initialValue // TODO: copy initial value
            this.signature = "${clazz.signature}#$name"
            this.qualifiedName = "${clazz.qualifiedName}.$name"
        }

        override fun assignType(other: ShakeType): ShakeType {
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
        fun from(clazz: ShakeClass, scope: ShakeScope, it: ShakeClassField): ShakeClassField = Impl(clazz, scope, it)
    }
}
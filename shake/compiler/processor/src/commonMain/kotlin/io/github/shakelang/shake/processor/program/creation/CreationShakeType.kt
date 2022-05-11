package io.github.shakelang.shake.processor.program.creation

import io.github.shakelang.shake.processor.program.types.ShakeType

abstract class CreationShakeType (
    override val name: String,
): ShakeType {

    override fun assignType(other: ShakeType): CreationShakeType? = null
    override fun additionAssignType(other: ShakeType): CreationShakeType? = null
    override fun subtractionAssignType(other: ShakeType): CreationShakeType? = null
    override fun multiplicationAssignType(other: ShakeType): CreationShakeType? = null
    override fun divisionAssignType(other: ShakeType): CreationShakeType? = null
    override fun modulusAssignType(other: ShakeType): CreationShakeType? = null
    override fun powerAssignType(other: ShakeType): CreationShakeType? = null
    override fun incrementBeforeType(): CreationShakeType? = null
    override fun incrementAfterType(): CreationShakeType? = null
    override fun decrementBeforeType(): CreationShakeType? = null
    override fun decrementAfterType(): CreationShakeType? = null

    abstract override fun additionType(other: ShakeType): CreationShakeType?
    abstract override fun subtractionType(other: ShakeType): CreationShakeType?
    abstract override fun multiplicationType(other: ShakeType): CreationShakeType?
    abstract override fun divisionType(other: ShakeType): CreationShakeType?
    abstract override fun modulusType(other: ShakeType): CreationShakeType?
    abstract override fun powerType(other: ShakeType): CreationShakeType?
    override fun equalsType(other: ShakeType): ShakeType? = Primitives.BOOLEAN
    override fun notEqualsType(other: ShakeType): ShakeType? = Primitives.BOOLEAN
    abstract override fun greaterThanType(other: ShakeType): ShakeType?
    abstract override fun greaterThanOrEqualType(other: ShakeType): ShakeType?
    abstract override fun lessThanType(other: ShakeType): ShakeType?
    abstract override fun lessThanOrEqualType(other: ShakeType): ShakeType?
    abstract override fun andType(other: ShakeType): ShakeType?
    abstract override fun orType(other: ShakeType): ShakeType?
    abstract override fun notType(): ShakeType?
    override fun childType(name: String): ShakeType? = null
    override fun childFunctions(name: String): List<CreationShakeFunction>? = null
    override fun childInvokable(name: String): List<CreationShakeFunction>? = childFunctions(name)

    abstract override val kind: ShakeType.Kind

    abstract override fun castableTo(other: ShakeType): Boolean
    override fun compatibleTo(other: ShakeType): Boolean = compatibilityDistance(other) >= 0
    abstract override fun compatibilityDistance(other: ShakeType): Int

    abstract override fun toJson(): Map<String, Any?>

    class Object (
        val clazz: CreationShakeClass,
        name: String = clazz.qualifiedName,
    ) : CreationShakeType(name) {
        override val signature: String
            get() = "L${clazz.qualifiedName};"
        override fun additionType(other: ShakeType): CreationShakeType? = null
        override fun subtractionType(other: ShakeType): CreationShakeType? = null
        override fun multiplicationType(other: ShakeType): CreationShakeType? = null
        override fun divisionType(other: ShakeType): CreationShakeType? = null
        override fun modulusType(other: ShakeType): CreationShakeType? = null
        override fun powerType(other: ShakeType): CreationShakeType? = null
        override fun equalsType(other: ShakeType): CreationShakeType? = null
        override fun notEqualsType(other: ShakeType): CreationShakeType? = null
        override fun greaterThanType(other: ShakeType): CreationShakeType? = null
        override fun greaterThanOrEqualType(other: ShakeType): CreationShakeType? = null
        override fun lessThanType(other: ShakeType): CreationShakeType? = null
        override fun lessThanOrEqualType(other: ShakeType): CreationShakeType? = null
        override fun andType(other: ShakeType): CreationShakeType? = null
        override fun orType(other: ShakeType): CreationShakeType? = null
        override fun notType(): CreationShakeType? = null

        override fun childType(name: String): ShakeType? = clazz.fields.find { it.name == name }?.type
        override fun childFunctions(name: String): List<CreationShakeFunction> {
            return clazz.methods.filter { it.name == name }
        }

        override val kind: ShakeType.Kind
            get() = ShakeType.Kind.OBJECT

        override fun castableTo(other: ShakeType): Boolean {
            return other is Object && other.clazz.compatibleTo(clazz)
        }

        override fun compatibleTo(other: ShakeType): Boolean {
            return other is Object && clazz.compatibleTo(other.clazz)
        }

        override fun compatibilityDistance(other: ShakeType): Int {
            return if(other is Object) clazz.compatibilityDistance(other.clazz) else -1
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf("type" to "object", "class" to clazz.qualifiedName)
        }
    }

    class Array (
        name: String,
        val elementType: CreationShakeType
    ) : CreationShakeType(name) {
        override val signature: String = "[$name"
        override fun additionType(other: ShakeType): CreationShakeType? = null
        override fun subtractionType(other: ShakeType): CreationShakeType? = null
        override fun multiplicationType(other: ShakeType): CreationShakeType? = null
        override fun divisionType(other: ShakeType): CreationShakeType? = null
        override fun modulusType(other: ShakeType): CreationShakeType? = null
        override fun powerType(other: ShakeType): CreationShakeType? = null
        override fun equalsType(other: ShakeType): CreationShakeType? = null
        override fun notEqualsType(other: ShakeType): CreationShakeType? = null
        override fun greaterThanType(other: ShakeType): CreationShakeType? = null
        override fun greaterThanOrEqualType(other: ShakeType): CreationShakeType? = null
        override fun lessThanType(other: ShakeType): CreationShakeType? = null
        override fun lessThanOrEqualType(other: ShakeType): CreationShakeType? = null
        override fun andType(other: ShakeType): CreationShakeType? = null
        override fun orType(other: ShakeType): CreationShakeType? = null
        override fun notType(): CreationShakeType? = null

        override val kind: ShakeType.Kind
            get() = ShakeType.Kind.ARRAY

        override fun castableTo(other: ShakeType): Boolean {
            return other is Array && other.elementType.castableTo(elementType)
        }

        override fun compatibleTo(other: ShakeType): Boolean {
            return other is Array && elementType.compatibleTo(other.elementType)
        }

        override fun compatibilityDistance(other: ShakeType): Int {
            return if(other is Array) elementType.compatibilityDistance(other.elementType) else -1
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf("type" to "array", "elementType" to elementType.toJson())
        }
    }

    class Lambda (
        name: String,
        val parameters: List<CreationShakeParameter>,
        val returnType: CreationShakeType
    ) : CreationShakeType(name) {
        override val signature: String = "(${parameters.joinToString { it.type.signature }})${returnType.signature}"
        override fun additionType(other: ShakeType): CreationShakeType? = null
        override fun subtractionType(other: ShakeType): CreationShakeType? = null
        override fun multiplicationType(other: ShakeType): CreationShakeType? = null
        override fun divisionType(other: ShakeType): CreationShakeType? = null
        override fun modulusType(other: ShakeType): CreationShakeType? = null
        override fun powerType(other: ShakeType): CreationShakeType? = null
        override fun equalsType(other: ShakeType): CreationShakeType? = null
        override fun notEqualsType(other: ShakeType): CreationShakeType? = null
        override fun greaterThanType(other: ShakeType): CreationShakeType? = null
        override fun greaterThanOrEqualType(other: ShakeType): CreationShakeType? = null
        override fun lessThanType(other: ShakeType): CreationShakeType? = null
        override fun lessThanOrEqualType(other: ShakeType): CreationShakeType? = null
        override fun andType(other: ShakeType): CreationShakeType? = null
        override fun orType(other: ShakeType): CreationShakeType? = null
        override fun notType(): CreationShakeType? = null

        override val kind: ShakeType.Kind
            get() = ShakeType.Kind.LAMBDA

        override fun castableTo(other: ShakeType): Boolean {
            // TODO: Check parameters
            return other is Lambda && other.parameters.size == parameters.size && other.returnType.castableTo(returnType)
        }

        override fun compatibleTo(other: ShakeType): Boolean {
            // TODO: Check parameters
            return other is Lambda && returnType.compatibleTo(other.returnType)
        }

        override fun compatibilityDistance(other: ShakeType): Int {
            // TODO: Check parameters
            return if(other is Lambda) returnType.compatibilityDistance(other.returnType) else -1
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf("type" to "lambda", "parameters" to parameters.map { it.toJson() }, "returnType" to returnType.toJson())
        }
    }

    object Primitives {
        val BOOLEAN = ShakeType.Primitive.BYTE
        val BYTE = ShakeType.Primitive.BYTE
        val SHORT = ShakeType.Primitive.SHORT
        val INT = ShakeType.Primitive.INT
        val LONG = ShakeType.Primitive.LONG
        val FLOAT = ShakeType.Primitive.FLOAT
        val DOUBLE = ShakeType.Primitive.DOUBLE
        val UNSIGNED_BYTE = ShakeType.Primitive.UNSIGNED_BYTE
        val UNSIGNED_SHORT = ShakeType.Primitive.UNSIGNED_SHORT
        val UNSIGNED_INT = ShakeType.Primitive.UNSIGNED_INT
        val UNSIGNED_LONG = ShakeType.Primitive.UNSIGNED_LONG
        val UBYTE = UNSIGNED_BYTE
        val USHORT = UNSIGNED_SHORT
        val UINT = UNSIGNED_INT
        val ULONG = UNSIGNED_LONG
        val CHAR = ShakeType.Primitive.CHAR
        val VOID = ShakeType.Primitive.VOID
    }

    companion object {
        fun array(elementType: CreationShakeType): CreationShakeType {
            return Array("${elementType.name}[]", elementType)
        }
        fun objectType(clazz: CreationShakeClass): CreationShakeType {
            return Object(clazz, clazz.name)
        }
    }
}
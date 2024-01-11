package com.shakelang.shake.bytecode.interpreter.format

import com.shakelang.shake.bytecode.interpreter.format.attribute.Attribute
import com.shakelang.shake.bytecode.interpreter.format.attribute.MutableAttribute
import com.shakelang.shake.bytecode.interpreter.format.pool.ConstantPool
import com.shakelang.shake.bytecode.interpreter.format.pool.ConstantPoolEntry
import com.shakelang.shake.bytecode.interpreter.format.pool.MutableConstantPool
import com.shakelang.util.io.streaming.input.DataInputStream
import com.shakelang.util.io.streaming.output.ByteArrayOutputStream
import com.shakelang.util.io.streaming.output.DataOutputStream
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * A class that represents a class in the bytecode
 *
 * See [Class Specification](https://spec.shakelang.com/bytecode/storage-format#classes)
 *
 * @property pool The [ConstantPool] of the class
 * @property nameConstant The constant of the name of the class
 * @property superNameConstant The constant of the super name of the class
 * @property flags The flags of the class
 * @property interfacesConstants The constants of the interfaces of the class
 * @property subClasses The subclasses of the class
 * @property methods The methods of the class
 * @property fields The fields of the class
 * @property attributes The attributes of the class
 *
 * @constructor Creates a [Class] with the given [pool], [nameConstant], [superNameConstant], [flags], [interfacesConstants], [subClasses], [methods], [fields] and [attributes]
 * @param pool The [ConstantPool] of the class
 * @param nameConstant The constant of the name of the class
 * @param superNameConstant The constant of the super name of the class
 * @param flags The flags of the class
 * @param interfacesConstants The constants of the interfaces of the class
 * @param subClasses The subclasses of the class
 * @param methods The methods of the class
 * @param fields The fields of the class
 * @param attributes The attributes of the class
 *
 * @see MutableClass
 *
 * @since 0.1.0
 * @version 0.1.0
 */
open class Class(

    /**
     * The [ConstantPool] of the class
     *
     * See [ConstantPool Specification](https://spec.shakelang.com/bytecode/storage-format#constant-pool)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val pool: ConstantPool,

    /**
     * The index of a [ConstantPoolEntry.Utf8Constant] constant in the [ConstantPool]
     * that represents the name of the class
     * @see name
     * @since 0.1.0
     * @version 0.1.0
     */
    open val nameConstant: Int,

    /**
     * The index of a [ConstantPoolEntry.Utf8Constant] constant in the [ConstantPool]
     * that represents the super name of the class
     * @see superName
     * @since 0.1.0
     * @version 0.1.0
     */
    open val superNameConstant: Int,

    /**
     * The flags of the class
     *
     * See [Class Specification](https://spec.shakelang.com/bytecode/storage-format#classes)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val flags: Short,

    /**
     * The constants of the interfaces of the class
     *
     * See [Class Specification](https://spec.shakelang.com/bytecode/storage-format#classes)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val interfacesConstants: List<Int>,

    /**
     * The subclasses of the class
     *
     * See [Class Specification](https://spec.shakelang.com/bytecode/storage-format#classes)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val subClasses: List<Class>,

    /**
     * The methods of the class
     *
     * See [Method Specification](https://spec.shakelang.com/bytecode/storage-format#methods)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val methods: List<Method>,

    /**
     * The fields of the class
     *
     * See [Field Specification](https://spec.shakelang.com/bytecode/storage-format#fields)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val fields: List<Field>,

    /**
     * The attributes of the class
     *
     * See [Attribute Specification](https://spec.shakelang.com/bytecode/storage-format#attributes)
     *
     * @since 0.1.0
     * @version 0.1.0
     */
    open val attributes: List<Attribute>,
) {

    /**
     * The name of the class
     *
     * @see nameConstant
     * @since 0.1.0
     * @version 0.1.0
     */
    open val name: String get() = pool.getUtf8(nameConstant).value

    /**
     * The super name of the class
     *
     * @see superNameConstant
     * @since 0.1.0
     * @version 0.1.0
     */
    open val superName: String get() = pool.getUtf8(superNameConstant).value

    /**
     * The interfaces of the class
     *
     * @see interfacesConstants
     * @since 0.1.0
     * @version 0.1.0
     */
    open val interfaces: List<String> get() = interfacesConstants.map { pool.getUtf8(it).value }

    /**
     * Returns if the class is public
     *
     * (Checks if the first bit of the [flags] is set)
     *
     * @see flags
     * @since 0.1.0
     * @version 0.1.0
     */
    open val isPublic: Boolean
        get() = flags and 0b00000000_00000001.toShort() != 0.toShort()

    /**
     * Returns if the class is private
     *
     * (Checks if the second bit of the [flags] is set)
     *
     * @see flags
     * @since 0.1.0
     * @version 0.1.0
     */
    open val isPrivate: Boolean
        get() = flags and 0b00000000_00000010.toShort() != 0.toShort()

    /**
     * Returns if the class is protected
     *
     * (Checks if the third bit of the [flags] is set)
     *
     * @see flags
     * @since 0.1.0
     * @version 0.1.0
     */
    open val isProtected: Boolean
        get() = flags and 0b00000000_00000100.toShort() != 0.toShort()

    /**
     * Returns if the class is static
     *
     * (Checks if the fourth bit of the [flags] is set)
     *
     * @see flags
     * @since 0.1.0
     * @version 0.1.0
     */
    open val isStatic: Boolean
        get() = flags and 0b00000000_00001000.toShort() != 0.toShort()

    /**
     * Returns if the class is final
     *
     * (Checks if the fifth bit of the [flags] is set)
     *
     * @see flags
     * @since 0.1.0
     * @version 0.1.0
     */
    open val isFinal: Boolean
        get() = flags and 0b00000000_00010000.toShort() != 0.toShort()

    open val isInterface: Boolean
        get() = flags and 0b00000000_00100000.toShort() != 0.toShort()

    open val isAbstract: Boolean
        get() = flags and 0b00000000_01000000.toShort() != 0.toShort()

    open val isSynthetic: Boolean
        get() = flags and 0b00000000_10000000.toShort() != 0.toShort()

    open val isAnnotation: Boolean
        get() = flags and 0b00000001_00000000.toShort() != 0.toShort()

    open val isEnum: Boolean
        get() = flags and 0b00000010_00000000.toShort() != 0.toShort()

    open val isObject: Boolean
        get() = flags and 0b00000100_00000000.toShort() != 0.toShort()

    fun dump(stream: DataOutputStream) {
        stream.writeInt(nameConstant)
        stream.writeInt(superNameConstant)
        stream.writeShort(flags)
        stream.writeShort(interfacesConstants.size.toShort())
        for (interfaceConstant in interfacesConstants) stream.writeInt(interfaceConstant)
        stream.writeShort(subClasses.size.toShort())
        for (subClass in subClasses) subClass.dump(stream)
        stream.writeShort(methods.size.toShort())
        for (method in methods) method.dump(stream)
        stream.writeShort(fields.size.toShort())
        for (field in fields) field.dump(stream)
        stream.writeShort(attributes.size.toShort())
        for (attribute in attributes) attribute.dump(stream)
    }

    fun dump(): ByteArray {
        val byteStream = ByteArrayOutputStream()
        val stream = DataOutputStream(byteStream)
        dump(stream)
        return byteStream.toByteArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Class) return false

        if (pool != other.pool) return false
        if (nameConstant != other.nameConstant) return false
        if (superNameConstant != other.superNameConstant) return false
        if (flags != other.flags) return false

        // TODO this is not the best way to do this (O(n^2))

        // find matching interfaces

        for (interfaceConstant in interfacesConstants) {
            if (interfaceConstant !in other.interfacesConstants) return false
        }

        for (interfaceConstant in other.interfacesConstants) {
            if (interfaceConstant !in interfacesConstants) return false
        }

        // find matching fields

        for (field in fields) {
            if (field !in other.fields) return false
        }

        for (field in other.fields) {
            if (field !in fields) return false
        }

        // find matching methods

        for (method in methods) {
            if (method !in other.methods) return false
        }

        for (method in other.methods) {
            if (method !in methods) return false
        }

        // find matching subclasses

        for (subClass in subClasses) {
            if (subClass !in other.subClasses) return false
        }

        for (subClass in other.subClasses) {
            if (subClass !in subClasses) return false
        }

        // find matching attributes

        for (attribute in attributes) {
            if (attribute !in other.attributes) return false
        }

        for (attribute in other.attributes) {
            if (attribute !in attributes) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = pool.hashCode()
        result = 31 * result + nameConstant
        result = 31 * result + superNameConstant
        result = 31 * result + flags
        result = 31 * result + interfacesConstants.hashCode()
        result = 31 * result + fields.hashCode()
        result = 31 * result + methods.hashCode()
        result = 31 * result + subClasses.hashCode()
        result = 31 * result + attributes.hashCode()
        return result
    }

    companion object {
        fun fromStream(pool: ConstantPool, stream: DataInputStream): Class {
            val name = stream.readInt()
            val superName = stream.readInt()
            val flags = stream.readShort()
            val interfacesCount = stream.readShort().toInt()
            val interfaces = (0 until interfacesCount).map { stream.readInt() }
            val subClassesCount = stream.readShort().toInt()
            val subClasses = (0 until subClassesCount).map { fromStream(pool, stream) }
            val methodsCount = stream.readShort().toInt()
            val methods = (0 until methodsCount).map { Method.fromStream(pool, stream) }
            val fieldsCount = stream.readShort().toInt()
            val fields = (0 until fieldsCount).map { Field.fromStream(pool, stream) }
            val attributesCount = stream.readShort().toInt()
            val attributes = (0 until attributesCount).map { Attribute.fromStream(pool, stream) }
            return Class(
                pool,
                name,
                superName,
                flags,
                interfaces,
                subClasses,
                methods,
                fields,
                attributes,
            )
        }
    }
}

class MutableClass(
    override val pool: MutableConstantPool,
    override var nameConstant: Int,
    override var superNameConstant: Int,
    override var flags: Short,
    override var interfacesConstants: MutableList<Int>,
    fields: MutableList<MutableField>,
    methods: MutableList<MutableMethod>,
    subClasses: MutableList<MutableClass>,
    attributes: MutableList<MutableAttribute>,
) : Class(pool, nameConstant, superNameConstant, flags, interfacesConstants, subClasses, methods, fields, attributes) {

    @Suppress("UNCHECKED_CAST")
    override val fields: MutableList<MutableField>
        get() = super.fields as MutableList<MutableField>

    @Suppress("UNCHECKED_CAST")
    override val methods: MutableList<MutableMethod>
        get() = super.methods as MutableList<MutableMethod>

    @Suppress("UNCHECKED_CAST")
    override val subClasses: MutableList<MutableClass>
        get() = super.subClasses as MutableList<MutableClass>

    @Suppress("UNCHECKED_CAST")
    override val attributes: MutableList<MutableAttribute>
        get() = super.attributes as MutableList<MutableAttribute>

    override var isPublic: Boolean
        get() = super.isPublic
        set(value) {
            flags = if (value) flags or 0b00000000_00000001.toShort() else flags and 0b11111111_11111110.toShort()
        }

    override var isPrivate: Boolean
        get() = super.isPrivate
        set(value) {
            flags = if (value) flags or 0b00000000_00000010.toShort() else flags and 0b11111111_11111101.toShort()
        }

    override var isProtected: Boolean
        get() = super.isProtected
        set(value) {
            flags = if (value) flags or 0b00000000_00000100.toShort() else flags and 0b11111111_11111011.toShort()
        }

    override var isStatic: Boolean
        get() = super.isStatic
        set(value) {
            flags = if (value) flags or 0b00000000_00001000.toShort() else flags and 0b11111111_11110111.toShort()
        }

    override var isFinal: Boolean
        get() = super.isFinal
        set(value) {
            flags = if (value) flags or 0b00000000_00010000.toShort() else flags and 0b11111111_11101111.toShort()
        }

    override var isInterface: Boolean
        get() = super.isInterface
        set(value) {
            flags = if (value) flags or 0b00000000_00100000.toShort() else flags and 0b11111111_11011111.toShort()
        }

    override var isAbstract: Boolean
        get() = super.isAbstract
        set(value) {
            flags = if (value) flags or 0b00000000_01000000.toShort() else flags and 0b11111111_10111111.toShort()
        }

    override var isSynthetic: Boolean
        get() = super.isSynthetic
        set(value) {
            flags = if (value) flags or 0b00000000_10000000.toShort() else flags and 0b11111111_01111111.toShort()
        }

    override var isAnnotation: Boolean
        get() = super.isAnnotation
        set(value) {
            flags = if (value) flags or 0b00000001_00000000.toShort() else flags and 0b11111110_11111111.toShort()
        }

    override var isEnum: Boolean
        get() = super.isEnum
        set(value) {
            flags = if (value) flags or 0b00000010_00000000.toShort() else flags and 0b11111101_11111111.toShort()
        }

    override var isObject: Boolean
        get() = super.isObject
        set(value) {
            flags = if (value) flags or 0b00000100_00000000.toShort() else flags and 0b11111011_11111111.toShort()
        }

    override var name: String
        get() = pool.getUtf8(nameConstant).value
        set(value) {
            nameConstant = pool.resolveUtf8(value)
        }

    override var superName: String
        get() = pool.getUtf8(superNameConstant).value
        set(value) {
            superNameConstant = pool.resolveUtf8(value)
        }

    override var interfaces: List<String>
        get() = interfacesConstants.map { pool.getUtf8(it).value }
        set(value) {
            interfacesConstants = value.map { pool.resolveUtf8(it) }.toMutableList()
        }

    companion object {
        fun fromClass(pool: MutableConstantPool, clazz: Class): MutableClass {
            return MutableClass(
                pool,
                clazz.nameConstant,
                clazz.superNameConstant,
                clazz.flags,
                clazz.interfacesConstants.toMutableList(),
                clazz.fields.map { MutableField.fromField(pool, it) }.toMutableList(),
                clazz.methods.map { MutableMethod.fromMethod(pool, it) }.toMutableList(),
                clazz.subClasses.map { fromClass(pool, it) }.toMutableList(),
                clazz.attributes.map { MutableAttribute.fromAttribute(it) }.toMutableList(),
            )
        }

        fun fromStream(pool: MutableConstantPool, stream: DataInputStream): MutableClass {
            val name = stream.readInt()
            val superName = stream.readInt()
            val flags = stream.readShort()
            val interfacesCount = stream.readShort().toInt()
            val interfaces = (0 until interfacesCount).map { stream.readInt() }.toMutableList()
            val fieldsCount = stream.readShort().toInt()
            val fields = (0 until fieldsCount).map { MutableField.fromStream(pool, stream) }.toMutableList()
            val methodsCount = stream.readShort().toInt()
            val methods = (0 until methodsCount).map { MutableMethod.fromStream(pool, stream) }.toMutableList()
            val subClassesCount = stream.readShort().toInt()
            val subClasses = (0 until subClassesCount).map { fromStream(pool, stream) }.toMutableList()
            val attributesCount = stream.readShort().toInt()
            val attributes = (0 until attributesCount).map { MutableAttribute.fromStream(pool, stream) }.toMutableList()
            return MutableClass(
                pool,
                name,
                superName,
                flags,
                interfaces,
                fields,
                methods,
                subClasses,
                attributes,
            )
        }
    }
}

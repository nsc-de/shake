package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeType {

    /**
     * The name of the type.
     */
    val name: String

    /**
     * The signature of the type.
     */
    val signature: String

    /**
     * The kind of this type. [ShakeType.Kind]
     */
    val kind: Kind


    /**
     * When the assign operator is overloaded, this will return the
     * type returned by the assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun assignType(other: ShakeType): ShakeType?

    /**
     * When the add-assign operator is overloaded, this will return the
     * type returned by the add-assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun additionAssignType(other: ShakeType): ShakeType?

    /**
     * When the subtract-assign operator is overloaded, this will return the
     * type returned by the subtract-assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun subtractionAssignType(other: ShakeType): ShakeType?

    /**
     * When the multiply-assign operator is overloaded, this will return the
     * type returned by the multiply-assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun multiplicationAssignType(other: ShakeType): ShakeType?

    /**
     * When the divide-assign operator is overloaded, this will return the
     * type returned by the divide-assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun divisionAssignType(other: ShakeType): ShakeType?

    /**
     * When the modulo-assign operator is overloaded, this will return the
     * type returned by the modulo-assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun modulusAssignType(other: ShakeType): ShakeType?

    /**
     * When the power-assign operator is overloaded, this will return the
     * type returned by the power-assign operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @param other the right-hand side of the assign operator
     * @return the type returned by the assign operator
     */
    fun powerAssignType(other: ShakeType): ShakeType?

    /**
     * When the increment operator is overloaded, this will return the
     * type returned by the increment operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @return the type returned by the assign operator
     */
    fun incrementBeforeType(): ShakeType?

    /**
     * When the increment operator is overloaded, this will return the
     * type returned by the increment operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @return the type returned by the assign operator
     */
    fun incrementAfterType(): ShakeType?

    /**
     * When the decrement operator is overloaded, this will return the
     * type returned by the decrement operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @return the type returned by the assign operator
     */
    fun decrementBeforeType(): ShakeType?

    /**
     * When the decrement operator is overloaded, this will return the
     * type returned by the decrement operator. Otherwise, it will return
     * null.
     * This is only important for operator overloading. You normally can't
     * use the assign operator on a variables value.
     *
     * @return the type returned by the assign operator
     */
    fun decrementAfterType(): ShakeType?

    /**
     * This returns the type of the value returned by the plus operator.
     *
     * @param other the right-hand side of the plus operator
     * @return the type returned by the plus operator
     */
    fun additionType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the minus operator.
     *
     * @param other the right-hand side of the minus operator
     * @return the type returned by the minus operator
     */
    fun subtractionType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the multiply operator.
     *
     * @param other the right-hand side of the multiply operator
     * @return the type returned by the multiply operator
     */
    fun multiplicationType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the divide operator.
     *
     * @param other the right-hand side of the divide operator
     * @return the type returned by the divide operator
     */
    fun divisionType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the modulo operator.
     *
     * @param other the right-hand side of the modulo operator
     * @return the type returned by the modulo operator
     */
    fun modulusType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the power operator.
     *
     * @param other the right-hand side of the power operator
     * @return the type returned by the power operator
     */
    fun powerType(other: ShakeType): ShakeType?


    /**
     * This returns the type of the value returned by the equality operator.
     *
     * @param other the right-hand side of the equality operator
     * @return the type returned by the equality operator
     */
    fun equalsType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the not-equality operator.
     *
     * @param other the right-hand side of the not-equality operator
     * @return the type returned by the not-equality operator
     */
    fun notEqualsType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the greater-than operator.
     *
     * @param other the right-hand side of the greater-than operator
     * @return the type returned by the greater-than operator
     */
    fun greaterThanType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the greater-than-or-equal operator.
     *
     * @param other the right-hand side of the greater-than-or-equal operator
     * @return the type returned by the greater-than-or-equal operator
     */
    fun greaterThanOrEqualType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the less-than operator.
     *
     * @param other the right-hand side of the less-than operator
     * @return the type returned by the less-than operator
     */
    fun lessThanType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the less-than-or-equal operator.
     *
     * @param other the right-hand side of the less-than-or-equal operator
     * @return the type returned by the less-than-or-equal operator
     */
    fun lessThanOrEqualType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the logical-and operator.
     *
     * @param other the right-hand side of the logical-and operator
     * @return the type returned by the logical-and operator
     */
    fun andType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the logical-or operator.
     *
     * @param other the right-hand side of the logical-or operator
     * @return the type returned by the logical-or operator
     */
    fun orType(other: ShakeType): ShakeType?

    /**
     * This returns the type of the value returned by the logical-xor operator.
     *
     * @param other the right-hand side of the logical-xor operator
     * @return the type returned by the logical-xor operator
     */
    fun xorType(other: ShakeType): ShakeType?


    /**
     * This returns the type of the value returned not operator.
     *
     * @return the type returned by the not operator
     */
    fun notType(): ShakeType?

    /**
     * This returns the type of the value returned by the get-child operator.
     *
     * @param name the child to get
     * @return the type returned by the get-child operator
     */
    fun childType(name: String): ShakeType?

    /**
     * This returns a list of functions with a given name.
     *
     * @param name the name of the function
     * @return a list of functions with the given name
     */
    fun childFunctions(name: String): List<ShakeFunctionType>?

    /**
     * This returns a list of invokable functions with a given name.
     *
     * @param name the name of the function
     * @return a list of invokable functions with the given name
     */
    fun childInvokable(name: String): List<ShakeInvokable>?


    /**
     * Is this type castable to the given type?
     *
     * @param other the type to cast to
     * @return true if this type is castable to the given type
     */
    fun castableTo(other: ShakeType): Boolean

    /**
     * Is this type compatible with the given type?
     * For example an integer is compatible with a long, float or double.
     *
     * @param other the type to check compatibility with
     * @return true if this type is compatible with the given type
     */
    fun compatibleTo(other: ShakeType): Boolean

    /**
     * This returns the compatibility distance between this type and the given type.
     * If the types are not compatible, this will return -1.
     * For example an integer is compatible with a long, float or double.
     * The distance to an integer is 0, the distance to a long is 1,
     * the distance to a float is 2 and the distance to a double is 3.
     * This is used to determine which function to call when multiple functions
     * with the same name are compatible.
     *
     * @param other the type to check compatibility with
     * @return the distance between the two types, or -1 if the types are not compatible
     */
    fun compatibilityDistance(other: ShakeType): Int

    /**
     * This returns a json representation of this type.
     */
    fun toJson(): Map<String, Any?>

    /**
     * The different kinds of type.
     */
    enum class Kind {
        PRIMITIVE,
        OBJECT,
        ARRAY,
        LAMBDA,
    }

    /**
     * The different kinds of primitive types.
     */
    enum class PrimitiveType(val signature: String) {
        BOOLEAN("b"),
        BYTE("B"),
        CHAR("C"),
        SHORT("S"),
        INT("I"),
        LONG("L"),
        FLOAT("F"),
        DOUBLE("D"),
        UNSIGNED_BYTE("UB"),
        UNSIGNED_SHORT("US"),
        UNSIGNED_INT("UI"),
        UNSIGNED_LONG("UL"),
        VOID("V");
    }

    /**
     * A primitive type.
     */
    interface Primitive : ShakeType {

        val type: PrimitiveType
        override val kind: Kind get() = Kind.PRIMITIVE
        override val signature: String get() = type.signature

        override fun assignType(other: ShakeType): ShakeType? = null
        override fun additionAssignType(other: ShakeType): ShakeType? = null
        override fun subtractionAssignType(other: ShakeType): ShakeType? = null
        override fun multiplicationAssignType(other: ShakeType): ShakeType? = null
        override fun divisionAssignType(other: ShakeType): ShakeType? = null
        override fun modulusAssignType(other: ShakeType): ShakeType? = null
        override fun powerAssignType(other: ShakeType): ShakeType? = null
        override fun incrementBeforeType(): ShakeType? = null
        override fun incrementAfterType(): ShakeType? = null
        override fun decrementBeforeType(): ShakeType? = null
        override fun decrementAfterType(): ShakeType? = null
        override fun equalsType(other: ShakeType): ShakeType = bool()
        override fun notEqualsType(other: ShakeType): ShakeType? = bool()
        override fun childType(name: String): ShakeType? = null
        override fun childFunctions(name: String): List<ShakeFunction>? = null
        override fun childInvokable(name: String): List<ShakeFunction>? = null
        override fun compatibleTo(other: ShakeType): Boolean = compatibilityDistance(other) >= 0

        override fun castableTo(other: ShakeType): Boolean =
            other is Primitive &&
                    (other.type == PrimitiveType.BYTE
                            || other.type == PrimitiveType.SHORT
                            || other.type == PrimitiveType.INT
                            || other.type == PrimitiveType.LONG
                            || other.type == PrimitiveType.FLOAT
                            || other.type == PrimitiveType.DOUBLE
                            || other.type == PrimitiveType.UNSIGNED_BYTE
                            || other.type == PrimitiveType.UNSIGNED_SHORT
                            || other.type == PrimitiveType.UNSIGNED_INT
                            || other.type == PrimitiveType.UNSIGNED_LONG
                            || other.type == PrimitiveType.CHAR)

        /**
         * This returns a pointer pointing towards this type.
         */
        fun pointer(): Pointer<Primitive>

        companion object {

            /**
             * This returns the primitive boolean type.
             * @return the primitive boolean type
             */
            private fun bool(): Primitive {
                return BOOLEAN
            }

            /**
             * This returns the primitive byte type.
             * @return the primitive byte type
             */
            private fun byte(): Primitive {
                return BYTE
            }

            /**
             * This returns the primitive char type.
             * @return the primitive char type
             */
            private fun short(): Primitive {
                return SHORT
            }

            /**
             * This returns the primitive int type.
             * @return the primitive int type
             */
            private fun int(): Primitive {
                return INT
            }

            /**
             * This returns the primitive long type.
             * @return the primitive long type
             */
            private fun long(): Primitive {
                return LONG
            }

            /**
             * This returns the primitive float type.
             * @return the primitive float type
             */
            private fun float(): Primitive {
                return FLOAT
            }

            /**
             * This returns the primitive double type.
             * @return the primitive double type
             */
            private fun double(): Primitive {
                return DOUBLE
            }

            /**
             * This returns the primitive unsigned byte type.
             * @return the primitive unsigned byte type
             */
            private fun unsignedByte(): Primitive {
                return UNSIGNED_BYTE
            }

            /**
             * This returns the primitive unsigned short type.
             * @return the primitive unsigned short type
             */
            private fun unsignedShort(): Primitive {
                return UNSIGNED_SHORT
            }

            /**
             * This returns the primitive unsigned int type.
             * @return the primitive unsigned int type
             */
            private fun unsignedInt(): Primitive {
                return UNSIGNED_INT
            }

            /**
             * This returns the primitive unsigned long type.
             * @return the primitive unsigned long type
             */
            private fun unsignedLong(): Primitive {
                return UNSIGNED_LONG
            }

            /**
             * This returns the primitive void type.
             * @return the primitive void type
             */
            private fun char(): Primitive {
                return CHAR
            }

            /**
             * This returns the primitive void type.
             * @return the primitive void type
             */
            private fun void(): Primitive {
                return VOID
            }

            /**
             * A pointer pointing towards the primitive boolean type.
             */
            val booleanPointer = bool().point()

            /**
             * A pointer pointing towards the primitive byte type.
             */
            val bytePointer = byte().point()

            /**
             * A pointer pointing towards the primitive char type.
             */
            val shortPointer = short().point()

            /**
             * A pointer pointing towards the primitive int type.
             */
            val intPointer = int().point()

            /**
             * A pointer pointing towards the primitive long type.
             */
            val longPointer = long().point()

            /**
             * A pointer pointing towards the primitive float type.
             */
            val floatPointer = float().point()

            /**
             * A pointer pointing towards the primitive double type.
             */
            val doublePointer = double().point()

            /**
             * A pointer pointing towards the primitive unsigned byte type.
             */
            val unsignedBytePointer = unsignedByte().point()

            /**
             * A pointer pointing towards the primitive unsigned short type.
             */
            val unsignedShortPointer = unsignedShort().point()

            /**
             * A pointer pointing towards the primitive unsigned int type.
             */
            val unsignedIntPointer = unsignedInt().point()

            /**
             * A pointer pointing towards the primitive unsigned long type.
             */
            val unsignedLongPointer = unsignedLong().point()

            /**
             * A pointer pointing towards the primitive char type.
             */
            val charPointer = char().point()

            /**
             * A pointer pointing towards the primitive void type.
             */
            val voidPointer = void().point()


            /**
             * The primitive boolean type.
             */
            val BOOLEAN: Primitive = object : Primitive {
                override val name: String get() = "boolean"
                override val type: PrimitiveType get() = PrimitiveType.BOOLEAN

                override fun additionType(other: ShakeType): ShakeType? = null
                override fun subtractionType(other: ShakeType): ShakeType? = null
                override fun multiplicationType(other: ShakeType): ShakeType? = null
                override fun divisionType(other: ShakeType): ShakeType? = null
                override fun modulusType(other: ShakeType): ShakeType? = null
                override fun powerType(other: ShakeType): ShakeType? = null

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun xorType(other: ShakeType): ShakeType? = bool()
                override fun notType(): ShakeType = bool()

                override fun castableTo(other: ShakeType): Boolean =
                    other is Primitive && other.type == PrimitiveType.BOOLEAN
                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other is Primitive && other.type == PrimitiveType.BOOLEAN) 0 else -1

                override fun pointer() = booleanPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "boolean")
                }
            }

            /**
             * The primitive byte type.
             */
            val BYTE: Primitive = object : Primitive {

                override val name: String get() = "byte"
                override val type: PrimitiveType get() = PrimitiveType.BYTE

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE -> byte()
                            PrimitiveType.SHORT -> short()
                            PrimitiveType.INT -> int()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> short()
                            PrimitiveType.UNSIGNED_SHORT -> int()
                            PrimitiveType.UNSIGNED_INT,
                            PrimitiveType.UNSIGNED_LONG -> long()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.BYTE -> 0
                        PrimitiveType.SHORT -> 1
                        PrimitiveType.INT -> 2
                        PrimitiveType.LONG -> 3
                        PrimitiveType.FLOAT -> 4
                        PrimitiveType.DOUBLE -> 4
                        else -> -1
                    }

                override fun pointer() = bytePointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "byte")
                }
            }

            /**
             * The primitive short type.
             */
            val SHORT: Primitive = object : Primitive {

                override val name: String get() = "short"
                override val type: PrimitiveType get() = PrimitiveType.SHORT

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE,
                            PrimitiveType.SHORT -> short()
                            PrimitiveType.INT -> int()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> short()
                            PrimitiveType.UNSIGNED_SHORT -> int()
                            PrimitiveType.UNSIGNED_INT,
                            PrimitiveType.UNSIGNED_LONG -> long()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.SHORT -> 0
                        PrimitiveType.INT -> 1
                        PrimitiveType.LONG -> 2
                        PrimitiveType.FLOAT -> 3
                        PrimitiveType.DOUBLE -> 4
                        else -> -1
                    }

                override fun pointer() = shortPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "short")
                }
            }

            /**
             * The primitive int type.
             */
            val INT: Primitive = object : Primitive {

                override val name: String get() = "int"
                override val type: PrimitiveType get() = PrimitiveType.INT

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE,
                            PrimitiveType.SHORT,
                            PrimitiveType.INT -> int()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> int()
                            PrimitiveType.UNSIGNED_SHORT -> int()
                            PrimitiveType.UNSIGNED_INT,
                            PrimitiveType.UNSIGNED_LONG -> long()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.INT -> 0
                        PrimitiveType.LONG -> 1
                        PrimitiveType.FLOAT -> 2
                        PrimitiveType.DOUBLE -> 3
                        else -> -1
                    }

                override fun pointer() = intPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "int")
                }
            }

            /**
             * The primitive long type.
             */
            val LONG: Primitive = object : Primitive {

                override val name: String get() = "long"
                override val type: PrimitiveType get() = PrimitiveType.LONG

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE,
                            PrimitiveType.SHORT,
                            PrimitiveType.INT,
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE,
                            PrimitiveType.UNSIGNED_SHORT,
                            PrimitiveType.UNSIGNED_INT,
                            PrimitiveType.UNSIGNED_LONG -> long()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.LONG -> 0
                        PrimitiveType.FLOAT -> 1
                        PrimitiveType.DOUBLE -> 2
                        else -> -1
                    }

                override fun pointer() = longPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "long")
                }
            }

            /**
             * The primitive float type.
             */
            val FLOAT: Primitive = object : Primitive {

                override val name: String get() = "float"
                override val type: PrimitiveType get() = PrimitiveType.FLOAT

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.UNSIGNED_BYTE,
                            PrimitiveType.UNSIGNED_SHORT,
                            PrimitiveType.UNSIGNED_INT,
                            PrimitiveType.UNSIGNED_LONG,
                            PrimitiveType.BYTE,
                            PrimitiveType.SHORT,
                            PrimitiveType.INT,
                            PrimitiveType.LONG,
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.FLOAT -> 0
                        PrimitiveType.DOUBLE -> 1
                        else -> -1
                    }

                override fun pointer() = floatPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "float")
                }
            }

            val DOUBLE: Primitive = object : Primitive {

                override val name: String get() = "double"
                override val type: PrimitiveType get() = PrimitiveType.DOUBLE

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE -> double()
                            PrimitiveType.SHORT -> double()
                            PrimitiveType.INT -> double()
                            PrimitiveType.LONG -> double()
                            PrimitiveType.FLOAT -> double()
                            PrimitiveType.DOUBLE -> double()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.DOUBLE -> 0
                        else -> -1
                    }

                override fun pointer() = doublePointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "double")
                }
            }

            /**
             * The primitive unsigned byte type.
             */
            val UNSIGNED_BYTE: Primitive = object : Primitive {

                override val name: String get() = "unsigned_byte"
                override val type: PrimitiveType get() = PrimitiveType.UNSIGNED_BYTE

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE -> short()
                            PrimitiveType.SHORT -> int()
                            PrimitiveType.INT -> long()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> unsignedByte()
                            PrimitiveType.UNSIGNED_SHORT -> unsignedShort()
                            PrimitiveType.UNSIGNED_INT -> unsignedInt()
                            PrimitiveType.UNSIGNED_LONG -> unsignedLong()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.UNSIGNED_BYTE -> 0
                        PrimitiveType.UNSIGNED_SHORT -> 1
                        PrimitiveType.UNSIGNED_INT -> 2
                        PrimitiveType.UNSIGNED_LONG -> 3
                        PrimitiveType.SHORT -> 4
                        PrimitiveType.INT -> 5
                        PrimitiveType.LONG -> 6
                        PrimitiveType.FLOAT -> 7
                        PrimitiveType.DOUBLE -> 8
                        else -> -1
                    }

                override fun pointer() = unsignedBytePointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_byte")
                }
            }

            /**
             * The primitive unsigned short type.
             */
            val UNSIGNED_SHORT: Primitive = object : Primitive {

                override val name: String get() = "unsigned_short"
                override val type: PrimitiveType get() = PrimitiveType.UNSIGNED_SHORT

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE -> short()
                            PrimitiveType.SHORT -> int()
                            PrimitiveType.INT -> long()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> unsignedByte()
                            PrimitiveType.UNSIGNED_SHORT -> unsignedShort()
                            PrimitiveType.UNSIGNED_INT -> unsignedInt()
                            PrimitiveType.UNSIGNED_LONG -> unsignedLong()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.UNSIGNED_SHORT -> 0
                        PrimitiveType.UNSIGNED_INT -> 1
                        PrimitiveType.UNSIGNED_LONG -> 2
                        PrimitiveType.BYTE -> 3
                        PrimitiveType.SHORT -> 4
                        PrimitiveType.INT -> 5
                        PrimitiveType.LONG -> 6
                        PrimitiveType.FLOAT -> 7
                        PrimitiveType.DOUBLE -> 8
                        else -> -1
                    }

                override fun pointer() = unsignedShortPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_short")
                }
            }

            /**
             * The primitive unsigned int type.
             */
            val UNSIGNED_INT: Primitive = object : Primitive {

                override val name: String get() = "unsigned_int"
                override val type: PrimitiveType get() = PrimitiveType.UNSIGNED_INT

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE -> int()
                            PrimitiveType.SHORT -> long()
                            PrimitiveType.INT -> long()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> unsignedByte()
                            PrimitiveType.UNSIGNED_SHORT -> unsignedShort()
                            PrimitiveType.UNSIGNED_INT -> unsignedInt()
                            PrimitiveType.UNSIGNED_LONG -> unsignedLong()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.UNSIGNED_INT -> 0
                        PrimitiveType.UNSIGNED_LONG -> 1
                        PrimitiveType.BYTE -> 2
                        PrimitiveType.SHORT -> 3
                        PrimitiveType.INT -> 4
                        PrimitiveType.LONG -> 5
                        PrimitiveType.FLOAT -> 6
                        PrimitiveType.DOUBLE -> 7
                        else -> -1
                    }

                override fun pointer() = unsignedIntPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_int")
                }
            }

            /**
             * The primitive unsigned long type.
             */
            val UNSIGNED_LONG: Primitive = object : Primitive {

                override val name: String get() = "unsigned_long"
                override val type: PrimitiveType get() = PrimitiveType.UNSIGNED_LONG

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE -> long()
                            PrimitiveType.SHORT -> long()
                            PrimitiveType.INT -> long()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE -> unsignedByte()
                            PrimitiveType.UNSIGNED_SHORT -> unsignedShort()
                            PrimitiveType.UNSIGNED_INT -> unsignedInt()
                            PrimitiveType.UNSIGNED_LONG -> unsignedLong()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.UNSIGNED_LONG -> 0
                        PrimitiveType.BYTE -> 1
                        PrimitiveType.SHORT -> 2
                        PrimitiveType.INT -> 3
                        PrimitiveType.LONG -> 4
                        PrimitiveType.FLOAT -> 5
                        PrimitiveType.DOUBLE -> 6
                        else -> -1
                    }

                override fun pointer() = unsignedLongPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_long")
                }
            }

            /**
             * The primitive char type.
             */
            val CHAR = object : Primitive {

                override val name: String get() = "char"
                override val type: PrimitiveType get() = PrimitiveType.CHAR

                private fun gType(other: ShakeType): ShakeType? {
                    if (other is Primitive) {
                        return when (other.type) {
                            PrimitiveType.BYTE,
                            PrimitiveType.SHORT,
                            PrimitiveType.INT -> int()
                            PrimitiveType.LONG -> long()
                            PrimitiveType.FLOAT -> float()
                            PrimitiveType.DOUBLE -> double()
                            PrimitiveType.UNSIGNED_BYTE,
                            PrimitiveType.UNSIGNED_SHORT,
                            PrimitiveType.UNSIGNED_INT -> unsignedInt()
                            PrimitiveType.UNSIGNED_LONG -> unsignedLong()
                            else -> null
                        }
                    }
                    return null
                }

                override fun additionType(other: ShakeType): ShakeType? = gType(other)
                override fun subtractionType(other: ShakeType): ShakeType? = gType(other)
                override fun multiplicationType(other: ShakeType): ShakeType? = gType(other)
                override fun divisionType(other: ShakeType): ShakeType? = gType(other)
                override fun modulusType(other: ShakeType): ShakeType? = gType(other)
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.CHAR -> 0
                        PrimitiveType.SHORT -> 1
                        PrimitiveType.UNSIGNED_SHORT -> 1
                        PrimitiveType.INT -> 2
                        PrimitiveType.UNSIGNED_INT -> 2
                        PrimitiveType.LONG -> 3
                        PrimitiveType.UNSIGNED_LONG -> 3
                        PrimitiveType.FLOAT -> 5
                        PrimitiveType.DOUBLE -> 6
                        else -> -1
                    }

                override fun pointer() = charPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "char")
                }
            }

            /**
             * The primitive void type.
             */
            val VOID = object : Primitive {

                override val name: String get() = "void"
                override val type: PrimitiveType get() = PrimitiveType.VOID

                override fun additionType(other: ShakeType): ShakeType? = null
                override fun subtractionType(other: ShakeType): ShakeType? = null
                override fun multiplicationType(other: ShakeType): ShakeType? = null
                override fun divisionType(other: ShakeType): ShakeType? = null
                override fun modulusType(other: ShakeType): ShakeType? = null
                override fun powerType(other: ShakeType): ShakeType = double()

                override fun greaterThanType(other: ShakeType): ShakeType = bool()
                override fun greaterThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun lessThanType(other: ShakeType): ShakeType = bool()
                override fun lessThanOrEqualType(other: ShakeType): ShakeType = bool()
                override fun andType(other: ShakeType): ShakeType? = null
                override fun orType(other: ShakeType): ShakeType? = null
                override fun xorType(other: ShakeType): ShakeType? = null
                override fun notType(): ShakeType? = null

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.VOID -> 0
                        else -> -1
                    }

                override fun pointer(): Pointer<Primitive> = voidPointer

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "void")
                }
            }
        }
    }

    /**
     * The object type.
     * This type points towards an object.
     */
    interface Object : ShakeType {

        /**
         * A pointer towards the class of the referenced object
         */
        val classPointer: Pointer<ShakeClass>

        /**
         * The class of the referenced object
         */
        val clazz: ShakeClass

        override val kind: Kind get() = Kind.OBJECT

        /**
         * The qualified name of the class of the referenced object
         */
        val qualifiedName: String

        class Impl : Object {
            override val name: String get() = signature
            override val signature: String get() = "L${clazz.qualifiedName}"
            override val qualifiedName: String get() = clazz.qualifiedName

            override val classPointer: Pointer<ShakeClass>
            override val clazz: ShakeClass get() = clazz

            constructor(clazz: ShakeClass) {
                this.classPointer = clazz.point()
            }

            constructor(classPointer: Pointer<ShakeClass>) {
                this.classPointer = classPointer
            }

            override fun assignType(other: ShakeType): ShakeType? = null
            override fun additionAssignType(other: ShakeType): ShakeType? = null
            override fun subtractionAssignType(other: ShakeType): ShakeType? = null
            override fun multiplicationAssignType(other: ShakeType): ShakeType? = null
            override fun divisionAssignType(other: ShakeType): ShakeType? = null
            override fun modulusAssignType(other: ShakeType): ShakeType? = null
            override fun powerAssignType(other: ShakeType): ShakeType? = null
            override fun incrementBeforeType(): ShakeType? = null
            override fun incrementAfterType(): ShakeType? = null
            override fun decrementBeforeType(): ShakeType? = null
            override fun decrementAfterType(): ShakeType? = null
            override fun additionType(other: ShakeType): ShakeType? = null
            override fun subtractionType(other: ShakeType): ShakeType? = null
            override fun multiplicationType(other: ShakeType): ShakeType? = null
            override fun divisionType(other: ShakeType): ShakeType? = null
            override fun modulusType(other: ShakeType): ShakeType? = null
            override fun powerType(other: ShakeType): ShakeType? = null
            override fun equalsType(other: ShakeType): ShakeType = Primitive.BOOLEAN
            override fun notEqualsType(other: ShakeType): ShakeType = Primitive.BOOLEAN
            override fun greaterThanType(other: ShakeType): ShakeType? = null
            override fun greaterThanOrEqualType(other: ShakeType): ShakeType? = null
            override fun lessThanType(other: ShakeType): ShakeType? = null
            override fun lessThanOrEqualType(other: ShakeType): ShakeType? = null
            override fun andType(other: ShakeType): ShakeType? = null
            override fun orType(other: ShakeType): ShakeType? = null
            override fun xorType(other: ShakeType): ShakeType? = null
            override fun notType(): ShakeType? = null
            override fun childType(name: String): ShakeType? = clazz.fields.find { it.name == name }?.type
            override fun childFunctions(name: String): List<ShakeFunctionType>
                = clazz.methods.filter { it.name == name }

            override fun childInvokable(name: String): List<ShakeFunctionType> = childFunctions(name)

            override fun castableTo(other: ShakeType): Boolean
                = other is Object && other.clazz.compatibleTo(clazz)

            override fun compatibleTo(other: ShakeType): Boolean
                = other is Object && clazz.compatibleTo(other.clazz)
            override fun compatibilityDistance(other: ShakeType): Int
                = if(other is Object) clazz.compatibilityDistance(other.clazz) else -1
            override fun toJson(): Map<String, Any?> = mapOf("type" to "object", "class" to clazz.qualifiedName)
        }
    }

    interface Array : ShakeType {
        val elementTypePointer: Pointer<ShakeType>
        val elementType: ShakeType

        class Impl : Array {
            override val name: String get() = signature
            override val signature: String = "[${elementType.name}"

            override val elementTypePointer: Pointer<ShakeType>
            override val elementType: ShakeType get() = elementTypePointer.value

            constructor(
                elementType: ShakeType
            ) : super() {
                this.elementTypePointer = elementType.point()
            }

            constructor(
                elementTypePointer: Pointer<ShakeType>
            ) : super() {
                this.elementTypePointer = elementTypePointer
            }

            override fun assignType(other: ShakeType): ShakeType? = null
            override fun additionAssignType(other: ShakeType): ShakeType? = null
            override fun subtractionAssignType(other: ShakeType): ShakeType? = null
            override fun multiplicationAssignType(other: ShakeType): ShakeType? = null
            override fun divisionAssignType(other: ShakeType): ShakeType? = null
            override fun modulusAssignType(other: ShakeType): ShakeType? = null
            override fun powerAssignType(other: ShakeType): ShakeType? = null
            override fun incrementBeforeType(): ShakeType? = null
            override fun incrementAfterType(): ShakeType? = null
            override fun decrementBeforeType(): ShakeType? = null
            override fun decrementAfterType(): ShakeType? = null
            override fun additionType(other: ShakeType): ShakeType? = null
            override fun subtractionType(other: ShakeType): ShakeType? = null
            override fun multiplicationType(other: ShakeType): ShakeType? = null
            override fun divisionType(other: ShakeType): ShakeType? = null
            override fun modulusType(other: ShakeType): ShakeType? = null
            override fun powerType(other: ShakeType): ShakeType? = null
            override fun equalsType(other: ShakeType): ShakeType? = null
            override fun notEqualsType(other: ShakeType): ShakeType? = null
            override fun greaterThanType(other: ShakeType): ShakeType? = null
            override fun greaterThanOrEqualType(other: ShakeType): ShakeType? = null
            override fun lessThanType(other: ShakeType): ShakeType? = null
            override fun lessThanOrEqualType(other: ShakeType): ShakeType? = null
            override fun andType(other: ShakeType): ShakeType? = null
            override fun orType(other: ShakeType): ShakeType? = null
            override fun xorType(other: ShakeType): ShakeType? = null
            override fun notType(): ShakeType? = null
            override fun childType(name: String): ShakeType? {
                TODO("Not yet implemented")
            }

            override fun childFunctions(name: String): List<ShakeFunction> {
                TODO("Not yet implemented")
            }

            override fun childInvokable(name: String): List<ShakeInvokable> {
                TODO("Not yet implemented")
            }

            override val kind: Kind
                get() = Kind.ARRAY

            override fun castableTo(other: ShakeType): Boolean {
                return other is Array && other.elementType.castableTo(elementType)
            }

            override fun compatibleTo(other: ShakeType): Boolean {
                return other is Array && elementType.compatibleTo(other.elementType)
            }

            override fun compatibilityDistance(other: ShakeType): Int {
                return if (other is Array) elementType.compatibilityDistance(other.elementType) else -1
            }

            override fun toJson(): Map<String, Any?> {
                return mapOf("type" to "array", "elementType" to elementType.toJson())
            }
        }
    }

    interface Lambda : ShakeType {
        val parameters: List<ShakeParameter>
        val returnType: ShakeType

        override fun toJson(): Map<String, Any?> {
            return mapOf("type" to "lambda", "parameters" to parameters.map { it.toJson() }, "returnType" to returnType.toJson())
        }
    }

    companion object {
        fun objectType(clazz: ShakeClass): Object {
            return Object.Impl(clazz)
        }
        fun objectType(clazz: Pointer<ShakeClass>): Object {
            return Object.Impl(clazz)
        }
        fun arrayType(elementType: ShakeType): Array {
            return Array.Impl(elementType)
        }
        fun arrayType(elementTypePointer: Pointer<ShakeType>): Array {
            return Array.Impl(elementTypePointer)
        }


        fun from(project: ShakeProject, type: ShakeType) : Pointer<ShakeType> {
            return when(type) {
                is Primitive -> type.point()
                is Object -> project.getClass(type.qualifiedName).transform { objectType(it ?: throw IllegalStateException("Class ${type.qualifiedName} not found")) }
                is Array -> from(project, type.elementType).transform { Array.Impl(it) }
                is Lambda -> TODO()
                else -> throw IllegalStateException("Unknown type ${type.name}")
            }
        }

        fun from(scope: ShakeScope, type: ShakeType) : Pointer<ShakeType> {
            return from(scope.project, type)
        }
    }
}
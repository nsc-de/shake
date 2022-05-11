package io.github.shakelang.shake.processor.program.types

interface ShakeType {

    val name: String
    val signature: String

    fun assignType(other: ShakeType): ShakeType?
    fun additionAssignType(other: ShakeType): ShakeType?
    fun subtractionAssignType(other: ShakeType): ShakeType?
    fun multiplicationAssignType(other: ShakeType): ShakeType?
    fun divisionAssignType(other: ShakeType): ShakeType?
    fun modulusAssignType(other: ShakeType): ShakeType?
    fun powerAssignType(other: ShakeType): ShakeType?
    fun incrementBeforeType(): ShakeType?
    fun incrementAfterType(): ShakeType?
    fun decrementBeforeType(): ShakeType?
    fun decrementAfterType(): ShakeType?

    fun additionType(other: ShakeType): ShakeType?
    fun subtractionType(other: ShakeType): ShakeType?
    fun multiplicationType(other: ShakeType): ShakeType?
    fun divisionType(other: ShakeType): ShakeType?
    fun modulusType(other: ShakeType): ShakeType?
    fun powerType(other: ShakeType): ShakeType?
    fun equalsType(other: ShakeType): ShakeType?
    fun notEqualsType(other: ShakeType): ShakeType?
    fun greaterThanType(other: ShakeType): ShakeType?
    fun greaterThanOrEqualType(other: ShakeType): ShakeType?
    fun lessThanType(other: ShakeType): ShakeType?
    fun lessThanOrEqualType(other: ShakeType): ShakeType?
    fun andType(other: ShakeType): ShakeType?
    fun orType(other: ShakeType): ShakeType?
    fun notType(): ShakeType?
    fun childType(name: String): ShakeType?
    fun childFunctions(name: String): List<ShakeFunction>?
    fun childInvokable(name: String): List<ShakeFunction>?

    val kind: Kind

    fun castableTo(other: ShakeType): Boolean
    fun compatibleTo(other: ShakeType): Boolean
    fun compatibilityDistance(other: ShakeType): Int

    fun toJson(): Map<String, Any?>

    enum class Kind {
        PRIMITIVE,
        OBJECT,
        ARRAY,
        LAMBDA,
    }

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
        override fun compatibleTo(other: ShakeType): Boolean = other.compatibleTo(this)

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



        companion object {

            private fun bool(): Primitive {
                return BOOLEAN
            }

            private fun byte(): Primitive {
                return BYTE
            }

            private fun short(): Primitive {
                return SHORT
            }

            private fun int(): Primitive {
                return INT
            }

            private fun long(): Primitive {
                return LONG
            }

            private fun float(): Primitive {
                return FLOAT
            }

            private fun double(): Primitive {
                return DOUBLE
            }

            private fun unsignedByte(): Primitive {
                return UNSIGNED_BYTE
            }

            private fun unsignedShort(): Primitive {
                return UNSIGNED_SHORT
            }

            private fun unsignedInt(): Primitive {
                return UNSIGNED_INT
            }

            private fun unsignedLong(): Primitive {
                return UNSIGNED_LONG
            }

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
                override fun notType(): ShakeType = bool()

                override fun castableTo(other: ShakeType): Boolean =
                    other is Primitive && other.type == PrimitiveType.BOOLEAN
                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other is Primitive && other.type == PrimitiveType.BOOLEAN) 0 else -1

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "boolean")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

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

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "byte")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.SHORT -> 0
                        PrimitiveType.INT -> 1
                        PrimitiveType.LONG -> 2
                        PrimitiveType.FLOAT -> 3
                        PrimitiveType.DOUBLE -> 4
                        else -> -1
                    }

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "short")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.INT -> 0
                        PrimitiveType.LONG -> 1
                        PrimitiveType.FLOAT -> 2
                        PrimitiveType.DOUBLE -> 3
                        else -> -1
                    }

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "int")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.LONG -> 0
                        PrimitiveType.FLOAT -> 1
                        PrimitiveType.DOUBLE -> 2
                        else -> -1
                    }

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "long")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.FLOAT -> 0
                        PrimitiveType.DOUBLE -> 1
                        else -> -1
                    }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.DOUBLE -> 0
                        else -> -1
                    }

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "double")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

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

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_byte")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

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

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_short")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

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

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_int")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

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

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "unsigned_long")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

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

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "char")
                }
            }

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
                override fun andType(other: ShakeType): ShakeType = bool()
                override fun orType(other: ShakeType): ShakeType = bool()
                override fun notType(): ShakeType = bool()

                override fun compatibilityDistance(other: ShakeType): Int =
                    if(other !is Primitive) -1 else when(other.type) {
                        PrimitiveType.VOID -> 0
                        else -> -1
                    }

                override fun toJson(): Map<String, Any?> {
                    return mapOf("type" to "void")
                }
            }
        }
    }

    interface Object : ShakeType {
        val clazz: ShakeClass
        override val kind: Kind
            get() = Kind.OBJECT
        val qualifiedName: String

        class Impl(override val clazz: ShakeClass) : Object {
            override val name: String get() = signature
            override val signature: String = "L${clazz.qualifiedName}"
            override val qualifiedName: String get() = clazz.qualifiedName

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
            override fun notType(): ShakeType? = null
            override fun childType(name: String): ShakeType? = clazz.fields.find { it.name == name }?.type
            override fun childFunctions(name: String): List<ShakeFunction>
                = clazz.methods.filter { it.name == name }

            override fun childInvokable(name: String): List<ShakeFunction>? = childFunctions(name)

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
        val elementType: ShakeType

        class Impl(override val elementType: ShakeType) : Array {
            override val name: String get() = signature
            override val signature: String = "[${elementType.name}"

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
            override fun notType(): ShakeType? = null
            override fun childType(name: String): ShakeType? {
                TODO("Not yet implemented")
            }

            override fun childFunctions(name: String): List<ShakeFunction>? {
                TODO("Not yet implemented")
            }

            override fun childInvokable(name: String): List<ShakeFunction>? {
                TODO("Not yet implemented")
            }

            override val kind: ShakeType.Kind
                get() = ShakeType.Kind.ARRAY

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
        fun from(project: ShakeProject, type: ShakeType) : ShakeType {
            return when(type) {
                is Primitive -> type
                is Object -> project.getClass(type.qualifiedName)?.let { objectType(it) } ?: throw IllegalStateException("Class ${type.qualifiedName} not found")
                is Array -> Array.Impl(from(project, type.elementType))
                is Lambda -> TODO()
                else -> throw IllegalStateException("Unknown type ${type.name}")
            }
        }
    }
}
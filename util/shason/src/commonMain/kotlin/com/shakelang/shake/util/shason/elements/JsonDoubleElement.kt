package com.shakelang.shake.util.shason.elements

import com.shakelang.shake.util.shason.JSON

/**
 * A json representation of double values
 * @since 0.1.0
 * @version 0.1.0
 */
class JsonDoubleElement(

    /**
     * The value of the [JsonDoubleElement]
     * @since 0.1.0
     * @version 0.1.0
     */
    override val value: Double

) : JsonPrimitive {

    /**
     * Override toString to generate via [JSON.stringify]
     * @since 0.1.0
     * @version 0.1.0
     */
    override fun toString(): String = JSON.stringify(value)

    override fun isNull(): Boolean = false
    override fun isBoolean(): Boolean = false
    override fun isString(): Boolean = false
    override fun isDouble(): Boolean = true
    override fun isInt(): Boolean = false
    override fun isJsonPrimitive(): Boolean = true
    override fun isJsonArray(): Boolean = false
    override fun isJsonObject(): Boolean = false
    override fun isJsonDouble(): Boolean = true
    override fun isJsonString(): Boolean = false
    override fun isJsonInteger(): Boolean = false
    override fun isJsonNull(): Boolean = false
    override fun isJsonBoolean(): Boolean = false

}

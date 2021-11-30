package io.github.shakelang.shason.elements

import io.github.shakelang.shason.JSON

/**
 * A json representation of string values
 *
 * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
 */
class JsonStringElement(

    /**
     * The value of the [JsonStringElement]
     */
    override val value: String

) : JsonPrimitive {

    /**
     * Override toString to generate via [JSON.stringify]
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    override fun toString(): String = JSON.stringify(value)

}
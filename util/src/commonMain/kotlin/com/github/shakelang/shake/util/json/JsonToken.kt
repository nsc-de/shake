package com.github.shakelang.shake.util.json

import com.github.shakelang.shake.util.characterinput.position.PositionMap


/**
 * A [JsonToken] is a Token that should be parsed by the [JsonParser] generated by the [JsonLexer]
 *
 * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
 */
@Suppress("unused")
class JsonToken (

    /**
     * The type of the [JsonToken]
     */
    val type: JsonTokenType,

    /**
     * The start index of the [JsonToken]
     */
    val start: Int,

    /**
     * The end index of the [JsonToken]
     */
    val end: Int = start,

    /**
     * The value of the [JsonToken]
     */
    val value: String? = null,

    ) {

    /**
     * Constructor for [JsonToken]
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    constructor(type: JsonTokenType, start: Int, value: String? = null) : this(type, start, start, value)

    /**
     * Has the [JsonToken] a value?
     */
    val hasValue: Boolean
        get() = this.value != null

    /**
     * Stringify the [JsonToken]
     */
    override fun toString() = "JsonToken{type=$type,start=$start,end=$end,value=$value}"

}

/**
 * Token-types for the [JsonToken]s
 */
enum class JsonTokenType {

    /**
     * A [LCURL] [JsonTokenType] represents a '{' in the source
     */
    LCURL,              // '{'

    /**
     * A [RCURL] [JsonTokenType] represents a '}' in the source
     */
    RCURL,              // '}'

    /**
     * A [LSQUARE] [JsonTokenType] represents a '[' in the source
     */
    LSQUARE,            // '['

    /**
     * A [RSQUARE] [JsonTokenType] represents a ']' in the source
     */
    RSQUARE,            // ']'

    /**
     * A [COMMA] [JsonTokenType] represents a ',' in the source
     */
    COMMA,              // ','

    /**
     * A [COLON] [JsonTokenType] represents a ':' in the source
     */
    COLON,              // ':'

    /**
     * A [TRUE] [JsonTokenType] represents a 'true' in the source
     */
    TRUE,

    /**
     * A [FALSE] [JsonTokenType] represents a 'false' in the source
     */
    FALSE,

    /**
     * A [STRING] [JsonTokenType] represents a string (e.g. "hello world") in the source
     */
    STRING,

    /**
     * A [DOUBLE] [JsonTokenType] represents a double (e.g. '0.1') in the source
     */
    DOUBLE,

    /**
     * A [INT] [JsonTokenType] represents a integer (e.g. '42') in the source
     */
    INT,

}

/**
 * A [JsonTokenInputStream] provides the [JsonToken]s for a Parser. It is
 * created by the [com.github.shakelang.shake.util.json.JsonLexer]
 *
 * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
 */
class JsonTokenInputStream (

    /**
     * The source (mostly filename) of the [JsonTokenInputStream]
     */
    val source: String,

    /**
     * The tokens that are contained in the [JsonTokenInputStream]
     */
    private val tokens: Array<JsonToken>,

    /**
     * The PositionMap to resolve the [com.github.shakelang.shake.util.characterinput.position.Position]s
     * of the [JsonToken]s
     */
    val map: PositionMap,

    /**
     * The position that the TokenInputStream is actually at
     */
    private var position: Int = -1

) {

    /**
     * Get the size of the [JsonTokenInputStream]
     *
     * @return the length of the [JsonTokenInputStream.tokens] array
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    val size: Int
        get() = this.tokens.size

    /**
     * Get a specific token from the [JsonTokenInputStream]
     *
     * @param position the position to get
     * @return the token at the given position
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    operator fun get(position: Int): JsonToken {
        testPosition(position)
        return this.tokens[position]
    }

    /**
     * Checks if the [JsonTokenInputStream] has left a given number of tokens
     *
     * @param num the number of tokens to check
     * @return has the [JsonTokenInputStream] left the given amount of [JsonToken]s?
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    fun has(num: Int): Boolean {
        // When the number to check is smaller than 0 throw an error
        // in other case just check if the required tokens are left
        if (num < 1) throw Error("You should only give positive numbers to this function")
        return position + num < tokens.size
    }

    /**
     * Checks if the [JsonTokenInputStream] has a token left
     *
     * @return has the [JsonTokenInputStream] another [JsonToken] left?
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    operator fun hasNext(): Boolean {
        // We could also use has(1) here, but for performance-reasons
        // that here should be better
        return position + 1 < tokens.size
    }

    /**
     * Returns the next token of the [JsonTokenInputStream] (and skips)
     *
     * @return the next token
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    operator fun next(): JsonToken {
        // skip to next token and then return the actual token
        skip()
        return actual()
    }

    /**
     * Skips the next token of the [JsonTokenInputStream]
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    fun skip() {
        // Check if the input has a next token.
        // If so then increase the position. If not throw an error
        if (hasNext()) position++ else throw Error("Input already finished")
    }

    /**
     * Returns the actual [JsonToken] of the [JsonTokenInputStream]
     *
     * @return The actual [JsonToken]
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    fun actual(): JsonToken {
        // Just return the actual token
        // That is possible, because the position should never get
        // bigger than the token length.
        return this[this.position]
    }

    /**
     * Returns the next [JsonToken] of the [JsonTokenInputStream] without skipping
     *
     * @return The next [JsonToken]
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    fun peek(): JsonToken {
        return if (position + 1 < tokens.size) this[position + 1] else throw Error("Not enough tokens left")
    }

    /**
     * Returns a string-representation of the [JsonTokenInputStream]
     *
     * @return the string-representation of the [JsonTokenInputStream]
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    override fun toString(): String {
        // Return a string-representation of the input just showing all the sub-elements
        return "TokenInputStream{" +
                "source='" + source + '\'' +
                ", tokens=" + this.tokens.contentToString() +
                ", position=" + position +
                '}'
    }

    /**
     * This function checks if the given position is a valid position for the [tokens]-array
     * throws an error if the position is a wrong one
     *
     * @param position the position to check
     *
     * @author [Nicolas Schmidt &lt;@nsc-de&gt;](https://github.com/nsc-de)
     */
    private fun testPosition(position: Int) {
        // If the position is out of range of the tokens array throw an error
        if (position < 0) throw Error("Position mustn't be smaller than 0.")
        if (position >= this.size) throw Error(
            "The given position is to high. The maximum value is %${this.size - 1}, but given was $position",
        )
    }
}
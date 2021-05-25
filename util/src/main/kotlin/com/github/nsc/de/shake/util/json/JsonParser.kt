package com.github.nsc.de.shake.util.json

import com.github.nsc.de.shake.util.Characters
import com.github.nsc.de.shake.util.CompilerError
import com.github.nsc.de.shake.util.characterinput.position.Position
import com.github.nsc.de.shake.util.json.JSONTokenType.*

@Suppress("unused")
class JsonParser(
    val tokens: JSONTokenInputStream,
    private val createSets: Boolean = false
) {

    fun parse(): Any {

        val ret = parseValue()
        if(tokens.hasNext()) throw ParserError("Input not finished")
        return ret

    }

    private fun parseValue(): Any {

        return when (tokens.next().type) {

            LCURL -> parseMap()
            LSQUARE -> parseArray()
            STRING -> Characters.parseString(tokens.actual().value!!)
            INT -> tokens.actual().value!!.toLong()
            DOUBLE -> tokens.actual().value!!.toDouble()

            else -> throw ParserError("")


        }

    }

    private fun parseMap(): Map<String, Any> {
        if(tokens.actual().type != LCURL) throw ParserError("Expecting '{'")

        val map = mutableMapOf<String, Any>()
        var next = true

        while(tokens.hasNext() && next) {
            val key = if(tokens.peek().type == STRING) Characters.parseString(tokens.next().value!!) else break
            if(tokens.next().type != COLON) throw ParserError("Expecting ':'")
            map[key] = parseValue()

            next = tokens.peek().type == COMMA
            if(next) tokens.skip()

        }

        if(tokens.next().type != RCURL) throw ParserError("Expecting '}'")

        return map
    }

    private fun parseArray(): Any {
        if(tokens.actual().type != LSQUARE) throw ParserError("Expecting '['")

        val arr = mutableSetOf<Any>()
        var next = true

        while(tokens.hasNext() && next) {

            if(tokens.peek().type == RSQUARE) break
            arr.add(parseValue())
            next = tokens.peek().type == COMMA
            if(next) tokens.skip()

        }

        if(tokens.next().type != RSQUARE) throw ParserError("Expecting ']")
        return if(createSets) arr else arr.toTypedArray()

    }

    // ****************************************************************************
    // Errors
    inner class ParserError(message: String, name: String, details: String, start: Position, end: Position) :
        CompilerError(message, name, details, start, end) {

        init {
            println(start)
            println(end)
        }

        constructor(
            name: String,
            details: String,
            start: Position,
            end: Position
        ) : this(
            "Error occurred in parser: " + name + ", " + details + " in " + start.source + ":" + start.line + ":" + start.column,
            name,
            details,
            start,
            end
        )

        constructor(details: String, start: Position, end: Position) : this("ParserError", details, start, end)
        constructor(details: String, start: Int, end: Int) : this(
            "ParserError",
            details,
            tokens.map.resolve(start),
            tokens.map.resolve(end)
        )

        constructor(error: String, position: Int) : this(
            error,
            tokens.map.resolve(tokens[position].start),
            tokens.map.resolve(tokens[position].end)
        )

        constructor(error: String) : this(
            error,
            tokens.map.resolve(tokens.peek().start),
            tokens.map.resolve(tokens.peek().end)
        )
    }

}
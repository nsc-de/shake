package com.shakelang.shake.shasambly.shasp.lexer

import com.shakelang.shake.lexer.token.ShasPToken
import com.shakelang.shake.shasambly.shasp.lexer.token.OnDemandLexingShasPTokenInputStream
import com.shakelang.shake.shasambly.shasp.lexer.token.ShasPTokenBasedInputStream
import com.shakelang.util.parseutils.characters.streaming.CharacterInputStream

class ShasPLexer(
    input: CharacterInputStream,
) : ShasPLexingBase(input) {

    fun makeTokens(): ShasPTokenBasedInputStream {
        val tokens = mutableListOf<ShasPToken>()
        while (input.hasNext()) tokens.add(makeToken())
        return ShasPTokenBasedInputStream(tokens.toTypedArray(), input.positionMaker.createPositionMap())
    }

    fun stream(): OnDemandLexingShasPTokenInputStream {
        return OnDemandLexingShasPTokenInputStream(input)
    }
}

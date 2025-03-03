package com.shakelang.shake.lexer

import com.shakelang.shake.lexer.token.ShakeToken
import com.shakelang.shake.lexer.token.ShakeTokenType
import com.shakelang.shake.lexer.token.stream.ShakeOnDemandLexingTokenInputStream
import com.shakelang.shake.lexer.token.stream.ShakeTokenBasedTokenInputStream
import com.shakelang.util.parseutils.characters.streaming.CharacterInputStream

class ShakeLexer(
    input: CharacterInputStream,
) : ShakeLexingBase(input) {

    fun makeTokens(): ShakeTokenBasedTokenInputStream {
        val tokens = mutableListOf<ShakeToken>()
        while (input.hasNext()) {
            val token = makeToken()
            if (token.type == ShakeTokenType.EOF) break
            tokens.add(token)
        }
        return ShakeTokenBasedTokenInputStream(input.source.location, tokens.toTypedArray(), input.positionMaker)
    }

    fun stream(): ShakeOnDemandLexingTokenInputStream {
        return ShakeOnDemandLexingTokenInputStream(input)
    }
}

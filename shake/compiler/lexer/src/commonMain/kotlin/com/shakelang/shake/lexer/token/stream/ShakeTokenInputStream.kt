package com.shakelang.shake.lexer.token.stream

import com.shakelang.shake.lexer.token.ShakeToken
import com.shakelang.shake.lexer.token.ShakeTokenType
import com.shakelang.util.parseutils.lexer.token.stream.TokenInputStream

/**
 * A [ShakeTokenInputStream] provides the [ShakeToken]s for a Parser. It is
 * created by the [com.shakelang.shake.lexer.ShakeLexer]
 */
@Suppress("unused")
interface ShakeTokenInputStream : TokenInputStream<ShakeTokenType, ShakeToken> {
    fun skipIgnorable(): ShakeTokenInputStream {
        while (hasNext() && peek().type == ShakeTokenType.LINE_SEPARATOR) {
            skip()
        }
        return this
    }
}

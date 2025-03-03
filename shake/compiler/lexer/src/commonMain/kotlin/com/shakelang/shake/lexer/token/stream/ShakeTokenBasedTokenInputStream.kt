package com.shakelang.shake.lexer.token.stream

import com.shakelang.shake.lexer.token.ShakeToken
import com.shakelang.shake.lexer.token.ShakeTokenType
import com.shakelang.util.parseutils.characters.position.PositionMap
import com.shakelang.util.parseutils.lexer.token.stream.TokenBasedTokenInputStream

/**
 * A [ShakeTokenBasedTokenInputStream] provides the [ShakeToken]s for a Parser. It is
 * created by the [com.shakelang.shake.lexer.ShakeLexer]
 */
@Suppress("unused")
class ShakeTokenBasedTokenInputStream(
    override val source: String,

    /**
     * The tokenTypes that are contained in the [ShakeTokenBasedTokenInputStream]
     */
    tokens: Array<ShakeToken>,
    map: PositionMap,

) : ShakeTokenInputStream, TokenBasedTokenInputStream<ShakeTokenType, ShakeToken>(tokens, map)

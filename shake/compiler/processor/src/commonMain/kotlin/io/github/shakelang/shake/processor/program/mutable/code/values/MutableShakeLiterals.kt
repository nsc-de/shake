package io.github.shakelang.shake.processor.program.mutable.code.values

import io.github.shakelang.shake.processor.program.types.code.values.ShakeBooleanLiteral
import io.github.shakelang.shake.processor.program.types.code.values.ShakeCharacterLiteral
import io.github.shakelang.shake.processor.program.types.code.values.ShakeDoubleLiteral

interface MutableShakeDoubleLiteral : MutableShakeValue, ShakeDoubleLiteral
interface MutableShakeIntLiteral : MutableShakeValue
interface MutableShakeBooleanLiteral : MutableShakeValue, ShakeBooleanLiteral
interface MutableShakeCharacterLiteral : MutableShakeValue, ShakeCharacterLiteral
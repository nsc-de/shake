package io.github.shakelang.shake.processor.program.mutable.code.statements

import io.github.shakelang.shake.processor.program.types.code.statements.ShakeDoWhile
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeFor
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeWhile

interface MutableShakeWhile : MutableShakeStatement, ShakeWhile
interface MutableShakeDoWhile : MutableShakeStatement, ShakeDoWhile
interface MutableShakeFor : MutableShakeStatement, ShakeFor

package io.github.shakelang.shake.processor.program.mutable.code

import io.github.shakelang.shake.processor.program.mutable.code.statements.MutableShakeStatement
import io.github.shakelang.shake.processor.program.mutable.code.values.MutableShakeValue
import io.github.shakelang.shake.processor.program.types.code.ShakeInvocation

interface MutableShakeInvocation: MutableShakeValue, MutableShakeStatement, ShakeInvocation
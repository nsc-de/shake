package io.github.shakelang.shake.processor.program.mutable.code.values

import io.github.shakelang.shake.processor.program.mutable.MutableShakeAssignable
import io.github.shakelang.shake.processor.program.types.code.values.ShakeChild
import io.github.shakelang.shake.processor.program.types.code.values.ShakeChildUsage

interface MutableShakeChild : MutableShakeAssignable, ShakeChild

interface MutableShakeChildUsage : MutableShakeUsage, ShakeChildUsage
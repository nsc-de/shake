package io.github.shakelang.shake.processor.program.mutable.code.values

import io.github.shakelang.shake.processor.program.types.code.values.*

interface MutableShakeUsage : MutableShakeValue, ShakeUsage
interface MutableShakeClassFieldUsage : MutableShakeUsage, ShakeClassFieldUsage
interface MutableShakeStaticClassFieldUsage : MutableShakeUsage, ShakeStaticClassFieldUsage
interface MutableShakeFieldUsage : MutableShakeUsage, ShakeFieldUsage
interface MutableShakeVariableUsage : MutableShakeUsage, ShakeVariableUsage
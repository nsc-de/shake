package io.github.shakelang.shake.processor.program.mutable

import io.github.shakelang.shake.processor.program.types.ShakeScope

interface MutableShakeScope : ShakeScope {
    interface MutableShakeClassInstanceScope : MutableShakeScope, ShakeScope.ShakeClassInstanceScope
    interface MutableShakeClassStaticScope : MutableShakeScope, ShakeScope.ShakeClassStaticScope
    interface MutableShakeFunctionTypeScope : MutableShakeScope, ShakeScope.ShakeFunctionTypeScope
    interface MutableShakeFunctionScope : MutableShakeScope, ShakeScope.ShakeFunctionScope
    interface MutableShakeMethodScope : MutableShakeScope, ShakeScope.ShakeMethodScope
    interface MutableShakeConstructorScope : MutableShakeScope, ShakeScope.ShakeConstructorScope
    interface MutableShakeFileScope : MutableShakeScope, ShakeScope.ShakeFileScope
    interface MutableShakePackageScope : MutableShakeScope, ShakeScope.ShakePackageScope
    interface MutableShakeProjectScope : MutableShakeScope, ShakeScope.ShakeProjectScope
}
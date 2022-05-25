package io.github.shakelang.shake.processor.program.mutable

import io.github.shakelang.shake.processor.program.types.*
import io.github.shakelang.shake.processor.util.MutablePointerList

/**
 * A mutable version of [ShakeProject]
 */
interface MutableShakeProject : ShakeProject {

    override val classPointers: MutablePointerList<MutableShakeClass>
    override val fieldPointers: MutablePointerList<MutableShakeField>
    override val functionPointers: MutablePointerList<MutableShakeFunction>
    override val packagePointers: MutablePointerList<MutableShakePackage>

    override val classes: MutableList<MutableShakeClass>
    override val fields: MutableList<MutableShakeField>
    override val functions: MutableList<MutableShakeFunction>
    override val packages: MutableList<MutableShakePackage>

    override val scope: MutableShakeScope.MutableShakeProjectScope

}
package io.github.shakelang.shake.processor.program.mutable

import io.github.shakelang.shake.processor.program.types.ShakePackage
import io.github.shakelang.shake.processor.util.MutablePointerList
import io.github.shakelang.shake.processor.util.Pointer

interface MutableShakePackage : ShakePackage {

    override val parent: MutableShakePackage?
    override val project: MutableShakeProject

    override val scope: MutableShakeScope.MutableShakePackageScope

    override val subpackagePointers: MutablePointerList<MutableShakePackage>
    override val classPointers: MutablePointerList<MutableShakeClass>
    override val functionPointers: MutablePointerList<MutableShakeFunction>
    override val fieldPointers: MutablePointerList<MutableShakeField>

    override val subpackages: MutableList<MutableShakePackage>
    override val classes: MutableList<MutableShakeClass>
    override val functions: MutableList<MutableShakeFunction>
    override val fields: MutableList<MutableShakeField>

    override fun getPackage(name: String): Pointer<MutableShakePackage?>
    override fun getPackage(parts: Array<String>): Pointer<MutableShakePackage?>

}
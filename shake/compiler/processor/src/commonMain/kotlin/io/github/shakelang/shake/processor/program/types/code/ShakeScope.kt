package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeClass
import io.github.shakelang.shake.processor.program.types.ShakeDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeFunction

interface ShakeScope {
    val parent: ShakeScope?
    fun get(name: String): ShakeAssignable?
    fun getFunctions(name: String): List<ShakeFunction>
    fun getClass(name: String): ShakeClass?
    fun getInvokable(name: String): List<ShakeInvokable>
    fun use(name: String) 
}
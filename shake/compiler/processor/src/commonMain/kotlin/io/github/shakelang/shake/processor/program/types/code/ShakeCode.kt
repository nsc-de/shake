package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement

interface ShakeCode {
    val statements: List<ShakeStatement>
    fun toJson(): Map<String, Any>

    class Impl(override val statements: List<ShakeStatement>) : ShakeCode {
        override fun toJson(): Map<String, Any> = mapOf("statements" to statements.map { it.toJson() })
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeCode): ShakeCode {
            return Impl(it.statements.map { ShakeStatement.from(prj, it) })
        }
    }
}


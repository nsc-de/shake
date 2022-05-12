package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeProject

interface ShakeStatement {
    fun toJson(): Map<String, Any?>

    companion object {
        fun from(prj: ShakeProject, statement: ShakeStatement): ShakeStatement {
            TODO("Not yet implemented")
        }
    }
}
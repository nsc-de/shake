package io.github.shakelang.shake.processor.program.types.code.statements

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeWhile : ShakeStatement {
    val condition: ShakeValue
    val body: ShakeCode

    class Impl(override val condition: ShakeValue, override val body: ShakeCode) : ShakeWhile {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "while",
                "condition" to condition.toJson(),
                "body" to body.toJson()
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeWhile): ShakeWhile {
            return Impl(
                ShakeValue.from(prj, it.condition),
                ShakeCode.from(prj, it.body),
            )
        }
    }
}

interface ShakeDoWhile : ShakeStatement {
    val body: ShakeCode
    val condition: ShakeValue

    class Impl(override val condition: ShakeValue, override val body: ShakeCode) : ShakeDoWhile {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "do_while",
                "body" to body.toJson(),
                "condition" to condition.toJson()
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeDoWhile): ShakeDoWhile {
            return Impl(
                ShakeValue.from(prj, it.condition),
                ShakeCode.from(prj, it.body),
            )
        }
    }
}

interface ShakeFor : ShakeStatement {
    val init: ShakeStatement
    val condition: ShakeValue
    val update: ShakeStatement
    val body: ShakeCode

    class Impl(
        override val init: ShakeStatement,
        override val condition: ShakeValue,
        override val update: ShakeStatement,
        override val body: ShakeCode
    ) : ShakeFor {
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "for",
                "init" to init.toJson(),
                "condition" to condition.toJson(),
                "update" to update.toJson(),
                "body" to body.toJson()
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeFor): ShakeFor {
            return Impl(
                ShakeStatement.from(prj, it.init),
                ShakeValue.from(prj, it.condition),
                ShakeStatement.from(prj, it.update),
                ShakeCode.from(prj, it.body),
            )
        }
    }
}
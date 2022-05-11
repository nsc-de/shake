package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.parser.node.factor.ShakeIntegerNode
import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeType

interface ShakeDoubleLiteral : ShakeValue {
    val value: Double

    class Impl(override val value: Double) : ShakeDoubleLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.DOUBLE
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "double_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeDoubleLiteral): ShakeDoubleLiteral {
            return it
        }
    }
}

interface ShakeIntLiteral : ShakeValue {
    val value: Int

    class Impl(override val value: Int) : ShakeIntLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.INT
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "int_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeIntLiteral): ShakeIntLiteral {
            return it
        }
    }
}

interface ShakeBooleanLiteral : ShakeValue {
    val value: Boolean

    class Impl(override val value: Boolean) : ShakeBooleanLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.BOOLEAN
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "boolean_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeBooleanLiteral): ShakeBooleanLiteral {
            return it
        }
    }
}

interface ShakeCharacterLiteral : ShakeValue {
    val value: Char

    class Impl(override val value: Char) : ShakeCharacterLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.CHAR
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "character_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeCharacterLiteral): ShakeCharacterLiteral {
            return it
        }
    }
}
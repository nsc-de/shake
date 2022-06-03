package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeDoubleLiteral : ShakeValue {
    val value: Double

    class Impl(
        override val scope: ShakeScope,
        override val value: Double
    ) : ShakeDoubleLiteral {

        override val type: ShakeType get() = ShakeType.Primitive.DOUBLE
        override val typePointer: Pointer<ShakeType> get() = ShakeType.Primitive.DOUBLE.pointer()

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "double_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeDoubleLiteral): ShakeDoubleLiteral {
            return Impl(scope, it.value)
        }
    }
}

interface ShakeIntLiteral : ShakeValue {
    val value: Int

    class Impl(
        override val scope: ShakeScope,
        override val value: Int
    ) : ShakeIntLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.INT
        override val typePointer: Pointer<ShakeType> get() = ShakeType.Primitive.INT.pointer()
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "int_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeIntLiteral): ShakeIntLiteral {
            return Impl(scope, it.value)
        }
    }
}

interface ShakeBooleanLiteral : ShakeValue {
    val value: Boolean

    class Impl(
        override val scope: ShakeScope,
        override val value: Boolean
    ) : ShakeBooleanLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.BOOLEAN
        override val typePointer: Pointer<ShakeType> get() = ShakeType.Primitive.BOOLEAN.pointer()
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "boolean_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeBooleanLiteral): ShakeBooleanLiteral {
            return Impl(scope, it.value)
        }
    }
}

interface ShakeCharacterLiteral : ShakeValue {
    val value: Char

    class Impl(
        override val scope: ShakeScope,
        override val value: Char
    ) : ShakeCharacterLiteral {
        override val type: ShakeType get() = ShakeType.Primitive.CHAR
        override val typePointer: Pointer<ShakeType> get() = ShakeType.Primitive.CHAR.pointer()
        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "type" to "character_literal",
                "value" to value
            )
        }
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeCharacterLiteral): ShakeCharacterLiteral {
            return Impl(scope, it.value)
        }
    }
}
package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeClassField
import io.github.shakelang.shake.processor.program.types.ShakeDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeUsage : ShakeValue {
    val scope: ShakeScope
    val declaration: ShakeDeclaration
}

interface ShakeClassFieldUsage : ShakeUsage {
    override val scope: ShakeScope
    override val declaration: ShakeClassField
    val receiver: ShakeValue?
    val name : String

    class Impl(
        override val scope: ShakeScope,
        override val declaration: ShakeClassField,
        override val receiver: ShakeValue?,
        override val name: String,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeClassFieldUsage {
        override val type: ShakeType
            get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "declaration" to declaration.toJson(),
                "receiver" to receiver?.toJson(),
                "name" to name,
                "type" to type.toJson()
            )
        }
    }

}

interface ShakeStaticClassFieldUsage : ShakeUsage {
    val name : String

    class Impl(
        override val scope: ShakeScope,
        override val declaration: ShakeClassField,
        override val name: String,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeStaticClassFieldUsage {
        override val type: ShakeType
            get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "declaration" to declaration.toJson(),
                "name" to name,
                "type" to type.toJson()
            )
        }
    }
}

interface ShakeFieldUsage : ShakeUsage {
    val receiver: ShakeValue?
    val name get() = declaration.name

    class Impl(
        override val scope: ShakeScope,
        override val declaration: ShakeDeclaration,
        override val receiver: ShakeValue?,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeFieldUsage {
        override val type: ShakeType
            get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "declaration" to declaration.toJson(),
                "receiver" to receiver?.toJson(),
                "type" to type.toJson()
            )
        }
    }
}

interface ShakeVariableUsage : ShakeUsage {
    override val declaration: ShakeVariableDeclaration

    class Impl(
        override val scope: ShakeScope,
        override val declaration: ShakeVariableDeclaration,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeVariableUsage {
        override val type: ShakeType
            get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "declaration" to declaration.toJson(),
                "type" to type.toJson()
            )
        }
    }
}
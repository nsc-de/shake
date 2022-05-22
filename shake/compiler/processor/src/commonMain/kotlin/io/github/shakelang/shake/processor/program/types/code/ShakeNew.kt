package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeConstructor
import io.github.shakelang.shake.processor.program.types.ShakeProject
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeNew : ShakeValue, ShakeStatement {

    val reference: ShakeConstructor
    val arguments: List<ShakeValue>
    val parent: ShakeValue?
    val name: String

    class Impl(
        override val reference: ShakeConstructor,
        override val arguments: List<ShakeValue>,
        override val parent: ShakeValue?,
        override val name: String,
        override val typePointer: Pointer<ShakeType>
    ) : ShakeNew {
        override val type: ShakeType get() = typePointer.value

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "reference" to reference.toJson(),
                "arguments" to arguments.map { it.toJson() },
                "parent" to parent?.toJson(),
                "name" to name,
                "type" to type.toJson()
            )
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeNew): ShakeNew {TODO()/*
            return Impl(
                reference =
                arguments = it.arguments.map { ShakeValue.from(prj, it) },
                parent = it.parent?.let { ShakeValue.from(prj, it) },
                name = it.name,
                type = it.type
            )*/
        }
    }

}
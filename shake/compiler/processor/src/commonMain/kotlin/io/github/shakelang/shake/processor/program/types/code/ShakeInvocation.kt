package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

interface ShakeInvocation: ShakeValue, ShakeStatement {
    val invokablePointer: Pointer<ShakeInvokable>
    val invokable: ShakeInvokable
    val arguments: List<ShakeValue>
    val parent: ShakeValue?
    val name: String
    val isAnonymous: Boolean

    override fun toJson(): Map<String, Any?>

    class Impl: ShakeInvocation {
        override val scope: ShakeScope
        override val invokablePointer: Pointer<ShakeInvokable>
        override val invokable: ShakeInvokable get() = invokablePointer.value
        override val arguments: List<ShakeValue>
        override val parent: ShakeValue?
        override val name: String
        override val isAnonymous: Boolean
        override val typePointer: Pointer<ShakeType>
        override val type: ShakeType get() = typePointer.value

        constructor(
            scope: ShakeScope,
            callable: ShakeInvokable,
            arguments: List<ShakeValue>,
            parent: ShakeValue?,
            name: String,
            isAnonymous: Boolean,
            typePointer: Pointer<ShakeType>
        ) {
            this.scope = scope
            this.invokablePointer = callable.point()
            this.arguments = arguments
            this.parent = parent
            this.name = name
            this.isAnonymous = isAnonymous
            this.typePointer = typePointer
        }

        constructor(
            scope: ShakeScope,
            it: ShakeInvocation,
        ) {
            this.scope = scope
            this.invokablePointer = ShakeInvokable.from(scope.project, it.invokable)
            this.arguments = it.arguments.map { ShakeValue.from(scope, it) }
            this.parent = it.parent?.let { ShakeValue.from(scope, it) }
            this.name = it.name
            this.isAnonymous = it.isAnonymous
            this.typePointer = it.typePointer
        }


        override fun toJson(): Map<String, Any?> = mapOf(
            "callable" to invokable.toJson(),
            "arguments" to arguments.map { it.toJson() },
            "parent" to parent?.toJson(),
            "name" to name,
            "isAnonymous" to isAnonymous
        )
    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeInvocation): ShakeInvocation {
            return Impl(scope, it)
        }
    }
}
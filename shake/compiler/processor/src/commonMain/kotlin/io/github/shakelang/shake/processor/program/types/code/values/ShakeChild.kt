package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.ShakeScope

interface ShakeChild : ShakeAssignable {
    val scope: ShakeScope
    val parent : ShakeValue
    val name: String
    val access : ShakeValue

    class Impl(
        override val scope: ShakeScope,
        override val parent: ShakeValue,
        override val name: String,
        override val access: ShakeValue,
        override val type: ShakeType
    ) : ShakeChild {
        override val qualifiedName: String get() = "${parent.type.name}.$name"
        override fun assignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun additionAssignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun subtractionAssignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun multiplicationAssignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun divisionAssignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun modulusAssignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun powerAssignType(other: ShakeType): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun incrementBeforeType(): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun incrementAfterType(): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun decrementBeforeType(): ShakeType? {
            TODO("Not yet implemented")
        }

        override fun decrementAfterType(): ShakeType? {
            TODO("Not yet implemented")
        }

    }
}

interface ShakeChildUsage : ShakeUsage {
    val used: ShakeChild
}
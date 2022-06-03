package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement

interface ShakeCode {
    val parentScope: ShakeScope
    val scope: ShakeScope
    val statements: List<ShakeStatement>
    fun toJson(): Map<String, Any>

    class Impl : ShakeCode {
        override val parentScope: ShakeScope
        override val statements: List<ShakeStatement>
        override val scope: ShakeScope

        internal constructor(
            constructor: Constructor,
            parentScope: ShakeScope,
            statements: List<ShakeStatement>,
        ) {
            when(constructor) {
                    Constructor.COPY -> {
                    this.parentScope = parentScope
                    this.scope = ShakeScope.ShakeCodeScope.from(this)
                    this.statements = statements.map { ShakeStatement.from(this.scope, it) }
                }
                Constructor.CREATE -> {
                    this.parentScope = parentScope
                    this.scope = ShakeScope.ShakeCodeScope.from(this)
                    this.statements = statements
                }
            }
        }

        constructor(
            parentScope: ShakeScope,
            statements: List<ShakeStatement>,
        ) : this(Constructor.CREATE, parentScope, statements)

        override fun toJson(): Map<String, Any> = mapOf("statements" to statements.map { it.toJson() })

        internal enum class Constructor {
            CREATE, COPY
        }

    }

    companion object {
        fun from(scope: ShakeScope, it: ShakeCode): ShakeCode {
            return Impl(Impl.Constructor.COPY, scope, it.statements)
        }
    }
}


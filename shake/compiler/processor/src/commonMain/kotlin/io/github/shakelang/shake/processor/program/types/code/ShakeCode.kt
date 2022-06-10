package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.program.types.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeStatement

interface ShakeCode {
    val parentScope: ShakeScope
    val scope: ShakeScope
    val statements: List<ShakeStatement>
    fun toJson(): Map<String, Any>

    class Impl : ShakeCode {
        override val parentScope: ShakeScope.ShakeScopeImpl
        override val statements: List<ShakeStatement>
        override val scope: ShakeScope

        internal constructor(
            constructor: Constructor,
            parentScope: ShakeScope.ShakeScopeImpl,
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
            parentScope: ShakeScope.ShakeScopeImpl,
            statements: List<ShakeStatement>,
        ) : this(Constructor.CREATE, parentScope, statements)

        internal constructor(
            parentScope: ShakeScope.ShakeScopeImpl,
            createStatements: (ShakeScope.ShakeCodeScope.Impl) -> List<ShakeStatement>,
        ) {
            this.parentScope = parentScope
            this.scope = ShakeScope.ShakeCodeScope.from(this)
            this.statements = createStatements(this.scope)
        }

        override fun toJson(): Map<String, Any> = mapOf("statements" to statements.map { it.toJson() })

        internal enum class Constructor {
            CREATE, COPY
        }

    }

    companion object {
        fun from(scope: ShakeScope.ShakeScopeImpl, it: ShakeCode): ShakeCode {
            return Impl(Impl.Constructor.COPY, scope, it.statements)
        }

        fun create(scope: ShakeScope.ShakeScopeImpl, createStatements: (scope: ShakeScope.ShakeCodeScope.Impl) -> List<ShakeStatement>): Impl {
            return Impl(scope, createStatements)
        }
    }
}


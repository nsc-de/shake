package io.github.shakelang.shake.processor.program.types.code.values

import io.github.shakelang.shake.processor.program.types.ShakeClassField
import io.github.shakelang.shake.processor.program.types.ShakeDeclaration
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration

interface ShakeUsage : ShakeValue {
    val scope: ShakeScope
    val declaration: ShakeDeclaration
}

interface ShakeClassFieldUsage : ShakeUsage {
    override val scope: ShakeScope
    override val declaration: ShakeClassField
    val receiver: ShakeValue?
    val name : String
}

interface ShakeStaticClassFieldUsage : ShakeUsage {
    val name : String
}

interface ShakeFieldUsage : ShakeUsage {
    val receiver: ShakeValue?
    val name get() = declaration.name
}

interface ShakeVariableUsage : ShakeUsage {
    override val declaration: ShakeVariableDeclaration
    val name: String
}
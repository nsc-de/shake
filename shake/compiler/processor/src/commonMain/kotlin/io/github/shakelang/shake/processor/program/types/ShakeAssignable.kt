package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer

interface ShakeAssignable {

    val qualifiedName: String
    val typePointer: Pointer<ShakeType>
    val type: ShakeType
    val declaration: ShakeDeclaration

    //fun access(scope: ShakeScope): ShakeValue

    fun assignType(other: ShakeType): ShakeType?
    fun additionAssignType(other: ShakeType): ShakeType?
    fun subtractionAssignType(other: ShakeType): ShakeType?
    fun multiplicationAssignType(other: ShakeType): ShakeType?
    fun divisionAssignType(other: ShakeType): ShakeType?
    fun modulusAssignType(other: ShakeType): ShakeType?
    fun powerAssignType(other: ShakeType): ShakeType?
    fun incrementBeforeType(): ShakeType?
    fun incrementAfterType(): ShakeType?
    fun decrementBeforeType(): ShakeType?
    fun decrementAfterType(): ShakeType?
    fun access(scope: ShakeScope, receiver: ShakeValue? = null): ShakeValue

    companion object {
        fun from(scope: ShakeScope, variable: ShakeAssignable): Pointer<ShakeAssignable> {
            return when (variable) {
                is ShakeVariableDeclaration -> TODO("Implement ShakeVariableDeclaration")
                is ShakeField -> scope.project.getFieldBySignature(variable.signature)
                    .transform { it ?: throw IllegalStateException("Field not found: ${variable.signature}") }
                is ShakeClassField -> scope.project.getClassFieldBySignature(variable.signature)
                    .transform { throw IllegalStateException("ClassField not found: ${variable.signature}") }
                else -> {
                    throw IllegalStateException("Unknown assignable: $variable")
                }
            }
        }
    }
}
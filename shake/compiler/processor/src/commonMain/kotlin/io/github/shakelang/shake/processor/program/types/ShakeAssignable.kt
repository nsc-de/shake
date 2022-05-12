package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

interface ShakeAssignable {

    val qualifiedName: String
    val type: ShakeType

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

    companion object {
        fun from(prj: ShakeProject, variable: ShakeAssignable): ShakeAssignable {
            return when (variable) {
                is ShakeVariableDeclaration -> TODO("Implement ShakeVariableDeclaration")
                is ShakeField -> prj.getFieldBySignature(variable.signature) ?: throw IllegalStateException("Field not found: ${variable.signature}")
                is ShakeClassField -> prj.getClassFieldBySignature(variable.signature) ?: throw IllegalStateException("ClassField not found: ${variable.signature}")
                else -> {
                    throw IllegalStateException("Unknown assignable: $variable")
                }
            }
        }
    }
}
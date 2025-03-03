package com.shakelang.shake.processor

import com.shakelang.shake.lexer.ShakeLexer
import com.shakelang.shake.parser.node.*
import com.shakelang.shake.parser.node.expression.*
import com.shakelang.shake.parser.node.factor.*
import com.shakelang.shake.parser.node.functions.ShakeInvocationNode
import com.shakelang.shake.parser.node.functions.ShakeReturnNode
import com.shakelang.shake.parser.node.loops.ShakeDoWhileNode
import com.shakelang.shake.parser.node.loops.ShakeForNode
import com.shakelang.shake.parser.node.loops.ShakeWhileNode
import com.shakelang.shake.parser.node.objects.ShakeClassConstructionNode
import com.shakelang.shake.parser.node.variables.*
import com.shakelang.shake.processor.program.creation.CreationShakeAssignable
import com.shakelang.shake.processor.program.creation.CreationShakeProject
import com.shakelang.shake.processor.program.creation.CreationShakeScope
import com.shakelang.shake.processor.program.creation.CreationShakeType
import com.shakelang.shake.processor.program.creation.code.*
import com.shakelang.shake.processor.program.creation.code.statements.*
import com.shakelang.shake.processor.program.creation.code.values.*
import com.shakelang.util.logger.debug
import com.shakelang.util.parseutils.File
import com.shakelang.util.parseutils.characters.streaming.CharacterInputStream
import com.shakelang.util.parseutils.characters.streaming.SourceCharacterInputStream

class ShakeProcessorOptions {
    var precalculate: Boolean = true
}

abstract class ShakeProcessor<T> {

    val options: ShakeProcessorOptions = ShakeProcessorOptions()

    abstract val src: T

    open fun parseFile(src: String): ShakeFileNode {
        return parseFile(src, File(src).contents)
    }

    open fun parseFile(src: String, contents: CharArray): ShakeFileNode {
        val chars: CharacterInputStream = SourceCharacterInputStream(src, contents)
        val lexer = ShakeLexer(chars)
        val tokens = lexer.makeTokens()
        val parser = com.shakelang.shake.parser.ShakeParser.from(tokens)
        return parser.parse()
    }

    abstract fun loadFile(directory: String, src: String)

    abstract fun phase1()
    abstract fun phase2()
    abstract fun phase3()
    abstract fun phase4()

    fun <O> generate(f: (T) -> O): O {
        return f(src)
    }

    companion object {
        val debug = debug("shake:compiler:processor")
    }
}

open class ShakePackageBasedProcessor : ShakeProcessor<CreationShakeProject>() {

    val codeProcessor = ShakeASTProcessor()
    open val project = CreationShakeProject(codeProcessor)
    override val src: CreationShakeProject
        get() = project

    open fun loadSynthetic(src: String, contents: ShakeFileNode) {
        val reformatted = src.replace("\\", "/")
        project.putFile(reformatted, contents)
    }

    open fun loadSynthetic(src: String, contents: String) {
        loadSynthetic(src, parseFile(src, contents.toCharArray()))
    }

    override fun loadFile(directory: String, src: String) {
        val reformatted = src.replace("\\", "/")
        val contents = parseFile("$directory/$src")
        project.putFile(reformatted, contents)
    }

    override fun phase1() {
        project.phase1()
    }

    override fun phase2() {
        project.phase2()
    }

    override fun phase3() {
        project.phase3()
    }

    override fun phase4() {
        project.phase4()
    }

    fun finish(): CreationShakeProject {
        project.finish()
        return project
    }
}

open class ShakeASTProcessor {

    fun visitValue(scope: CreationShakeScope, value: ShakeValuedNode): CreationShakeValue {
        return when (value) {
            // Literals
            is ShakeIntegerNode -> visitIntegerNode(scope, value)
            is ShakeDoubleNode -> visitDoubleNode(scope, value)
            is ShakeStringNode -> visitStringNode(scope, value)
            is ShakeLogicalTrueNode -> visitLogicalTrueNode(scope, value)
            is ShakeLogicalFalseNode -> visitLogicalFalseNode(scope, value)
            is ShakeNullNode -> visitNullNode(scope, value)

            is ShakeLogicalAndNode -> visitLogicalAndNode(scope, value)
            is ShakeLogicalOrNode -> visitLogicalOrNode(scope, value)
            is ShakeLogicalXOrNode -> visitLogicalXOrNode(scope, value)
            is ShakeEqualNode -> visitEqEqualsNode(scope, value)
            is ShakeGreaterThanOrEqualNode -> visitBiggerEqualsNode(scope, value)
            is ShakeLessThanOrEqualNode -> visitSmallerEqualsNode(scope, value)
            is ShakeGreaterThanNode -> visitBiggerNode(scope, value)
            is ShakeLessThanNode -> visitSmallerNode(scope, value)
            is ShakeAddNode -> visitAddNode(scope, value)
            is ShakeSubNode -> visitSubNode(scope, value)
            is ShakeMulNode -> visitMulNode(scope, value)
            is ShakeDivNode -> visitDivNode(scope, value)
            is ShakeModNode -> visitModNode(scope, value)
            is ShakePowNode -> visitPowNode(scope, value)
            is ShakeVariableAssignmentNode -> visitVariableAssignmentNode(scope, value)
            is ShakeVariableAddAssignmentNode -> visitVariableAddAssignmentNode(scope, value)
            is ShakeVariableSubAssignmentNode -> visitVariableSubAssignmentNode(scope, value)
            is ShakeVariableMulAssignmentNode -> visitVariableMulAssignmentNode(scope, value)
            is ShakeVariableDivAssignmentNode -> visitVariableDivAssignmentNode(scope, value)
            is ShakeVariableModAssignmentNode -> visitVariableModAssignmentNode(scope, value)
            is ShakeVariablePowAssignmentNode -> visitVariablePowAssignmentNode(scope, value)
            is ShakeVariableIncreaseNode -> visitVariableIncrementNode(scope, value)
            is ShakeVariableDecreaseNode -> visitVariableDecrementNode(scope, value)
            is ShakeVariableUsageNode -> visitVariableUsageNode(scope, value)
            is ShakeCastNode -> visitCastNode(scope, value)
            is ShakeInvocationNode -> visitFunctionCallNode(scope, value)
            is ShakeClassConstructionNode -> visitClassConstruction(scope, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value::class.simpleName}")
        }
    }

    fun visitStatement(scope: CreationShakeScope, statement: ShakeStatementNode): CreationShakeStatement {
        return when (statement) {
            is ShakeIfNode -> visitIfNode(scope, statement)
            is ShakeWhileNode -> visitWhileNode(scope, statement)
            is ShakeDoWhileNode -> visitDoWhileNode(scope, statement)
            is ShakeForNode -> visitForNode(scope, statement)
            is ShakeReturnNode -> visitReturnNode(scope, statement)
            is ShakeVariableDeclarationNode -> visitVariableDeclarationNode(scope, statement)
            is ShakeVariableAssignmentNode -> visitVariableAssignmentNode(scope, statement)
            is ShakeVariableAddAssignmentNode -> visitVariableAddAssignmentNode(scope, statement)
            is ShakeVariableSubAssignmentNode -> visitVariableSubAssignmentNode(scope, statement)
            is ShakeVariableMulAssignmentNode -> visitVariableMulAssignmentNode(scope, statement)
            is ShakeVariableDivAssignmentNode -> visitVariableDivAssignmentNode(scope, statement)
            is ShakeVariableModAssignmentNode -> visitVariableModAssignmentNode(scope, statement)
            is ShakeVariablePowAssignmentNode -> visitVariablePowAssignmentNode(scope, statement)
            is ShakeVariableIncreaseNode -> visitVariableIncrementNode(scope, statement)
            is ShakeVariableDecreaseNode -> visitVariableDecrementNode(scope, statement)
            is ShakeInvocationNode -> visitFunctionCallNode(scope, statement)
            is ShakeClassConstructionNode -> visitClassConstruction(scope, statement)
            else -> throw IllegalArgumentException("Unsupported statement type: ${statement::class}")
        }
    }

    fun visitTree(scope: CreationShakeScope, t: ShakeBlockNode): CreationShakeCode {
        return CreationShakeCode(
            t.children.map {
                visitStatement(scope, it)
            },
        )
    }

    fun visitType(scope: CreationShakeScope, t: ShakeVariableType): CreationShakeType? {
        return when (t.type) {
            ShakeVariableType.Type.DYNAMIC -> null
            ShakeVariableType.Type.BYTE -> CreationShakeType.Primitives.BYTE
            ShakeVariableType.Type.SHORT -> CreationShakeType.Primitives.SHORT
            ShakeVariableType.Type.INTEGER -> CreationShakeType.Primitives.INT
            ShakeVariableType.Type.LONG -> CreationShakeType.Primitives.LONG
            ShakeVariableType.Type.FLOAT -> CreationShakeType.Primitives.FLOAT
            ShakeVariableType.Type.DOUBLE -> CreationShakeType.Primitives.DOUBLE
            ShakeVariableType.Type.BOOLEAN -> CreationShakeType.Primitives.BOOLEAN
            else -> null
        }
    }

    private fun visitDoubleNode(scope: CreationShakeScope, n: ShakeDoubleNode): CreationShakeDoubleLiteral {
        return CreationShakeDoubleLiteral(scope.project, n.number)
    }

    private fun visitIntegerNode(scope: CreationShakeScope, n: ShakeIntegerNode): CreationShakeIntegerLiteral {
        return CreationShakeIntegerLiteral(scope.project, n.number)
    }

    private fun visitStringNode(scope: CreationShakeScope, n: ShakeStringNode): CreationShakeStringLiteral {
        return CreationShakeStringLiteral(scope.project, n.value)
    }

    private fun visitAddNode(scope: CreationShakeScope, n: ShakeAddNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val aw = left.type.additionOperator(right.type, scope)
        return if (aw.overload != null) {
            CreationShakeInvocation.create(scope.project, aw.overload, listOf(right), left)
        } else {
            throw Exception("No addition operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitSubNode(scope: CreationShakeScope, n: ShakeSubNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val aw = left.type.subtractionOperator(right.type, scope)
        return if (aw.overload != null) {
            CreationShakeInvocation.create(scope.project, aw.overload, listOf(right), left)
        } else {
            throw Exception("No subtraction operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitMulNode(scope: CreationShakeScope, n: ShakeMulNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val aw = left.type.multiplicationOperator(right.type, scope)
        return if (aw.overload != null) {
            CreationShakeInvocation.create(scope.project, aw.overload, listOf(right), left)
        } else {
            throw Exception("No multiplication operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitDivNode(scope: CreationShakeScope, n: ShakeDivNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val aw = left.type.divisionOperator(right.type, scope)
        return if (aw.overload != null) {
            CreationShakeInvocation.create(scope.project, aw.overload, listOf(right), left)
        } else {
            throw Exception("No division operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitModNode(scope: CreationShakeScope, n: ShakeModNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val aw = left.type.modulusOperator(right.type, scope)
        return if (aw.overload != null) {
            CreationShakeInvocation.create(scope.project, aw.overload, listOf(right), left)
        } else {
            throw Exception("No modulus operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitPowNode(scope: CreationShakeScope, n: ShakePowNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val aw = left.type.powerOperator(right.type, scope)
        return if (aw.overload != null) {
            CreationShakeInvocation.create(scope.project, aw.overload, listOf(right), left)
        } else {
            throw Exception("No power operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitVariableDeclarationNode(
        scope: CreationShakeScope,
        n: ShakeVariableDeclarationNode,
    ): CreationShakeVariableDeclaration {
        val value = if (n.value != null) visitValue(scope, n.value!!) else null
        val type = visitType(scope, n.type) ?: value?.type ?: throw Exception("Cannot infer type of variable ${n.name}")
        val decl = CreationShakeVariableDeclaration(scope, n.name, type, value, n.isFinal)
        scope.setField(decl)
        return decl
    }

    private fun getAssignable(scope: CreationShakeScope, n: ShakeValuedNode): CreationShakeAssignable? {
        if (n is ShakeVariableUsageNode) {
            val identifier = n.identifier
            if (identifier.parent != null) {
                val parent = visitValue(scope, identifier.parent!!)
                val type = parent.type.childType(identifier.name, scope)
                    ?: throw Exception("Cannot access ${identifier.name} in ${parent.type}")
                return CreationShakeChild(
                    scope.project,
                    scope,
                    parent,
                    parent.type.childField(identifier.name, scope)
                        ?: throw Exception("No field named ${identifier.name} in ${parent.type}"),
                )
            }
            return scope.getField(identifier.name)
        }
        return CreationShakeAssignable.wrap(scope.project, visitValue(scope, n))
    }

    private fun visitVariableAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariableAssignmentNode,
    ): CreationShakeAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createAssignment(value, scope)
    }

    private fun visitVariableAddAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariableAddAssignmentNode,
    ): CreationShakeAddAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createAddAssignment(value, scope)
    }

    private fun visitVariableSubAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariableSubAssignmentNode,
    ): CreationShakeSubAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createSubtractAssignment(value, scope)
    }

    private fun visitVariableMulAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariableMulAssignmentNode,
    ): CreationShakeMulAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createMultiplyAssignment(value, scope)
    }

    private fun visitVariableDivAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariableDivAssignmentNode,
    ): CreationShakeDivAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createDivideAssignment(value, scope)
    }

    private fun visitVariableModAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariableModAssignmentNode,
    ): CreationShakeModAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createModulusAssignment(value, scope)
    }

    private fun visitVariablePowAssignmentNode(
        scope: CreationShakeScope,
        n: ShakeVariablePowAssignmentNode,
    ): CreationShakePowerAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if (variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        return variable.createPowerAssignment(value, scope)
    }

    private fun visitVariableIncrementNode(
        scope: CreationShakeScope,
        n: ShakeVariableIncreaseNode,
    ): CreationShakeIncrementBefore {
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        return variable.createIncrementBeforeAssignment(scope)
    }

    private fun visitVariableDecrementNode(
        scope: CreationShakeScope,
        n: ShakeVariableDecreaseNode,
    ): CreationShakeDecrementBefore {
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        return variable.createDecrementBeforeAssignment(scope)
    }

    private fun visitVariableUsageNode(scope: CreationShakeScope, n: ShakeVariableUsageNode): CreationShakeValue {
        val identifier = n.identifier
        if (identifier.parent != null) {
            val parent = visitValue(scope, identifier.parent!!)
            val type = parent.type.childType(identifier.name, scope)
                ?: throw Exception("Cannot access ${identifier.name} in ${parent.type}")
            return CreationShakeChild(
                scope.project,
                scope,
                parent,
                parent.type.childField(identifier.name, scope)
                    ?: throw Exception("No field named ${identifier.name} in ${parent.type}"),
            ).access(scope)
        }
        val variable = scope.getField(identifier.name) ?: throw Exception("Variable ${identifier.name} not declared")
        return variable.access(scope) // TODO null value
    }

    private fun visitEqEqualsNode(scope: CreationShakeScope, n: ShakeEqualNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val eq = left.type.equalsOperator(right.type, scope)
        return if (eq.overload != null) {
            CreationShakeInvocation.create(scope.project, eq.overload, listOf(right), left)
        } else {
            throw Exception("No equals operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitBiggerEqualsNode(
        scope: CreationShakeScope,
        n: ShakeGreaterThanOrEqualNode,
    ): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val eq = left.type.greaterThanOrEqualOperator(right.type, scope)
        return if (eq.overload != null) {
            CreationShakeInvocation.create(scope.project, eq.overload, listOf(right), left)
        } else {
            throw Exception("No greater equals operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitSmallerEqualsNode(
        scope: CreationShakeScope,
        n: ShakeLessThanOrEqualNode,
    ): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val eq = left.type.lessThanOrEqualOperator(right.type, scope)
        return if (eq.overload != null) {
            CreationShakeInvocation.create(scope.project, eq.overload, listOf(right), left)
        } else {
            throw Exception("No less equals operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitBiggerNode(scope: CreationShakeScope, n: ShakeGreaterThanNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val eq = left.type.greaterThanOperator(right.type, scope)
        return if (eq.overload != null) {
            CreationShakeInvocation.create(scope.project, eq.overload, listOf(right), left)
        } else {
            throw Exception("No greater operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitSmallerNode(scope: CreationShakeScope, n: ShakeLessThanNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val eq = left.type.lessThanOperator(right.type, scope)
        return if (eq.overload != null) {
            CreationShakeInvocation.create(scope.project, eq.overload, listOf(right), left)
        } else {
            throw Exception("No less operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitLogicalAndNode(scope: CreationShakeScope, n: ShakeLogicalAndNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val operator = left.type.andOperator(right.type, scope)
        return if (operator.overload != null) {
            CreationShakeInvocation.create(scope.project, operator.overload, listOf(right), left)
        } else {
            throw Exception("No and operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitLogicalOrNode(scope: CreationShakeScope, n: ShakeLogicalOrNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val operator = left.type.orOperator(right.type, scope)
        return if (operator.overload != null) {
            CreationShakeInvocation.create(scope.project, operator.overload, listOf(right), left)
        } else {
            throw Exception("No or operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitLogicalXOrNode(scope: CreationShakeScope, n: ShakeLogicalXOrNode): CreationShakeValue {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val operator = left.type.xorOperator(right.type, scope)
        return if (operator.overload != null) {
            CreationShakeInvocation.create(scope.project, operator.overload, listOf(right), left)
        } else {
            throw Exception("No xor operator for ${left.type} and ${right.type}")
        }
    }

    private fun visitBoolean(scope: CreationShakeScope, n: ShakeValuedNode): CreationShakeValue {
        val value = visitValue(scope, n)
        return if (value.type == CreationShakeType.Primitives.BOOLEAN) {
            value
        } else {
            throw Exception("Cannot convert ${value.type} to boolean")
        }
    }

    private fun visitWhileNode(scope: CreationShakeScope, n: ShakeWhileNode): CreationShakeWhile {
        val condition = visitBoolean(scope, n.condition)
        val body = visitTree(scope, n.body)
        return CreationShakeWhile(condition, body)
    }

    private fun visitDoWhileNode(scope: CreationShakeScope, n: ShakeDoWhileNode): CreationShakeDoWhile {
        val condition = visitBoolean(scope, n.condition)
        val body = visitTree(scope, n.body)
        return CreationShakeDoWhile(condition, body)
    }

    private fun visitForNode(scope: CreationShakeScope, n: ShakeForNode): CreationShakeFor {
        val init = visitStatement(scope, n.declaration)
        val condition = visitBoolean(scope, n.condition)
        val update = visitStatement(scope, n.round)
        val body = visitTree(scope, n.body)
        return CreationShakeFor(init, condition, update, body)
    }

    private fun visitIfNode(scope: CreationShakeScope, n: ShakeIfNode): CreationShakeIf {
        val condition = visitBoolean(scope, n.condition)
        val body = visitTree(scope, n.body)
        if (n.elseBody != null) {
            val elseBody = visitTree(scope, n.elseBody!!)
            return CreationShakeIf(condition, body, elseBody)
        }
        return CreationShakeIf(condition, body)
    }

    private fun visitClassConstruction(scope: CreationShakeScope, n: ShakeClassConstructionNode): CreationShakeNew {
        val classNode = n.type
        if (classNode is ShakeVariableUsageNode) {
            val identifierNode = classNode.identifier
            if (identifierNode.parent != null) {
                TODO("Construction of inner classes not implemented")
            }
            val className = identifierNode.name
            val clz = scope.getClass(className) ?: throw Exception("Class $className not found")
            val args = n.args.map { visitValue(scope, it) }
            val types = args.map { it.type }
            val constructors = clz.constructors
            if (constructors.isEmpty()) throw Exception("No constructor found for class $className")
            val constructor = ShakeSelect.selectConstructor(constructors, types)
                ?: throw Exception("No constructor found for class $className with arguments $types")
            return CreationShakeNew(scope.project, constructor, args)
        }
        TODO("Returned constructor. Will this ever be possible?")
    }

    private fun visitFunctionCallNode(scope: CreationShakeScope, n: ShakeInvocationNode): CreationShakeInvocation {
        val functionNode = n.function
        if (functionNode is ShakeVariableUsageNode) {
            val identifierNode = functionNode.identifier
            if (identifierNode.parent != null) {
                val parent = visitValue(scope, identifierNode.parent!!)
                val name = identifierNode.name
                val args = n.args.map { visitValue(scope, it) }
                val types = args.map { it.type }
                val functions = parent.type.childFunctions(name, scope)
                    ?: throw Exception("No function named $name in ${parent.type}")
                val function = ShakeSelect.selectFunction(functions, types)
                    ?: throw Exception("No function named $name with arguments $types in ${parent.type}")
                return CreationShakeInvocation.create(scope.project, function, args, parent)
            }

            val name = identifierNode.name
            val args = n.args.map { visitValue(scope, it) }
            val types = args.map { it.type }
            val functions = scope.getFunctions(name)
            if (functions.isEmpty()) throw Exception("No function named $name")
            val function = ShakeSelect.selectFunction(functions, types)
                ?: throw Exception("No function named $name with arguments $types")
            return CreationShakeInvocation.create(scope.project, function, args, null)
        }
        TODO("Direct returned lambda functions")
    }

    private fun visitLogicalTrueNode(scope: CreationShakeScope, n: ShakeLogicalTrueNode): CreationShakeValue {
        return CreationShakeBooleanLiteral(scope.project, true)
    }

    private fun visitLogicalFalseNode(scope: CreationShakeScope, n: ShakeLogicalFalseNode): CreationShakeValue {
        return CreationShakeBooleanLiteral(scope.project, false)
    }

    private fun visitNullNode(scope: CreationShakeScope, n: ShakeNullNode): CreationShakeValue {
        return CreationShakeNullLiteral(scope.project)
    }

    private fun visitCastNode(scope: CreationShakeScope, n: ShakeCastNode): CreationShakeCast {
        val value = visitValue(scope, n.value)
        val target = when (n.castTarget.type) {
            ShakeCastNode.CastTarget.CastTargetType.CHAR -> CreationShakeType.Primitives.CHAR
            ShakeCastNode.CastTarget.CastTargetType.BYTE -> CreationShakeType.Primitives.BYTE
            ShakeCastNode.CastTarget.CastTargetType.SHORT -> CreationShakeType.Primitives.SHORT
            ShakeCastNode.CastTarget.CastTargetType.INT -> CreationShakeType.Primitives.INT
            ShakeCastNode.CastTarget.CastTargetType.LONG -> CreationShakeType.Primitives.LONG
            ShakeCastNode.CastTarget.CastTargetType.FLOAT -> CreationShakeType.Primitives.FLOAT
            ShakeCastNode.CastTarget.CastTargetType.DOUBLE -> CreationShakeType.Primitives.DOUBLE
            ShakeCastNode.CastTarget.CastTargetType.BOOLEAN -> CreationShakeType.Primitives.BOOLEAN
            ShakeCastNode.CastTarget.CastTargetType.OBJECT -> {
                val st = n.castTarget.subtype ?: throw Exception("No subtype for object cast")
                if (n.castTarget.subtype!!.parts.size != 1) TODO()
                val type = scope.getClass(n.castTarget.subtype!!.parts[0])
                    ?: throw Exception("No class named ${n.castTarget.subtype!!.parts[0]} for object cast")
                type.asType()
            }

            else -> throw Exception("Unsupported cast target type ${n.castTarget.type}")
        }
        return CreationShakeCast(scope.project, value, target)
    }

    private fun visitReturnNode(scope: CreationShakeScope, n: ShakeReturnNode): CreationShakeReturn {
        val value = visitValue(scope, n.value)
        return CreationShakeReturn(value)
    }
}

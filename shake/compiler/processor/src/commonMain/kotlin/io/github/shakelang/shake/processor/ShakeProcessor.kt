package io.github.shakelang.shake.processor

import io.github.shakelang.parseutils.File
import io.github.shakelang.parseutils.characters.streaming.CharacterInputStream
import io.github.shakelang.parseutils.characters.streaming.SourceCharacterInputStream
import io.github.shakelang.shake.lexer.ShakeLexer
import io.github.shakelang.shake.parser.ShakeParser
import io.github.shakelang.shake.parser.node.*
import io.github.shakelang.shake.parser.node.ShakeFileNode
import io.github.shakelang.shake.parser.node.expression.*
import io.github.shakelang.shake.parser.node.factor.ShakeDoubleNode
import io.github.shakelang.shake.parser.node.factor.ShakeIntegerNode
import io.github.shakelang.shake.parser.node.functions.ShakeFunctionArgumentNode
import io.github.shakelang.shake.parser.node.functions.ShakeFunctionCallNode
import io.github.shakelang.shake.parser.node.functions.ShakeFunctionDeclarationNode
import io.github.shakelang.shake.parser.node.functions.ShakeReturnNode
import io.github.shakelang.shake.parser.node.logical.*
import io.github.shakelang.shake.parser.node.loops.ShakeDoWhileNode
import io.github.shakelang.shake.parser.node.loops.ShakeForNode
import io.github.shakelang.shake.parser.node.loops.ShakeWhileNode
import io.github.shakelang.shake.parser.node.objects.ShakeClassConstructionNode
import io.github.shakelang.shake.parser.node.objects.ShakeClassDeclarationNode
import io.github.shakelang.shake.parser.node.objects.ShakeConstructorDeclarationNode
import io.github.shakelang.shake.parser.node.variables.*
import io.github.shakelang.shake.processor.program.types.*
import io.github.shakelang.shake.processor.program.types.code.*
import io.github.shakelang.shake.processor.program.types.code.statements.*
import io.github.shakelang.shake.processor.program.types.code.values.*
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.notNull
import io.github.shakelang.shake.processor.util.point

open class ShakeCodeProcessor {

    private val project: ShakeProject.Impl = ShakeProject.empty()
    val src: ShakeProject
        get() = project

    open fun parseFile(src: String): ShakeFileNode {

        val file = File(src).contents
        val chars: CharacterInputStream = SourceCharacterInputStream(src, file)

        val lexer = ShakeLexer(chars)
        val tokens = lexer.makeTokens()
        val parser = ShakeParser.from(tokens)
        return parser.parse()

    }

    fun <O> generate(f: (ShakeProject) -> O): O {
        return f(src)
    }

    fun loadFile(directory: String, src: String) {
        val reformatted = src.replace("\\", "/")
        val contents = parseFile("$directory/$src")
        addFile(reformatted, contents)
    }

    fun addFile(name: String, contents: ShakeFileNode) {
        val reformatted = name.replace("\\", "/")
        addFile(name.split("/").toTypedArray(), contents)
    }

    fun addFile(name: Array<String>, contents: ShakeFileNode) {
        val fname = name.last()
        addFile(name.dropLast(1).toTypedArray(), fname, contents)
    }

    fun addFile(pkg: Array<String>, name: String, contents: ShakeFileNode) {
        val pkgInstance = if(pkg.isNotEmpty()) project.getPackageF(pkg) else null
        visitFileNode(pkgInstance, name, contents)
    }

    fun visitFileNode(pkg: ShakePackage.Impl?, name: String, node: ShakeFileNode): ShakeFile.Impl {
        val scope = pkg?.scope ?: project.scope
        return ShakeFile.create(name, scope, { file ->
            node.children.filterIsInstance<ShakeImportNode>().map { visitImportNode(file, it) }
        }, { file ->
            node.children.filterIsInstance<ShakeVariableDeclarationNode>().map { visitFieldNode(file, it) }
        }, { file ->
            node.children.filterIsInstance<ShakeFunctionDeclarationNode>().map { visitFunctionNode(file, it) }
        }, { file ->
            node.children.filterIsInstance<ShakeClassDeclarationNode>().map { visitClassNode(file, it) }
        })
    }

    fun visitImportNode(file: ShakeFile.Impl, node: ShakeImportNode): ShakeImport.Impl {
        return ShakeImport.create(file, node.import)
    }

    fun visitClassFieldNode(clazz: ShakeClass.Impl, node: ShakeVariableDeclarationNode)
        = ShakeClassField.create(
            clazz,
            node.name,
            clazz.project.getType(node.type).notNull(),
            if(node.isStatic) clazz.staticScope else clazz.instanceScope,
            node.isStatic,
            node.isFinal,
            false,
            node.access == ShakeAccessDescriber.PUBLIC,
            node.access == ShakeAccessDescriber.PROTECTED,
            node.access == ShakeAccessDescriber.PRIVATE,
            node.value?.let { visitValue(if(node.isStatic) clazz.staticScope else clazz.instanceScope, it)}
        )

    fun visitMethodNode(clazz: ShakeClass.Impl, node: ShakeFunctionDeclarationNode) = ShakeMethod.create(
        clazz,
        if(node.isStatic) clazz.staticScope else clazz.instanceScope,
        node.name,
        node.isStatic,
        node.isFinal,
        false,
        false,
        false,
        node.access == ShakeAccessDescriber.PUBLIC,
        node.access == ShakeAccessDescriber.PROTECTED,
        node.access == ShakeAccessDescriber.PRIVATE,
        project.getType(node.type).notNull("Type not found: ${node.type}"),
        node.args.map { visitParameterNode(it) },
    ) { fn: ShakeMethod.Impl ->
        Pointer.task {
            ShakeCode.create(fn.scope) { scope -> node.body.children.map { this.visitStatement(scope, it) } }
        }
    }

    fun visitConstructorNode(clazz: ShakeClass.Impl, node: ShakeConstructorDeclarationNode) = ShakeConstructor.create(
        clazz,
        clazz.instanceScope,
        node.name,
        false,
        node.access == ShakeAccessDescriber.PRIVATE,
        node.access == ShakeAccessDescriber.PROTECTED,
        node.access == ShakeAccessDescriber.PUBLIC,
        node.args.map { visitParameterNode(it) },
    ) { fn: ShakeConstructor.Impl ->
        Pointer.task {
            ShakeCode.create(fn.scope) { scope -> node.body.children.map { this.visitStatement(scope, it) } }
        }
    }

    fun visitClassNode(file: ShakeFile.Impl, node: ShakeClassDeclarationNode) : ShakeClass.Impl
        = ShakeClass.create(
            file.scope,
            node.name,
            { clz ->
                node.methods.filter { !it.isStatic }.map { visitMethodNode(clz, it) }
            },
            { clz ->
                node.fields.filter { !it.isStatic }.map { visitClassFieldNode(clz, it) }
            },
            { clz ->
                node.classes.filter { !it.isStatic }.map { visitClassNode(file, it) } // TODO Subclasses
            },
            { clz ->
                node.methods.filter { it.isStatic }.map { visitMethodNode(clz, it) }
            },
            { clz ->
                node.fields.filter { it.isStatic }.map { visitClassFieldNode(clz, it) }
            },
            { clz ->
                node.classes.filter { it.isStatic }.map { visitClassNode(file, it) } // TODO Subclasses
            },
            { clz ->
                node.constructors.map { visitConstructorNode(clz, it) }
            },
            null.point(),
            listOf(),
        )



    fun visitFieldNode(file: ShakeFile.Impl, node: ShakeVariableDeclarationNode)
        = ShakeField.create(
            project,
            file.pkg,
            node.name,
            project.getType(node.type).notNull("Type not found: ${node.type}"),
            file.scope,
            node.isStatic,
            node.isFinal,
            false,
            node.access == ShakeAccessDescriber.PRIVATE,
            node.access == ShakeAccessDescriber.PROTECTED,
            node.access == ShakeAccessDescriber.PUBLIC,
            node.value ?.let { this.visitValue(file.pkg?.scope ?: project.scope, it) }
        )

    fun visitFunctionNode(file: ShakeFile.Impl, node: ShakeFunctionDeclarationNode): ShakeFunction.Impl {
        return ShakeFunction.create(
            project,
            file.pkg,
            file.scope,
            node.name,
            node.isStatic,
            node.isFinal,
            false, // TODO...
            false,
            false,
            node.access == ShakeAccessDescriber.PRIVATE,
            node.access == ShakeAccessDescriber.PROTECTED,
            node.access == ShakeAccessDescriber.PUBLIC,
            project.getType(node.type).notNull("Type not found: ${node.type}"),
            node.args.map { visitParameterNode(it) }
        ) { fn: ShakeFunction.Impl ->
            Pointer.task {
                ShakeCode.create(fn.scope) { scope -> node.body.children.map { this.visitStatement(scope, it) } }
            }
        }
    }

    fun visitParameterNode(node: ShakeFunctionArgumentNode): ShakeParameter.Impl {
        return ShakeParameter.create(
            node.name,
            project.getType(node.type).notNull("Type not found: ${node.type}"),
        )
    }

    fun visitValue(scope: ShakeScope.ShakeScopeImpl, value: ShakeValuedNode): ShakeValue {
        return when(value) {
            is ShakeIntegerNode -> visitIntegerNode(scope, value)
            is ShakeDoubleNode -> visitDoubleNode(scope, value)
            is ShakeLogicalTrueNode -> visitLogicalTrueNode(scope, value)
            is ShakeLogicalFalseNode -> visitLogicalFalseNode(scope, value)
            is ShakeLogicalAndNode -> visitLogicalAndNode(scope, value)
            is ShakeLogicalOrNode -> visitLogicalOrNode(scope, value)
            is ShakeLogicalXOrNode -> visitLogicalXOrNode(scope, value)
            is ShakeLogicalEqEqualsNode -> visitEqEqualsNode(scope, value)
            is ShakeLogicalBiggerEqualsNode -> visitBiggerEqualsNode(scope, value)
            is ShakeLogicalSmallerEqualsNode -> visitSmallerEqualsNode(scope, value)
            is ShakeLogicalBiggerNode -> visitBiggerNode(scope, value)
            is ShakeLogicalSmallerNode -> visitSmallerNode(scope, value)
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
            is ShakeFunctionCallNode -> visitFunctionCallNode(scope, value)
            is ShakeClassConstructionNode -> visitClassConstruction(scope, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value::class}")
        }
    }

    fun visitStatement(scope: ShakeScope.ShakeCodeScope.Impl, statement: ShakeStatementNode): ShakeStatement {
        return when(statement) {
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
            is ShakeFunctionCallNode -> visitFunctionCallNode(scope, statement)
            is ShakeClassConstructionNode -> visitClassConstruction(scope, statement)
            else -> throw IllegalArgumentException("Unsupported statement type: ${statement::class}")
        }
    }

    fun visitTree(scope: ShakeScope.ShakeScopeImpl, t: ShakeTree): ShakeCode {
        return ShakeCode.create(scope) { sc ->
            t.children.map { visitStatement(sc, it) }
        }
    }

    fun visitType(scope: ShakeScope.ShakeCodeScope.Impl, t: ShakeVariableType): ShakeType? {
        return when(t.type) {
            ShakeVariableType.Type.DYNAMIC -> null
            ShakeVariableType.Type.BYTE -> ShakeType.Primitive.BYTE
            ShakeVariableType.Type.SHORT -> ShakeType.Primitive.SHORT
            ShakeVariableType.Type.INTEGER -> ShakeType.Primitive.INT
            ShakeVariableType.Type.LONG -> ShakeType.Primitive.LONG
            ShakeVariableType.Type.FLOAT -> ShakeType.Primitive.FLOAT
            ShakeVariableType.Type.DOUBLE -> ShakeType.Primitive.DOUBLE
            ShakeVariableType.Type.BOOLEAN -> ShakeType.Primitive.BOOLEAN
            else -> null
        }
    }

    fun visitDoubleNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeDoubleNode): ShakeDoubleLiteral {
        return ShakeDoubleLiteral.create(scope, n.number)
    }

    fun visitIntegerNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeIntegerNode): ShakeIntLiteral {
        return ShakeIntLiteral.create(scope, n.number)
    }

    fun visitAddNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeAddNode): ShakeAddition {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val type = left.type.additionType(right.type) ?: throw Exception("Cannot add ${left.type} and ${right.type}")
        return ShakeAddition.create(scope, left, right, type)
    }

    fun visitSubNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeSubNode): ShakeSubtraction {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val type = left.type.subtractionType(right.type) ?: throw Exception("Cannot subtract ${left.type} and ${right.type}")
        return ShakeSubtraction.create(scope, left, right, type)
    }

    fun visitMulNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeMulNode): ShakeMultiplication {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val type = left.type.multiplicationType(right.type) ?: throw Exception("Cannot multiply ${left.type} and ${right.type}")
        return ShakeMultiplication.create(scope, left, right, type)
    }

    fun visitDivNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeDivNode): ShakeDivision {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val type = left.type.divisionType(right.type) ?: throw Exception("Cannot divide ${left.type} and ${right.type}")
        return ShakeDivision.create(scope, left, right, type)
    }

    fun visitModNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeModNode): ShakeModulus {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val type = left.type.modulusType(right.type) ?: throw Exception("Cannot modulus ${left.type} and ${right.type}")
        return ShakeModulus.create(scope, left, right, type)
    }

    fun visitPowNode(scope: ShakeScope.ShakeScopeImpl, n: ShakePowNode): ShakePower {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        val type = left.type.powerType(right.type) ?: throw Exception("Cannot power ${left.type} and ${right.type}")
        return ShakePower.create(scope, left, right, type)
    }

    fun visitVariableDeclarationNode(scope: ShakeScope.ShakeCodeScope.Impl, n: ShakeVariableDeclarationNode): ShakeVariableDeclaration {
        val value = if(n.value != null) visitValue(scope, n.value!!) else null
        val type = visitType(scope, n.type) ?: value?.type ?: throw Exception("Cannot infer type of variable ${n.name}")
        val decl = ShakeVariableDeclaration.create(scope, n.name, type, value, n.isFinal)
        scope.addVariable(decl)
        return decl
    }

    fun getAssignable(scope: ShakeScope.ShakeScopeImpl, n: ShakeValuedNode): ShakeAssignable? {
        return if(n !is ShakeIdentifierNode) {
            //visitValue(scope, n)#
            TODO()
        }
        else if(n.parent != null) {
            val parent = visitValue(scope, n.parent!!)
            ShakeChild.create(scope, parent, n.name, parent.type.childType(n.name) ?: throw Exception("Cannot find child ${n.name} in ${parent.type}"))
        } else scope.get(n.name)
    }

    fun visitVariableAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableAssignmentNode): ShakeAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.assignType(value.type) ?: variable.type
        return ShakeAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariableAddAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableAddAssignmentNode): ShakeAddAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.additionAssignType(value.type) ?: variable.type
        return ShakeAddAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariableSubAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableSubAssignmentNode): ShakeSubAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.subtractionAssignType(value.type) ?: variable.type
        return ShakeSubAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariableMulAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableMulAssignmentNode): ShakeMulAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.multiplicationAssignType(value.type) ?: variable.type
        return ShakeMulAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariableDivAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableDivAssignmentNode): ShakeDivAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.divisionAssignType(value.type) ?: variable.type
        return ShakeDivAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariableModAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableModAssignmentNode): ShakeModAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.modulusAssignType(value.type) ?: variable.type
        return ShakeModAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariablePowAssignmentNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariablePowAssignmentNode): ShakePowAssignment {
        val value = visitValue(scope, n.value)
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        if(variable.type != value.type) throw Exception("Cannot assign ${value.type} to ${variable.type}")
        val assignType = variable.powerAssignType(value.type) ?: variable.type
        return ShakePowAssignment.create(scope, variable, value, assignType)
    }

    fun visitVariableIncrementNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableIncreaseNode): ShakeIncrementAfter {
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        val assignType = variable.incrementAfterType() ?: variable.type
        return ShakeIncrementAfter.create(scope, variable, assignType)
    }

    fun visitVariableDecrementNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableDecreaseNode): ShakeDecrementAfter {
        val variable = getAssignable(scope, n.variable) ?: throw Exception("Cannot assign to ${n.variable}")
        val assignType = variable.decrementAfterType() ?: variable.type
        return ShakeDecrementAfter.create(scope, variable, assignType)
    }

    fun visitVariableUsageNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeVariableUsageNode): ShakeValue {
        val identifier = n.variable
        if(identifier.parent != null) {
            val parent = visitValue(scope, identifier.parent!!)
            val type = parent.type.childType(identifier.name)
                ?: throw Exception("Cannot access ${identifier.name} in ${parent.type}")
            return ShakeChild.create(scope, parent, identifier.name, type).access(scope)
        }
        val variable = scope.get(identifier.name) ?: throw Exception("Variable ${identifier.name} not declared")
        return variable.access(scope) // TODO null value
    }

    fun visitEqEqualsNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalEqEqualsNode): ShakeEquals {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeEquals.create(scope, left, right, left.type.equalsType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitBiggerEqualsNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalBiggerEqualsNode): ShakeGreaterThanOrEqual {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeGreaterThanOrEqual.create(scope, left, right, left.type.greaterThanOrEqualType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitSmallerEqualsNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalSmallerEqualsNode): ShakeLessThanOrEqual {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeLessThanOrEqual.create(scope, left, right, left.type.lessThanOrEqualType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitBiggerNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalBiggerNode): ShakeGreaterThan {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeGreaterThan.create(scope, left, right, left.type.equalsType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitSmallerNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalSmallerNode): ShakeLessThan {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeLessThan.create(scope, left, right, left.type.equalsType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitLogicalAndNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalAndNode): ShakeAnd {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeAnd.create(scope, left, right, left.type.equalsType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitLogicalOrNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalOrNode): ShakeOr {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeOr.create(scope, left, right, left.type.equalsType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitLogicalXOrNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalXOrNode): ShakeXor {
        val left = visitValue(scope, n.left)
        val right = visitValue(scope, n.right)
        return ShakeXor.create(scope, left, right, left.type.equalsType(right.type)
            ?: throw Exception("Cannot compare ${left.type} to ${right.type}"))
    }

    fun visitBoolean(scope: ShakeScope.ShakeScopeImpl, n: ShakeValuedNode): ShakeValue {
        val value = visitValue(scope, n)
        return if(value.type == ShakeType.Primitive.BOOLEAN) value
            else throw Exception("Cannot convert ${value.type} to boolean")
    }

    fun visitWhileNode(scope: ShakeScope.ShakeCodeScope.Impl, n: ShakeWhileNode): ShakeWhile {
        val condition = visitBoolean(scope, n.condition)
        val body = visitTree(scope, n.body)
        return ShakeWhile.create(scope, condition, body)
    }

    fun visitDoWhileNode(scope: ShakeScope.ShakeCodeScope.Impl, n: ShakeDoWhileNode): ShakeDoWhile {
        val condition = visitBoolean(scope, n.condition)
        val body = visitTree(scope, n.body)
        return ShakeDoWhile.create(scope, condition, body)
    }

    fun visitForNode(scope: ShakeScope.ShakeCodeScope.Impl, n: ShakeForNode): ShakeFor {
        val init = visitStatement(scope, n.declaration)
        val condition = visitBoolean(scope, n.condition)
        val update = visitStatement(scope, n.round)
        val body = visitTree(scope, n.body)
        return ShakeFor.create(scope, init, condition, update, body)
    }

    fun visitIfNode(scope: ShakeScope.ShakeCodeScope.Impl, n: ShakeIfNode): ShakeIf {
        val condition = visitBoolean(scope, n.condition)
        val body = visitTree(scope, n.body)
        if(n.elseBody != null) {
            val elseBody = visitTree(scope, n.elseBody!!)
            return ShakeIf.create(scope, condition, body, elseBody)
        }
        return ShakeIf.create(scope, condition, body)
    }

    fun visitClassConstruction(scope: ShakeScope.ShakeScopeImpl, n: ShakeClassConstructionNode): ShakeNew {
        val classNode = n.type
        if(classNode is ShakeIdentifierNode) {
            if(classNode.parent != null) {
                TODO("Construction of inner classes not implemented")
            }
            val className = classNode.name
            val clz = scope.getClass(className)?: throw Exception("Class $className not found")
            val args = n.args.map { visitValue(scope, it) }
            val types = args.map { it.type }
            val constructors = clz.constructors
            if(constructors.isEmpty()) throw Exception("No constructor found for class $className")
            val constructor = ShakeSelect.selectConstructor(constructors, types) ?:
                throw Exception("No constructor found for class $className with arguments $types")
            return ShakeNew.create(scope, constructor, args, constructor.clazz.asType())
        }
        TODO("Returned constructor. Will this ever be possible?")
    }

    fun visitFunctionCallNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeFunctionCallNode): ShakeInvocation {
        val functionNode = n.function
        if(functionNode is ShakeIdentifierNode) {
            if(functionNode.parent != null) {
                val parent = visitValue(scope, functionNode.parent!!)
                val name = functionNode.name
                val args = n.args.map { visitValue(scope, it) }
                val types = args.map { it.type }
                val functions = (parent.type.childFunctions(name) ?: throw Exception("No function named $name in ${parent.type}")) as List<ShakeFunction> // TODO
                val function = ShakeSelect.selectFunction(functions, types)
                    ?: throw Exception("No function named $name with arguments $types in ${parent.type}")
                return ShakeInvocation.create(scope, function, args, parent, function.returnType)
            }

            val name = functionNode.name
            val args = n.args.map { visitValue(scope, it) }
            val types = args.map { it.type }
            val functions = scope.getFunctions(name)
            if(functions.isEmpty()) throw Exception("No function named $name")
            val function = ShakeSelect.selectFunction(functions, types)
                ?: throw Exception("No function named $name with arguments $types")
            return ShakeInvocation.create(scope, function, args, null, function.returnType)
        }
        TODO("Direct returned lambda functions")
    }

    fun visitLogicalTrueNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalTrueNode): ShakeValue {
        return ShakeBooleanLiteral.True(scope)
    }

    fun visitLogicalFalseNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeLogicalFalseNode): ShakeValue {
        return ShakeBooleanLiteral.False(scope)
    }

    fun visitCastNode(scope: ShakeScope.ShakeScopeImpl, n: ShakeCastNode): ShakeCast {
        val value = visitValue(scope, n.value)
        val target = when(n.castTarget.type) {
            ShakeCastNode.CastTarget.CastTargetType.CHAR -> ShakeType.Primitive.CHAR
            ShakeCastNode.CastTarget.CastTargetType.BYTE -> ShakeType.Primitive.BYTE
            ShakeCastNode.CastTarget.CastTargetType.SHORT -> ShakeType.Primitive.SHORT
            ShakeCastNode.CastTarget.CastTargetType.INT -> ShakeType.Primitive.INT
            ShakeCastNode.CastTarget.CastTargetType.LONG -> ShakeType.Primitive.LONG
            ShakeCastNode.CastTarget.CastTargetType.FLOAT -> ShakeType.Primitive.FLOAT
            ShakeCastNode.CastTarget.CastTargetType.DOUBLE -> ShakeType.Primitive.DOUBLE
            ShakeCastNode.CastTarget.CastTargetType.BOOLEAN -> ShakeType.Primitive.BOOLEAN
            ShakeCastNode.CastTarget.CastTargetType.OBJECT -> {
                val st = n.castTarget.subtype ?: throw Exception("No subtype for object cast")
                if(st.parent != null) TODO()
                val type = scope.getClass(st.name) ?: throw Exception("No class named ${st.name} for object cast")
                type.asType()
            }
            else -> throw Exception("Unsupported cast target type ${n.castTarget.type}")
        }
        return ShakeCast.create(scope, value, target)
    }

    fun visitReturnNode(scope: ShakeScope.ShakeCodeScope.Impl, n: ShakeReturnNode): ShakeReturn {
        val value = visitValue(scope, n.value)
        return ShakeReturn.create(scope, value)
    }

    fun finish() = project
}
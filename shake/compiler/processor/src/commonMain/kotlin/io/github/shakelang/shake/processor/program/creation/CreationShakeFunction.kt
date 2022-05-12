package io.github.shakelang.shake.processor.program.creation

import io.github.shakelang.shake.parser.node.ShakeAccessDescriber
import io.github.shakelang.shake.parser.node.functions.ShakeFunctionDeclarationNode
import io.github.shakelang.shake.processor.ShakeCodeProcessor
import io.github.shakelang.shake.processor.program.creation.code.CreationShakeCode
import io.github.shakelang.shake.processor.program.creation.code.CreationShakeInvokable
import io.github.shakelang.shake.processor.program.creation.code.statements.CreationShakeVariableDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeFunction
import io.github.shakelang.shake.processor.program.types.ShakeType

open class CreationShakeFunction (
    override val prj: CreationShakeProject,
    override val pkg: CreationShakePackage?,
    override val parentScope: CreationShakeScope,
    override val name: String,
    body: CreationShakeCode,
    override val isStatic: Boolean,
    override val isFinal: Boolean,
    override val isAbstract: Boolean,
    override val isSynchronized: Boolean,
    override val isStrict: Boolean,
    override val isPrivate: Boolean,
    override val isProtected: Boolean,
    override val isPublic: Boolean,
): CreationShakeInvokable(body), ShakeFunction {

    override val qualifiedName: String
        get() = (pkg?.qualifiedName?.plus(".") ?: "") + name

    final override lateinit var returnType: ShakeType
        private set

    constructor(
        prj: CreationShakeProject,
        pkg: CreationShakePackage?,
        parentScope: CreationShakeScope,
        name: String,
        parameters: List<CreationShakeParameter>,
        returnType: ShakeType,
        body: CreationShakeCode,
        isStatic: Boolean,
        isFinal: Boolean,
        isAbstract: Boolean,
        isSynchronized: Boolean,
        isStrict: Boolean,
        isPrivate: Boolean,
        isProtected: Boolean,
        isPublic: Boolean
    ): this(prj, pkg, parentScope, name, body, isStatic, isFinal, isAbstract, isSynchronized, isStrict, isPrivate, isProtected, isPublic) {
        this.parameters = parameters
        this.returnType = returnType
    }

    override val scope : CreationShakeScope = CreationShakeScope.CreationShakeFunctionScope.from(this)
    override val signature: String
        get() = "${pkg?.name ?: ""}#$name(${parameters.joinToString(", ") { it.type.signature }})${returnType.signature}"

    fun lateinitReturnType(): (ShakeType) -> ShakeType {
        return {
            returnType = it
            it
        }
    }

    fun lateinitParameterTypes(names: List<String>): List<(ShakeType) -> ShakeType> {
        this.parameters = names.map {
            CreationShakeParameter(it)
        }
        return this.parameters.map {
            it.lateinitType()
        }
    }

    open fun processCode() {
        if(body is CreationShakeCode.ShakeLateProcessCode) (body as CreationShakeCode.ShakeLateProcessCode).process(scope)
    }

    override fun toJson(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "isStatic" to isStatic,
            "isFinal" to isFinal,
            "isAbstract" to isAbstract,
            "isSynchronized" to isSynchronized,
            "isStrict" to isStrict,
            "isPrivate" to isPrivate,
            "isProtected" to isProtected,
            "isPublic" to isPublic,
            "returnType" to returnType.toJson(),
            "parameters" to parameters.map { it.toJson() },
            "body" to body.toJson()
        )
    }

    companion object {
        fun from(baseProject: CreationShakeProject, pkg: CreationShakePackage?, parentScope: CreationShakeScope, node: ShakeFunctionDeclarationNode): CreationShakeFunction {
            return CreationShakeFunction(
                baseProject,
                pkg,
                parentScope,
                node.name,
                CreationShakeCode.fromTree(node.body),
                node.isStatic,
                node.isFinal,
                false,
                false,
                false,
                node.access == ShakeAccessDescriber.PRIVATE,
                node.access == ShakeAccessDescriber.PROTECTED,
                node.access == ShakeAccessDescriber.PUBLIC
            ).let {
                it.lateinitReturnType().let { run -> baseProject.getType(node.type) { t -> run(t) } }
                it.lateinitParameterTypes(node.args.map { p -> p.name })
                    .forEachIndexed { i, run -> baseProject.getType(node.args[i].type) { t -> run(t) } }
                it
            }
        }
    }
}
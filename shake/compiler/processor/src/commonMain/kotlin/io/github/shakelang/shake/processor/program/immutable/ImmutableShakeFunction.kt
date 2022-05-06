package io.github.shakelang.shake.processor.program.immutable

import io.github.shakelang.shake.processor.program.types.ShakeAssignable
import io.github.shakelang.shake.processor.program.types.ShakeFunction
import io.github.shakelang.shake.processor.program.types.ShakeParameter
import io.github.shakelang.shake.processor.program.types.ShakeType
import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration

open class ImmutableShakeFunction (
    override val prj: ImmutableShakeProject,
    override val pkg: ImmutableShakePackage?,
    override val parentScope: ImmutableShakeScope,
    override val name: String,
    body: ShakeCode,
    override val isStatic: Boolean,
    override val isFinal: Boolean,
    override val isAbstract: Boolean,
    override val isSynchronized: Boolean,
    override val isStrict: Boolean,
    override val isPrivate: Boolean,
    override val isProtected: Boolean,
    override val isPublic: Boolean,
): ImmutableShakeInvokable(body), ShakeFunction {

    override val qualifiedName: String
        get() = (pkg?.qualifiedName?.plus(".") ?: "") + name

    final override lateinit var returnType: ShakeType
        private set

    constructor(
        prj: ImmutableShakeProject,
        pkg: ImmutableShakePackage?,
        parentScope: ImmutableShakeScope,
        name: String,
        parameters: List<ShakeParameter>,
        returnType: ShakeType,
        body: ShakeCode,
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

    override val scope : ImmutableShakeScope = ShakeFunctionScope()

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

    inner class ShakeFunctionScope: ImmutableShakeScope {

        val variables = mutableListOf<ShakeVariableDeclaration>()

        override val parent: ImmutableShakeScope = parentScope

        override fun get(name: String): ShakeAssignable? {
            return variables.find { it.name == name } ?: parent.get(name)
        }

        override fun getFunctions(name: String): List<ImmutableShakeFunction> {
            return parent.getFunctions(name)
        }

        override fun getClass(name: String): ImmutableShakeClass? {
            return parent.getClass(name)
        }

    }
}
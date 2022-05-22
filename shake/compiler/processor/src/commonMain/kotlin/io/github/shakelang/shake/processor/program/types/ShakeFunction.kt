package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

/**
 * Represents a function in the Shake language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeFunction : ShakeInvokable {

    /**
     * The name of this function.
     */
    val name: String

    /**
     * The [ShakeProject] that this function belongs to.
     */
    val project: ShakeProject

    /**
     * The [ShakePackage] that this function belongs to (if any).
     */
    val pkg: ShakePackage?

    /**
     * The parent [ShakeScope] of this function (normally the scope of it's [ShakeFile])
     */
    val parentScope: ShakeScope

    /**
     * Is this function static?
     */
    val isStatic: Boolean

    /**
     * Is this function final?
     */
    val isFinal: Boolean

    /**
     * Is this function abstract?
     */
    val isAbstract: Boolean

    /**
     * Is this function synchronized?
     */
    val isSynchronized: Boolean

    /**
     * Is this function strict?
     */
    val isStrict: Boolean

    /**
     * Is this function private?
     */
    val isPrivate: Boolean

    /**
     * Is this function protected?
     */
    val isProtected: Boolean

    /**
     * Is this function public?
     */
    val isPublic: Boolean


    /**
     * The qualified name of this function.
     */
    override val qualifiedName: String

    /**
     * The scope of this function.
     */
    val scope : ShakeScope.ShakeFunctionScope

    /**
     * The signature of this function.
     */
    val signature: String

    override fun toJson(): Map<String, Any?>

    class Impl : ShakeFunction {
        override val project: ShakeProject
        override val pkg: ShakePackage?
        override val parentScope: ShakeScope
        override val name: String
        override val isStatic: Boolean
        override val isFinal: Boolean
        override val isAbstract: Boolean
        override val isSynchronized: Boolean
        override val isStrict: Boolean
        override val isPrivate: Boolean
        override val isProtected: Boolean
        override val isPublic: Boolean

        override val returnTypePointer: Pointer<ShakeType>
        override val returnType: ShakeType get () = returnTypePointer.value

        override val parameters: List<ShakeParameter>
        override val body: ShakeCode
        override val signature: String

        constructor(
            prj: ShakeProject,
            pkg: ShakePackage?,
            parentScope: ShakeScope,
            name: String,
            isStatic: Boolean,
            isFinal: Boolean,
            isAbstract: Boolean,
            isSynchronized: Boolean,
            isStrict: Boolean,
            isPrivate: Boolean,
            isProtected: Boolean,
            isPublic: Boolean,
            returnType: ShakeType,
            parameters: List<ShakeParameter>,
            body: ShakeCode
        ) {
            this.project = prj
            this.pkg = pkg
            this.parentScope = parentScope
            this.name = name
            this.isStatic = isStatic
            this.isFinal = isFinal
            this.isAbstract = isAbstract
            this.isSynchronized = isSynchronized
            this.isStrict = isStrict
            this.isPrivate = isPrivate
            this.isProtected = isProtected
            this.isPublic = isPublic
            this.returnTypePointer = returnType.point()
            this.parameters = parameters
            this.body = body
            this.signature =  "${pkg?.qualifiedName ?: ""}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"
        }

        internal constructor(
            prj: ShakeProject,
            pkg: ShakePackage?,
            parentScope: ShakeScope,
            it: ShakeFunction
        ) {
            this.project = prj
            this.pkg = pkg
            this.parentScope = parentScope
            this.name = it.name
            this.isStatic = it.isStatic
            this.isFinal = it.isFinal
            this.isAbstract = it.isAbstract
            this.isSynchronized = it.isSynchronized
            this.isStrict = it.isStrict
            this.isPrivate = it.isPrivate
            this.isProtected = it.isProtected
            this.isPublic = it.isPublic
            this.returnTypePointer = ShakeType.from(prj, it.returnType)
            this.parameters = it.parameters // TODO: copy parameters
            this.body = it.body // TODO: copy body
            this.signature =  "${pkg?.qualifiedName ?: ""}#$name(${parameters.joinToString(",") { it.type.signature }})${returnType.signature}"
        }

        override val qualifiedName: String get() = "${pkg?.qualifiedName?.plus(".") ?: ""}$name"
        override val scope: ShakeScope.ShakeFunctionScope = ShakeScope.ShakeFunctionScope.from(this)

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
                "returnType" to returnType.toJson()
            )
        }
    }

    companion object {
        fun from(project: ShakeProject, pkg: ShakePackage?, it: ShakeFunction): ShakeFunction = Impl(project, pkg, it.parentScope, it)
    }

}
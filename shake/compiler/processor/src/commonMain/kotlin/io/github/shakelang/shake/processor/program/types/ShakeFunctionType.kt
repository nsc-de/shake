package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable

/**
 * Represents a function or method in the Shake language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeFunctionType : ShakeInvokable {

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
    val scope : ShakeScope.ShakeFunctionTypeScope

    /**
     * The signature of this function.
     */
    val signature: String

    override fun toJson(): Map<String, Any?>

}
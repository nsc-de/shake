package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.point

/**
 * Represents a field in the Shake programming language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeFieldType : ShakeDeclaration, ShakeAssignable {

    /**
     * The project the field belongs to.
     */
    val project: ShakeProject

    /**
     * The package the field belongs to (if any).
     */
    val pkg: ShakePackage?

    /**
     * The scope the field uses for initialization.
     */
    val scope: ShakeScope

    /**
     * Is the field a static field?
     */
    val isStatic: Boolean

    /**
     * Is the field a final field?
     */
    val isFinal: Boolean

    /**
     * Is the field abstract?
     */
    val isAbstract: Boolean

    /**
     * Is the field private?
     */
    val isPrivate: Boolean

    /**
     * Is the field protected?
     */
    val isProtected: Boolean

    /**
     * Is the field public?
     */
    val isPublic: Boolean

    /**
     * The initial value of the field (if any).
     */
    val initialValue: ShakeValue?


    /**
     * The signature of the field.
     */
    val signature: String
}
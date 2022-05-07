package io.github.shakelang.shake.processor.program.immutable

import io.github.shakelang.shake.processor.program.types.ShakeClassField
import io.github.shakelang.shake.processor.program.types.code.values.ShakeValue

open class ImmutableShakeClassField (
    override val clazz: ImmutableShakeClass,
    parentScope: ImmutableShakeScope,
    name: String,
    isStatic: Boolean,
    isFinal: Boolean,
    isAbstract: Boolean,
    isPrivate: Boolean,
    isProtected: Boolean,
    isPublic: Boolean,
    initialValue: ShakeValue? = null
): ImmutableShakeField(
    clazz.prj,
    clazz.pkg,
    parentScope,
    name,
    isStatic,
    isFinal,
    isAbstract,
    isPrivate,
    isProtected,
    isPublic,
    initialValue
), ShakeClassField {
    override val qualifiedName: String
        get() = "${clazz.qualifiedName}.$name"
}
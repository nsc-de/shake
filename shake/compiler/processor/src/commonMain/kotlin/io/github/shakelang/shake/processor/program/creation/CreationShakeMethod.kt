package io.github.shakelang.shake.processor.program.creation

import io.github.shakelang.shake.processor.ShakeCodeProcessor
import io.github.shakelang.shake.processor.program.creation.code.CreationShakeCode
import io.github.shakelang.shake.processor.program.creation.code.statements.CreationShakeVariableDeclaration
import io.github.shakelang.shake.processor.program.types.ShakeMethod

class CreationShakeMethod (
    override val clazz: CreationShakeClass,
    parentScope: CreationShakeScope,
    name: String,
    body: CreationShakeCode,
    isStatic: Boolean,
    isFinal: Boolean,
    isAbstract: Boolean,
    isSynchronized: Boolean,
    isStrict: Boolean,
    isPrivate: Boolean,
    isProtected: Boolean,
    isPublic: Boolean,
) : CreationShakeFunction(
    clazz.prj,
    clazz.pkg,
    parentScope,
    name,
    body,
    isStatic,
    isFinal,
    isAbstract,
    isSynchronized,
    isStrict,
    isPrivate,
    isProtected,
    isPublic
), ShakeMethod {

    override val scope = CreationShakeScope.CreationShakeMethodScope.from(this)

    override fun processCode() {
        if(body is CreationShakeCode.ShakeLateProcessCode) body.process(scope)
    }

    override fun toJson(): Map<String, Any?> {
        return super.toJson() + mapOf(
            "type" to "method",
            "class" to clazz.name,
            "body" to body.toJson()
        )
    }
}
package com.shakelang.shake.processor.program.creation

import com.shakelang.shake.parser.node.ShakeVariableType
import com.shakelang.shake.processor.ShakeASTProcessor
import com.shakelang.shake.processor.program.creation.code.CreationShakeInvokable
import com.shakelang.shake.processor.program.types.code.ShakeScope

abstract class CreationShakeScope : ShakeScope {
    val classRequirements = mutableListOf<CreationShakeProject.ClassRequirement>()
    private var initialized: Boolean = false

    abstract val project: CreationShakeProject

    abstract override val parent: CreationShakeScope?
    abstract override fun getField(name: String): CreationShakeAssignable?
    abstract override fun getFields(name: String): List<CreationShakeAssignable>
    abstract fun setField(value: CreationShakeDeclaration)
    abstract override fun getFunctions(name: String): List<CreationShakeMethod>
    abstract fun setFunctions(function: CreationShakeMethod)
    abstract override fun getClass(name: String): CreationShakeClass?
    abstract override fun getClasses(name: String): List<CreationShakeClass>
    abstract fun setClass(klass: CreationShakeClass)
    override fun getInvokable(name: String): List<CreationShakeInvokable> {
        val functions = getFunctions(name)
        val variable = getField(name)
        if (variable != null && variable is CreationShakeInvokable) {
            return listOf(variable, *functions.toTypedArray())
        }
        return functions
    }

    override fun use(name: String) {
        val declaration = getField(name)
    }

    fun getClass(name: String, then: (CreationShakeClass) -> Unit) {
        this.init()
        this.classRequirements.add(CreationShakeProject.ClassRequirement(name, then))
    }

    fun getType(clzName: String): CreationShakeType {
        return CreationShakeType.objectType(this.getClass(clzName) ?: throw IllegalArgumentException("Class $clzName not found"))
    }

    fun getType(type: ShakeVariableType): CreationShakeType {
        return when (type.type) {
            ShakeVariableType.Type.OBJECT -> {
                val namespace = (type as ShakeVariableType.Object).namespace
                    ?: throw IllegalArgumentException("Object type must have subtype")
                val clzName = namespace.parts.joinToString(".")
                this.getType(clzName)
            }

            ShakeVariableType.Type.ARRAY -> {
                val subtype = (type as ShakeVariableType.Array).subtype
                this.getType(subtype)
            }

            ShakeVariableType.Type.BYTE -> CreationShakeType.Primitives.BYTE
            ShakeVariableType.Type.SHORT -> CreationShakeType.Primitives.SHORT
            ShakeVariableType.Type.INTEGER -> CreationShakeType.Primitives.INT
            ShakeVariableType.Type.LONG -> CreationShakeType.Primitives.LONG
            ShakeVariableType.Type.FLOAT -> CreationShakeType.Primitives.FLOAT
            ShakeVariableType.Type.DOUBLE -> CreationShakeType.Primitives.DOUBLE
            ShakeVariableType.Type.UNSIGNED_BYTE -> CreationShakeType.Primitives.UBYTE
            ShakeVariableType.Type.UNSIGNED_SHORT -> CreationShakeType.Primitives.USHORT
            ShakeVariableType.Type.UNSIGNED_INTEGER -> CreationShakeType.Primitives.UINT
            ShakeVariableType.Type.UNSIGNED_LONG -> CreationShakeType.Primitives.ULONG
            ShakeVariableType.Type.BOOLEAN -> CreationShakeType.Primitives.BOOLEAN
            ShakeVariableType.Type.CHAR -> CreationShakeType.Primitives.CHAR
            ShakeVariableType.Type.DYNAMIC -> CreationShakeType.Primitives.DYNAMIC
            ShakeVariableType.Type.VOID -> CreationShakeType.Primitives.VOID
            ShakeVariableType.Type.UNKNOWN -> CreationShakeType.Primitives.DYNAMIC // TODO: Change this
        }
    }

    open fun init(): CreationShakeScope {
        if (this.initialized) return this
        project.addScope(this)
        this.initialized = true
        return this
    }

    abstract val processor: ShakeASTProcessor
}

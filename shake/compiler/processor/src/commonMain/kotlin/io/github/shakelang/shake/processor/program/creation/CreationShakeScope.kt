package io.github.shakelang.shake.processor.program.creation

import io.github.shakelang.shake.processor.ShakeCodeProcessor
import io.github.shakelang.shake.processor.program.creation.code.CreationShakeInvokable
import io.github.shakelang.shake.processor.program.creation.code.statements.CreationShakeVariableDeclaration
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope

interface CreationShakeScope : ShakeScope {
    override val parent: CreationShakeScope?
    override fun get(name: String): CreationShakeAssignable?
    fun set(value: CreationShakeDeclaration)
    override fun getFunctions(name: String): List<CreationShakeFunction>
    fun setFunctions(function: CreationShakeFunction)
    override fun getClass(name: String): CreationShakeClass?
    fun setClass(klass: CreationShakeClass)
    override fun getInvokable(name: String): List<CreationShakeInvokable> {
        val functions = getFunctions(name)
        val variable = get(name)
        if(variable != null && variable is CreationShakeInvokable) {
            return listOf(variable, *functions.toTypedArray())
        }
        return functions
    }
    val processor : ShakeCodeProcessor

    interface CreationShakeClassInstanceScope : CreationShakeScope, ShakeScope.ShakeClassInstanceScope {
        class Impl (val it: CreationShakeClass) : CreationShakeClassInstanceScope {

            override val processor: ShakeCodeProcessor get() = parent.processor
            override val parent: CreationShakeScope get() = it.parentScope

            override fun get(name: String): CreationShakeAssignable? {
                return it.fields.find { it.name == name } ?: it.staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun set(value: CreationShakeDeclaration) {
                throw IllegalStateException("Cannot set in this scope")
            }

            override fun getFunctions(name: String): List<CreationShakeFunction> {
                return it.methods.filter { it.name == name } + it.staticMethods.filter { it.name == name } + parent.getFunctions(name)
            }

            override fun setFunctions(function: CreationShakeFunction) {
                throw IllegalStateException("Cannot set in this scope")
            }

            override fun getClass(name: String): CreationShakeClass? {
                return it.classes.find { it.name == name } ?: parent.getClass(name)
            }

            override fun setClass(klass: CreationShakeClass) {
                throw IllegalStateException("Cannot set in this scope")
            }

            override val signature: String get() = "CI${it.signature}"
        }

        companion object {
            fun from(clazz: CreationShakeClass): CreationShakeScope = Impl(clazz)
        }
    }

    interface CreationShakeClassStaticScope : CreationShakeScope, ShakeScope.ShakeClassStaticScope {
        class Impl(val it: CreationShakeClass) : CreationShakeClassStaticScope {

            override val processor: ShakeCodeProcessor get() = parent.processor
            override val parent: CreationShakeScope get() = it.parentScope

            override fun get(name: String): CreationShakeAssignable? {
                return it.staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun set(value: CreationShakeDeclaration) {
                throw IllegalStateException("Cannot set in this scope")
            }

            override fun getFunctions(name: String): List<CreationShakeFunction> {
                return it.staticMethods.filter { it.name == name } + parent.getFunctions(name)
            }

            override fun setFunctions(function: CreationShakeFunction) {
                throw IllegalStateException("Cannot set in this scope")
            }

            override fun getClass(name: String): CreationShakeClass? {
                return it.staticClasses.find { it.name == name } ?: parent.getClass(name)
            }

            override fun setClass(klass: CreationShakeClass) {
                throw IllegalStateException("Cannot set in this scope")
            }

            override val signature: String get() = "CS${it.signature}"

        }

        companion object {
            fun from(clazz: CreationShakeClass): CreationShakeScope = Impl(clazz)
        }
    }

    interface CreationShakeFunctionScope : CreationShakeScope, ShakeScope.ShakeFunctionScope {
        class Impl(val it: CreationShakeFunction) : CreationShakeFunctionScope {

            val variables = mutableListOf<CreationShakeVariableDeclaration>()

            override val parent: CreationShakeScope = it.parentScope

            override fun get(name: String): CreationShakeAssignable? {
                return variables.find { it.name == name } ?: parent.get(name)
            }

            override fun set(value: CreationShakeDeclaration) {
                if(value !is CreationShakeVariableDeclaration) throw IllegalArgumentException("Only variable declarations can be set in a method scope")
                if(variables.any { it.name == value.name }) throw IllegalArgumentException("Variable ${value.name} already exists in this scope")
                variables.add(value)
            }

            override fun getFunctions(name: String): List<CreationShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun setFunctions(function: CreationShakeFunction) {
                throw IllegalArgumentException("Cannot set a function in a method scope")
            }

            override fun getClass(name: String): CreationShakeClass? {
                return parent.getClass(name)
            }

            override fun setClass(klass: CreationShakeClass) {
                throw IllegalArgumentException("Cannot set a class in a method scope")
            }

            override val processor: ShakeCodeProcessor
                get() = it.prj.projectScope.processor

            override val signature: String
                    = "FU${it.signature}"
        }

        companion object {
            fun from(it: CreationShakeFunction): CreationShakeFunctionScope = Impl(it)
        }
    }

    interface CreationShakeMethodScope : CreationShakeScope, ShakeScope.ShakeMethodScope {
        class Impl(val it: CreationShakeMethod) : CreationShakeMethodScope {
            override val parent: CreationShakeScope = if(it.isStatic) it.clazz.staticScope else it.clazz.instanceScope

            val variables = mutableListOf<CreationShakeVariableDeclaration>()

            override fun get(name: String): CreationShakeAssignable? {
                return variables.find { it.name == name } ?: parent.get(name)
            }

            override fun set(value: CreationShakeDeclaration) {
                if(value !is CreationShakeVariableDeclaration) throw IllegalArgumentException("Only variable declarations can be set in a method scope")
                if(variables.any { it.name == value.name }) throw IllegalArgumentException("Variable ${value.name} already exists in this scope")
                variables.add(value)
            }

            override fun getFunctions(name: String): List<CreationShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun setFunctions(function: CreationShakeFunction) {
                throw IllegalStateException("Cannot set function in method scope")
            }

            override fun getClass(name: String): CreationShakeClass? {
                return parent.getClass(name)
            }

            override fun setClass(klass: CreationShakeClass) {
                throw IllegalStateException("Cannot set class in method scope")
            }

            override val processor: ShakeCodeProcessor
                get() = parent.processor

            override val signature: String
                get() = "CM${it.signature}"
        }

        companion object {
            fun from(it: CreationShakeMethod): CreationShakeMethodScope = Impl(it)
        }
    }

    interface CreationShakeConstructorScope : ShakeScope, ShakeScope.ShakeConstructorScope {
        class Impl(val it: CreationShakeConstructor) : CreationShakeConstructorScope {
            val variables = mutableListOf<CreationShakeVariableDeclaration>()

            override val parent: CreationShakeScope = it.scope

            override fun get(name: String): CreationShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<CreationShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return parent.getInvokable(name)
            }

            override fun getClass(name: String): CreationShakeClass? {
                return parent.getClass(name)
            }

            override val signature: String
                get() = "CM${it.signature}"
        }

        companion object {
            fun from(it: CreationShakeConstructor): CreationShakeConstructorScope = Impl(it)
        }
    }
}
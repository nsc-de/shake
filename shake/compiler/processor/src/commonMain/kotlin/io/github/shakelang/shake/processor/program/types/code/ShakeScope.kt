package io.github.shakelang.shake.processor.program.types.code

import io.github.shakelang.shake.processor.ShakeCodeProcessor
import io.github.shakelang.shake.processor.program.creation.*
import io.github.shakelang.shake.processor.program.types.*
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration

interface ShakeScope {
    val parent: ShakeScope?
    fun get(name: String): ShakeAssignable?
    fun getFunctions(name: String): List<ShakeFunction>
    fun getClass(name: String): ShakeClass?
    fun getInvokable(name: String): List<ShakeInvokable>

    val signature: String

    interface ShakeClassInstanceScope : ShakeScope {
        class Impl (val it: ShakeClass) : ShakeClassInstanceScope {

            override val parent: ShakeScope get() = it.parentScope

            override fun get(name: String): ShakeAssignable? {
                return it.fields.find { it.name == name } ?: it.staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return it.methods.filter { it.name == name } + it.staticMethods.filter { it.name == name } + parent.getFunctions(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return it.classes.find { it.name == name } ?: parent.getClass(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return getFunctions(name) + parent.getInvokable(name)
            }

            override val signature: String get() = "CI${it.signature}"
        }

        companion object {
            fun from(clazz: ShakeClass): ShakeScope = Impl(clazz)
        }
    }

    interface ShakeClassStaticScope : ShakeScope {
        class Impl(val it: ShakeClass) : ShakeScope {

            override val parent: ShakeScope get() = it.parentScope

            override fun get(name: String): ShakeAssignable? {
                return it.staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return it.staticMethods.filter { it.name == name } + parent.getFunctions(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return it.staticClasses.find { it.name == name } ?: parent.getClass(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return getFunctions(name) + parent.getInvokable(name)
            }

            override val signature: String get() = "CS${it.signature}"

        }

        companion object {
            fun from(clazz: ShakeClass): ShakeScope = Impl(clazz)
        }
    }

    interface ShakeFunctionScope : ShakeScope {
        class Impl(val it: ShakeFunction) : ShakeFunctionScope {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            override val parent: ShakeScope get() = it.parentScope

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return parent.getInvokable(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return parent.getClass(name)
            }

            override val signature: String
                    = "FU${it.signature}"
        }

        companion object {
            fun from(it: ShakeFunction): ShakeFunctionScope = Impl(it)
        }
    }

    interface ShakeMethodScope : ShakeScope {
        class Impl(val it: ShakeMethod) : ShakeMethodScope{
            val variables = mutableListOf<ShakeVariableDeclaration>()

            override val parent: ShakeScope = it.parentScope

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return parent.getInvokable(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return parent.getClass(name)
            }

            override val signature: String
                get() = "CM${it.signature}"
        }

        companion object {
            fun from(it: ShakeMethod): ShakeMethodScope = Impl(it)
        }
    }

    interface ShakeConstructorScope : ShakeScope {
        class Impl(val it: ShakeConstructor) : ShakeConstructorScope {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            override val parent: ShakeScope = it.scope

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return parent.getFunctions(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return parent.getInvokable(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return parent.getClass(name)
            }

            override val signature: String
                get() = "CM${it.signature}"
        }

        interface ShakeFileScope : ShakeScope {
            class Impl(val it: ShakeFile) : ShakeFileScope {
                override val parent: CreationShakeScope = scope

                override fun get(name: String): CreationShakeAssignable? {
                    return imports.zip(imported).filter {
                        val last = it.first.import.last()
                        last == name || last == "*"
                    }.firstNotNullOfOrNull {
                        it.second!!.fields.find { f -> f.name == name }
                    } ?: parent.get(name)
                }

                override fun set(value: CreationShakeDeclaration) {
                    parent.set(value)
                }

                override fun getFunctions(name: String): List<CreationShakeFunction> {
                    return imports.zip(imported).filter {
                        val last = it.first.import.last()
                        last == name || last == "*"
                    }.flatMap {
                        it.second!!.functions.filter { f -> f.name == name }
                    } + parent.getFunctions(name)
                }

                override fun setFunctions(function: CreationShakeFunction) {
                    parent.setFunctions(function)
                }

                override fun getClass(name: String): CreationShakeClass? {
                    return imports.zip(imported).filter {
                        val last = it.first.import.last()
                        last == name || last == "*"
                    }.firstNotNullOfOrNull {
                        it.second!!.classes.find { c -> c.name == name }
                    } ?: parent.getClass(name)
                }

                override fun setClass(klass: CreationShakeClass) {
                    parent.setClass(klass)
                }

                override val processor: ShakeCodeProcessor
                    get() = parent.processor

            }

        companion object {
            fun from(it: ShakeConstructor): ShakeConstructorScope = Impl(it)
        }
    }
}
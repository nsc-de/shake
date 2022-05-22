package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
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

            override val parent: ShakeScope get() = it.staticScope

            override fun get(name: String): ShakeAssignable? {
                return it.fields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return it.methods.filter { it.name == name } + parent.getFunctions(name)
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
            fun from(clazz: ShakeClass): ShakeClassInstanceScope = Impl(clazz)
        }
    }

    interface ShakeClassStaticScope : ShakeScope {
        class Impl(val it: ShakeClass) : ShakeClassStaticScope {

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
            fun from(clazz: ShakeClass): ShakeClassStaticScope = Impl(clazz)
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
        class Impl(val it: ShakeMethod) : ShakeMethodScope {
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
    }

    interface ShakeFileScope : ShakeScope {
        class Impl(val it: ShakeFile, override val parent: ShakeScope) : ShakeFileScope {
            override fun get(name: String): ShakeAssignable? {
                return it.imports.firstNotNullOfOrNull { import ->
                    val last = import.it.last()
                    if (last == "*" || last == name) import.targetPackage.fields.find { it.name == name } else null
                } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return it.imports.flatMap { import ->
                    val last = import.it.last()
                    if(last == "*" && last == name) import.targetPackage.functions.filter { it.name == name } else emptyList()
                } + parent.getFunctions(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return it.imports.firstNotNullOfOrNull { import ->
                    val last = import.it.last()
                    if (last == "*" || last == name) import.targetPackage.classes.find { it.name == name } else null
                } ?: parent.getClass(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return getFunctions(name) + parent.getInvokable(name)
            }

            override val signature: String
                get() = "FI${it.signature}"

        }

        companion object {
            fun from(it: ShakeFile, parent: ShakeScope): ShakeFileScope = Impl(it, parent)
        }
    }

    interface ShakeProjectScope : ShakeScope {
        class Impl(val it: ShakeProject) : ShakeProjectScope {
            override val parent: ShakeScope? = null

            override fun get(name: String): ShakeAssignable? {
                return it.fields.find { it.name == name }
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return it.functions.filter { it.name == name }
            }

            override fun getClass(name: String): ShakeClass? {
                return it.classes.find { it.name == name }
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return it.functions.filter { it.name == name }
            }

            override val signature: String
                get() = "PS"
        }

        companion object {
            fun from(it: ShakeProject): ShakeProjectScope = Impl(it)
        }
    }

    interface ShakePackageScope : ShakeScope {
        class Impl(val it: ShakePackage) : ShakePackageScope {
            override val parent: ShakeScope get() = it.project.scope

            override fun get(name: String): ShakeAssignable? {
                return it.fields.find { it.name == name }
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return it.functions.filter { it.name == name }
            }

            override fun getClass(name: String): ShakeClass? {
                return it.classes.find { it.name == name }
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return it.functions.filter { it.name == name }
            }

            override val signature: String
                get() = "PK${it.signature}"
        }

        companion object {
            fun from(it: ShakePackage): ShakePackageScope = Impl(it)
        }
    }
}
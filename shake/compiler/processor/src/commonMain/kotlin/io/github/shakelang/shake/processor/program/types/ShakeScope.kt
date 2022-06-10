package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeCode
import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration
import io.github.shakelang.shake.processor.util.Pointer

/**
 * Represents a scope in the Shake program.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeScope {

    interface ShakeScopeImpl : ShakeScope {
        override val parent: ShakeScopeImpl?
        override val pkg: ShakePackage.Impl?
        override val project: ShakeProject.Impl get() = parent?.project ?: pkg?.project ?: error("No project found")
    }

    /**
     * Returns the parent scope of this scope.
     */
    val parent: ShakeScope?
    val pkg: ShakePackage?

    val project: ShakeProject get() = parent?.project ?: throw IllegalStateException("No project found")

    /**
     * Returns the variable with the given name in this scope or null if it doesn't exist.
     *
     * @param name The name of the variable to look for.
     * @return The variable with the given name or null if it doesn't exist.
     */
    fun get(name: String): ShakeAssignable?

    /**
     * Returns all functions with the given name in this scope or null if none exist.
     *
     * @param name The name of the function to look for.
     * @return All functions with the given name or null if none exist.
     */
    fun getFunctions(name: String): List<ShakeFunctionType>

    /**
     * Returns the class with the given name in this scope or null if it doesn't exist.
     *
     * @param name The name
     * @return All variables with the given name or null if none exist.
     */
    fun getClass(name: String): ShakeClass?

    /**
     * Returns a list of invokable functions in this scope. This also includes invokable variables with
     * the given name.
     *
     * @param name The name of the variable to look for.
     * @return The variable with the given name or null if it doesn't exist.
     */
    fun getInvokable(name: String): List<ShakeInvokable>

    /**
     * The signature of this scope.
     */
    val signature: String


    /**
     * Instance scope for classes. This scope is used to store the instance variables of a class.
     * It extends the classes [ShakeClassStaticScope].
     * All the non-static variables, functions and classes also use this scope as their parent for
     * code execution.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeClassInstanceScope : ShakeScope {
        class Impl (val it: ShakeClass.Impl) : ShakeClassInstanceScope, ShakeScopeImpl{

            override val parent get() = it.staticScope
            override val pkg get() = it.pkg

            init {
                it.project.registerScope(this)
            }

            override fun get(name: String): ShakeAssignable? {
                return it.fields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunctionType> {
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
            fun from(clazz: ShakeClass.Impl) = Impl(clazz)
        }
    }


    /**
     * Static scope for classes. This scope is used to store the static variables of a class.
     * All the static variables, functions and classes also use this scope as their parent for
     * code execution.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeClassStaticScope : ShakeScope {
        class Impl(val it: ShakeClass.Impl) : ShakeClassStaticScope, ShakeScopeImpl {

            override val parent get() = it.parentScope
            override val pkg get() = it.pkg

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override fun get(name: String): ShakeAssignable? {
                return it.staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunctionType> {
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
            fun from(clazz: ShakeClass.Impl) = Impl(clazz)
        }
    }

    /**
     * Scope for all function types (Methods & Functions).
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeFunctionTypeScope : ShakeScope

    /**
     * Scope for functions. This scope is used to store the variables of a function and it's
     * parameters.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeFunctionScope : ShakeFunctionTypeScope {
        class Impl(val it: ShakeFunction.Impl) : ShakeFunctionScope, ShakeScopeImpl {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            override val parent get() = it.parentScope
            override val pkg get() = it.pkg

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunctionType> {
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
            fun from(it: ShakeFunction.Impl) = Impl(it)
        }
    }

    /**
     * Scope for methods. This scope is used to store the variables of a method and it's parameters.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeMethodScope : ShakeFunctionTypeScope {
        class Impl(val it: ShakeMethod.Impl) : ShakeMethodScope, ShakeScopeImpl {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override val parent = it.parentScope
            override val pkg get() = it.pkg

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunctionType> {
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
            fun from(it: ShakeMethod.Impl) = Impl(it)
        }
    }

    /**
     * Scope for constructors. This scope is used to store the variables of a constructor and it's
     * parameters.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeConstructorScope : ShakeScope {
        class Impl(val it: ShakeConstructor.Impl) : ShakeConstructorScope, ShakeScopeImpl {

            val variables = mutableListOf<ShakeVariableDeclaration>()

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override val parent = it.scope
            override val pkg get() = it.clazz.pkg

            override fun get(name: String): ShakeAssignable? {
                return variables.find { it.name == name } ?: it.parameters.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunctionType> {
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
            fun from(it: ShakeConstructor.Impl) = Impl(it)
        }
    }

    /**
     * Scope for a file. This scope is used to store everything that is defined in a file.
     * It also provides the file's imports.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeFileScope : ShakeScope {
        class Impl(val it: ShakeFile.Impl) : ShakeFileScope, ShakeScopeImpl {

            override val pkg get() = it.pkg
            override val parent get() = it.parentScope

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override fun get(name: String): ShakeAssignable? {
                return it.imports.firstNotNullOfOrNull { import ->
                    val last = import.it.last()
                    if (last == "*" || last == name) import.targetPackage.fields.find { it.name == name } else null
                } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunctionType> {
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
            fun from(it: ShakeFile.Impl) = Impl(it)
        }
    }

    /**
     * Scope for a package. This scope is used to store everything that is defined in a package.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakePackageScope : ShakeScope {
        class Impl(val it: ShakePackage.Impl) : ShakePackageScope, ShakeScopeImpl {
            override val parent get() = it.project.scope
            override val pkg get() = it

            init {
                it.project.registerScope(this)
            }

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
            fun from(it: ShakePackage.Impl) = Impl(it)
        }
    }

    /**
     * Scope for a project. This scope is used to store everything that is defined in a project.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeProjectScope : ShakeScope {
        class Impl(val it: ShakeProject) : ShakeProjectScope, ShakeScopeImpl {

            override val parent = null
            override val pkg get() = null

            init { if (it is ShakeProject.Impl) it.registerScope(this) }

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
            fun from(it: ShakeProject) = Impl(it)
        }
    }

    /**
     * Scope for code. This scope is used to store everything that is defined in a code block
     */
    interface ShakeCodeScope : ShakeScope {

        val variables: List<ShakeVariableDeclaration>

        fun addVariable(value: ShakeVariableDeclaration)

        class Impl(val it: ShakeCode.Impl) : ShakeCodeScope, ShakeScopeImpl {

            override val variables: MutableList<ShakeVariableDeclaration> = mutableListOf()
            override val pkg get() = parent.pkg

            override fun addVariable(value: ShakeVariableDeclaration) {
                this.variables.add(value)
            }

            override val parent get() = it.parentScope

            override fun get(name: String): ShakeAssignable? = it.scope.get(name) ?: parent.get(name)
            override fun getFunctions(name: String): List<ShakeFunctionType> = parent.getFunctions(name)
            override fun getClass(name: String): ShakeClass? = parent.getClass(name)
            override fun getInvokable(name: String): List<ShakeInvokable> = parent.getInvokable(name)

            override val signature: String
                get() = "CD" // TODO
        }

        companion object {
            fun from(impl: ShakeCode.Impl) = Impl(impl)
        }
    }

    companion object {
        fun from(prj: ShakeProject, it: ShakeScope): Pointer<ShakeScope?> {
            return prj.getScopeBySignature(it.signature)
        }
        fun from(scope: ShakeScope, it: ShakeScope): Pointer<ShakeScope?> {
            return scope.project.getScopeBySignature(it.signature)
        }
    }
}
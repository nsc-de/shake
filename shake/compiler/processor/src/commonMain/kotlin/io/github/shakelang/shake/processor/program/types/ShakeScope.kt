package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.statements.ShakeVariableDeclaration
import io.github.shakelang.shake.processor.util.Pointer

/**
 * Represents a scope in the Shake program.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeScope {

    /**
     * Returns the parent scope of this scope.
     */
    val parent: ShakeScope?

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
        class Impl (val it: ShakeClass) : ShakeClassInstanceScope {

            override val parent: ShakeScope get() = it.staticScope

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

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
            fun from(clazz: ShakeClass): ShakeClassInstanceScope = Impl(clazz)
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
        class Impl(val it: ShakeClass) : ShakeClassStaticScope {

            override val parent: ShakeScope get() = it.parentScope

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
            fun from(clazz: ShakeClass): ShakeClassStaticScope = Impl(clazz)
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
        class Impl(val it: ShakeFunction) : ShakeFunctionScope {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            override val parent: ShakeScope get() = it.parentScope

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
            fun from(it: ShakeFunction): ShakeFunctionScope = Impl(it)
        }
    }

    /**
     * Scope for methods. This scope is used to store the variables of a method and it's parameters.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeMethodScope : ShakeFunctionTypeScope {
        class Impl(val it: ShakeMethod) : ShakeMethodScope {
            val variables = mutableListOf<ShakeVariableDeclaration>()

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override val parent: ShakeScope = it.parentScope

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
            fun from(it: ShakeMethod): ShakeMethodScope = Impl(it)
        }
    }

    /**
     * Scope for constructors. This scope is used to store the variables of a constructor and it's
     * parameters.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeConstructorScope : ShakeScope {
        class Impl(val it: ShakeConstructor) : ShakeConstructorScope {

            val variables = mutableListOf<ShakeVariableDeclaration>()

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

            override val parent: ShakeScope = it.scope

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
    }

    /**
     * Scope for a file. This scope is used to store everything that is defined in a file.
     * It also provides the file's imports.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeFileScope : ShakeScope {
        class Impl(val it: ShakeFile, override val parent: ShakeScope) : ShakeFileScope {

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
            fun from(it: ShakeFile, parent: ShakeScope): ShakeFileScope = Impl(it, parent)
        }
    }

    /**
     * Scope for a package. This scope is used to store everything that is defined in a package.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakePackageScope : ShakeScope {
        class Impl(val it: ShakePackage) : ShakePackageScope {
            override val parent: ShakeScope get() = it.project.scope

            init { if (it.project is ShakeProject.Impl) (it.project as ShakeProject.Impl).registerScope(this) }

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

    /**
     * Scope for a project. This scope is used to store everything that is defined in a project.
     *
     * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
     */
    interface ShakeProjectScope : ShakeScope {
        class Impl(val it: ShakeProject) : ShakeProjectScope {

            override val parent: ShakeScope? = null

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
            fun from(it: ShakeProject): ShakeProjectScope = Impl(it)
        }
    }

    companion object {
        fun from(prj: ShakeProject, scope: ShakeScope): Pointer<ShakeScope?> {
            return prj.getScopeBySignature(scope.signature)
        }
    }
}
package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.PointingList
import io.github.shakelang.shake.processor.util.latePoint
import io.github.shakelang.shake.processor.util.point

/**
 * Represents a package in the Shake language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakePackage {

    /**
     * The [ShakeProject] of this package.
     */
    val project: ShakeProject

    /**
     * The name of this package.
     */
    val name: String

    /**
     * The parent [ShakePackage] of this package (if any).
     */
    val parent: ShakePackage?

    /**
     * A list of pointers to all the sub-packages of this package.
     */
    val subpackagePointers: List<Pointer<ShakePackage>>

    /**
     * A list of pointers to all the [ShakeClass]es in this package.
     */
    val classPointers: List<Pointer<ShakeClass>>

    /**
     * A list of pointers to all the [ShakeFunction]s in this package.
     */
    val functionPointers: List<Pointer<ShakeFunction>>

    /**
     * A list of pointers to all the [ShakeVariable]s in this package.
     */
    val fieldPointers: List<Pointer<ShakeField>>

    /**
     * The subpackages of this package.
     */
    val subpackages: List<ShakePackage>

    /**
     * The classes in this package.
     */
    val classes: List<ShakeClass>

    /**
     * The functions in this package.
     */
    val functions: List<ShakeFunction>

    /**
     * The fields in this package.
     */
    val fields: List<ShakeField>

    /**
     * The qualified name of this package.
     */
    val qualifiedName: String

    /**
     * The [ShakeScope] of this package. This is the scope that all the
     * [ShakeClass]es, [ShakeFunction]s, and [ShakeField]s in this package
     * will be added to. Its parent is the [ShakeScope] of the project.
     * All the [ShakeClass]es, [ShakeFunction]s inside this package will
     * use this scope as their parent and code executed statically to
     * initialize [ShakeField] values directly use this scope.
     */
    val scope: ShakeScope.ShakePackageScope

    /**
     * The signature of this package. The package signature is used to
     * determine if two packages are the same and to find the package
     * from the [ShakeProject].
     */
    val signature: String

    /**
     * Get a subpackage by its name. If the name contains a dot, it will be split and
     * the package will be searched recursively in subpackages. This will give back
     * a [Pointer] to the package, so if the package is not found, but created at a
     * later point, it will be returned, but only if it is already created at the
     * point of access of the [Pointer.value] property. Until creation the pointer
     * will point to null.
     *
     * @param name The name of the package.
     * @return A pointer to the package.
     */
    fun getPackage(name: String): Pointer<ShakePackage?>

    /**
     * Get a package by the parts of its name. The parts must not contain dots.
     * If they do so, it will search for packages with a dot in their name.
     * This will give back a [Pointer] to the package, so if the package is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param parts The parts of the package name.
     * @return A pointer to the package.
     */
    fun getPackage(parts: Array<String>): Pointer<ShakePackage?>

    /**
     * Get a child class by its signature. This will give back a [Pointer] to the
     * class, so if the class is not found, but created at a later point, it will
     * be returned, but only if it is already created at the point of access of the
     * [Pointer.value] property. Until creation the pointer will point to null.
     * This will also search for classes with a dot in their name.
     *
     * @param signature The signature of the class.
     * @return A pointer to the class.
     */
    fun getClassBySignature(signature: String): Pointer<ShakeClass?> = Pointer.task { classes.firstOrNull { it.signature == signature } }

    /**
     * Get a child function by its signature. This will give back a [Pointer] to the
     * function, so if the function is not found, but created at a later point, it will
     * be returned, but only if it is already created at the point of access of the
     * [Pointer.value] property. Until creation the pointer will point to null.
     *
     * @param signature The signature of the function.
     * @return A pointer to the function.
     */
    fun getFunctionBySignature(signature: String): Pointer<ShakeFunction?> = Pointer.task { functions.firstOrNull { it.signature == signature } }


    /**
     * Get a child field by its signature. This will give back a [Pointer] to the
     * field, so if the field is not found, but created at a later point, it will
     * be returned, but only if it is already created at the point of access of the
     * [Pointer.value] property. Until creation the pointer will point to null.
     *
     * @param signature The signature of the field.
     * @return A pointer to the field.
     */
    fun getFieldBySignature(signature: String): Pointer<ShakeField?> = Pointer.task { fields.firstOrNull { it.signature == signature } }

    /**
     * Get a child class by its name. This will give back a [Pointer] to the
     * class, so if the class is not found, but created at a later point, it will
     * be returned, but only if it is already created at the point of access of the
     * [Pointer.value] property. Until creation the pointer will point to null.
     *
     * @param name The name of the class.
     * @return A pointer to the class.
     */
    fun getClassByName(name: String): Pointer<ShakeClass?> = Pointer.task { classes.firstOrNull { it.name == name } }

    /**
     * Get a child functions by their name. This will give back a [Pointer] to the
     * functions, so if no functions are found, but created at a later point, they will
     * be included, but only if they are already created at the point of access of the
     * [Pointer.value] property. Until creation the pointer will point to null.
     *
     * @param name The name of the functions
     * @return A pointer to a list of [ShakeFunction].
     */
    fun getFunctionsByName(name: String): Pointer<List<ShakeFunction>> = Pointer.task { functions.filter { it.name == name } }


    /**
     * Returns a JSON map representation of this package. This is used to serialize
     * the package to JSON.
     */
    fun toJson(): Map<String, Any?>

    class Impl : ShakePackage {
        override val project: ShakeProject
        override val name: String
        override val parent: ShakePackage?

        override val subpackagePointers: List<Pointer<ShakePackage>>
        override val classPointers: List<Pointer<ShakeClass>>
        override val functionPointers: List<Pointer<ShakeFunction>>
        override val fieldPointers: List<Pointer<ShakeField>>

        override val subpackages: List<ShakePackage>
        override val classes: List<ShakeClass>
        override val functions: List<ShakeFunction>
        override val fields: List<ShakeField>

        override val qualifiedName: String get() = if (parent == null) name else "${parent.qualifiedName}.$name"
        override val scope: ShakeScope.ShakePackageScope = ShakeScope.ShakePackageScope.from(this)
        override val signature: String get() = qualifiedName

        constructor(
            baseProject: ShakeProject,
            name: String,
            parent: ShakePackage?,
            subpackages: List<ShakePackage>,
            classes: List<ShakeClass>,
            functions: List<ShakeFunction>,
            fields: List<ShakeField>
        ) {
            this.project = baseProject
            this.name = name
            this.parent = parent

            this.subpackagePointers = subpackages.map { it.point() }
            this.classPointers = classes.map { it.point() }
            this.functionPointers = functions.map { it.point() }
            this.fieldPointers = fields.map { it.point() }

            this.subpackages = PointingList.from(subpackagePointers)
            this.classes = PointingList.from(classPointers)
            this.functions = PointingList.from(functionPointers)
            this.fields = PointingList.from(fieldPointers)
        }

        internal constructor(
            baseProject: ShakeProject.Impl,
            parent: Impl?,
            it: ShakePackage
        ) {
            this.project = baseProject
            this.name = it.name
            this.parent = parent

            val subpackagePointers = it.subpackages.map { latePoint<ShakePackage>() }.toMutableList()
            val classPointers = it.classes.map { latePoint<ShakeClass>() }
            val functionPointers = it.functions.map { latePoint<ShakeFunction>() }
            val fieldPointers = it.fields.map { latePoint<ShakeField>() }

            this.subpackagePointers = subpackagePointers
            this.classPointers = classPointers
            this.functionPointers = functionPointers
            this.fieldPointers = fieldPointers

            this.subpackages = PointingList.from(subpackagePointers)
            this.classes = PointingList.from(classPointers)
            this.functions = PointingList.from(functionPointers)
            this.fields = PointingList.from(fieldPointers)

            it.subpackages.zip(subpackagePointers) { pkg, pointer ->
                pointer.init(from(baseProject, this, pkg))
            }

            it.classes.zip(classPointers) { clz, pointer ->
                pointer.init(ShakeClass.from(baseProject, this, clz))
            }

            it.functions.zip(functionPointers) { fn, pointer ->
                pointer.init(ShakeFunction.from(baseProject, this, fn))
            }

            it.fields.zip(fieldPointers) { f, pointer ->
                pointer.init(ShakeField.from(baseProject, this, f))
            }

        }

        override fun getPackage(name: String): Pointer<ShakePackage?> {
            if(name.contains(".")) return getPackage(name.split(".").toTypedArray())
            return Pointer.task { subpackages.find { it.name == name } ?: throw Error("Package $name not found") }
        }

        override fun getPackage(parts: Array<String>): Pointer<ShakePackage?> {
            if(parts.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            return getPackage(parts.first()).chainAllowNull { it?.getPackage(parts.drop(1).toTypedArray())  }
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "name" to name,
                "subpackages" to subpackages.map { it.name },
                "classes" to classes.map { it.name },
                "functions" to functions.map { it.name },
                "fields" to fields.map { it.name }
            )
        }
    }

    companion object {
        /**
         * Clone a [ShakePackage]. This is used to create deep copies of [ShakeProject]s.
         */
        fun from(project: ShakeProject.Impl, parent: Impl?, it: ShakePackage): ShakePackage = Impl(project, parent, it)
    }
}
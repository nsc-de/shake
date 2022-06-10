package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.parser.node.ShakeIdentifierNode
import io.github.shakelang.shake.parser.node.ShakeValuedNode
import io.github.shakelang.shake.parser.node.ShakeVariableType
import io.github.shakelang.shake.processor.util.*
import io.github.shakelang.shason.json

/**
 * A Shake Project is a collection of packages and subpackages each containing a set of classes, functions, and fields.
 * It is the root of a Shake program. There is only one Shake Project per program.
 *
 *
 * @property packages The packages in the project.
 * @property classes The classes in the root of the project.
 * @property functions The functions in root of the project.
 * @property fields The fields in root of the project.
 * @property scope The scope of the project.
 *
 *
 * @author Nicolas Schmidt (<[nsc-de](https://github.com/nsc-de)>)
 */
interface ShakeProject {

    /**
     * List containing pointers to all root packages in the project.
     */
    val packagePointers: PointerList<ShakePackage>

    /**
     * List containing pointers to all root files in the project.
     */
    val filePointers: PointerList<ShakeFile>

    /**
     * List containing pointers to all root classes in the project.
     */
    val classPointers: PointerList<ShakeClass>

    /**
     * List containing pointers to all root functions in the project.
     */
    val functionPointers: PointerList<ShakeFunction>

    /**
     * List containing pointers to all root fields in the project.
     */
    val fieldPointers: PointerList<ShakeField>


    /**
     * List containing all the root packages in the project.
     */
    val packages: List<ShakePackage>

    /**
     * List containing all the root files in the project.
     */
    val files: List<ShakeFile>

    /**
     * List containing all the root classes in the project.
     */
    val classes: List<ShakeClass>

    /**
     * List containing all the root functions in the project.
     */
    val functions: List<ShakeFunction>

    /**
     * List containing all the root fields in the project.
     */
    val fields: List<ShakeField>

    /**
     * The scope of the project. It is comparable to a global scope because
     * it is extended by all packages, subpackages, classes and functions.
     */
    val scope: ShakeScope.ShakeProjectScope

    /**
     * Get a package by its name. If the name contains a dot, it will be split and
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
     * Get a class by its name. If the name contains a dot, it will be split and
     * the class will be searched recursively in subpackages. This will give back
     * a [Pointer] to the class, so if the class is not found, but created at a
     * later point, it will be returned, but only if it is already created at the
     * point of access of the [Pointer.value] property. Until creation the pointer
     * will point to null.
     *
     * @param name The name of the class.
     * @return A pointer to the class.
     */
    fun getClass(name: String): Pointer<ShakeClass?>

    /**
     * Get a class by the parts of its name and the name. The parts must not contain dots.
     * If they do so, it will search for classes with a dot in their name.
     * This will give back a [Pointer] to the class, so if the class is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param pkg The parts of the class name.
     * @param name The name of the class.
     */
    fun getClass(pkg: Array<String>, name: String): Pointer<ShakeClass?>

    /**
     * Translate the ShakeProject to a json map representation. This is used for
     * debugging purposes.
     *
     * @return A json map representation of the project.
     */
    fun toJson(): Map<String, Any?>

    /**
     * Translate the ShakeProject to a JSON code. This is used for
     * debugging purposes.
     *
     * @return A JSON code representation of the project.
     */
    fun toJsonString(format: Boolean = false): String

    /**
     * Create a [ShakeType] from a [ShakeVariableType].
     */
    fun getType(type: ShakeVariableType): Pointer<ShakeType?>

    /**
     * Get a [ShakeClass] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeClass], so if the class is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param signature The signature of the class.
     * @return A pointer to the class.
     */
    fun getClassBySignature(signature: String): Pointer<ShakeClass?> {
        val parts = signature.split("#")
        val pkg = getPackage(parts[0])
        return pkg.chainAllowNull { it?.getClassBySignature(signature) }
    }

    /**
     * Get a [ShakeFunction] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeFunction], so if the function is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param signature The signature of the function.
     * @return A pointer to the function.
     */
    fun getFunctionBySignature(signature: String): Pointer<ShakeFunction?> {
        val parts = signature.split("#")
        val pkg = getPackage(parts[0])
        return pkg.chainAllowNull { it?.getFunctionBySignature(signature) }
    }

    /**
     * Get a [ShakeField] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeField], so if the field is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param signature The signature of the field.
     * @return A pointer to the field.
     */
    fun getFieldBySignature(signature: String): Pointer<ShakeField?> {
        val parts = signature.split("#")
        val pkg = getPackage(parts[0])
        return pkg.chainAllowNull { it?.getFieldBySignature(signature) }
    }

    /**
     * Get a [ShakeMethod] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeMethod], so if the function is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param signature The signature of the function.
     * @return A pointer to the [ShakeMethod].
     */
    fun getMethodBySignature(signature: String): Pointer<ShakeMethod?> {
        val parts = signature.split("#")
        val clz = getClassBySignature("${parts[0]}#${parts[1]}")
        return clz.transform { it?.getMethodBySignature(signature) }
    }

    /**
     * Get a [ShakeClassField] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeClassField], so if the field is
     * not found, but created at a later point, it will be returned, but only if it is
     * already created at the point of access of the [Pointer.value] property. Until
     * creation the pointer will point to null.
     *
     * @param signature The signature of the field.
     * @return A pointer to the [ShakeClassField].
     */
    fun getClassFieldBySignature(signature: String): Pointer<ShakeClassField?> {
        val parts = signature.split("#")
        val clz = getClassBySignature("${parts[0]}#${parts[1]}")
        return clz.transform { it?.getFieldBySignature(signature) }
    }

    /**
     * Get a [ShakeConstructor] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeConstructor], so if the constructor is
     * not found, but created at a later point, it will be returned, but only if it is
     * already created at the point of access of the [Pointer.value] property. Until
     * creation the pointer will point to null.
     *
     * @param signature The signature of the constructor.
     * @return A pointer to the [ShakeConstructor].
     */
    fun getConstructorBySignature(signature: String): Pointer<ShakeConstructor?> {
        val parts = signature.split("#")
        val clz = getClassBySignature("${parts[0]}#${parts[1]}")
        return clz.transform { it?.getConstructorBySignature(signature) }
    }

    /**
     * Get a [ShakeScope] by its shake-signature.
     * This will give back a [Pointer] to the [ShakeScope], so if the scope is not found,
     * but created at a later point, it will be returned, but only if it is already
     * created at the point of access of the [Pointer.value] property. Until creation
     * the pointer will point to null.
     *
     * @param signature The signature of the scope.
     * @return A pointer to the [ShakeScope].
     */
    fun getScopeBySignature(signature: String): Pointer<ShakeScope?>

    class Impl: ShakeProject {

        override val packagePointers: MutablePointerList<ShakePackage.Impl>
        override val filePointers: MutablePointerList<ShakeFile.Impl>
        override val classPointers: MutablePointerList<ShakeClass.Impl>
        override val functionPointers: MutablePointerList<ShakeFunction.Impl>
        override val fieldPointers: MutablePointerList<ShakeField.Impl>

        override val packages: List<ShakePackage.Impl>
        override val files: List<ShakeFile.Impl>
        override val classes: List<ShakeClass.Impl>
        override val functions: List<ShakeFunction.Impl>
        override val fields: List<ShakeField.Impl>
        override val scope: ShakeScope.ShakeProjectScope.Impl = ShakeScope.ShakeProjectScope.from(this)

        private val scopeList: MutableList<ShakeScope> = mutableListOf()
        val scopes: List<ShakeScope> get() = scopeList

        constructor(
            subpackages: List<ShakePackage.Impl>,
            files: List<ShakeFile.Impl>,
            classes: List<ShakeClass.Impl>,
            functions: List<ShakeFunction.Impl>,
            fields: List<ShakeField.Impl>
        ) {
            packagePointers = subpackages.mutablePoints()
            filePointers = files.mutablePoints()
            classPointers = classes.mutablePoints()
            functionPointers = functions.mutablePoints()
            fieldPointers = fields.mutablePoints()

            this.packages = PointingList.from(packagePointers)
            this.files = PointingList.from(filePointers)
            this.classes = PointingList.from(classPointers)
            this.functions = PointingList.from(functionPointers)
            this.fields = PointingList.from(fieldPointers)
        }

        internal constructor(
            it: ShakeProject
        ) {
            val subpackagePointers = it.packages.map { latePoint<ShakePackage.Impl>() }
            val filePointers = it.files.map { latePoint<ShakeFile.Impl>() }
            val classPointers = it.classes.map { latePoint<ShakeClass.Impl>() }
            val functionPointers = it.functions.map { latePoint<ShakeFunction.Impl>() }
            val fieldPointers = it.fields.map { latePoint<ShakeField.Impl>() }

            this.packagePointers = subpackagePointers.toMutableList()
            this.filePointers = filePointers.toMutableList()
            this.classPointers = classPointers.toMutableList()
            this.functionPointers = functionPointers.toMutableList()
            this.fieldPointers = fieldPointers.toMutableList()

            this.packages = PointingList.from(subpackagePointers)
            this.files = PointingList.from(filePointers)
            this.classes = PointingList.from(classPointers)
            this.functions = PointingList.from(functionPointers)
            this.fields = PointingList.from(fieldPointers)

            it.packages.zip(subpackagePointers) { pkg, pointer ->
                pointer.init(ShakePackage.from(this, null, pkg))
            }

            it.files.zip(filePointers) { file, pointer ->
                pointer.init(ShakeFile.from(scope, file))
            }

            this.files.flatMap { it.classes }.zip(classPointers) { clz, pointer ->
                pointer.init(clz)
            }

            this.files.flatMap { it.functions }.zip(functionPointers) { func, pointer ->
                pointer.init(func)
            }

            this.files.flatMap { it.fields }.zip(fieldPointers) { field, pointer ->
                pointer.init(field)
            }
        }

        constructor() : this(
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )

        override fun getPackage(name: String): Pointer<ShakePackage?> {
            if(name.contains(".")) return getPackage(name.split(".").toTypedArray())
            return Pointer.task {
                packages.find { it.name == name }
            }
        }

        override fun getPackage(parts: Array<String>): Pointer<ShakePackage?> {
            if(parts.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            return Pointer.task {
                getPackage(parts.first()).value?.getPackage(parts.drop(1).toTypedArray())?.value
            }
        }

        override fun getClass(pkg: Array<String>, name: String): Pointer<ShakeClass?> {
            return if(pkg.isEmpty()) Pointer.task { this.classes.find { it.name == name } }
            else Pointer.task { this.getPackage(pkg).value?.classes?.find { it.name == name } }
        }

        override fun getClass(name: String): Pointer<ShakeClass?> {
            val parts = name.split(".")
            val clazzName = parts.last()
            val pkg = parts.dropLast(1).toTypedArray()
            return getClass(pkg, clazzName)
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "classes" to classes.map { it.toJson() },
                "functions" to functions.map { it.toJson() },
                "fields" to fields.map { it.toJson() },
                "subpackages" to packages.map { it.toJson() }
            )
        }

        override fun toJsonString(format: Boolean): String {
            return json.stringify(toJson(), format)
        }

        override fun getType(type: ShakeVariableType): Pointer<ShakeType?> {
            return when (type.type) {
                ShakeVariableType.Type.BYTE -> ShakeType.Primitive.BYTE.pointer()
                ShakeVariableType.Type.SHORT -> ShakeType.Primitive.SHORT.pointer()
                ShakeVariableType.Type.INTEGER -> ShakeType.Primitive.INT.pointer()
                ShakeVariableType.Type.LONG -> ShakeType.Primitive.LONG.pointer()
                ShakeVariableType.Type.FLOAT -> ShakeType.Primitive.FLOAT.pointer()
                ShakeVariableType.Type.DOUBLE -> ShakeType.Primitive.DOUBLE.pointer()
                ShakeVariableType.Type.BOOLEAN -> ShakeType.Primitive.BOOLEAN.pointer()
                ShakeVariableType.Type.CHAR -> ShakeType.Primitive.CHAR.pointer()
                ShakeVariableType.Type.OBJECT -> {
                    val clz = mutableListOf<String>()
                    var identifier: ShakeValuedNode? = type.subtype!!
                    while(identifier != null) {
                        if(identifier !is ShakeIdentifierNode) throw IllegalArgumentException("Invalid type ${type.subtype}")
                        clz.add(identifier.name)
                        identifier = identifier.parent
                    }
                    val clzName = clz.reversed().joinToString(".")
                    this.getClass(clzName).transform { it?.let {ShakeType.objectType(it) } }
                }
                else -> {
                    throw IllegalArgumentException("Invalid type ${type.type}")
                }
            }
        }

        override fun getScopeBySignature(signature: String): Pointer<ShakeScope?> {
            return Pointer.task { scopes.find { it.signature == signature } }
        }

        fun getPackageF(name: String): ShakePackage.Impl {
            if(name.contains(".")) return getPackageF(name.split(".").toTypedArray())
            return packages.find { it.name == name } ?: ShakePackage.create(this, null, name)
        }

        fun getPackageF(parts: Array<String>): ShakePackage.Impl {
            if(parts.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            if(parts.size == 1) return getPackageF(parts.first())
            return getPackageF(parts.first()).getPackageF(parts.drop(1).toTypedArray())
        }

        internal fun registerScope(scope: ShakeScope) = scopeList.add(scope)
    }

    companion object {
        /**
         * Copies a [ShakeProject] into a new [ShakeProject]
         *
         * @param it The project to copy
         * @return A new [ShakeProject]
         */
        fun from(it: ShakeProject): ShakeProject = Impl(it)
        fun empty() = Impl()
    }
}
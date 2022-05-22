package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.util.*
import kotlin.math.min

/**
 * Represents a class in the Shake language.
 *
 * @author Nicolas Schmidt ([nsc-de](https://github.com/nsc-de))
 */
interface ShakeClass {

    /**
     * The name of the class.
     */
    val name: String

    /**
     * The [ShakeProject] this class belongs to.
     */
    val prj: ShakeProject

    /**
     * The [ShakePackage] this class belongs to (if any).
     */
    val pkg: ShakePackage?


    /**
     * The [ShakeScope] this class extends.
     */
    val parentScope: ShakeScope

    /**
     * The [ShakeScope] of this class in a static context. It extends the [parentScope] and contains all the static
     * [fields] and [methods] and [classes] of this class.
     */
    val staticScope: ShakeScope.ShakeClassStaticScope

    /**
     * The [ShakeScope] of this class in an instance context. It extends the [staticScope] and contains all the
     * non-static [fields] and [methods] and [classes] of this class.
     */
    val instanceScope: ShakeScope.ShakeClassInstanceScope


    /**
     * A list of pointers to the inner [ShakeClass]es of this class.
     */
    val classPointers: PointerList<ShakeClass>

    /**
     * A list of pointers to the static [ShakeClass]es of this class.
     */
    val staticClassPointers: PointerList<ShakeClass>

    /**
     * A list of pointers to the instance-[ShakeMethod]s of this class.
     */
    val methodPointers: PointerList<ShakeMethod>

    /**
     * A list of pointers to the static-[ShakeMethod]s of this class.
     */
    val staticMethodPointers: PointerList<ShakeMethod>

    /**
     * A list of pointers to the instance-[ShakeField]s of this class.
     */
    val fieldPointers: PointerList<ShakeField>

    /**
     * A list of pointers to the static-[ShakeField]s of this class.
     */
    val staticFieldPointers: PointerList<ShakeField>
    /**
     * A list of pointers to the [ShakeConstructor]s of this class.
     */
    val constructorPointers: PointerList<ShakeConstructor>


    /**
     * A list of all inner [ShakeClass]es of this class.
     */
    val classes: List<ShakeClass>

    /**
     * A list of all static [ShakeClass]es of this class.
     */
    val staticClasses: List<ShakeClass>

    /**
     * A list of all instance-[ShakeMethod]s of this class.
     */
    val methods: List<ShakeMethod>

    /**
     * A list of all static-[ShakeMethod]s of this class.
     */
    val staticMethods: List<ShakeMethod>

    /**
     * A list of all instance-[ShakeField]s of this class.
     */
    val fields: List<ShakeClassField>

    /**
     * A list of all static-[ShakeField]s of this class.
     */
    val staticFields: List<ShakeClassField>

    /**
     * A list of all [ShakeConstructor]s of this class.
     */
    val constructors: List<ShakeConstructor>

    /**
     * The qualified name of this class.
     */
    val qualifiedName: String

    /**
     * A [Pointer] to the super [ShakeClass] of this class.
     */
    val superClassPointer: Pointer<ShakeClass?>

    /**
     * The super [ShakeClass] of this class.
     */
    val superClass: ShakeClass?

    /**
     * A list of [Pointer]s to all interfaces this class implements.
     */
    val interfacePointers: PointerList<ShakeClass>

    /**
     * A list of all interfaces this class implements.
     */
    val interfaces: List<ShakeClass>

    /**
     * The signature of this class.
     */
    val signature: String


    /**
     * Checks if this class is compatible to the given [ShakeClass]. This means that this class is either the same
     * class or it extends or implements the given class.
     *
     * @param other The [ShakeClass] to check.
     * @return `true` if this class is compatible to the given [ShakeClass].
     */
    fun compatibleTo(other: ShakeClass): Boolean

    /**
     * Checks the compatibility distance between this class and the given [ShakeClass].
     * A smaller value means a nearer compatibility. A value of `-1` means that this
     * class is not compatible to the given [ShakeClass].
     * This is used to determine the order
     * of function to choose from.
     *
     * For example:
     * ```kotlin
     * class A
     * class B : A
     * class C : B
     * class D
     *
     * A.compatibilityDistance(A) = 0
     * A.compatibilityDistance(B) = 1
     * A.compatibilityDistance(C) = 2
     * A.compatibilityDistance(D) = -1
     * ```
     *
     * @param other The [ShakeClass] to check.
     * @return The compatibility distance between this class and the given [ShakeClass] or `-1` if the
     *         classes are not compatible.
     */
    fun compatibilityDistance(other: ShakeClass): Int

    /**
     * Creates a [ShakeType] for this class.
     */
    fun asType(): ShakeType

    /**
     * Search for a [ShakeClass] with the given signature.
     *
     * @param signature The signature of the [ShakeClass] to search for.
     * @return The [ShakeClass] with the given signature or `null` if no such class exists.
     */
    fun getClassBySignature(signature: String): ShakeClass? = classes.find { it.signature == signature }

    /**
     * Search for a [ShakeMethod] with the given signature. A signature is unique, so there can only be one
     * [ShakeMethod] with the given signature. A method is identified by its name and the parameters it takes
     * and the return type.
     *
     * @param signature The signature of the [ShakeMethod] to search for.
     * @return The [ShakeMethod] with the given signature or `null` if no such method exists.
     */
    fun getMethodBySignature(signature: String): ShakeMethod? = methods.find { it.signature == signature }

    /**
     * Search for a [ShakeField] with the given signature.
     *
     * @param signature The signature of the [ShakeField] to search for.
     * @return The [ShakeField] with the given name or `null` if no such field exists.
     */
    fun getFieldBySignature(signature: String): ShakeClassField? = fields.find { it.signature == signature }

    /**
     * Search for a [ShakeConstructor] with the given signature.
     *
     * @param signature The signature of the [ShakeConstructor] to search for.
     * @return The [ShakeConstructor] with the given signature or `null` if no such constructor exists.
     */
    fun getConstructorBySignature(signature: String): ShakeConstructor? = constructors.find { it.signature == signature }

    /**
     * Search for a [ShakeClass] with the given name.
     *
     * @param name The name of the [ShakeClass] to search for.
     * @return The [ShakeClass] with the given name or `null` if no such class exists.
     */
    fun getClassByName(name: String): ShakeClass? = classes.find { it.name == name }

    /**
     * Search for a [ShakeMethod] with the given name.
     *
     * @param name The name of the [ShakeMethod] to search for.
     * @return The [ShakeMethod] with the given name or `null` if no such method exists.
     */
    fun getMethodByName(name: String): ShakeMethod? = methods.find { it.name == name }

    /**
     * Search for a [ShakeField] with the given name.
     *
     * @param name The name of the [ShakeField] to search for.
     * @return The [ShakeField] with the given name or `null` if no such field exists.
     */
    fun getFieldByName(name: String): ShakeClassField? = fields.find { it.name == name }

    fun toJson(): Map<String, Any?>

    class Impl : ShakeClass {
        override val prj: ShakeProject
        override val pkg: ShakePackage?
        override val parentScope: ShakeScope
        override val name: String

        override val methodPointers: PointerList<ShakeMethod>
        override val fieldPointers: PointerList<ShakeField>
        override val classPointers: PointerList<ShakeClass>
        override val staticFieldPointers: PointerList<ShakeField>
        override val staticMethodPointers: PointerList<ShakeMethod>
        override val staticClassPointers: PointerList<ShakeClass>
        override val constructorPointers: PointerList<ShakeConstructor>

        override val methods: List<ShakeMethod>
        override val fields: List<ShakeClassField>
        override val classes: List<ShakeClass>
        override val staticMethods: List<ShakeMethod>
        override val staticFields: List<ShakeClassField>
        override val staticClasses: List<ShakeClass>
        override val constructors: List<ShakeConstructor>

        override val superClassPointer: Pointer<ShakeClass?>
        override val interfacePointers: PointerList<ShakeClass>

        override val superClass: ShakeClass? get() = superClassPointer.value
        override var interfaces: List<ShakeClass> = listOf()
            private set

        override val signature: String

        constructor(
            prj: ShakeProject,
            pkg: ShakePackage?,
            parentScope: ShakeScope,
            name: String,
            methods: List<ShakeMethod>,
            fields: List<ShakeClassField>,
            classes: List<ShakeClass>,
            staticMethods: List<ShakeMethod>,
            staticFields: List<ShakeClassField>,
            staticClasses: List<ShakeClass>,
            constructors: List<ShakeConstructor>,
            superClass: ShakeClass?,
            interfaces: List<ShakeClass>
        ) {
            this.prj = prj
            this.pkg = pkg
            this.parentScope = parentScope
            this.name = name

            this.methodPointers = methods.points()
            this.fieldPointers = fields.points()
            this.classPointers = classes.points()
            this.staticMethodPointers = staticMethods.points()
            this.staticFieldPointers = staticFields.points()
            this.staticClassPointers = staticClasses.points()
            this.constructorPointers = constructors.points()

            this.superClassPointer = superClass.point()
            this.interfacePointers = interfaces.points()

            this.methods = methodPointers.values()
            this.fields = fieldPointers.values()
            this.classes = classPointers.values()
            this.staticMethods = methodPointers.values()
            this.staticFields = fieldPointers.values()
            this.staticClasses = classPointers.values()
            this.constructors = constructorPointers.values()

            this.interfaces = interfacePointers.values()

            this.signature = "${pkg ?: ""}#$name"
        }

        constructor(
            prj: ShakeProject.Impl,
            pkg: ShakePackage.Impl?,
            parentScope: ShakeScope,
            it: ShakeClass
        ) {
            this.prj = prj
            this.pkg = pkg
            this.parentScope = parentScope
            this.name = it.name

            val methodPointers = it.methods.map { Pointer.late<ShakeMethod>() }
            val fieldPointers = it.fields.map { Pointer.late<ShakeClassField>() }
            val classPointers = it.classes.map { Pointer.late<ShakeClass>() }
            val staticMethodPointers = it.staticMethods.map { Pointer.late<ShakeMethod>() }
            val staticFieldPointers = it.staticFields.map { Pointer.late<ShakeClassField>() }
            val staticClassPointers = it.staticClasses.map { Pointer.late<ShakeClass>() }
            val constructorPointers = it.constructors.map { Pointer.late<ShakeConstructor>() }

            this.methodPointers = methodPointers
            this.fieldPointers = fieldPointers
            this.classPointers = classPointers
            this.staticMethodPointers = staticMethodPointers
            this.staticFieldPointers = staticFieldPointers
            this.staticClassPointers = staticClassPointers
            this.constructorPointers = constructorPointers

            this.methods = methodPointers.map { it.value }
            this.fields = fieldPointers.map { it.value }
            this.classes = classPointers.map { it.value }
            this.staticMethods = staticMethodPointers.map { it.value }
            this.staticFields = staticFieldPointers.map { it.value }
            this.staticClasses = staticClassPointers.map { it.value }
            this.constructors = constructorPointers.map { it.value }

            this.superClassPointer = it.superClass?.qualifiedName?.let { it1 -> prj.getClass(it1) } ?: Pointer.of(null)
            this.interfacePointers = it.interfaces.map { it1 -> prj.getClass(it1.qualifiedName).transform { it ?: throw Error("Implemented class does not exist") } }

            it.methods.zip(methodPointers).forEach { (method, pointer) -> pointer.init(ShakeMethod.from(this, method)) }
            it.fields.zip(fieldPointers).forEach { (field, pointer) -> pointer.init(ShakeClassField.from(this, field)) }
            it.classes.zip(classPointers).forEach { (clazz, pointer) -> pointer.init(from(prj, pkg, it)) }
            it.staticMethods.zip(staticMethodPointers).forEach { (method, pointer) -> pointer.init(ShakeMethod.from(this, method)) }
            it.staticFields.zip(staticFieldPointers).forEach { (field, pointer) -> pointer.init(ShakeClassField.from(this, field)) }
            it.staticClasses.zip(staticClassPointers).forEach { (_, pointer) -> pointer.init(from(prj, pkg, it)) }
            it.constructors.zip(constructorPointers).forEach { (constructor, pointer) -> pointer.init(ShakeConstructor.from(this, constructor)) }

            this.signature = "${pkg ?: ""}#$name"
        }

        override val staticScope: ShakeScope.ShakeClassStaticScope = ShakeScope.ShakeClassStaticScope.from(this)
        override val instanceScope: ShakeScope.ShakeClassInstanceScope = ShakeScope.ShakeClassInstanceScope.from(this)

        override val qualifiedName: String
            get() = (pkg?.qualifiedName?.plus(".") ?: "") + name

        override fun compatibleTo(other: ShakeClass): Boolean {
            if (this == other) return true
            if (this.superClass != null && this.superClass!!.compatibleTo(other)) return true
            return this.interfaces.any { it.compatibleTo(other) }
        }

        override fun compatibilityDistance(other: ShakeClass): Int {
            if (this == other) return 0
            val scd = (this.superClass?.compatibilityDistance(other) ?: -1) + 1
            val intDistance = (this.interfaces.minOfOrNull { it.compatibilityDistance(other) } ?: -2) + 1
            if(scd < 0) return intDistance
            if(intDistance < 0) return scd
            return min(scd, intDistance)
        }

        override fun asType(): ShakeType {
            return ShakeType.objectType(this)
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "name" to this.name,
                "super" to this.superClass?.name,
                "interfaces" to this.interfaces.map { it.name },
                "methods" to this.methods.map { it.toJson() },
                "staticMethods" to this.staticMethods.map { it.toJson() },
                "fields" to this.fields.map { it.toJson() },
                "staticFields" to this.staticFields.map { it.toJson() },
                "constructors" to this.constructors.map { it.toJson() },
                "classes" to this.classes.map { it.toJson() },
                "staticClasses" to this.staticClasses.map { it.toJson() },
            )
        }
    }

    companion object {
        fun from(project: ShakeProject, pkg: ShakePackage?, it: ShakeClass): ShakeClass = TODO()
    }
}
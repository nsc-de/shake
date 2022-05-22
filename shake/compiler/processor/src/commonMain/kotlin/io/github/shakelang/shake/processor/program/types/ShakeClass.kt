package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.util.*
import kotlin.math.min

interface ShakeClass {
    val staticScope: ShakeScope
    val instanceScope: ShakeScope
    val prj: ShakeProject
    val pkg: ShakePackage?
    val parentScope: ShakeScope
    val name: String

    val methodPointers: PointerList<ShakeMethod>
    val fieldPointers: PointerList<ShakeField>
    val classPointers: PointerList<ShakeClass>
    val staticFieldPointers: PointerList<ShakeField>
    val staticMethodPointers: PointerList<ShakeMethod>
    val staticClassPointers: PointerList<ShakeClass>
    val constructorPointers: PointerList<ShakeConstructor>

    val methods: List<ShakeMethod>
    val fields: List<ShakeClassField>
    val classes: List<ShakeClass>
    val staticMethods: List<ShakeMethod>
    val staticFields: List<ShakeClassField>
    val staticClasses: List<ShakeClass>
    val constructors: List<ShakeConstructor>

    val qualifiedName: String

    val superClassPointer: Pointer<ShakeClass?>
    val superClass: ShakeClass?

    val interfacePointers: PointerList<ShakeClass>
    val interfaces: List<ShakeClass>

    val signature: String

    fun compatibleTo(other: ShakeClass): Boolean
    fun compatibilityDistance(other: ShakeClass): Int
    fun asType(): ShakeType

    fun getMethodBySignature(signature: String): ShakeMethod? = methods.find { it.signature == signature }
    fun getFieldBySignature(signature: String): ShakeClassField? = fields.find { it.signature == signature }
    fun getClassBySignature(signature: String): ShakeClass? = classes.find { it.signature == signature }

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


        override val instanceScope: ShakeScope
            get() = ShakeScope.ShakeClassInstanceScope.from(this)

        override val staticScope: ShakeScope
            get() = ShakeScope.ShakeClassStaticScope.from(this)

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
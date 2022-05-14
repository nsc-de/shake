package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.PointingList
import io.github.shakelang.shake.processor.util.latePoint
import io.github.shakelang.shake.processor.util.point

interface ShakePackage {
    val baseProject: ShakeProject
    val name: String
    val parent: ShakePackage?

    val subpackagePointers: List<Pointer<ShakePackage>>
    val classPointers: List<Pointer<ShakeClass>>
    val functionPointers: List<Pointer<ShakeFunction>>
    val fieldPointers: List<Pointer<ShakeField>>

    val subpackages: List<ShakePackage>
    val classes: List<ShakeClass>
    val functions: List<ShakeFunction>
    val fields: List<ShakeField>

    val qualifiedName: String
    val scope: ShakeScope
    val signature: String

    fun getPackage(name: String): Pointer<ShakePackage?>
    fun getPackage(name: Array<String>): Pointer<ShakePackage?>

    fun getFunctionBySignature(signature: String): ShakeFunction? = functions.firstOrNull { it.signature == signature }
    fun getFieldBySignature(signature: String): ShakeField? = fields.firstOrNull { it.signature == signature }
    fun getClassBySignature(signature: String): ShakeClass? = classes.firstOrNull { it.signature == signature }


    fun toJson(): Map<String, Any?>

    class Impl : ShakePackage {
        override val baseProject: ShakeProject
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
        override val scope: ShakeScope = ShakeScope.ShakePackageScope.from(this)
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
            this.baseProject = baseProject
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
            this.baseProject = baseProject
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

        override fun getPackage(name: Array<String>): Pointer<ShakePackage?> {
            if(name.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            return getPackage(name.first()).chainAllowNull { it?.getPackage(name.drop(1).toTypedArray())  }
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
        fun from(project: ShakeProject.Impl, parent: Impl?, it: ShakePackage): ShakePackage = Impl(project, parent, it)
    }
}
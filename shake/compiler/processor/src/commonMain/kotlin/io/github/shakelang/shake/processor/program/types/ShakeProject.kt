package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.parser.node.ShakeIdentifierNode
import io.github.shakelang.shake.parser.node.ShakeValuedNode
import io.github.shakelang.shake.parser.node.ShakeVariableType
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import io.github.shakelang.shake.processor.util.Pointer
import io.github.shakelang.shake.processor.util.PointingList
import io.github.shakelang.shake.processor.util.latePoint
import io.github.shakelang.shason.json

interface ShakeProject {

    val subpackagePointers: List<Pointer<ShakePackage>>
    val classPointers: List<Pointer<ShakeClass>>
    val functionPointers: List<Pointer<ShakeFunction>>
    val fieldPointers: List<Pointer<ShakeField>>

    val subpackages: List<ShakePackage>
    val classes: List<ShakeClass>
    val functions: List<ShakeFunction>
    val fields: List<ShakeField>

    val projectScope: ShakeScope

    fun getPackage(name: String): Pointer<ShakePackage?>
    fun getPackage(name: Array<String>): Pointer<ShakePackage?>
    fun getClass(pkg: Array<String>, name: String): Pointer<ShakeClass?>
    fun getClass(clz: String): Pointer<ShakeClass?>
    fun toJson(): Map<String, Any?>
    fun toJsonString(format: Boolean = false): String

    fun getType(type: ShakeVariableType): Pointer<ShakeType?>

    fun getFunctionBySignature(signature: String): Pointer<ShakeFunction?> {
        val parts = signature.split("#")
        val pkg = getPackage(parts[0])
        return pkg.transform { it?.getFunctionBySignature(signature) }
    }

    fun getFieldBySignature(signature: String): Pointer<ShakeField?> {
        val parts = signature.split("#")
        val pkg = getPackage(parts[0])
        return pkg.transform { it?.getFieldBySignature(signature) }
    }

    fun getClassBySignature(signature: String): Pointer<ShakeClass?> {
        val parts = signature.split("#")
        val pkg = getPackage(parts[0])
        return pkg.transform { it?.getClassBySignature(signature) }
    }

    fun getMethodBySignature(signature: String): Pointer<ShakeMethod?> {
        val parts = signature.split("#")
        val clz = getClassBySignature("${parts[0]}#${parts[1]}")
        return clz.transform { it?.getMethodBySignature(signature) }
    }

    fun getClassFieldBySignature(signature: String): Pointer<ShakeClassField?> {
        val parts = signature.split("#")
        val clz = getClassBySignature("${parts[0]}#${parts[1]}")
        return clz.transform { it?.getFieldBySignature(signature) }
    }

    class Impl: ShakeProject {

        override val subpackagePointers: List<Pointer<ShakePackage>>
        override val classPointers: List<Pointer<ShakeClass>>
        override val functionPointers: List<Pointer<ShakeFunction>>
        override val fieldPointers: List<Pointer<ShakeField>>

        override val subpackages: List<ShakePackage>
        override val classes: List<ShakeClass>
        override val functions: List<ShakeFunction>
        override val fields: List<ShakeField>
        override val projectScope: ShakeScope = ShakeScope.ShakeProjectScope.from(this)

        constructor(
            subpackages: List<ShakePackage>,
            classes: List<ShakeClass>,
            functions: List<ShakeFunction>,
            fields: List<ShakeField>
        ) {
            subpackagePointers = subpackages.map { Pointer.of(it) }
            classPointers = classes.map { Pointer.of(it) }
            functionPointers = functions.map { Pointer.of(it) }
            fieldPointers = fields.map { Pointer.of(it) }

            this.subpackages = PointingList.from(subpackagePointers)
            this.classes = PointingList.from(classPointers)
            this.functions = PointingList.from(functionPointers)
            this.fields = PointingList.from(fieldPointers)
        }

        internal constructor(
            it: ShakeProject
        ) {
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
                pointer.init(ShakePackage.from(this, null, pkg))
            }

            it.classes.zip(classPointers) { clz, pointer ->
                pointer.init(ShakeClass.from(this, null, clz))
            }

            it.functions.zip(functionPointers) { fn, pointer ->
                pointer.init(ShakeFunction.from(this, null, fn))
            }

            it.fields.zip(fieldPointers) { f, pointer ->
                pointer.init(ShakeField.from(this, null, f))
            }
        }

        override fun getPackage(name: String): Pointer<ShakePackage?> {
            if(name.contains(".")) return getPackage(name.split(".").toTypedArray())
            return Pointer.task {
                subpackages.find { it.name == name }
            }
        }

        override fun getPackage(name: Array<String>): Pointer<ShakePackage?> {
            if(name.isEmpty()) throw IllegalArgumentException("Cannot get package from empty name")
            return Pointer.task {
                getPackage(name.first()).value?.getPackage(name.drop(1).toTypedArray())?.value
            }
        }

        override fun getClass(pkg: Array<String>, name: String): Pointer<ShakeClass?> {
            return if(pkg.isEmpty()) Pointer.task { this.classes.find { it.name == name } }
            else Pointer.task { this.getPackage(pkg).value?.classes?.find { it.name == name } }
        }

        override fun getClass(clz: String): Pointer<ShakeClass?> {
            val parts = clz.split(".")
            val name = parts.last()
            val pkg = parts.dropLast(1).toTypedArray()
            return getClass(pkg, name)
        }

        override fun toJson(): Map<String, Any?> {
            return mapOf(
                "classes" to classes.map { it.toJson() },
                "functions" to functions.map { it.toJson() },
                "fields" to fields.map { it.toJson() },
                "subpackages" to subpackages.map { it.toJson() }
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

        private class ClassRequirement(val name: String, val then: (ShakeClass) -> Unit)
        private class PackageRequirement(val name: String, val then: (ShakePackage) -> Unit)
    }

    companion object {
        fun from(it: ShakeProject): ShakeProject = Impl(it)
    }
}
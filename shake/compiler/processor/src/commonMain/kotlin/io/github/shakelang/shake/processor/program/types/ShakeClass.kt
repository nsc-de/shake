package io.github.shakelang.shake.processor.program.types

import io.github.shakelang.shake.processor.program.types.code.ShakeInvokable
import io.github.shakelang.shake.processor.program.types.code.ShakeScope
import kotlin.math.min

interface ShakeClass {
    val staticScope: ShakeScope
    val instanceScope: ShakeScope
    val prj: ShakeProject
    val pkg: ShakePackage?
    val parentScope: ShakeScope
    val name: String
    val methods: List<ShakeMethod>
    val fields: List<ShakeClassField>
    val classes: List<ShakeClass>
    val staticMethods: List<ShakeMethod>
    val staticFields: List<ShakeClassField>
    val staticClasses: List<ShakeClass>
    val constructors: List<ShakeConstructor>

    val qualifiedName: String
    val superClass: ShakeClass?

    val interfaces: List<ShakeClass>

    fun compatibleTo(other: ShakeClass): Boolean
    fun compatibilityDistance(other: ShakeClass): Int
    fun asType(): ShakeType

    fun toJson(): Map<String, Any?>

    class Impl(
        override val prj: ShakeProject,
        override val pkg: ShakePackage?,
        override val parentScope: ShakeScope,
        override val name: String,
        override val methods: List<ShakeMethod>,
        override val fields: List<ShakeClassField>,
        override val classes: List<ShakeClass>,
        override val staticMethods: List<ShakeMethod>,
        override val staticFields: List<ShakeClassField>,
        override val staticClasses: List<ShakeClass>,
        override val constructors: List<ShakeConstructor>,
        override val superClass: ShakeClass?,
        override val interfaces: List<ShakeClass>
    ) : ShakeClass {

        override val instanceScope: ShakeScope
            get() = InstanceScope()

        override val staticScope: ShakeScope
            get() = StaticScope()

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

        inner class StaticScope : ShakeScope {

            override val parent: ShakeScope get() = parentScope

            override fun get(name: String): ShakeAssignable? {
                return staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return staticMethods.filter { it.name == name } + parent.getFunctions(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return staticClasses.find { it.name == name } ?: parent.getClass(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return getFunctions(name) + parent.getInvokable(name)
            }

        }

        inner class InstanceScope : ShakeScope {

            override val parent: ShakeScope get() = parentScope

            override fun get(name: String): ShakeAssignable? {
                return fields.find { it.name == name } ?: staticFields.find { it.name == name } ?: parent.get(name)
            }

            override fun getFunctions(name: String): List<ShakeFunction> {
                return methods.filter { it.name == name } + staticMethods.filter { it.name == name } + parent.getFunctions(name)
            }

            override fun getClass(name: String): ShakeClass? {
                return classes.find { it.name == name } ?: parent.getClass(name)
            }

            override fun getInvokable(name: String): List<ShakeInvokable> {
                return getFunctions(name) + parent.getInvokable(name)
            }
        }
    }
}
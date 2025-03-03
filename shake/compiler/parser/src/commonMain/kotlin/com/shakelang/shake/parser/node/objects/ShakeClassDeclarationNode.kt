package com.shakelang.shake.parser.node.objects

import com.shakelang.shake.parser.node.ShakeAccessDescriber
import com.shakelang.shake.parser.node.ShakeFileChildNodeImpl
import com.shakelang.shake.parser.node.ShakeNamespaceNode
import com.shakelang.shake.parser.node.functions.ShakeFunctionDeclarationNode
import com.shakelang.shake.parser.node.variables.ShakeVariableDeclarationNode
import com.shakelang.util.parseutils.characters.position.PositionMap
import kotlin.jvm.JvmOverloads

@Suppress("unused")
class ShakeClassDeclarationNode @JvmOverloads constructor(
    map: PositionMap,
    val name: String,
    val extends: ShakeNamespaceNode?,
    val implements: Array<ShakeNamespaceNode>,
    val fields: Array<ShakeVariableDeclarationNode>,
    val methods: Array<ShakeFunctionDeclarationNode>,
    val classes: Array<ShakeClassDeclarationNode>,
    val constructors: Array<ShakeConstructorDeclarationNode>,
    val access: ShakeAccessDescriber = ShakeAccessDescriber.PACKAGE,
    val type: ShakeClassType = ShakeClassType.CLASS,
    val isStatic: Boolean,
    val isFinal: Boolean,
    val isAbstract: Boolean,
    val isNative: Boolean,
) : ShakeFileChildNodeImpl(map) {

    constructor(
        map: PositionMap,
        name: String,
        extends: ShakeNamespaceNode?,
        implements: List<ShakeNamespaceNode>,
        fields: List<ShakeVariableDeclarationNode>,
        methods: List<ShakeFunctionDeclarationNode>,
        classes: List<ShakeClassDeclarationNode>,
        constructors: List<ShakeConstructorDeclarationNode>,
        access: ShakeAccessDescriber,
        type: ShakeClassType,
        isStatic: Boolean,
        isFinal: Boolean,
        isAbstract: Boolean,
        isNative: Boolean,
    ) : this(
        map,
        name,
        extends,
        implements.toTypedArray(),
        fields.toTypedArray(),
        methods.toTypedArray(),
        classes.toTypedArray(),
        constructors.toTypedArray(),
        access,
        type,
        isStatic,
        isFinal,
        isAbstract,
        isNative,
    )

    override fun toJson(): Map<String, *> =
        mapOf(
            "name" to nodeName,
            "is_static" to isStatic,
            "is_final" to isFinal,
            "access" to access.toString(),
            "fields" to fields.map { it.json },
            "methods" to methods.map { it.json },
            "classes" to classes.map { it.json },
            "constructors" to constructors.map { it.json },
        )

    override fun equalsIgnorePosition(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShakeClassDeclarationNode) return false
        if (name != other.name) return false
        if (extends != other.extends) return false
        if (!implements.contentEquals(other.implements)) return false
        if (!fields.contentEquals(other.fields)) return false
        if (!methods.contentEquals(other.methods)) return false
        if (!classes.contentEquals(other.classes)) return false
        if (!constructors.contentEquals(other.constructors)) return false
        if (access != other.access) return false
        if (type != other.type) return false
        if (isStatic != other.isStatic) return false
        if (isFinal != other.isFinal) return false
        if (isAbstract != other.isAbstract) return false
        if (isNative != other.isNative) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShakeClassDeclarationNode) return false
        if (name != other.name) return false
        if (extends != other.extends) return false
        if (!implements.contentEquals(other.implements)) return false
        if (!fields.contentEquals(other.fields)) return false
        if (!methods.contentEquals(other.methods)) return false
        if (!classes.contentEquals(other.classes)) return false
        if (!constructors.contentEquals(other.constructors)) return false
        if (access != other.access) return false
        if (type != other.type) return false
        if (isStatic != other.isStatic) return false
        if (isFinal != other.isFinal) return false
        if (isAbstract != other.isAbstract) return false
        if (isNative != other.isNative) return false
        if (map != other.map) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (extends?.hashCode() ?: 0)
        result = 31 * result + implements.contentHashCode()
        result = 31 * result + fields.contentHashCode()
        result = 31 * result + methods.contentHashCode()
        result = 31 * result + classes.contentHashCode()
        result = 31 * result + constructors.contentHashCode()
        result = 31 * result + access.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + isStatic.hashCode()
        result = 31 * result + isFinal.hashCode()
        result = 31 * result + isAbstract.hashCode()
        result = 31 * result + isNative.hashCode()
        result = 31 * result + map.hashCode()
        return result
    }
}

enum class ShakeClassType {
    CLASS,
    INTERFACE,
    ENUM,
    OBJECT,
}

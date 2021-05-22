package com.github.nsc.de.shake.parser.node.objects

import com.github.nsc.de.shake.parser.node.ValuedNode
import com.github.nsc.de.shake.parser.node.variables.VariableDeclarationNode
import com.github.nsc.de.shake.parser.node.functions.FunctionDeclarationNode
import com.github.nsc.de.shake.parser.node.AccessDescriber
import com.github.nsc.de.shake.util.characterinput.position.PositionMap

@Suppress("unused")
class ClassDeclarationNode @JvmOverloads constructor(
    map: PositionMap,
    val name: String,
    val fields: Array<VariableDeclarationNode>,
    val methods: Array<FunctionDeclarationNode>,
    val classes: Array<ClassDeclarationNode>,
    val constructors: Array<ConstructorDeclarationNode>,
    val access: AccessDescriber = AccessDescriber.PACKAGE,
    val isInClass: Boolean = false,
    val isStatic: Boolean = false,
    val isFinal: Boolean = false
) : ValuedNode(map) {

    constructor(
        map: PositionMap,
        name: String,
        fields: List<VariableDeclarationNode>,
        methods: List<FunctionDeclarationNode>,
        classes: List<ClassDeclarationNode>,
        constructors: List<ConstructorDeclarationNode>,
        access: AccessDescriber,
        isInClass: Boolean,
        isStatic: Boolean,
        isFinal: Boolean
    ) : this(
        map,
        name,
        fields.toTypedArray(),
        methods.toTypedArray(),
        classes.toTypedArray(),
        constructors.toTypedArray(),
        access,
        isInClass,
        isStatic,
        isFinal
    )

    constructor(
        map: PositionMap,
        name: String,
        fields: List<VariableDeclarationNode>,
        methods: List<FunctionDeclarationNode>,
        classes: List<ClassDeclarationNode>,
        constructors: List<ConstructorDeclarationNode>
    ) : this(
        map,
        name,
        fields.toTypedArray(),
        methods.toTypedArray(),
        classes.toTypedArray(),
        constructors.toTypedArray()
    )

    override fun toString(): String = "ClassDeclarationNode{" +
            "fields=${ fields.contentToString() }, methods=${ methods.contentToString() }, " +
            "classes=${ classes.contentToString() }, constructors=${ constructors.contentToString() }}"
}
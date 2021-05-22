package com.github.nsc.de.shake.parser.node.functions

import com.github.nsc.de.shake.parser.node.VariableType
import com.github.nsc.de.shake.parser.node.Node
import com.github.nsc.de.shake.util.characterinput.position.PositionMap

class FunctionArgumentNode @JvmOverloads constructor(
    map: PositionMap,
    val name: String,
    private val type: VariableType = VariableType.DYNAMIC
) : Node(map) {
    override fun toString(): String = "FunctionArgumentNode{name='$name', type='$type'}"
}
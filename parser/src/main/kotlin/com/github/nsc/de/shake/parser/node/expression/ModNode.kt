package com.github.nsc.de.shake.parser.node.expression

import com.github.nsc.de.shake.parser.node.ValuedNode
import com.github.nsc.de.shake.util.characterinput.position.PositionMap

class ModNode(map: PositionMap, left: ValuedNode, right: ValuedNode, operatorPosition: Int) :
    ExpressionNode(map, left, right, operatorPosition) {
    override val operator: Char
        get() = '%'

    override fun toJson(): Map<String, *> = mapOf("name" to "ModNode", "left" to left, "right" to right)
}
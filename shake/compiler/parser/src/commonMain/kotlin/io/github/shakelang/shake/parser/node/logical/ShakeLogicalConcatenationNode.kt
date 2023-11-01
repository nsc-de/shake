package io.github.shakelang.shake.parser.node.logical

import io.github.shakelang.shake.util.parseutils.characters.position.PositionMap
import io.github.shakelang.shake.parser.node.ShakeValuedNode

abstract class ShakeLogicalConcatenationNode(map: PositionMap, val left: ShakeValuedNode, val right: ShakeValuedNode) :
    ShakeLogicalNode(map)
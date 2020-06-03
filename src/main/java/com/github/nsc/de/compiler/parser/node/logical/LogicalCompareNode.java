package com.github.nsc.de.compiler.parser.node.logical;

import com.github.nsc.de.compiler.parser.node.ValuedNode;
import com.github.nsc.de.compiler.parser.node.expression.NumberNode;

public abstract class LogicalCompareNode implements LogicalNode {
    private final ValuedNode left;
    private final ValuedNode right;

    public LogicalCompareNode(ValuedNode left, ValuedNode right) {
        this.left = left;
        this.right = right;
    }

    public ValuedNode getLeft() {
        return left;
    }
    public ValuedNode getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "("+(getLeft() != null && !(getLeft() instanceof NumberNode && ((NumberNode)getLeft()).getNumber() == 0) ? getLeft() : "")+getOperator()+getRight()+")";
    }
}

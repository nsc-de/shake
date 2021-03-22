package com.github.nsc.de.shake.parser.node.variables;

import com.github.nsc.de.shake.lexer.characterinput.position.PositionMap;
import com.github.nsc.de.shake.parser.node.ValuedNode;

public class VariableDecreaseNode extends ValuedNode {

    private final ValuedNode variable;
    private final int operatorPosition;

    public VariableDecreaseNode(PositionMap map, ValuedNode variable, int operatorPosition) {
        super(map);
        this.variable = variable;
        this.operatorPosition = operatorPosition;
    }

    public ValuedNode getVariable() {
        return variable;
    }
    public int getOperatorPosition() {
        return operatorPosition;
    }

    @Override
    public String toString() {
        return '{' + this.getVariable().toString() + "--}";
    }
}

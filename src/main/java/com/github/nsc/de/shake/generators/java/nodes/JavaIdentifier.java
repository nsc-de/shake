package com.github.nsc.de.shake.generators.java.nodes;

import com.github.nsc.de.shake.generators.java.JavaVariable;
import com.github.nsc.de.shake.generators.java.JavaVariableType;

import java.util.Arrays;

public class JavaIdentifier implements JavaValued {

    private final String identifier;
    private final JavaValued parent;

    public JavaIdentifier(String identifier, JavaValued parent) {
        this.identifier = identifier;
        this.parent = parent;
    }

    public JavaIdentifier(String identifier, String ...parents) {
        this.identifier = identifier;
        if(parents.length > 0) {
            this.parent = new JavaIdentifier(parents[0],
                    Arrays.copyOfRange(parents, 1, parents.length));
        } else this.parent = null;
    }

    public JavaVariable createDummyVariable() {
        return JavaVariable.createJavaArgument(identifier, JavaVariableType.UNKNOWN, true);
    }

    public JavaVariable.JavaVariableAccessDescriptor createDummyVariableAccess() {
        return createDummyVariable().access(this);
    }

    @Override
    public String toString(String indent, String add) {
        return (this.parent != null ? this.parent.toString(indent, add) + '.': "") + identifier;
    }

    @Override
    public JavaVariableType getType() {
        return JavaVariableType.UNKNOWN;
    }

    @Override
    public boolean expectToBe(JavaVariableType type) {
        throw new Error();
    }

    @Override
    public String toString() {
        return (parent != null ? this.parent.toString() + '.' : "") + this.identifier;
    }
}

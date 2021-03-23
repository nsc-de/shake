package com.github.nsc.de.shake.generators.java.nodes;

import com.github.nsc.de.shake.generators.java.JavaVariable;

public class JavaVariableDeclaration implements JavaNode.JavaOperation {

    private final JavaVariable variable;
    private final JavaValued assignment;
    private final boolean isStatic;
    private final boolean isFinal;
    private final JavaAccessDescriptor access;

    public JavaVariableDeclaration(JavaVariable variable, JavaValued assignment, boolean isStatic, boolean isFinal, JavaAccessDescriptor access) {

        this.variable = variable;
        this.assignment = assignment;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public JavaVariableDeclaration(JavaVariable variable, boolean isStatic, boolean isFinal, JavaAccessDescriptor access) {

        this.variable = variable;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
        this.assignment = null;

    }

    @Override
    public String toString(String indent, String add) {
        return access.toString() + (isStatic ? "static " : "") + (isFinal ? "final " : "")
                + variable.getType().toString() + ' ' + variable.getIdentifier() +
                (assignment != null ? " = " + this.assignment.toString(indent, add) : "");
    }
}

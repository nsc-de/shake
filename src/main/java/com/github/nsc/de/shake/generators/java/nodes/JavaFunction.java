package com.github.nsc.de.shake.generators.java.nodes;

import com.github.nsc.de.shake.generators.java.JavaVariable;
import com.github.nsc.de.shake.generators.java.JavaVariableType;

public class JavaFunction implements JavaNode {

    private final String name;
    private final JavaVariableType type;
    private final JavaVariable[] args;
    private final JavaTree body;
    private final JavaAccessDescriptor access;
    private final boolean isStatic;
    private final boolean isFinal;

    public JavaFunction(String name, JavaVariableType type, JavaVariable[] args, JavaTree body,
                        JavaAccessDescriptor access, boolean isStatic, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.args = args;
        this.body = body;
        this.access = access;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
    }

    @Override
    public String toString(String indent, String add) {
        StringBuilder str = new StringBuilder().append(this.access);
        if(isStatic) str.append("static ");
        if(isFinal) str.append("final ");
        str.append(this.type.toString()).append(' ').append(this.name).append("(");
        for(int i = 0; i < args.length; i++) {
            str.append(args[i].toString(indent, add));
            if(i < args.length - 1) str.append(", ");
        }
        return str.append(") ").append(this.body.toString(indent, add)).toString();
    }

}

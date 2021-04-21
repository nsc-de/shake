package com.github.nsc.de.shake.generators.java;

import com.github.nsc.de.shake.generators.java.nodes.JavaAccessDescriptor;
import com.github.nsc.de.shake.generators.java.nodes.JavaIdentifier;
import com.github.nsc.de.shake.generators.java.nodes.JavaNode;
import com.github.nsc.de.shake.generators.java.nodes.JavaValued;

public class JavaVariable implements JavaNode.JavaOperation {

    private final String identifier;
    private JavaVariableType type;
    private final boolean typeFixed;
    private final JavaValued assignment;
    private final boolean isStatic;
    private final boolean isFinal;
    private final JavaAccessDescriptor access;


    public JavaVariable(String identifier, JavaVariableType type, boolean typeFixed, JavaValued assignment,
                        boolean isStatic, boolean isFinal, JavaAccessDescriptor access) {
        this.identifier = identifier;
        this.type = type;
        this.typeFixed = typeFixed;
        this.assignment = assignment;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public JavaVariable(String identifier, JavaVariableType type, JavaValued assignment, boolean isStatic,
                        boolean isFinal, JavaAccessDescriptor access) {
        if(type == null) throw new Error("Variable-Type must not be null");
        this.identifier = identifier;
        this.type = type;
        this.typeFixed = false;
        this.assignment = assignment;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public JavaVariable(String identifier, JavaValued assignment, boolean isStatic, boolean isFinal,
                        JavaAccessDescriptor access) {
        this.identifier = identifier;
        this.type = JavaVariableType.UNKNOWN;
        this.typeFixed = false;
        this.assignment = assignment;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }


    public JavaVariable(String identifier, JavaVariableType type, boolean typeFixed, boolean isStatic, boolean isFinal,
                        JavaAccessDescriptor access) {
        this.identifier = identifier;
        this.type = type;
        this.typeFixed = typeFixed;
        this.assignment = null;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public JavaVariable(String identifier, JavaVariableType type, boolean isStatic, boolean isFinal,
                        JavaAccessDescriptor access) {
        if(type == null) throw new Error("Variable-Type must not be null");
        this.identifier = identifier;
        this.type = type;
        this.typeFixed = false;
        this.assignment = null;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public JavaVariable(String identifier, boolean isStatic, boolean isFinal, JavaAccessDescriptor access) {
        this.identifier = identifier;
        this.type = JavaVariableType.UNKNOWN;
        this.typeFixed = false;
        this.assignment = null;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.access = access;
    }

    public boolean expectToBe(JavaVariableType type) {
        if(type == null) throw new Error("Variable-Type must not be null");
        if(this.type == JavaVariableType.UNKNOWN) {
            this.type = type;
            return true;
        }
        if(type.is(this.type)) return true;
        else if(this.typeFixed) return false;
        JavaVariableType t = JavaVariableType.findNearestEqualParent(this.type, type);
        if(t == null) return false;
        this.type = t;
        return true;
    }

    public void checkJavaArgumentAble() {
        if(this.isFinal || this.isFinal || this.access != JavaAccessDescriptor.PACKAGE || this.assignment != null)
            throw new Error("This variable declaration can't be an argument");
    }

    public String getIdentifier() {
        return identifier;
    }

    public JavaVariableType getType() {
        return type;
    }

    public void setType(JavaVariableType type) {
        if(!this.typeFixed) throw new Error("Variable-Type is fixed");
        this.type = type;
    }

    public JavaVariableAccessDescriptor access(JavaIdentifier identifier) {
        return new JavaVariableAccessDescriptor(identifier);
    }

    public static JavaVariable createJavaArgument(String identifier, JavaVariableType type, boolean typeFixed) {
        return new JavaVariable(identifier, type, typeFixed, null, false, false, JavaAccessDescriptor.PACKAGE);
    }

    /**
     * Generates Java code
     *
     * @param indent the space the code should be indented with
     * @param add    the space to be added to indent per indention
     * @return the generated java code
     */
    @Override
    public String toString(String indent, String add) {
        return access.toString() + (isStatic ? "static " : "") + (isFinal ? "final " : "")
                + type.toString() + ' ' + identifier +
                (assignment != null ? " = " + this.assignment.toString(indent, add) : "");
    }

    public class JavaVariableAccessDescriptor implements JavaValued {

        private final JavaIdentifier accessIdentifier;

        public JavaVariableAccessDescriptor(JavaIdentifier accessIdentifier) {
            this.accessIdentifier = accessIdentifier;
        }

        /**
         * Generates Java code
         *
         * @param indent the space the code should be indented with
         * @param add    the space to be added to indent per indention
         * @return the generated java code
         */
        @Override
        public String toString(String indent, String add) {
            return accessIdentifier.toString(indent, add);
        }

        @Override
        public JavaVariableType getType() {
            return JavaVariable.this.getType();
        }

        @Override
        public boolean expectToBe(JavaVariableType type) {
            return JavaVariable.this.expectToBe(type);
        }
    }
}

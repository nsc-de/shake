package com.github.nsc.de.shake.generators.java;

public class JavaVariable {


    private final String identifier;
    private JavaVariableType type;
    private final boolean typeFixed;


    public JavaVariable(String identifier, JavaVariableType type, boolean typeFixed) {
        this.identifier = identifier;
        this.type = type;
        this.typeFixed = typeFixed;
    }

    public JavaVariable(String identifier, JavaVariableType type) {
        if(type == null) throw new Error("Variable-Type must not be null");
        this.identifier = identifier;
        this.type = type;
        this.typeFixed = false;
    }

    public JavaVariable(String identifier) {
        this.identifier = identifier;
        this.type = JavaVariableType.UNKNOWN;
        this.typeFixed = false;
    }

    public boolean expectVariableToBe(JavaVariableType type) {
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
}

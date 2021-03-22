package com.github.nsc.de.shake.generators.java;

import com.github.nsc.de.shake.generators.java.nodes.JavaClass;

public class JavaGenerationContext {

    private final JavaClass actualClass;
    private final boolean isInRoot;
    private final JavaGenerationContextVariableMap map;

    public JavaGenerationContext(JavaClass actualClass, boolean isInRoot, JavaGenerationContextVariableMap map) {
        this.actualClass = actualClass;
        this.isInRoot = isInRoot;
        this.map = map;
    }

    public JavaGenerationContext(JavaClass actualClass, boolean isInRoot) {
        this.actualClass = actualClass;
        this.isInRoot = isInRoot;
        this.map = new JavaGenerationContextVariableMap();
    }

    public JavaClass getActualClass() {
        return actualClass;
    }

    public boolean isInRoot() {
        return isInRoot;
    }

    public JavaGenerationContextVariableMap getMap() {
        return map;
    }
}

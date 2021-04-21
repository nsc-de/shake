package com.github.nsc.de.shake.generators.java;

import com.github.nsc.de.shake.parser.node.VariableType;

public class JavaVariableType {

    public static final JavaVariableType OBJECT =
            new JavaVariableType("Object", new JavaVariableType[0]);

    public static final JavaVariableType STRING =
            new JavaVariableType("String", new JavaVariableType[] { OBJECT });

    // Object Primitives
    public static final JavaVariableType DOUBLE_OBJ
            = new JavaVariableType("Double", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType FLOAT_OBJ
            = new JavaVariableType("Float", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType LONG_OBJ
            = new JavaVariableType("Long", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType INTEGER_OBJ
            = new JavaVariableType("Integer", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType SHORT_OBJ
            = new JavaVariableType("Short", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType BYTE_OBJ
            = new JavaVariableType("Byte", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType CHARACTER_OBJ
            = new JavaVariableType("Character", new JavaVariableType[] { OBJECT });
    public static final JavaVariableType BOOLEAN_OBJ
            = new JavaVariableType("Boolean", new JavaVariableType[] { OBJECT });


    // Primitives
    public static final JavaVariableType DOUBLE =
            new JavaVariableType("double", new JavaVariableType[0]);
    public static final JavaVariableType FLOAT
            = new JavaVariableType("float", new JavaVariableType[] { DOUBLE });
    public static final JavaVariableType LONG
            = new JavaVariableType("long", new JavaVariableType[] { FLOAT });
    public static final JavaVariableType INT
            = new JavaVariableType("int", new JavaVariableType[] { LONG });
    public static final JavaVariableType SHORT
            = new JavaVariableType("short", new JavaVariableType[] { INT });
    public static final JavaVariableType BYTE
            = new JavaVariableType("byte", new JavaVariableType[] { SHORT });
    public static final JavaVariableType CHAR
            = new JavaVariableType("char", new JavaVariableType[0]);
    public static final JavaVariableType BOOLEAN
            = new JavaVariableType("boolean", new JavaVariableType[0]);

    public static final JavaVariableType VOID
            = new JavaVariableType("void", new JavaVariableType[0]);

    // Unknown
    public static final JavaVariableType UNKNOWN = new JavaVariableType("Object", new JavaVariableType[0]) {
        int i = 1;
    };


    private final String name;
    private final JavaVariableType[] directParents;

    public JavaVariableType(String name, JavaVariableType[] directParents) {
        this.name = name;
        this.directParents = directParents;
    }

    public static JavaVariableType from(VariableType type, JavaGenerator gen, JavaGenerationContext ctx) {

        switch (type.getType()) {
            case BYTE: return BYTE;
            case SHORT: return SHORT;
            case INTEGER: return INT;
            case LONG: return LONG;
            case FLOAT: return FLOAT;
            case DOUBLE: return DOUBLE;
            case BOOLEAN: return BOOLEAN;
            case CHAR: return CHAR;
            case OBJECT: // return new JavaVariableType(gen.visitIdentifierNode(type.getSubtype(), ctx));
            case ARRAY: throw new Error("Not implemented yet!"); // TODO
            case DYNAMIC: return UNKNOWN;
        }

        throw new Error();

    }

    public JavaVariableType[] getDirectParents() {
        return directParents;
    }

    public boolean is(JavaVariableType type) {
        if(type == this) return true;
        for(JavaVariableType subtype : this.getDirectParents()) {
            if(subtype.is(type)) return true;
        }
        return false;
    }

    public int isLayer(JavaVariableType type) {
        if(type == this) return 0;
        int found = -1;
        for(JavaVariableType subtype : this.getDirectParents()) {
            int actualFoundLayer = isLayer(subtype);
            if(actualFoundLayer != -1 && actualFoundLayer < found) found = actualFoundLayer;
        }
        return found;
    }

    public static JavaVariableType findNearestEqualParent(JavaVariableType t1,
                                                          JavaVariableType t2) {
        if(t1.is(t2)) return t2;
        SubLevelFindNearestEqualParentReturn ret = subLevelFindNearestEqualParent(t1, t2);
        return ret != null ? ret.type : null;

    }

    private static SubLevelFindNearestEqualParentReturn subLevelFindNearestEqualParent(JavaVariableType t1,
                                                                                       JavaVariableType t2) {
        if(t2.is(t1)) return new SubLevelFindNearestEqualParentReturn(0, t1);

        SubLevelFindNearestEqualParentReturn ret = null;
        for(JavaVariableType type : t2.getDirectParents()) {

            SubLevelFindNearestEqualParentReturn next = subLevelFindNearestEqualParent(t1, type);
            if(next != null) {
                if(ret == null || next.level + 1 < ret.level) {
                    next.level++;
                    ret = next;
                }
            }
        }

        return ret;
    }

    private static class SubLevelFindNearestEqualParentReturn {

        private int level;
        private final JavaVariableType type;

        private SubLevelFindNearestEqualParentReturn(int level, JavaVariableType type) {
            this.level = level;
            this.type = type;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}

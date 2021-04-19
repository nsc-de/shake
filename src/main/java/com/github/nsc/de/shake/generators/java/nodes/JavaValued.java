package com.github.nsc.de.shake.generators.java.nodes;

import com.github.nsc.de.shake.generators.java.JavaVariableType;

public interface JavaValued extends JavaNode {

    class JavaExpression implements JavaValued {

        private final JavaValued left;
        private final JavaValued right;
        private final String operator;

        public JavaExpression(JavaValued left, JavaValued right, String operator) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        @Override
        public String toString(String indent, String add) {
            return left.toString(indent, add) + ' ' + operator + ' ' + right.toString(indent, add);
        }

        @Override
        public JavaVariableType getType() {
            // TODO Check
            return JavaVariableType.findNearestEqualParent(left.getType(), right.getType());
        }
    }

    class JavaIntegerPart implements JavaValued {
        private final int value;

        public JavaIntegerPart(int value) {
            this.value = value;
        }

        public int getNumber() {
            return this.value;
        }

        @Override
        public String toString(String indent, String add) {
            return String.valueOf(this.value);
        }


        @Override
        public JavaVariableType getType() {
            return JavaVariableType.INT;
        }
    }

    class JavaDoublePart implements JavaValued {
        private final double value;

        public JavaDoublePart(double value) {
            this.value = value;
        }

        @Override
        public String toString(String indent, String add) {
            return String.valueOf(this.value);
        }

        public double getNumber() {
            return this.value;
        }


        @Override
        public JavaVariableType getType() {
            return JavaVariableType.DOUBLE;
        }
    }

    class JavaCharacterPart implements JavaValued {
        private final char value;

        public JavaCharacterPart(char value) {
            this.value = value;
        }

        @Override
        public String toString(String indent, String add) {
            return "'" + this.value + '\'';
        }

        public double getValue() {
            return this.value;
        }

        @Override
        public JavaVariableType getType() {
            return JavaVariableType.CHAR;
        }
    }

    class JavaStringPart implements JavaValued {
        private final String value;

        public JavaStringPart(String value) {
            this.value = value;
        }

        @Override
        public String toString(String indent, String add) {
            return "\"" + this.value + '"';
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public JavaVariableType getType() {
            return JavaVariableType.CHAR;
        }
    }

    enum JavaBooleanValue implements JavaValued {
        TRUE(true), FALSE(false);

        private final boolean value;

        JavaBooleanValue(boolean value) {
            this.value = value;
        }

        @Override
        public String toString(String indent, String add) {
            return String.valueOf(this.value);
        }


        @Override
        public JavaVariableType getType() {
            return JavaVariableType.BOOLEAN;
        }

        public static JavaBooleanValue of(boolean b) {
            if(b) return TRUE;
            return FALSE;
        }
    }

    class JavaPriorityPart implements JavaValued {

        private final JavaValued operation;

        public JavaPriorityPart(JavaValued operation) {
            this.operation = operation;
        }

        @Override
        public String toString(String indent, String add) {
            return '(' + operation.toString() + ')';
        }

        @Override
        public JavaVariableType getType() {
            return this.operation.getType();
        }
    }

    class JavaFunctionCall implements JavaValuedOperation {

        private final JavaIdentifier function;
        private final JavaValued[] args;

        public JavaFunctionCall(JavaIdentifier function, JavaValued[] args) {
            this.function = function;
            this.args = args;
        }

        @Override
        public String toString(String indent, String add) {
            StringBuilder str = new StringBuilder();
            str.append(this.function.toString(indent, add)).append('(');
            for(int i = 0; i < args.length; i++) {
                str.append(args[i].toString(indent, add));
                if(i < args.length - 1) str.append(", ");
            }
            return str.append(")").toString();
        }

        @Override
        public JavaVariableType getType() {
            // TODO automatically track type
            return JavaVariableType.UNKNOWN;
        }
    }

    class JavaConstruction implements JavaValuedOperation {

        private final JavaIdentifier function;
        private final JavaValued[] args;

        public JavaConstruction(JavaIdentifier function, JavaValued[] args) {
            this.function = function;
            this.args = args;
        }

        @Override
        public String toString(String indent, String add) {
            StringBuilder str = new StringBuilder("new ").append(this.function.toString(indent, add)).append('(');
            for(int i = 0; i < args.length; i++) {
                str.append(args[i].toString(indent, add));
                if(i < args.length - 1) str.append(", ");
            }
            return str.append(")").toString();
        }

        @Override
        public JavaVariableType getType() {
            // TODO automatically track type
            return JavaVariableType.UNKNOWN;
        }
    }

    class JavaVariableAssignment implements JavaValuedOperation {

        private final JavaIdentifier name;
        private final JavaValued value;

        public JavaVariableAssignment(JavaIdentifier name, JavaValued value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString(String indent, String add) {
            return name.toString(indent, add) + " = " + value.toString(indent, add);
        }

        @Override
        public JavaVariableType getType() {
            return this.value.getType();
        }
    }

    class JavaVariableExpressionAssignment implements JavaValuedOperation {

        private final JavaIdentifier name;
        private final JavaValued value;
        private final char operator;

        public JavaVariableExpressionAssignment(JavaIdentifier name, JavaValued value, char operator) {
            this.name = name;
            this.value = value;
            this.operator = operator;
        }

        @Override
        public String toString(String indent, String add) {
            return name.toString(indent, add) + ' ' + this.operator + "= " + value.toString(indent, add);
        }

        @Override
        public JavaVariableType getType() {
            // TODO Automatically track type
            return JavaVariableType.UNKNOWN;
        }
    }

    class JavaVariableIncr implements JavaValuedOperation {

        private final JavaIdentifier variable;

        public JavaVariableIncr(JavaIdentifier variable) {
            this.variable = variable;
        }

        @Override
        public String toString(String indent, String add) {
            return this.variable.toString(indent, add) + "++";
        }

        @Override
        public JavaVariableType getType() {
            return variable.getType();
        }
    }

    class JavaVariableDecr implements JavaValuedOperation {

        private final JavaIdentifier variable;

        public JavaVariableDecr(JavaIdentifier variable) {
            this.variable = variable;
        }

        @Override
        public String toString(String indent, String add) {
            return this.variable.toString(indent, add) + "--";
        }

        @Override
        public JavaVariableType getType() {
            return variable.getType();
        }
    }

    interface JavaValuedOperation extends JavaOperation, JavaValued {  }

    JavaVariableType getType();
}

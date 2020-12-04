package com.github.nsc.de.compiler.interpreter.values;



/**
 * {@link InterpreterValue}s for booleans ({@link BooleanValue#TRUE} & {@link BooleanValue#FALSE}
 *
 * @author Nicolas Schmidt
 */
public enum BooleanValue implements InterpreterValue {


    // *******************************
    // Values

    /**
     * TRUE (true value for boolean)
     */
    TRUE(true),

    /**
     * FALSE (false value for boolean)
     */
    FALSE(false);



    // *******************************
    // statics

    /**
     * Create a {@link BooleanValue} from a boolean
     *
     * @param b the boolean to create the {@link BooleanValue}  from
     * @return the {@link BooleanValue} representation of the boolean
     *
     * @author Nicolas Schmidt
     */
    public static BooleanValue from(boolean b) {
        // if b ist true return TRUE
        // in other case return FALSE
        if(b) return TRUE;
        else return FALSE;
    }

    /**
     * Create a {@link BooleanValue} from a {@link InterpreterValue}
     *
     * @param v the {@link InterpreterValue} to create the {@link BooleanValue}  from
     * @return the {@link BooleanValue} representation of the boolean
     *
     * @author Nicolas Schmidt
     */
    public static BooleanValue from(InterpreterValue v) {
        // If v is a BooleanValue just return v
        if(v instanceof BooleanValue) return (BooleanValue) v;

        // if v is m IntegerValue return FALSE if v is <= 0
        if(v instanceof IntegerValue) {
            if(((IntegerValue) v).getValue() <= 0) return FALSE;
            return TRUE;
        }

        // if v is DoubleValue return FALSE if v is <= 0
        if(v instanceof DoubleValue) {
            if(((DoubleValue) v).getValue() <= 0) return FALSE;
            return TRUE;
        }

        // if v is a NullValue just return FALSE
        if(v instanceof NullValue) return FALSE;

        // if v is a Function just return TRUE
        if(v instanceof Function || v instanceof Class || v instanceof ObjectValue) return TRUE;
        throw new Error("Could not create boolean from " + v.getName());
    }



    // *******************************
    // boolean functionality

    /**
     * The value of the {@link BooleanValue}
     */
    private final boolean value;


    /**
     * Constructor for {@link BooleanValue}
     *
     * @param value the value of the boolean
     *
     * @author Nicolas Schmidt
     */
    BooleanValue(boolean value) {
        // just set the value field
        this.value = value;
    }

    /**
     * Returns the boolean representation of the {@link BooleanValue}
     *
     * @return the boolean representation of the {@link BooleanValue}
     *
     * @author Nicolas Schmidt
     */
    public boolean getValue() {
        // just return the value
        return value;
    }



    // *******************************
    // implementations for extended InterpreterValue

    /**
     * This function gets executed when the operator '||' is used on the value
     *
     * @param v The other value for the or operator
     * @return The Calculation-Result
     *
     * @author Nicolas Schmidt
     */
    @Override
    public InterpreterValue or(InterpreterValue v) {
        // if the given value is a BooleanValue check if at least one of the values is true and return a BooleanValue again
        // if not just throw an error
        if(v instanceof BooleanValue) {
            return BooleanValue.from(this.value || ((BooleanValue) v).value);
        }
        throw new Error("Operator '||' is not defined for type boolean and " + v.getName());
    }

    /**
     * This function gets executed when the operator '&amp;&amp;' is used on the value
     *
     * @param v The other value for the and operator
     * @return The Calculation-Result
     *
     * @author Nicolas Schmidt
     */
    @Override
    public InterpreterValue and(InterpreterValue v) {
        // if the given value is a BooleanValue check if the values are both true and return a BooleanValue again
        // if not just throw an error
        if(v instanceof BooleanValue) {
            return BooleanValue.from(this.value && ((BooleanValue) v).value);
        }
        throw new Error("Operator '&&' is not defined for type boolean and " + v.getName());
    }

    /**
     * This function gets executed when the operator '==' is used on the value
     *
     * @param v The value that should be the same
     * @return The Calculation-Result
     *
     * @author Nicolas Schmidt
     */
    @Override
    public InterpreterValue equals(InterpreterValue v) {
        // if the given value is a BooleanValue check if the values are the same and return a BooleanValue again
        // if not just throw an error
        if(v instanceof BooleanValue) {
            return BooleanValue.from(this.value == ((BooleanValue) v).value);
        }
        throw new Error("Operator '==' is not defined for type boolean and " + v.getName());
    }

    /**
     * Returns the name of the type of {@link InterpreterValue} (To identify the type of value)
     *
     * @return the name of the {@link InterpreterValue}
     *
     * @author Nicolas Schmidt
     */
    @Override
    public String getName() {
        // just return "boolean"
        return "boolean";
    }



    // *******************************
    // Override toString()

    /**
     * Returns the string representation of the {@link BooleanValue}
     *
     * @return the string representation of the {@link BooleanValue}
     *
     * @author Nicolas Schmidt
     */
    @Override
    public String toString() {
        // just return the string representation of this.value (either "true" or "false")
        return String.valueOf(this.value);
    }
}

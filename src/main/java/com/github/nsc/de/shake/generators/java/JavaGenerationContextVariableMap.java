package com.github.nsc.de.shake.generators.java;

import java.util.HashMap;
import java.util.Map;


/**
 * A list that contains variables
 *
 * @author <a href="https://github.com/nsc-de">Nicolas Schmidt &lt;@nsc-de&gt;</a>
 */
public class JavaGenerationContextVariableMap {

    private final Map<String, JavaGenerationContextVariable> variables;
    private final JavaGenerationContextVariableMap parentList;



    // *******************************
    // Constructors

    public JavaGenerationContextVariableMap(Map<String, JavaGenerationContextVariable> variables,
                                            JavaGenerationContextVariableMap parentList) {
        // apply values to fields
        this.variables = variables;
        this.parentList = parentList;
    }

    public JavaGenerationContextVariableMap(JavaGenerationContextVariableMap parentList) {
        // apply values to fields
        this.variables = new HashMap<>();
        this.parentList = parentList;
    }

    public JavaGenerationContextVariableMap(HashMap<String, JavaGenerationContextVariable> variables) {
        // apply given values to fields
        this.variables = variables;
        this.parentList = null;
    }

    public JavaGenerationContextVariableMap() {
        // apply values to fields
        this.variables = new HashMap<>();
        this.parentList = null;
    }



    // *******************************
    // getters

    public Map<String, JavaGenerationContextVariable> getVariables() {
        // just return the variables field
        return variables;
    }


    public JavaGenerationContextVariableMap getParentList() {
        // just return the parentList field
        return parentList;
    }



    // *******************************
    // VariableList functionality


    public boolean declare(JavaGenerationContextVariable v) {
        // Check if the variable-map already contains a Variable with this name (if so return false).
        // In other case put the variable into the map using the identifier as key and return true
        if (this.variables.containsKey(v.getIdentifier())) return false;
        this.variables.put(v.getIdentifier(), v);
        return true;
    }

    public JavaGenerationContextVariable get(String name) {

        // If the variable map contains the variable then return it.
        if(variables.containsKey(name)) return variables.get(name);

        // In other case if the VariableList has a parent-list try to get the Variable from the parentList.s
        else if (this.getParentList() != null) return this.getParentList().get(name);

        // In other case just return null (the variable is not declared)
        return null;

    }


    JavaGenerationContextVariableMap concat(JavaGenerationContextVariableMap list) {

        // Create a new HashMap from the variables of the VariableList
        HashMap<String, JavaGenerationContextVariable> variables = new HashMap<>(this.variables);

        // Loop over the given list and put the variables into the variables map
        list.getVariables().forEach(variables::put);

        // return the variable map
        return new JavaGenerationContextVariableMap(variables, list.getParentList());
    }

    public JavaGenerationContextVariableMap copy() {

        // Create a new HashMap for the variables
        Map<String, JavaGenerationContextVariable> vars = new HashMap<>();

        // Loop over the variables and put a copy of the variable into the vars Map
        this.variables.forEach((k, v) -> vars.put(k, v));

        // Return a new VariableList created using the created vars map
        return new JavaGenerationContextVariableMap(vars, this.parentList);

    }


    // *******************************
    // Override toString()

    /**
     * Returns the string representation of the {@link JavaGenerationContextVariableMap}
     *
     * @return the string representation of the {@link JavaGenerationContextVariableMap}
     *
     * @author <a href="https://github.com/nsc-de">Nicolas Schmidt &lt;@nsc-de&gt;</a>
     */
    @Override
    public String toString() {
        // just create a string out of the properties of the VariableList
        return String.format("{variables=%s,parentList=%s}", this.variables, this.parentList);
    }
}

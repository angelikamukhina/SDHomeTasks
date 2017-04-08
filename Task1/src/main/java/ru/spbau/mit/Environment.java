package ru.spbau.mit;

import java.util.HashMap;

/**
 * this class is wrapper for map which supports environment variables
 */

class Environment {

    /**
     * we save the instance of Environment class for future uses
     */
    private HashMap<String, String> variables = new HashMap<>();

    /**
     * Creates new environment variable
     *
     * @param key   name of new environment variable
     * @param value value of the variable
     */
    void setNewVariable(String key, String value) {
        variables.put(key, value);
    }

    /**
     * to get saved variable key
     *
     * @param var name of necessary variable
     * @return value of the variable
     */
    String getVariable(String var) {
        return variables.get(var);
    }
}

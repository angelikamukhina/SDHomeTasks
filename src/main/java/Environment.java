import java.util.HashMap;
//this singleton class is wrapper for map which supports environment variables
public class Environment {

    //we save the instance of Environment class for future uses
    private static Environment instance;

    //when the method is called for the first time, the instance of
    //the class is created, but when it is called lately, it gives the same instance
    public static Environment getInstance() {
        if(instance == null) {
            instance = new Environment();

        }
        return instance;
    }

    private HashMap<String, String> variables = new HashMap<>();

    //to create new environment variable
    void setNewVariable(String key, String value) {
        variables.put(key, value);
    }

    //to get saved variable key
    String getVariable(String var) {
        return variables.get(var);
    }
}

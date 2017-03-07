import java.util.HashMap;

public class Environment {
    private static Environment instance;

    public static Environment getInstance() {
        if(instance == null) {
            instance = new Environment();

        }
        return instance;
    }

    private HashMap<String, String> variables = new HashMap<>();
    public void setNewVariable(String key, String value) {
        variables.put(key, value);
    }

    public String getVariable(String key) {
        return variables.get(key);
    }
}

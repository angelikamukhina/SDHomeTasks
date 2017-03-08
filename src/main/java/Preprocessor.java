//class singleton for replacing "$var" to var's value in environment
// inside double quotes
public class Preprocessor {
    private static Preprocessor instance;


    public static Preprocessor getInstance() {
        if(instance == null) {
            instance = new Preprocessor();
        }
        return instance;
    }

    //replaces all $var in line to their values
    public String preprocess(String line) {
        boolean flag = false;
        boolean isPrevSingleQuote = false;
        String internalLine = "";
        String variable = "";
        for(int i = 0; i < line.length(); i++) {
            char currChar = line.charAt(i);
            if((currChar == '\'')) {
                if(isPrevSingleQuote) {
                    isPrevSingleQuote = false;
                }
                else {
                    isPrevSingleQuote = true;
                }
            }
            if((currChar == '$') && !isPrevSingleQuote) {
                variable = "";
                flag = true;
                continue;
            }
            if(((currChar == ' ') || (currChar == '\"')) && flag) {
                String value = valueOfKey(variable);
                if(value != null) {
                    internalLine += value;
                    flag = false;
                }
                else {
                    return internalLine;
                }
            }

            if(flag) {
                variable += line.charAt(i);
                continue;
            }
            internalLine += line.charAt(i);
        }
        if(flag) {
            String value = valueOfKey(variable);
            if(value != null) {
                internalLine += value;
            }
        }
        return internalLine;
    }

    private String valueOfKey(String key) {
        Environment env = Environment.getInstance();
        return env.getVariable(key);
    }

}

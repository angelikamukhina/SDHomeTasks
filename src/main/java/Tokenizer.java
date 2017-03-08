import java.util.Vector;

public class Tokenizer {

    private static Tokenizer instance;


    public static Tokenizer getInstance() {
        if(instance == null) {
            instance = new Tokenizer();
        }
        return instance;
    }

    //the method splits line on tokens: string (it can contain spaces),
    // pipes ("|") and commands
    public Vector<String> tokenize(String line) {
        String[] words = line.split(" ");
        String token = "";
        boolean flag = false;
        Vector<String> tokens = new Vector<>();
        for (String currWord : words) {
            int len = currWord.length();
            char firstSymbol = currWord.charAt(0);
            char lastSymbol = currWord.charAt(len - 1);

            if ((firstSymbol == '\"') && (lastSymbol != '\"') ||
                    (firstSymbol == '\'') && (lastSymbol != '\'')) {
                flag = true;
                token = currWord.substring(1);
                continue;
            }

            if ((firstSymbol == '\"') && (lastSymbol == '\"') ||
                    (firstSymbol == '\'') && (lastSymbol == '\'')) {
                flag = false;
                tokens.addElement(currWord.substring(1, len - 1));
                continue;
            }

            if (flag && ((lastSymbol == '\"') || (lastSymbol == '\''))) {
                token += " " + currWord.substring(0, len - 1);
                tokens.addElement(token);
                flag = false;
                continue;
            }

            if (flag) {
                token += " " + currWord;
                continue;
            }
            else {
                int indexOfPipe = -1;
                int amountOfPipes = 0;
                Vector<String> tokensBetweenPipes = new Vector<>();
                for(int i = 0; i < len; i++) {
                    if(currWord.charAt(i) == '|') {
                        if(i != 0) {
                            tokensBetweenPipes.addElement(currWord.substring(indexOfPipe + 1, i));
                        }
                        tokensBetweenPipes.addElement("|");
                        indexOfPipe = i;
                        amountOfPipes++;
                    }
                }

                if(indexOfPipe == -1) {
                    tokensBetweenPipes.addElement(currWord);
                }
                else if((amountOfPipes != 0) && (indexOfPipe != len -1)) {
                    tokensBetweenPipes.addElement(currWord.substring(indexOfPipe + 1, len));
                }

                for(String cmd : tokensBetweenPipes) {
                    if(cmd.equals("|")) {
                        tokens.addElement(cmd);
                        continue;
                    }
                    Vector<String> argsOfAssignment = parseAssignment(cmd);

                    if(argsOfAssignment == null) {
                        tokens.addElement(cmd);
                    }
                    else {
                        tokens.addElement("=");
                        tokens.addAll(argsOfAssignment);
                    }
                }
            }
        }

        return tokens;
    }

    private Vector<String> parseAssignment(String command) {
        Vector<String> answer = null;
        for(int i = 0; i < command.length(); i++) {
            char currChar = command.charAt(i);
            if(currChar == '=') {
                answer = new Vector<>();
                answer.addElement(command.substring(0, i));
                answer.addElement(command.substring(i+1, command.length()));
            }
        }
        return answer;

    }
 }

package ru.spbau.mit;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Object of this class convert input preprocessed
 * line to list of tokens - commands and their arguments.
 */
class Tokenizer {

    /**
     * The method splits line into tokens: string (it can contain spaces),
     * pipes ("|") and commands
     *
     * @param line preprocessed line
     * @return tokens
     */
    public List<String> tokenize(String line) {
        if (line.isEmpty()) return Collections.emptyList();
        String[] words = line.split(" ");
        String token = "";
        boolean isPrevQuote = false;
        Vector<String> tokens = new Vector<>();
        for (String currWord : words) {
            int len = currWord.length();
            char firstSymbol = currWord.charAt(0);
            char lastSymbol = currWord.charAt(len - 1);

            if ((firstSymbol == '\"') && (lastSymbol != '\"') ||
                    (firstSymbol == '\'') && (lastSymbol != '\'')) {
                isPrevQuote = true;
                token = currWord.substring(1);
                continue;
            }

            if ((firstSymbol == '\"') && (lastSymbol == '\"') ||
                    (firstSymbol == '\'') && (lastSymbol == '\'')) {
                isPrevQuote = false;
                tokens.addElement(currWord.substring(1, len - 1));
                continue;
            }

            if (isPrevQuote && ((lastSymbol == '\"') || (lastSymbol == '\''))) {
                token += " " + currWord.substring(0, len - 1);
                tokens.addElement(token);
                isPrevQuote = false;
                continue;
            }

            if (isPrevQuote) {
                token += " " + currWord;
            } else {
                int indexOfPipe = -1;
                int amountOfPipes = 0;
                Vector<String> tokensBetweenPipes = new Vector<>();
                for (int i = 0; i < len; i++) {
                    if (currWord.charAt(i) == '|') {
                        if (i != 0) {
                            tokensBetweenPipes.addElement(currWord.substring(indexOfPipe + 1, i));
                        }
                        tokensBetweenPipes.addElement("|");
                        indexOfPipe = i;
                        amountOfPipes++;
                    }
                }

                if (indexOfPipe == -1) {
                    tokensBetweenPipes.addElement(currWord);
                } else if ((amountOfPipes != 0) && (indexOfPipe != len - 1)) {
                    tokensBetweenPipes.addElement(currWord.substring(indexOfPipe + 1, len));
                }

                for (String cmd : tokensBetweenPipes) {
                    if (cmd.equals("|")) {
                        tokens.addElement(cmd);
                        continue;
                    }
                    Vector<String> argsOfAssignment = parseAssignment(cmd);

                    if (argsOfAssignment == null) {
                        tokens.addElement(cmd);
                    } else {
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
        for (int i = 0; i < command.length(); i++) {
            char currChar = command.charAt(i);
            if (currChar == '=') {
                answer = new Vector<>();
                answer.addElement(command.substring(0, i));
                answer.addElement(command.substring(i + 1, command.length()));
            }
        }
        return answer;
    }
}

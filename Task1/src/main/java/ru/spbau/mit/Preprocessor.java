package ru.spbau.mit;

/**
 * class singleton for replacing "$var" to var's value in environment
 * inside double quotes
 */

public class Preprocessor {

    /**
     * Replaces all $var in line to their values
     *
     * @param line user's line with '$'
     * @return string without '$', but with corresponding values
     */
    public String preprocess(String line, Environment env) {
        boolean isPrev$ = false;
        boolean isPrevSingleQuote = false;
        String internalLine = "";
        String variable = "";
        for (int i = 0; i < line.length(); i++) {
            char currChar = line.charAt(i);
            if ((currChar == '\'')) {
                isPrevSingleQuote = !isPrevSingleQuote;
            }
            if ((currChar == '$') && !isPrevSingleQuote) {
                variable = "";
                isPrev$ = true;
                continue;
            }
            if (((currChar == ' ') || (currChar == '\"')) && isPrev$) {
                String value = env.getVariable(variable);
                if (value != null) {
                    internalLine += value;
                    isPrev$ = false;
                } else {
                    return internalLine;
                }
            }

            if (isPrev$) {
                variable += line.charAt(i);
                continue;
            }
            internalLine += line.charAt(i);
        }
        if (isPrev$) {
            String value = env.getVariable(variable);
            if (value != null) {
                internalLine += value;
            }
        }
        return internalLine;
    }
}

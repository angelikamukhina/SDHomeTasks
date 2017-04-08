package ru.spbau.mit;

import java.util.List;

/**
 * Singleton class for execution of commands chain
 */

public class Executor {

    /**
     * Successively execute commands from tokens. If command is located in head, it's
     * arguments are next two or one (according to command) token from tokens
     *
     * @param tokens input vector of tokens
     */

    public void execute(List<String> tokens, Environment env, Stream stream) {
        Executable currCommand;
        boolean isPrevPipe = false;
        int j = 0;
        while (j != tokens.size()) {

            String currToken = tokens.get(j);

            if (currToken.equals("|")) {
                isPrevPipe = true;
                j++;
                continue;
            }

            if (Executable.possibleCmds.contains(currToken)) {
                currCommand = new Executable(currToken);

                j++; // shift to arguments

                while (!(j >= tokens.size()) && !tokens.get(j).equals("|")) {
                    stream.addToStream(tokens.get(j));
                    j++;
                }
                currCommand.execute(env, stream, isPrevPipe);
            } else {
                Runtime runtime = Runtime.getRuntime();
                Process prc = null;
                String cmd[] = {currToken};
                try {
                    prc = runtime.exec(cmd);
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


package ru.spbau.mit;

import java.util.List;
import java.util.Scanner;

/**
 * The main class of the shell. It reads user's commands,
 * calls preprocessor, tokenizer, executor.
 */

public class Shell {
    public static void main(String[] args) {
        Environment environment = new Environment();
        Preprocessor preprocessor = new Preprocessor();
        Tokenizer tokenizer = new Tokenizer();
        Executor executor = new Executor();
        Stream stream = new Stream();

        while (true) {
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            if (line.equals("exit")) {
                break;
            }

            line = preprocessor.preprocess(line.trim(), environment);

            List<String> tokens = tokenizer.tokenize(line);

            executor.execute(tokens, environment, stream);

            if (stream.getStream().isEmpty()) {
                continue;
            }

            for (String chunk : stream.getStream()) {
                System.out.println(chunk);
            }
            stream.clearStream();
        }
    }
}

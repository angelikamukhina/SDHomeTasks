import ru.spbau.mit.Stream;

import java.util.List;
import java.util.Scanner;

/**
 * The main class of the shell. It reads users commands, calls preprocessor for replacement
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
            String line;
            Scanner in = new Scanner(System.in);
            line = in.nextLine();

            line = preprocessor.preprocess(line.trim(), environment);

            List<String> tokens = tokenizer.tokenize(line);

            executor.execute(tokens, environment, stream);

            if (stream.getStream().isEmpty()) {
                continue;
            }

            System.out.println(stream.getStream().get(0));
            stream.clearStream();
        }
    }
}

import java.util.Scanner;
import java.util.Vector;

public class Shell {
    public static void main(String [] args) {

        boolean status = true;

        while(status) {
            String line;
            Scanner in = new Scanner(System.in);
            line = in.nextLine();

            Preprocessor preprocessor = Preprocessor.getInstance();
            line = preprocessor.preprocess(line);

            Tokenizer tokenizer = Tokenizer.getInstance();
            Vector<String> tokens = tokenizer.tokenize(line);

            Executor executor = Executor.getInstance();
            executor.execute(tokens);

            Stream stream = Stream.getInstance();
            if(stream.getStream().isEmpty()) {
                continue;
            }

            if(stream.getStream().elementAt(0).equals("EXIT!!!")) {
                status = false;
                continue;
            }

            System.out.println(stream.getStream().elementAt(0));
            stream.clearStream();

        }
    }
}

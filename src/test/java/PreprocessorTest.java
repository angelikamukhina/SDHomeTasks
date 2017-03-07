import org.junit.Test;

import static org.junit.Assert.*;

public class PreprocessorTest {
    @Test
    public void preprocess() throws Exception {
        Preprocessor pr = Preprocessor.getInstance();
        Environment env = Environment.getInstance();
        env.setNewVariable("FILE", "example");
        String answer = pr.preprocess("cat $FILE");
        assertEquals("cat example", answer);
        env.setNewVariable("g", "9");
        answer = pr.preprocess("echo $g");
        assertEquals("echo 9", answer);
    }

}
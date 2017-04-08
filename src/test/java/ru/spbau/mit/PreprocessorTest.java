import org.junit.Test;

import static org.junit.Assert.*;

public class PreprocessorTest {
    @Test
    public void preprocess() throws Exception {
        Preprocessor pr = new Preprocessor();
        Environment env = new Environment();
        env.setNewVariable("FILE", "example");
        String answer = pr.preprocess("cat $FILE", env);
        assertEquals("cat example", answer);
        env.setNewVariable("g", "9");
        answer = pr.preprocess("echo $g", env);
        assertEquals("echo 9", answer);
    }

}
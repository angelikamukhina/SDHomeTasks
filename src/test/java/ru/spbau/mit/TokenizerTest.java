import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;


public class TokenizerTest {
    @Test
    public void tokenize() throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        List<String> tokens = tokenizer.tokenize("some string");
        assertTrue(tokens.get(0).equals("some"));
        assertTrue(tokens.get(1).equals("string"));

        tokens = tokenizer.tokenize("x=8");
        assertEquals(3, tokens.size());
        assertEquals("=", tokens.get(0));

        tokens = tokenizer.tokenize("cat file | wc");
        assertEquals(4, tokens.size());
        assertEquals("cat", tokens.get(0));
        assertEquals("wc", tokens.get(tokens.size()-1));
    }

}
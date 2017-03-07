import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;


public class TokenizerTest {
    @Test
    public void getInstance() throws Exception {
        Tokenizer tokenizer = Tokenizer.getInstance();
        Tokenizer otherTokenizer = Tokenizer.getInstance();
        assertTrue(tokenizer == otherTokenizer);
    }

    @Test
    public void tokenize() throws Exception {
        Tokenizer tokenizer = Tokenizer.getInstance();
        Vector<String> tokens = tokenizer.tokenize("some string");
        assertTrue(tokens.elementAt(0).equals("some"));
        assertTrue(tokens.elementAt(1).equals("string"));

        tokens = tokenizer.tokenize("x=8");
        assertEquals(3, tokens.size());
        assertEquals("=", tokens.elementAt(0));

        tokens = tokenizer.tokenize("cat file | wc");
        assertEquals(4, tokens.size());
        assertEquals("cat", tokens.firstElement());
        assertEquals("wc", tokens.lastElement());
    }

}
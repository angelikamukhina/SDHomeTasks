import org.junit.Test;
import ru.spbau.mit.Stream;

import java.util.Vector;

import static org.junit.Assert.*;

public class ExecutorTest {
    @Test
    public void execute() throws Exception {
        Executor ex = new Executor();
        Vector<String> toks = new Vector<>();
        Stream stream = new Stream();
        Environment env = new Environment();
        stream.clearStream();
        toks.addElement("echo");
        toks.addElement("123");
        ex.execute(toks, env, stream);
        assertEquals("123", stream.getStream().get(0));
        stream.clearStream();

        toks.addElement("|");
        toks.addElement("wc");
        ex.execute(toks,env, stream);

        assertEquals("1 1 3", stream.getStream().get(0));

    }

}
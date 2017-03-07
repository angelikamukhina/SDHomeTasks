import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class ExecutorTest {
    @Test
    public void execute() throws Exception {
        Executor ex = Executor.getInstance();
        Vector<String> toks = new Vector<>();
        Stream stream = Stream.getInstance();
        stream.clearStream();
        toks.addElement("echo");
        toks.addElement("123");
        ex.execute(toks);
        assertEquals("123", stream.getStream().firstElement());
        stream.clearStream();

        toks.addElement("|");
        toks.addElement("wc");
        ex.execute(toks);

        assertEquals("1 1 3", stream.getStream().firstElement());

    }

}
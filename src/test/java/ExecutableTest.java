import org.junit.Test;

import static org.junit.Assert.*;


public class ExecutableTest {

    @Test
    public void execute() throws Exception {
        Stream stream = Stream.getInstance();
        stream.setStream("Hello");
        Executable exec = new Executable("echo");
        exec.execute(false);
        assertEquals("Hello", stream.getStream().firstElement());
        stream.setStream("123");

        exec = new Executable("wc");
        exec.execute(true);
        assertEquals("1 1 3", stream.getStream().firstElement());
    }

}
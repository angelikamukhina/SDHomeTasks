import org.junit.Test;

import static org.junit.Assert.*;

public class StreamTest {
    @Test
    public void size() throws Exception {
        Stream stream = Stream.getInstance();
        stream.addToStream("Hello ");
        stream.addToStream("World!");
        assertEquals(2, stream.size());

    }

    @Test
    public void setStream() throws Exception {
        Stream stream = Stream.getInstance();
        stream.addToStream("Hello");
        stream.addToStream("World");
        stream.setStream("Hello");
        assertEquals(1, stream.size());
    }

    @Test
    public void addToStream() throws Exception {
        Stream stream = Stream.getInstance();
        stream.clearStream();
        stream.addToStream("Hello");
        assertEquals("Hello", stream.getStream().firstElement());
    }

    @Test
    public void addToStreamElement() throws Exception {
        Stream stream = Stream.getInstance();
        stream.clearStream();
        stream.addToStream("Hello");
        stream.addToStreamElement(0, " World");
        assertEquals("Hello World", stream.getStream().elementAt(0));
    }


    @Test
    public void clearStream() throws Exception {
        Stream stream = Stream.getInstance();
        stream.addToStream("Hello");
        stream.clearStream();
        assertEquals(0, stream.size());
    }

}
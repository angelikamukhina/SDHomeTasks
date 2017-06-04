package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExecutableTest {

    @Test
    public void executeEchoWithOneArgumentTest() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable exec = new Executable("echo");
        stream.setStream("Hello");
        exec.execute(env, stream, false);
        assertEquals("Hello", stream.getStream().get(0));
    }

    @Test
    public void executeWcOnStringFromConsoleTest() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable exec = new Executable("wc");
        stream.setStream("123");
        exec.execute(env, stream, true);
        assertEquals("1 1 3", stream.getStream().get(0));
    }

    @Test
    public void executeGrepAfterPipeWithIKey() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable cat = new Executable("cat");
        stream.setStream("test.txt");
        boolean afterPipe = false;
        cat.execute(env, stream, afterPipe);
        Executable grep = new Executable("grep");
        stream.addToStream("-i");
        stream.addToStream("Just");
        afterPipe = true;
        grep.execute(env, stream, afterPipe);
        assertEquals("This is just test file", stream.getResult());
    }

    @Test
    public void executeGrepAfterPipeWithWKeyOnWholeWord() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable cat = new Executable("cat");
        stream.setStream("test.txt");
        boolean afterPipe = false;
        cat.execute(env, stream, afterPipe);
        Executable grep = new Executable("grep");
        stream.addToStream("-w");
        stream.addToStream("just");
        afterPipe = true;
        grep.execute(env, stream, afterPipe);
        assertEquals("This is just test file", stream.getResult());
    }

    @Test
    public void executeGrepAfterPipeWithWKeyOnPartWord() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable cat = new Executable("cat");
        stream.setStream("test.txt");
        boolean afterPipe = false;
        cat.execute(env, stream, afterPipe);
        Executable grep = new Executable("grep");
        stream.addToStream("-w");
        stream.addToStream("ju");
        afterPipe = true;
        grep.execute(env, stream, afterPipe);
        assertEquals(0, stream.size());
    }

    @Test
    public void executeGrepAfterPipeWithAKeyOnPartWord() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable cat = new Executable("cat");
        stream.setStream("test.txt");
        boolean afterPipe = false;
        cat.execute(env, stream, afterPipe);
        Executable grep = new Executable("grep");
        stream.addToStream("-A");
        stream.addToStream("1");
        stream.addToStream("ju");
        afterPipe = true;
        grep.execute(env, stream, afterPipe);
        assertEquals("This is just test file\naaaaaaaaaaaaaa", stream.getResult());
    }

    @Test
    public void executeGrepWithIKey() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        stream.setStream("-i");
        stream.addToStream("Just");
        stream.addToStream("test.txt");
        Executable grep = new Executable("grep");
        boolean afterPipe = false;
        grep.execute(env, stream, afterPipe);
        assertEquals("This is just test file", stream.getResult());
    }
}
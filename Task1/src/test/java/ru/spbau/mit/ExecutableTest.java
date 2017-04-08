package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExecutableTest {

    @Test
    public void executeEcho() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable exec = new Executable("echo");
        stream.setStream("Hello");
        exec.execute(env, stream, false);
        assertEquals("Hello", stream.getStream().get(0));
    }

    @Test
    public void executeWc() throws Exception {
        Stream stream = new Stream();
        Environment env = new Environment();
        Executable exec = new Executable("wc");
        stream.setStream("123");
        exec.execute(env, stream, true);
        assertEquals("1 1 3", stream.getStream().get(0));
    }
}
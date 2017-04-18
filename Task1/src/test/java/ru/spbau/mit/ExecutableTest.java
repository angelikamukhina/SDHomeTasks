package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExecutableTest {
    private static Stream stream = new Stream();
    private static Environment env = new Environment();

    private static final ArrayList<String> EXPECTED_OUTPUT_LS =
            new ArrayList<>(Arrays.asList(
            "./src/test/resources/LsTest",
            "./src/test/resources/LsTest/ipsum.txt",
            "./src/test/resources/LsTest/lorem.sh",
            "./src/test/resources/LsTest/test1.txt"
    ));

    @Test
    public void executeEcho() throws Exception {
        Executable exec = new Executable("echo");
        stream.setStream("Hello");
        exec.execute(env, stream);
        assertEquals("Hello", stream.getStream().get(0));
    }

    @Test
    public void executeWc() throws Exception {
        Executable exec = new Executable("wc");
        stream.setStream("123");
        exec.execute(env, stream, true);
        assertEquals("1 1 3", stream.getStream().get(0));
    }

    @Test
    public void executeLs() {
        Executable exec = new Executable("ls");

        stream.setStream("./src/test/resources/LsTest");

        exec.execute(env, stream);

        List<String> files = stream.getStream();
        for (int i = 0; i < stream.size(); i++) {
            assertEquals(EXPECTED_OUTPUT_LS.get(i), files.get(i));
        }
    }

    @Test
    public void executeCd() {
        Executable cd = new Executable("cd");
        Executable ls = new Executable("ls");

        stream.setStream("./src/test/resources/LsTest");
        cd.execute(env, stream);
        ls.execute(env, stream);

        List<String> files = stream.getStream();
        for (int i = 0; i < stream.size(); i++) {
            assertEquals(EXPECTED_OUTPUT_LS.get(i), files.get(i));
        }
    }
}
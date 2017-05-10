package ru.spbau.mit;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExecutableTest {
    private static Stream stream = new Stream();
    private static Environment env = new Environment();

    private static final ArrayList<String> EXPECTED_OUTPUT_LS;
    private static final String LS_TEST_DIRECTORY = "./src/test/resources/LsTest"
                                            .replace('/', File.separatorChar);
    private static final String HOME = System.getProperty("user.dir");

    static {
        ArrayList<String> output = new ArrayList<>(Arrays.asList(
                "./src/test/resources/LsTest/ipsum.txt",
                "./src/test/resources/LsTest/lorem.sh",
                "./src/test/resources/LsTest/test1.txt"
        ));

        for (int i = 0; i < output.size(); i++) {
            String path = output.get(i);
            path = path.replace('/', File.separatorChar);
            File file = new File(path);
            try {
                output.set(i, file.getCanonicalPath());
            } catch (IOException e) {
                System.out.println("Test system experienced an internal error, "
                        + "please tell about it to developers");
            }
        }

        EXPECTED_OUTPUT_LS = output;
    }

    @Before
    public void clear() {
        stream.clearStream();
    }

    @After
    public void returnToHomeDirectory() {
        System.setProperty("user.dir", HOME);
    }

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
        Executable ls = new Executable("ls");

        ls.setArgument(LS_TEST_DIRECTORY);
        ls.execute(env, stream);

        List<String> files = stream.getStream();
        for (int i = 0; i < stream.size(); i++) {
            assertEquals(EXPECTED_OUTPUT_LS.get(i), files.get(i));
        }
    }

    @Test
    public void executeCd() {
        Executable cd = new Executable("cd");
        Executable ls = new Executable("ls");

        cd.setArgument(LS_TEST_DIRECTORY);
        cd.execute(env, stream);
        ls.execute(env, stream);

        List<String> files = stream.getStream();
        for (int i = 0; i < stream.size(); i++) {
            assertEquals(EXPECTED_OUTPUT_LS.get(i), files.get(i));
        }
    }

    @Test
    public void upDownCdTest() {
        Executable cd = new Executable("cd");
        Executable ls = new Executable("ls");

        ls.execute(env, stream);
        List<String> currentDirectoryFiles = stream.getStream();
        String currentDirectory = System.getProperty("user.dir");

        cd.setArgument("..");
        cd.execute(env, stream);
        cd.setArgument(currentDirectory);
        cd.execute(env, stream);

        ls.execute(env, stream);
        List<String> afterUpDownFiles = stream.getStream();
        for (int i = 0; i < stream.size(); i++) {
            assertEquals(currentDirectoryFiles.get(i), afterUpDownFiles.get(i));
        }
    }
}
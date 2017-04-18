package ru.spbau.mit;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.System.exit;

/**
 * this class represents execution on the level of commands
 * it could be done easily by inheritance. But now we have only six commands,
 * and this functionality in my point of view is also acceptable
 */

class Executable {
    static HashSet<String> possibleCmds;

    static {
        possibleCmds = new HashSet<>();
        possibleCmds.add("cat");
        possibleCmds.add("echo");
        possibleCmds.add("wc");
        possibleCmds.add("pwd");
        possibleCmds.add("exit");
        possibleCmds.add("=");
        possibleCmds.add("ls");
        possibleCmds.add("cd");
    }

    private String command;
    private String argument;

    /**
     * constructor initialising field command. Field argument is initialised dynamically
     * from common stream
     *
     * @param command name of command from list above
     */
    Executable(String command) {
        this.command = command;
    }

    private void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * executes corresponding command
     *
     * @param afterPipe points out wc command where to take it's argument
     */
    void execute(Environment env, Stream stream, boolean afterPipe) {
        switch (command) {
            case "cat": {
                execCat(stream);
                break;
            }

            case "echo":
                execEcho(stream);
                break;

            case "wc": {
                execWc(stream, afterPipe);
                break;
            }

            case "pwd": {
                execPwd(stream);
                break;
            }

            case "exit": {
                exit(0);
                break;
            }

            case "=": {
                execAssign(env, stream);
                break;
            }

            case "ls": {
                execLs(stream);
                break;
            }

            case "cd": {
                execCd(stream);
                break;
            }
        }
    }

    void execute(Environment env, Stream stream) {
        execute(env, stream, false);
    }

    private void execCat(Stream stream) {
        Scanner fileContents;
        setArgument(stream.getStream().get(stream.size() - 1));
        stream.clearStream();
        try {
            fileContents = new Scanner(new File(argument));
        } catch (FileNotFoundException e) {
            System.out.println("There is no such file: " + argument);
            return;
        }
        stream.setStream(fileContents.nextLine());

        while (fileContents.hasNextLine()) {
            stream.addToStreamElement(0, "\n" + fileContents.nextLine());
        }
    }

    private void execEcho(Stream stream) {
        StringBuilder builder = new StringBuilder("");
        for (String word : stream.getStream()) {
            builder.append(word).append(" ");
        }
        argument = builder.toString();
        stream.setStream(argument.trim());
    }

    /**
     * @param afterPipe if is true, wc takes text to analyze from stream
     *                  otherwise it takes it from file (name of file is supposed to be in stream).
     */
    private void execWc(Stream stream, boolean afterPipe) {
        setArgument(stream.getStream().get(0));
        stream.clearStream();
        int numberOfLines = 0;
        int numberOfWords = 0;
        int numberOfBytes = 0;
        if (!afterPipe) {
            Scanner fileContents;

            try {
                fileContents = new Scanner(new File(argument));
            } catch (FileNotFoundException e) {
                System.out.println("There is no such file: " + argument);
                return;
            }

            while (fileContents.hasNextLine()) {
                String line = fileContents.nextLine();
                numberOfBytes += (line.getBytes()).length;
                numberOfWords += (line.split(" ").length);
                numberOfLines++;
            }

        } else {
            numberOfLines += (argument.split("\n")).length;
            numberOfBytes += (argument.getBytes()).length;
            numberOfWords += (argument.split(" ").length);
        }
        stream.setStream(numberOfLines + " " + numberOfWords + " " + numberOfBytes);
    }

    private void execPwd(Stream stream) {
        stream.setStream(new File("").getAbsolutePath());
    }

    private void execAssign(Environment env, Stream stream) {
        List<String> params = stream.getStream();
        env.setNewVariable(params.get(0), params.get(1));
        stream.clearStream();
    }

    /**
     * A command that prints list information about the files
     * (the current directory by default). It sorts entries alphabetically.
     * @param stream input stream that may contains argument for command --
     *               directory that we want to print.
     */
    private void execLs(@NotNull Stream stream) {
        String path;

        if (stream.size() == 0) {
            setArgument(null);
        } else {
            setArgument(stream.getStream().get(stream.size() - 1));
        }

        path = argument != null ? argument : System.getProperty("user.dir");
        File directory = new File(path);

        if (!directory.exists()) {
            System.out.println("There is no such directory: " + directory);
            return;
        }

        final File[] files = directory.listFiles();

        if (files == null) {
            stream.setStream("\n");
            return;
        }

        Arrays.sort(files);
        for (File f : files) {
            stream.addToStream(f.toString());
        }
    }

    /**
     * Unix-like command 'cd', for changing current working directory
     * @param stream input stream that may contains argument
     *               if stream is empty nothing is changed
     */
    private void execCd(@NotNull Stream stream) {
        if (stream.size() == 0) {
            return;
        }
        String newDirectory = stream.getStream().get(stream.size() - 1);

        if (! new File(newDirectory).exists()) {
            System.out.println("There is no such directory: " + newDirectory);
            return;
        }

        System.setProperty("user.dir", newDirectory);
    }
}

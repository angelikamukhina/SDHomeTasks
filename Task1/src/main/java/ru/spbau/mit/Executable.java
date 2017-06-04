package ru.spbau.mit;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

/**
 * this class represents execution on the level of commands
 * it could be done easily by inheritance. But now we have only six commands,
 * and this functionality in my point of view is also acceptable
 */
class Executable {
    private static HashSet<String> possibleCmds;

    static {
        possibleCmds = new HashSet<>();
        possibleCmds.add("cat");
        possibleCmds.add("echo");
        possibleCmds.add("wc");
        possibleCmds.add("pwd");
        possibleCmds.add("exit");
        possibleCmds.add("grep");
        possibleCmds.add("=");
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

    /**
     * checks if the cmd string correspond to any of available commands
     *
     * @param cmd string to check
     * @return true if it's available command and false if not
     */
    public static boolean isAvailableCommand(String cmd) {
        return possibleCmds.contains(cmd);
    }

    private void setArgument(String argument) {
        this.argument = argument;
    }

    /**
     * executes corresponding command
     *
     * @param afterPipe points out wc command where to take it's argument
     */
    public void execute(Environment env, Stream stream, boolean afterPipe) {
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

            case "grep": {
                execGrep(stream, afterPipe);
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
        }
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
        argument = "";
        for (String word : stream.getStream()) {
            argument += word + " ";
        }
        stream.setStream(argument.trim());
    }

    /**
     * handler for wc-command it cat read arguments from stream and from file.
     *
     * @param afterPipe if is true, wc takes text to analyze from stream
     *                  otherwise it take's it from file (name of file is supposed to be in stream).
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

    private void execGrep(Stream stream, boolean afterPipe) {
        CommandLine cmd = getCmd(stream);
        String[] args = cmd.getArgs();
        String regex;
        if (!afterPipe) {
            regex = args[0];
        } else {
            regex = args[args.length - 1];
        }

        if(cmd.hasOption("i")) {
            regex = "(?i)" + regex;
        }
        if (cmd.hasOption("w")) {
            regex = String.format("\\b%s\\b", regex);
        }
        int numberOfLines = 0;
        if (cmd.hasOption("A")) {
            numberOfLines = Integer.parseInt(cmd.getOptionValue("A"));
        }
        Pattern pattern = Pattern.compile("(" + regex + ")");

        List<String> lines;
        if (!afterPipe) {
            // creates stream for executing cat
            String fileName = args[1];
            Stream catStream = new Stream();
            catStream.addToStream(fileName);
            execCat(catStream);
            lines = Arrays.asList(catStream.getResult().split("\n"));
        } else {
            // stream.getResult() gives result of executing previous command
            try {
                lines = Arrays.asList(stream.getResult().split("\n"));
            } catch (NoSuchElementException e) {
                System.out.println("Wrong command format.");
                return;
            }
        }
        stream.clearStream();

        int lineIndex = 0;
        while (lineIndex < lines.size()) {
            String currString = lines.get(lineIndex);
            Matcher matcher = pattern.matcher(currString);
            if (matcher.find()) {
                if (stream.size() == 0) {
                    stream.addToStream(currString);
                } else {
                    stream.addToStreamElement(0, "\n" + currString);
                }
                appendNLines(stream, lines, lineIndex + 1, numberOfLines);
                lineIndex += numberOfLines;
            }
            lineIndex++;
        }
    }

    private CommandLine getCmd(Stream stream) {
        Options options = new Options();
        options.addOption("i", false, "case insensibility");
        options.addOption("w", false, "search only whole words");
        options.addOption("A", true, "number of lines to append after match");
        CommandLineParser parser = new DefaultParser();
        String[] line = new String[0];
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, stream.getStream().toArray(line));
        } catch (ParseException e) {
            System.out.println("Wrong command arguments");
        }

        if (cmd == null) {
            System.out.println("Error while parsing");
        }
        return cmd;
    }

    private void appendNLines(Stream stream, List<String> lines, int initialIndex, int numberOfLines) {
        for (int i = initialIndex; i < initialIndex + numberOfLines; i++) {
            stream.addToStreamElement(0, "\n" + lines.get(i));
        }
    }

    private void execAssign(Environment env, Stream stream) {
        List<String> params = stream.getStream();
        env.setNewVariable(params.get(0), params.get(1));
        stream.clearStream();
    }
}


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

public class Executable {
    public String command;
    private String argument;

    public static HashSet<String> possibleCmds;
    static {
        possibleCmds = new HashSet();
        possibleCmds.add("cat");
        possibleCmds.add("echo");
        possibleCmds.add("wc");
        possibleCmds.add("pwd");
        possibleCmds.add("exit");
        possibleCmds.add("=");
    }

    Executable(String command) {
        this.command = command;
    }

    void setArgument(String argument) {
        this.argument = argument;
    }


    void execute(boolean afterPipe) {

        switch (command) {
            case "cat" : {
                execCat();
                break;
            }

            case "echo" :
                execEcho();
                break;

            case "wc" : {
                execWc(afterPipe);
                break;
            }

            case "pwd" : {
                execPwd();
                break;
            }

            case "exit" : {
                Stream stream = Stream.getInstance();
                stream.setStream("EXIT!!!");
                break;
            }

            case "=" : {
                execAssign();
                break;
            }

        }

    }

    private void execCat() {
        Stream stream = Stream.getInstance();
        Scanner fileContents;
        setArgument(stream.getStream().lastElement());
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

    private void execEcho() {
        Stream stream = Stream.getInstance();
        setArgument(stream.getStream().elementAt(0));
        stream.setStream(argument);
    }

    private void execWc(boolean afterPipe) {
        Stream stream = Stream.getInstance();
        setArgument(stream.getStream().firstElement());
        stream.clearStream();
        int numberOfLines = 0;
        int numberOfWords = 0;
        int numberOfBytes = 0;
        if(!afterPipe) {
            Scanner fileContents;

            try {
                fileContents = new Scanner(new File(argument));
            } catch (FileNotFoundException e) {
                System.out.println("There is no such file: " + argument);
                return;
            }

            String line;
            String[] words;
            while (fileContents.hasNextLine()) {
                line = fileContents.nextLine();
                numberOfBytes += (line.getBytes()).length;
                numberOfWords += (line.split(" ").length);
                numberOfLines++;
            }


        }
        else {
            numberOfLines += (argument.split("\n")).length;
            numberOfBytes += (argument.getBytes()).length;
            numberOfWords += (argument.split(" ").length);

        }
        stream.setStream(numberOfLines + " " + numberOfWords + " " + numberOfBytes);

    }

    private void execPwd() {
        Stream stream = Stream.getInstance();
        stream.setStream(new File("").getAbsolutePath());
    }

    private void execAssign() {
        Stream stream = Stream.getInstance();
        Environment env = Environment.getInstance();
        Vector<String> params = stream.getStream();
        env.setNewVariable(params.elementAt(0), params.elementAt(1));
        stream.clearStream();
    }
}

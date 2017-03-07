import java.util.Vector;

//class singleton for execution of commands chain
public class Executor {
    private static Executor instance;
    public static Executor getInstance() {
        if(instance == null) {
            instance = new Executor();
        }
        return instance;
    }
//successively execute commands from tokens. If command is located in head, it's
//arguments are next two or one (according to command) token from tokens

    public void execute(Vector<String> tokens) {
        Executable currCommand = null;
        Stream stream = Stream.getInstance();
        boolean isPrevCommand = false;
        boolean isPrevPipe = false;
        int amountOfArguments = 0;
        for(int i = 0; i < tokens.size(); i++) {

            String currToken = tokens.elementAt(i);

            if(currToken.equals("|")) {
                isPrevPipe = true;
                continue;
            }
            if(i == 0) {
                if(Executable.possibleCmds.contains(currToken)) {
                    currCommand = new Executable(currToken);
                    if (!currToken.equals("pwd") && !currToken.equals("exit")) {
                        isPrevCommand = true;
                    } else {
                        currCommand.execute(false);
                    }
                    continue;
                }
                else {

                    Runtime runtime = Runtime.getRuntime();
                    Process prc = null;
                    String cmd[]={currToken};
                    try{
                        prc = runtime.exec(cmd);
                    }
                    catch(java.lang.Exception e){}
                }
            }
            if(isPrevCommand) {
                if(currCommand.command.equals("=") && amountOfArguments < 1) {
                    stream.addToStream(currToken);
                    amountOfArguments++;
                    continue;
                }

                stream.addToStream(currToken);
                currCommand.execute(false);
                isPrevCommand = false;
            }
            if(Executable.possibleCmds.contains(currToken) && isPrevPipe) {
                currCommand = new Executable(currToken);
                currCommand.execute(true);
                isPrevCommand = false;
                isPrevPipe = false;
            }
        }
    }
}

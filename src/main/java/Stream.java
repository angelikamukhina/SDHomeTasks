import java.util.Queue;
import java.util.Vector;

//class singleton for common stream for all commands
//command takes it's arguments from this stream and
//sets return value there
public class Stream {
    //use of Vector object helps support more than one argument for command
    //for example command "=" takes two arguments (variable and it's value)
    private Vector<String> stream;

    private static Stream instance;

    public static Stream getInstance() {
        if(instance == null) {
            instance = new Stream();
            instance.stream = new Vector<>();
        }
        return instance;
    }

//returns size of stream
    public int size() {
        return stream.size();
    }

    //overwrites stream by arg
    public void setStream(String arg) {
        stream = new Vector<>();
        stream.add(arg);
    }

    //doesn't overwrite existing stream, only add str as new element
    public void addToStream(String str) {
        stream.addElement(str);
    }

    //concatenates corresponding element with str
    public void addToStreamElement(int index, String str) {
        stream.insertElementAt(stream.get(index) + str, index);
        stream.remove(stream.size() - 1);
    }

    //returns Vector<String> stream
    public Vector<String> getStream() {
        return stream;
    }

    //creates empty stream
    public void clearStream() {
        stream = new Vector<>();
    }
}

import java.util.Queue;
import java.util.Vector;

public class Stream {
    private static Stream instance;

    public static Stream getInstance() {
        if(instance == null) {
            instance = new Stream();
            instance.stream = new Vector<>();
        }
        return instance;
    }

    private Vector<String> stream;

    public int size() {
        return stream.size();
    }
    public void setStream(String arg) {
        stream = new Vector<>();
        stream.add(arg);
    }

    public void addToStream(String str) {
        stream.addElement(str);
    }

    public void addToStreamElement(int index, String str) {
        stream.insertElementAt(stream.get(index) + str, index);
        stream.remove(stream.size() - 1);
    }

    public Vector<String> getStream() {
        return stream;
    }

    public void clearStream() {
        stream = new Vector<>();
    }
}

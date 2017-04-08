import java.util.List;
import java.util.Vector;

/**
 * class singleton for common stream for all commands
 * command takes it's arguments from this stream and
 * sets return value there
 */


public class Stream {
    /**
     * use of Vector object helps support more than one argument for commands.
     * for example, command "=" takes two arguments (variable and it's value)
     */
    private Vector<String> stream = new Vector<>();

    /**
     * @return size of stream
     */
    public int size() {
        return stream.size();
    }

    /**
     * Doesn't overwrite existing stream, only add str as new element
     *
     * @param str element to add
     */
    public void addToStream(String str) {
        stream.addElement(str);
    }

    /**
     * concatenates corresponding element with str
     *
     * @param index index of stream vector
     * @param str   string to concatenate with
     */
    public void addToStreamElement(int index, String str) {
        stream.insertElementAt(stream.get(index) + str, index);
        stream.remove(stream.size() - 1);
    }

    /**
     * @return Vector<String> stream
     */
    public List<String> getStream() {
        return stream;
    }

    /**
     * Clears whole content of the stream and adds new string to it
     *
     * @param arg new string
     */
    public void setStream(String arg) {
        stream = new Vector<>();
        stream.add(arg);
    }

    /**
     * clears stream
     */
    public void clearStream() {
        stream = new Vector<>();
    }
}

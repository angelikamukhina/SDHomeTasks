package ru.spbau.mit;

import java.util.List;
import java.util.Vector;

/**
 * Class stream for all commands.
 * Command takes it's arguments from this stream and
 * sets return value there
 */
class Stream {

    /**
     * use of Vector object helps support more than one argument for commands.
     * for example, command "=" takes two arguments (variable and it's value)
     */
    private Vector<String> stream = new Vector<>();

    /**
     * The method to get size of the stream
     *
     * @return size of stream
     */
    public int size() {
        return stream.size();
    }

    /**
     * Doesn't overwrite existing stream, just add str as new element
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
     * The method to get all strings in stream in list
     *
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

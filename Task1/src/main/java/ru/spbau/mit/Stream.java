package ru.spbau.mit;

import java.util.List;
import java.util.Vector;

/**
 * class singleton for common stream for all commands
 * command takes it's arguments from this stream and
 * sets return value there
 */


class Stream {
    /**
     * use of Vector object helps support more than one argument for commands.
     * for example, command "=" takes two arguments (variable and it's value)
     */
    private Vector<String> stream = new Vector<>();

    /**
     * @return size of stream
     */
    int size() {
        return stream.size();
    }

    /**
     * Doesn't overwrite existing stream, only add str as new element
     *
     * @param str element to add
     */
    void addToStream(String str) {
        stream.addElement(str);
    }

    /**
     * concatenates corresponding element with str
     *
     * @param index index of stream vector
     * @param str   string to concatenate with
     */
    void addToStreamElement(int index, String str) {
        stream.insertElementAt(stream.get(index) + str, index);
        stream.remove(stream.size() - 1);
    }

    /**
     * @return Vector<String> stream
     */
    List<String> getStream() {
        return stream;
    }

    /**
     * Clears whole content of the stream and adds new string to it
     *
     * @param arg new string
     */
    void setStream(String arg) {
        stream = new Vector<>();
        stream.add(arg);
    }

    /**
     * clears stream
     */
    void clearStream() {
        stream = new Vector<>();
    }
}

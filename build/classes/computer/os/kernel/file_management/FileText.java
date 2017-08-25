/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.os.kernel.file_management;

/**
 *
 * @author Administrator
 */
public class FileText {

    private byte[] text;
    private int length;

    public FileText() {
    }

    public FileText(byte[] text, int length) {
        this.text = text;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the text
     */
    public byte[] getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(byte[] text) {
        this.text = text;
    }
}

package ca.uhn.hl7v2.examples;
import javax.swing.JTextArea;

public class ConsoleTextArea extends JTextArea {
    public ConsoleTextArea(int rows, int columns) {
        super(rows, columns);
        setEditable(false); // Make it non-editable
    }

    // Method to append text to the console
    public void appendText(String text) {
        append(text);
        setCaretPosition(getDocument().getLength()); // Auto-scroll to the bottom
    }
}

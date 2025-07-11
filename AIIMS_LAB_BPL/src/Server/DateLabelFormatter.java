package Server;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DateLabelFormatter extends AbstractFormatter {

    private static final String DATE_PATTERN = "dd-MM-yyyy"; // Your desired date format
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text); // Convert text to Date
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            if (value instanceof Calendar) {
                // Convert Calendar to Date
                Calendar calendar = (Calendar) value;
                return dateFormatter.format(calendar.getTime());
            } else if (value instanceof Date) {
                // If it's already a Date
                return dateFormatter.format(value);
            }
        }
        return ""; // Return empty string if value is null
    }}

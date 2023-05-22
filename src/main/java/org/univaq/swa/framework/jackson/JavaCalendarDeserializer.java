
package org.univaq.swa.framework.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author didattica
 */
public class JavaCalendarDeserializer extends JsonDeserializer<Calendar> {

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public Calendar deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext)
            throws IOException {

        String dateAsString = jsonParser.getText();

        try {
            Date date = formatter.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}

package guru.qa.niffler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String fromDateToString(Date date, String dateFormat) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(dateFormat);
        return outputDateFormat.format(date);
    }

    public static String getDateInFormat(Date date, String format){
       return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }
}

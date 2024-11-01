package guru.qa.niffler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String fromDateToString(Date date, String dateFormat) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(dateFormat);
        return outputDateFormat.format(date);
    }
}

package baikal.web.footballapp;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateToString {
    private final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
    private final SimpleDateFormat format3 = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
    private final SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final SimpleDateFormat usualFormatDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private final SimpleDateFormat usualFormatTime = new SimpleDateFormat("HH:mm", Locale.US);
    private final SimpleDateFormat formatForServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+hh:mm", Locale.US);

    public String TimeForServer (String str, String inputFormat, String locale)
    {
        DateFormat formatParse = new SimpleDateFormat(inputFormat, new Locale(locale));

        try {
            return formatForServer.format(Objects.requireNonNull(formatParse.parse(str)));
        } catch (Exception e) {
            Log.d("Time to string: ", e.toString());
            return "";
        }
    }

    private Date tryParseStr(String str, SimpleDateFormat format) {
        try {
            return format.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public String ChangeDate(String str)  {
        Date date;

        if (null == (date = tryParseStr(str, format1)))
            if (null == (date = tryParseStr(str, format2)))
                if (null == (date = tryParseStr(str, format3)))
                    date = tryParseStr(str, format4);

        if (date == null)
            return "";

        return usualFormatDate.format(date);
    }

    public String ChangeTime(String str)  {
        Date date;

        if (null == (date = tryParseStr(str, format1)))
            date = tryParseStr(str, format2);

        if (date == null)
            return "";

        return usualFormatTime.format(date);
    }
}

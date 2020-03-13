package baikal.web.footballapp;
import android.content.res.Resources;
import android.util.Log;

import androidx.core.os.ConfigurationCompat;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateToString {
    private final static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private final static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
    private final static SimpleDateFormat format3 = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
    private final static SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final static SimpleDateFormat usualFormatDate = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private final static SimpleDateFormat usualFormatTime = new SimpleDateFormat("HH:mm", Locale.US);
    private final static SimpleDateFormat formatForServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+hh:mm", Locale.US);

    public static String TimeForServer (String str, String inputFormat, String locale)
    {
        DateFormat formatParse = new SimpleDateFormat(inputFormat, new Locale(locale));

        try {
            return formatForServer.format(Objects.requireNonNull(formatParse.parse(str)));
        } catch (Exception e) {
            Log.d("Time to string: ", e.toString());
            return "Дата не установлена";
        }
    }

    private static Date tryParseStr(String str, SimpleDateFormat format) {
        try {
            return format.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDate (String str) {
        Date date;

        if (null == (date = tryParseStr(str, format1)))
            if (null == (date = tryParseStr(str, format2)))
                if (null == (date = tryParseStr(str, format3)))
                    date = tryParseStr(str, format4);

        return date;
    }

    public static String ChangeDate(String str)  {
        Date date;

        if (null == (date = tryParseStr(str, format1)))
            if (null == (date = tryParseStr(str, format2)))
                if (null == (date = tryParseStr(str, format3)))
                    date = tryParseStr(str, format4);

        if (date == null)
            return "Не назначено";

        return usualFormatDate.format(date);
    }

    public static String ChangeTime(String str)  {
        Date date;

        if (null == (date = tryParseStr(str, format1)))
            date = tryParseStr(str, format2);

        if (date == null)
            return "Не назначено";

        return usualFormatTime.format(date);
    }

    public static String stringDate (String targetDate)
    {
        Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

        String formattedBirthDateStr = "Не назначено";
        if (targetDate == null || targetDate.equals(""))
            return formattedBirthDateStr;

        SimpleDateFormat outDateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);

        try {
            Date birthDate = ISO8601Utils.parse(targetDate, new ParsePosition(0));
            formattedBirthDateStr = outDateFormat.format(birthDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedBirthDateStr;
    }
}

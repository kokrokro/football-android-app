package baikal.web.footballapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeToString {
    public String ChangeTime(String str)  {
            String dateDOB = "";
            try {
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                Date date1;
                date1 = mdformat.parse(str);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (String.valueOf(cal.get(Calendar.HOUR)).length()==1){
                    dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
                }
                else {
                    dateDOB += cal.get(Calendar.HOUR) + ":";
                }
                if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length()==1)){
                    dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
                }
                else{
                    dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
                }
            } catch (ParseException e) {
                try{
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
                    Date date1;
                    date1 = mdformat.parse(str);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date1);
                    if (String.valueOf(cal.get(Calendar.HOUR)).length()==1){
                        dateDOB += "0" + cal.get(Calendar.HOUR) + ":";
                    }
                    else {
                        dateDOB += cal.get(Calendar.HOUR) + ":";
                    }
                    if ((String.valueOf(cal.get(Calendar.MINUTE) + 1).length()==1)){
                        dateDOB += "0" + (cal.get(Calendar.MINUTE) + 1);
                    }
                    else{
                        dateDOB += String.valueOf(cal.get(Calendar.MINUTE) + 1);
                    }
                }
                catch (ParseException t) {t.printStackTrace();}
            }
            return dateDOB;

    }
}

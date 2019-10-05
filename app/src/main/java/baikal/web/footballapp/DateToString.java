package baikal.web.footballapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateToString {
    public String ChangeDate(String str)  {
        String dateDOB = "";
        try {
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            Date date1;
            date1 = mdformat.parse(str);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            if (String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length()==1){
                dateDOB += "0" + cal.get(Calendar.DAY_OF_MONTH) + ".";
            }
            else {
                dateDOB += cal.get(Calendar.DAY_OF_MONTH) + ".";
            }
            if ((String.valueOf(cal.get(Calendar.MONTH) + 1).length()==1)){
                dateDOB += "0" + (cal.get(Calendar.MONTH) + 1) + ".";
            }
            else{
                dateDOB += (cal.get(Calendar.MONTH) + 1) + ".";
            }
            dateDOB += String.valueOf(cal.get(Calendar.YEAR));
//            holder.textTournamentDate.setText(dateDOB);
        } catch (ParseException e) {
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
            try{
                Date date1;
                date1 = mdformat.parse(str);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                if (String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length()==1){
                    dateDOB += "0" + cal.get(Calendar.DAY_OF_MONTH) + ".";
                }
                else {
                    dateDOB += cal.get(Calendar.DAY_OF_MONTH) + ".";
                }
                if ((String.valueOf(cal.get(Calendar.MONTH) + 1).length()==1)){
                    dateDOB += "0" + (cal.get(Calendar.MONTH) + 1) + ".";
                }
                else{
                    dateDOB += (cal.get(Calendar.MONTH) + 1) + ".";
                }
                dateDOB += String.valueOf(cal.get(Calendar.YEAR));
            }catch (ParseException t) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.US);
                    Date date;
                    date = dateFormat.parse(str);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    if (String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1) {
                        dateDOB = "0" + cal.get(Calendar.DAY_OF_MONTH) + ".";
                    } else {
                        dateDOB = cal.get(Calendar.DAY_OF_MONTH) + ".";
                    }
                    if ((String.valueOf(cal.get(Calendar.MONTH) + 1).length() == 1)) {
                        dateDOB += "0" + (cal.get(Calendar.MONTH) + 1) + ".";
                    } else {
                        dateDOB += (cal.get(Calendar.MONTH) + 1) + ".";
                    }
                    dateDOB += String.valueOf(cal.get(Calendar.YEAR));
                } catch (ParseException q) {}
            }
        }
        catch (NullPointerException e){
            dateDOB = "Не указано";
        }
        if (dateDOB.equals("")){
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date;
                date = dateFormat.parse(str);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                if (String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1) {
                    dateDOB = "0" + cal.get(Calendar.DAY_OF_MONTH) + ".";
                } else {
                    dateDOB = cal.get(Calendar.DAY_OF_MONTH) + ".";
                }
                if ((String.valueOf(cal.get(Calendar.MONTH) + 1).length() == 1)) {
                    dateDOB += "0" + (cal.get(Calendar.MONTH) + 1) + ".";
                } else {
                    dateDOB += (cal.get(Calendar.MONTH) + 1) + ".";
                }
                dateDOB += String.valueOf(cal.get(Calendar.YEAR));
            } catch (ParseException q) {
                dateDOB = "Не указано";
            }

        }
        if (dateDOB.equals("Не указано")){
            dateDOB = "";
        }
        return dateDOB;
    }
}

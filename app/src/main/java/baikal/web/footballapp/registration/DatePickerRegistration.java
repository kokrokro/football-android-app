package baikal.web.footballapp.registration;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import baikal.web.footballapp.R;
import baikal.web.footballapp.user.activity.DatePicker;
import baikal.web.footballapp.user.activity.PersonalInfo;
import baikal.web.footballapp.user.activity.UserInfo;

public class DatePickerRegistration extends DatePickerDialogFragment {

    public static DatePicker newInstance() {
        return new DatePicker();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.date_picker, null);
        final com.ycuwq.datepicker.date.DatePicker datePicker = view.findViewById(R.id.datePicker);
        HashMap<String, Integer> months = new HashMap<>();
        months.put("янв.", 1);
        months.put("февр.", 2);
        months.put("марта", 3);
        months.put("апр.", 4);
        months.put("мая", 5);
        months.put("июня", 6);
        months.put("июля", 7);
        months.put("авг.", 8);
        months.put("сент.", 9);
        months.put("окт.", 10);
        months.put("нояб.", 11);
        months.put("дек.", 12);

        String dateDOB= RegistrationUserActivity.textDOB.getText().toString();


        String[] date = dateDOB.split(" ");
        try {
            int month =  months.get(date[1]);
            datePicker.setDate(Integer.parseInt(date[2]), month,Integer.parseInt(date[0]));
        } catch(Exception e){
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        Date maxDate = cal.getTime();
        datePicker.setMaxDate(maxDate.getTime());
        cal.set(Calendar.YEAR, 1940);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date minDate = cal.getTime();
        datePicker.setMinDate(minDate.getTime());

        datePicker.setOnDateSelectedListener((year, month, day) -> {
            String str = Integer.toString(year);
        });


        mCancelButton = view.findViewById(R.id.datePickerClose);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton = view.findViewById(R.id.datePickerOk);
        mDecideButton.setOnClickListener(v -> {
            String str = datePicker.getDate();
            RegistrationUserActivity.textDOB.setText(str);
            dismiss();
        });

        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

}

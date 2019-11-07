package baikal.web.footballapp.user.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import android.view.View;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DatePicker extends DatePickerDialogFragment {
//public class DatePicker extends DialogFragment {

    Logger log = LoggerFactory.getLogger(PersonalActivity.class);
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

        String dateDOB;
        try{
            dateDOB = UserInfo.buttonDOB.getText().toString();
        }catch (Exception e){
            dateDOB = PersonalInfo.textDOB.getText().toString();
        }
        String[] date = dateDOB.split(" ");
        try {
            int month =  months.get(date[1]);
            datePicker.setDate(Integer.parseInt(date[2]), month,Integer.parseInt(date[0]));
        }
        catch (Exception e){
        }

//        datePicker.setTourney(Integer.parseInt(date[2]), months.get("дек."),Integer.parseInt(date[0]));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

//        datePicker.setMaxDate(new Date().getTime());
//        Date maxDate = new Date();
        Date maxDate = cal.getTime();
//        datePicker.setMaxDate(new Date().getTime());
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
//                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        });


        mCancelButton = view.findViewById(R.id.datePickerClose);
        mCancelButton.setOnClickListener(v -> dismiss());
        mDecideButton = view.findViewById(R.id.datePickerOk);
        mDecideButton.setOnClickListener(v -> {
            String str = datePicker.getDate();
//                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            try{
                UserInfo.buttonDOB.setText(str);
            }catch (Exception e){
                PersonalInfo.textDOB.setText(str);
            }

            dismiss();
        });



        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

//    @Override
//    protected void initChild() {
//        super.initChild();
//        mDatePicker.setMaxDate(new Date().getTime());
////        mCancelButton.setTextSize(mCancelButton.getTextSize() + 5);
//        mCancelButton.setText("закрыть");
////        mDecideButton.setTextSize(mDecideButton.getTextSize() + 5);
//        mDecideButton.setText("ок");
//        mDatePicker.setShowCurtain(false);
//    }
}

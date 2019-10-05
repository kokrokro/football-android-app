package baikal.web.footballapp;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class CheckError {
    public void checkError(Context context, Throwable error) throws IOException, JSONException {
        String str = "";

        try {
            if(error instanceof HttpException) {

                HttpException e = (HttpException) error;
                String errorBody = e.response().errorBody().string();
                JSONObject jsonObject = new JSONObject(errorBody);
                str = jsonObject.getString("message");
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }

            if (error instanceof SocketTimeoutException) {
                str = "Неполадки на сервере. Попробуйте позже";
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
            if (error instanceof ConnectException) {
                str = "Отсутствует соединение";
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
//            str = error.getMessage();
//            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

        } catch (ClassCastException n) {
            str = "Неполадки на сервере. Попробуйте позже";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e){

        }
    }
    public void checkHttpError(Context context, String errorBody) throws JSONException {
        String str = "";
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        try {
                JSONObject jsonObject = new JSONObject(errorBody);
                str = jsonObject.getString("message");
            String finalStr = str;
            Toast.makeText(context, finalStr, Toast.LENGTH_SHORT).show();

        } catch (ClassCastException n) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }
}

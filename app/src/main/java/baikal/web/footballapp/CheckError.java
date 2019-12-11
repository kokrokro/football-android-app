package baikal.web.footballapp;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import retrofit2.HttpException;

public class CheckError {

    void toastError(Context context, Throwable error) {
        String str = "";
        try {
            if (error instanceof HttpException) {
                HttpException exception = (HttpException) error;
                switch (exception.code()) {
                    case 408:
                        str = "Истекло время ожидания, попробуйте позже";
                        break;
                    case 500:
                        str = "Неполадки на сервере. Попробуйте позже";
                        break;
                    case 522:
                        str = "Отсутствует соединение";
                        break;
                    case 410:
                        str = "Wrong api request";
                    default:
                        break;
                }

                if(App.wasInBackground)
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
            if (error instanceof SocketTimeoutException) {
                str = "Неполадки на сервере. Попробуйте позже.";
                if(App.wasInBackground)
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
            if (error instanceof ConnectException) {
                str = "Отсутствует соединение.";
                if(App.wasInBackground)
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        } catch (ClassCastException n) {
            str = "Неполадки на сервере. Попробуйте позже.";
            if(App.wasInBackground)
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    public void checkError(Context context, Throwable error) throws IOException {
        String str;

        try {
            if(error instanceof HttpException) {

                HttpException e = (HttpException) error;
                String errorBody = Objects.requireNonNull(e.response().errorBody()).string();
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
        catch (JSONException ignored){ }
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

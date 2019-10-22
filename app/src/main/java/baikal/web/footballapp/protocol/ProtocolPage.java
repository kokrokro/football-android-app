package baikal.web.footballapp.protocol;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import baikal.web.footballapp.CheckError;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;
import baikal.web.footballapp.user.activity.ConfirmProtocol;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProtocolPage extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.protocol_page);
        ImageButton protocolButtonBack = findViewById(R.id.protocolButtonBack);
        protocolButtonBack.setOnClickListener(v -> finish());






        CheckError checkError = new CheckError();
        Controller.getApi().confirmProtocol(SaveSharedPreference.getObject().getToken(), "123123")
                .map(responseBody -> {
                    if (!responseBody.isSuccessful()) {
                        String srt = responseBody.errorBody().string();
                        showToast(srt);
                    }
                    if (responseBody.errorBody() != null) {
                        checkError.checkHttpError(this, responseBody.errorBody().string());
                    }
                    return responseBody.body();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            String str = "Изменения сохранены";
                            showToastResult(str);
                            finish();
                        },
                        error -> checkError.checkError(this, error));




    }


    private void showToast(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String str1 = jsonObject.getString("message");
            this.runOnUiThread(() -> Toast.makeText(ProtocolPage.this, str1, Toast.LENGTH_SHORT).show());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void showToastResult(String str) {
        this.runOnUiThread(() -> Toast.makeText(ProtocolPage.this, str, Toast.LENGTH_SHORT).show());
    }

}

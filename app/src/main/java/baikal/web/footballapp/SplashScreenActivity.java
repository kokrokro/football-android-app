package baikal.web.footballapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Timer splashTimer = new Timer();
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(() -> {
                    SplashScreenActivity.this.finish();
                    startActivity(new Intent(SplashScreenActivity.this, PersonalActivity.class));
                });
            }
        }, DELAY);
    }

}

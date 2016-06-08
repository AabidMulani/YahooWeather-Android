package in.abmulani.yahooweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import in.abmulani.yahooweather.BaseActivity;
import in.abmulani.yahooweather.R;

/**
 * Splash Screen which holds for 2000ms
 */
public class SplashActivity extends BaseActivity {

    private boolean onBackKeyPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!onBackKeyPressed) {
                    startActivity(new Intent(activity, MapsActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackKeyPressed = true;
    }
}

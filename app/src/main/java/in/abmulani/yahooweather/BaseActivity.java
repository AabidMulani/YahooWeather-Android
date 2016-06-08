package in.abmulani.yahooweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * App Base Activity to hold Activity level common code
 */
public class BaseActivity extends AppCompatActivity {

    public Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }


}

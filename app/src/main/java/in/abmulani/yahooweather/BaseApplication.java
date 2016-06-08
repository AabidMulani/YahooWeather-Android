package in.abmulani.yahooweather;

import android.support.multidex.MultiDexApplication;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

import in.abmulani.yahooweather.interfaces.YahooWeatherRetrofitInterface;
import in.abmulani.yahooweather.utils.AppConstants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Base Application to hold values in application scope
 */
public class BaseApplication extends MultiDexApplication {

    private YahooWeatherRetrofitInterface yahooWeatherRetrofitInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public YahooWeatherRetrofitInterface getOTPUMWebServiceInterface() {

        if (yahooWeatherRetrofitInterface != null) {
            return yahooWeatherRetrofitInterface;
        }
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        yahooWeatherRetrofitInterface = restAdapter.create(YahooWeatherRetrofitInterface.class);
        return yahooWeatherRetrofitInterface;
    }


}

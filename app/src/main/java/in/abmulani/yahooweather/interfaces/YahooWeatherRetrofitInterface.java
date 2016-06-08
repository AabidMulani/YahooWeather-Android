package in.abmulani.yahooweather.interfaces;

import in.abmulani.yahooweather.entityModels.ResponseSet;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit Adapter
 */
public interface YahooWeatherRetrofitInterface {

    @GET("v1/public/yql")
    Call<ResponseSet> getWeatherInformation(@Query("q") String query, @Query("format") String format);

}

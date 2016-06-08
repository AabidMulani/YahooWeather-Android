package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aabid on 6/8/16.
 */
public class ResponseSet {

    @SerializedName("query")
    @Expose
    private Query query;

    public Query getQuery() {
        return query;
    }
}

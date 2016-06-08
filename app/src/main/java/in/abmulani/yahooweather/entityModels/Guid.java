package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aabid on 6/8/16.
 */
public class Guid {

    @SerializedName("isPermaLink")
    @Expose
    private String isPermaLink;

    public String getIsPermaLink() {
        return isPermaLink;
    }
}

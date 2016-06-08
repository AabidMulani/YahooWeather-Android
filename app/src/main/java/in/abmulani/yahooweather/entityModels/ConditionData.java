package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aabid on 6/8/16.
 */
public class ConditionData {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("temp")
    @Expose
    private String temp;
    @SerializedName("text")
    @Expose
    private String text;

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public String getTemp() {
        return temp;
    }

    public String getText() {
        return text;
    }
}

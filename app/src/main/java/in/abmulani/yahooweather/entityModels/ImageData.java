package in.abmulani.yahooweather.entityModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aabid on 6/8/16.
 */
public class ImageData {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("url")
    @Expose
    private String url;

    public String getTitle() {
        return title;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getLink() {
        return link;
    }

    public String getUrl() {
        return url;
    }
}

package test.hellowdoc.com.hellowdoc;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataModel implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("author")
    private String author;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

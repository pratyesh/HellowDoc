package test.hellowdoc.com.hellowdoc;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseDetails implements Serializable {

    @SerializedName("hits")
    private List<DataModel> hits;
    @SerializedName("page")
    private String page;
    @SerializedName("nbPages")
    private String nbPages;

    public List<DataModel> getHits() {
        return hits;
    }

    public String getPage() {
        return page;
    }

    public String getNbPages() {
        return nbPages;
    }

}

package baikal.web.footballapp.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("news")
    @Expose
    private List<News_> news = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<News_> getNews() {
        return news;
    }

    public void setNews(List<News_> news) {
        this.news = news;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}

package baikal.web.footballapp.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.News_;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullscreenNewsActivity extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(FullscreenNewsActivity.class);
    private News_ news;

    private ImageView      imageNews;
    private TextView       textTitle;
    private TextView       textDate;
    private TextView       textDesc;
    private RelativeLayout imgHolder;
    private ProgressBar    progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.ANP_swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.d("FullscreenNewsActivity", "refreshing...");
            swipeRefreshLayout.setRefreshing(false);
            Controller.getApi().getSingleNewsCrud(news.getId()).enqueue(new Callback<List<News_>>() {
                @Override
                public void onResponse(@NonNull Call<List<News_>> call, @NonNull Response<List<News_>> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (!response.isSuccessful() || response.body()==null)
                        Toast.makeText(FullscreenNewsActivity.this, "Не удалось обновить...", Toast.LENGTH_SHORT).show();

                    news = response.body().get(0);
                    fillFeedData();
                }

                @Override
                public void onFailure(@NonNull Call<List<News_>> call, @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(FullscreenNewsActivity.this, "Не удалось обновить...", Toast.LENGTH_SHORT).show();
                }
            });
        });

        imageNews   = findViewById(R.id.newsInfoImg);
        textTitle   = findViewById(R.id.newsInfoTitle);
        textDate    = findViewById(R.id.newsInfoDate);
        textDesc    = findViewById(R.id.newsInfoText);
        imgHolder   = findViewById(R.id.newsInfoImgHolder);
        progressBar = findViewById(R.id.progressNews);

        Button button = findViewById(R.id.newsButtonBack);
        button.setOnClickListener(v -> finish());

        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            news = (News_) bundle.getSerializable("NEWS");

            fillFeedData ();
        }
        catch (Exception t){
            log.error("ERROR: ", t);
        }
    }

    private void fillFeedData () {
        textTitle.setText(news.getCaption());
        textDate.setText(DateToString.ChangeDate(news.getCreatedAt()));
        textDesc.setText(news.getContent());

        if (news.getImg() != null)
            SetImage.setImage(this, imageNews, imgHolder, progressBar, news.getImg());
        else
            imgHolder.setVisibility(View.GONE);
    }
}


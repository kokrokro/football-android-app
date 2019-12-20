package baikal.web.footballapp.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.SetImage;
import baikal.web.footballapp.model.News_;

public class FullscreenNewsActivity extends AppCompatActivity {
    private final Logger log = LoggerFactory.getLogger(FullscreenNewsActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        Button button;
        ImageView imageNews;
        TextView textTitle;
        TextView textDate;
        TextView textDesc;
        final ProgressBar progressBar = findViewById(R.id.progressNews);
        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            News_ news = (News_) bundle.getSerializable("NEWS");

            imageNews = findViewById(R.id.newsInfoImg);
            button = findViewById(R.id.newsButtonBack);
            textTitle = findViewById(R.id.newsInfoTitle);
            textDate = findViewById(R.id.newsInfoDate);
            textDesc = findViewById(R.id.newsInfoText);
            RelativeLayout imgHolder = findViewById(R.id.newsInfoImgHolder);

            textTitle.setText(news.getCaption());
            DateToString dateToString = new DateToString();
            String str = news.getCreatedAt();
            textDate.setText(dateToString.ChangeDate(str));
            textDesc.setText(news.getContent());

            if (news.getImg() != null)
                (new SetImage()).setImage(this, imageNews, imgHolder, progressBar, news.getImg());
            else
                imgHolder.setVisibility(View.GONE);

            button.setOnClickListener(v -> finish());
        }
        catch (Exception t){
            log.error("ERROR: ", t);
        }
    }
}


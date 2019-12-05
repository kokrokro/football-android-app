package baikal.web.footballapp.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import baikal.web.footballapp.Controller;
import baikal.web.footballapp.DateToString;
import baikal.web.footballapp.R;
import baikal.web.footballapp.model.News_;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            textTitle.setText(news.getCaption());
            DateToString dateToString = new DateToString();
            String str = news.getCreatedAt();
            textDate.setText(dateToString.ChangeDate(str));
            textDesc.setText(news.getContent());
            try {
                str = Controller.BASE_URL + "/" + news.getImg();
                Glide.with(this)
                        .asBitmap()
                        .load(str)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(new RequestOptions()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(imageNews);
            }catch (Exception e){
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.ic_logo2)
                        .apply(new RequestOptions()
                                .format(DecodeFormat.PREFER_ARGB_8888)
                                .priority(Priority.HIGH)
                                .centerCrop())
                        .into(imageNews);
            }

            button.setOnClickListener(v -> finish());


           // Intent intent2 = new Intent(this, ProtocolPage.class);
           // startActivity(intent2);

        }
        catch (Exception t){
            log.error("ERROR: ", t);
        }
    }
}


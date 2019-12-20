package baikal.web.footballapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import static baikal.web.footballapp.Controller.BASE_URL;

public class SetImage {
    @SuppressLint("CheckResult")
    public void setImage(Context context, ImageView imageView, String logo){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalCircleCrop();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        RequestOptions.errorOf(R.drawable.ic_logo2);
        requestOptions.override(500, 500);
        requestOptions.priority(Priority.HIGH);

        try {
            if (logo == null || logo.equals("R.drawable.ic_logo2"))
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_logo2)
                        .apply(requestOptions)
                        .into(imageView);
            else
                Glide.with(context)
                        .asBitmap()
                        .load(BASE_URL + "/" + logo)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .apply(requestOptions)
                        .into(imageView);
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.ic_logo2)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    @SuppressLint("CheckResult")
    public void setImage(Context context, ImageView imageView, RelativeLayout holder, ProgressBar progressBar, String logo){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.optionalCircleCrop();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        RequestOptions.errorOf(R.drawable.ic_logo2);
        requestOptions.override(500, 500);
        requestOptions.priority(Priority.HIGH);

        try {
            if (logo == null || logo.equals("R.drawable.ic_logo2"))
                holder.setVisibility(View.GONE);
            else
                Glide.with(context)
                        .asBitmap()
                        .load(BASE_URL + "/" + logo)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                progressBar.setTop(0);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(requestOptions)
                        .into(imageView);
        } catch (Exception e) {
            Toast.makeText(context, "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
            holder.setVisibility(View.GONE);
        }
    }
}

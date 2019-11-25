package baikal.web.footballapp;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import static baikal.web.footballapp.Controller.BASE_URL;

public class SetImage {
    public void setImage(Context context, ImageView imageView, String logo){
        String uriPic = BASE_URL;
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
                        .load(uriPic + "/" + logo)
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
}

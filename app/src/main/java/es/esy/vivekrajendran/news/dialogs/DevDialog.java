package es.esy.vivekrajendran.news.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import es.esy.vivekrajendran.news.R;


public class DevDialog extends DialogFragment {
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_circular, container);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null) {
            Log.i("TAG", "onViewCreated: ");
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.frame);
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void initViews(View view) {
        final ImageView profilePic = (ImageView) view.findViewById(R.id.circular_profile_pic);
        Glide.with(context)
                .load("https://avatars2.githubusercontent.com/u/14991281?v=3&s=460")
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_black_24px)
                .into(new BitmapImageViewTarget(profilePic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        profilePic.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }
}
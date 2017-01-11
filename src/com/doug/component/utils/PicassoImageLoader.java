package com.doug.component.utils;

import java.util.HashSet;
import java.util.Set;

import com.doug.FlashApplication;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

public class PicassoImageLoader implements UnicornImageLoader{

    private final Set<Target> protectedFromGarbageCollectorTargets = new HashSet<Target>();

    @Nullable
    @Override
    public Bitmap loadImageSync(String uri, int width, int height) {
        return null;
    }

    @Override
    public void loadImage(final String uri, final int width, final int height, final ImageLoaderListener listener) {
        final Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (listener != null) {
                    listener.onLoadComplete(bitmap);
                    protectedFromGarbageCollectorTargets.remove(this);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                if (listener != null) {
                    listener.onLoadFailed(null);
                    protectedFromGarbageCollectorTargets.remove(this);
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        final Activity current = FlashApplication.getInstance().getActivity();
        current.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestCreator requestCreator = Picasso.with(current).load(uri).config(Bitmap.Config.RGB_565);
                if (width > 0 && height > 0) {
                    requestCreator = requestCreator.resize(width, height);
                }
                protectedFromGarbageCollectorTargets.add(mTarget);
                requestCreator.into(mTarget);
            }
        });
    }
}

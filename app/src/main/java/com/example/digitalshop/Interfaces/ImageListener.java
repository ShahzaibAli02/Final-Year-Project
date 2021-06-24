package com.example.digitalshop.Interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

public interface ImageListener
{

    public  void  onImageLoaded(boolean error, Uri uri, Bitmap bitmap);

}

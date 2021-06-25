package com.example.digitalshop.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.digitalshop.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class ProgressDialogManager

{

    public static AlertDialog getProgressDialog(Context context)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ProgressBar progressBar = new ProgressBar(context);
        Sprite doubleBounce = new DoubleBounce();
        doubleBounce.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
        progressBar.setIndeterminateDrawable(doubleBounce);
        builder.setView(progressBar);
        builder.setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return alertDialog;
    }

    public static AlertDialog getProgressDialog(Context context,Sprite sprite,int color)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ProgressBar progressBar = new ProgressBar(context);
        sprite.setColor(context.getResources().getColor(color));
        progressBar.setIndeterminateDrawable(sprite);
        builder.setView(progressBar);
        builder.setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return alertDialog;
    }

}

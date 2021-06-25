package com.example.digitalshop.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.example.digitalshop.Interfaces.ImageListener;
import com.example.digitalshop.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

public class Util
{


    public  static  void  showSnackBarMessage(Activity activity,String Message)
    {
        Snackbar.make(activity.findViewById(android.R.id.content),Message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public static void showSnackBar(Activity activity, String message)
    {
        Snackbar.make(activity.findViewById(android.R.id.content),message,BaseTransientBottomBar.LENGTH_LONG).show();
    }
    public static Snackbar getSnackBar(Activity activity, String message)
    {
       return Snackbar.make(activity.findViewById(android.R.id.content),message,BaseTransientBottomBar.LENGTH_INDEFINITE);
    }
    public  static  void  showToastMessage(Activity activity,String Message)
    {
        Toast.makeText(activity,Message,Toast.LENGTH_LONG).show();
    }

    public  static Dialog createDialog(Activity activity,int layout)
    {

        Dialog dialog=new Dialog(activity);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        return dialog;

    }
    public  static  void  showCustomToast(Activity activity,String Message,boolean Error)
    {

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,null);

        CardView cardView = layout.findViewById(R.id.card);
        cardView.setCardBackgroundColor(Error? Color.RED:Color.GREEN);
        TextView text = (TextView) layout.findViewById(R.id.txtMessage);
        text.setText(Message);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }



    public static  boolean  isEmpty(EditText []  editTexts)
    {
        for(EditText editText:editTexts)
        {
            if(editText.getText().toString().isEmpty())
            {
                editText.setError("Required Field");
                editText.requestFocus();
                return true;
            }

        }
        return false;
    }

    public static boolean isEmailValid(String email)
    {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    public static boolean isPhoneValid(String phone)
    {
        return (Patterns.PHONE.matcher(phone).matches());

    }

    public  static  void readImageFromGallery (Activity activity, ImageListener imageListener)
    {

       PickImageDialog.build(new IPickResult()
       {
           @Override
           public void onPickResult (PickResult r) {


               if(r.getError()==null)
               {
                   imageListener.onImageLoaded(false,r.getUri(),r.getBitmap());
               }else
               {
                   imageListener.onImageLoaded(true,null,null);
               }

           }
       }).show((FragmentActivity) activity);
    }

    public static  void composeEmail(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:alishahzaib02@gmai.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Digital Shop");
        activity.startActivity(intent);
    }


}

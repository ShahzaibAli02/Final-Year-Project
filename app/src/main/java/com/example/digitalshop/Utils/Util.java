package com.example.digitalshop.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.digitalshop.Activities.Authentication.Login_SignUp;
import com.example.digitalshop.Interfaces.ImageListener;
import com.example.digitalshop.Interfaces.LocationListener;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
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
        if(activity==null)
            return;
        Snackbar.make(activity.findViewById(android.R.id.content),Message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public static void showSnackBar(Activity activity, String message)
    {
        if(activity==null)
            return;
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

        if(activity==null)
            return null;
        Dialog dialog=new Dialog(activity);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        return dialog;

    }
    public  static  void  showCustomToast(Activity activity,String Message,boolean Error)
    {


        if(activity==null)
            return;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,null);

        CardView cardView = layout.findViewById(R.id.card);
        cardView.setCardBackgroundColor(Error? Color.parseColor("#ad0000"):Color.parseColor("#0c7a00"));
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

    public static  boolean  needsLogIn(Activity activity)
    {
        if(activity==null)
            return true;
        if(SharedPref.getUser(activity)==null)
        {

            Dialog dialog = createDialog(activity , R.layout.lyt_dialog_login);
            Button btnLogin=dialog.findViewById(R.id.btnLogin);
            btnLogin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v)
                {
                    dialog.dismiss();
                    activity.startActivity(new Intent(activity, Login_SignUp.class));
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            return true;
        }
        return false;
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


    public  static  Dialog  getDialog (Context context, int layout)
    {
        Dialog dialog=new Dialog(context);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public static void loadLocation (AppCompatActivity activity, LocationListener locationListener)
    {


        final boolean[] isSent = {false};
        LocationProvider locationProvider=new LocationProvider.Builder(activity)
                .setInterval(5000)
                .setFastestInterval(2000)
                .setListener(new LocationProvider.MLocationCallback()
                {

                    @Override
                    public void onGoogleAPIClient (GoogleApiClient googleApiClient, String message)
                    {

                    }

                    @Override
                    public void onLocationUpdated(double latitude, double longitude,LocationProvider locationProvider)
                    {


                        if(! isSent[0])
                        {
                            locationListener.onLocationLoad(new LatLng(latitude,longitude));
                            isSent[0] =true;
                        }


                    }

                    @Override
                    public void onLocationUpdateRemoved()
                    {

                    }

                }).build();


        activity.getLifecycle().addObserver(locationProvider);
    }
    public static  void composeEmail(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:alishahzaib02@gmai.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Digital Shop");
        activity.startActivity(intent);
    }


}

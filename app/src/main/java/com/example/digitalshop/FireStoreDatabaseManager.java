package com.example.digitalshop;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.ImageUploadListener;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class FireStoreDatabaseManager
{

    public static  void  AddUser (User user, String pass,Uri imageUri, DataBaseResult dataBaseResult)
    {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(),pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete (@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task)
            {

                if (task.isSuccessful())
                {


                    uploadImage(imageUri , new ImageUploadListener()
                    {
                        @Override
                        public void onUpload (boolean Error , String Message , String url)
                        {
                            if(!Error)
                            {
                                user.setImage(url);
                                user.setUid(task.getResult().getUser().getUid());
                                FirebaseFirestore db=FirebaseFirestore.getInstance();
                                CollectionReference collectionReference = db.collection(Constants.DB_USERS);
                                collectionReference.document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete (@NonNull @NotNull Task<Void> task)
                                    {

                                        if(task.isSuccessful())
                                        {
                                            dataBaseResult.onResult(false,"User Created Successfully",null);
                                        }
                                        else
                                        {
                                            dataBaseResult.onResult(true,task.getException().getMessage(),null);
                                        }

                                    }
                                });

                            }
                            else
                            {
                                dataBaseResult.onResult(true,Message,null);
                            }


                        }
                    });

                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }
            }
        });


    }
    public static  void authenticateUser(String email,String password,DataBaseResult dataBaseResult)
    {

        email=email.trim().toLowerCase();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<AuthResult> task)
            {

                if(task.isSuccessful())
                {
                    getUser(task.getResult().getUser().getUid() , new DataBaseResult()
                    {
                        @Override
                        public void onResult (boolean error , String Message , Object data)
                        {
                            dataBaseResult.onResult(error,Message,data);
                        }
                    });
                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }

    public static void getUser(String uid,DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_USERS);
        collectionReference.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<DocumentSnapshot> task) {

                DocumentSnapshot  documentSnapshot= task.getResult();
                if(!documentSnapshot.exists())
                {
                    dataBaseResult.onResult(true,"User Data Not Found",null);
                }
                else
                {
                    dataBaseResult.onResult(false,"User Found",documentSnapshot.toObject(User.class));
                }
            }
        });



    }
    public  static  void  uploadImage (Uri uri, ImageUploadListener imageUploadListener)
    {

        if(uri==null)
        {
            imageUploadListener.onUpload(false,"","https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/user_male4-512.png");
            return;
        }


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(String.format("images/%s.jpg" , String.valueOf(Calendar.getInstance().getTimeInMillis())));

        imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<UploadTask.TaskSnapshot> task)
            {

                if(task.isSuccessful())
                {

                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete (@NonNull @NotNull Task<Uri> task)
                        {

                            if(task.isSuccessful())
                            {
                                imageUploadListener.onUpload(false,"",task.getResult().toString());

                            }
                            else
                            {
                                imageUploadListener.onUpload(true,task.getException().getMessage(),"");
                            }


                        }
                    });

                }
                else
                {
                    imageUploadListener.onUpload(true,task.getException().getMessage(),"");
                }

            }
        });
    }

}

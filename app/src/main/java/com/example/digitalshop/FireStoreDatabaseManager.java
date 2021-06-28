package com.example.digitalshop;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.digitalshop.Enums.OrderStatus;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.ImageUploadListener;
import com.example.digitalshop.Model.Analytics;
import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
                                CollectionReference collectionReferenceUsers = db.collection(Constants.DB_USERS);
                                CollectionReference collectionReferenceAnalytics = db.collection(Constants.DB_ANALYTICS);
                                collectionReferenceUsers.document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete (@NonNull @NotNull Task<Void> task)
                                    {

                                        if(task.isSuccessful())
                                        {
                                            collectionReferenceAnalytics.document(user.getUid()).set(new Analytics(user.getUid())).addOnCompleteListener(new OnCompleteListener<Void>()
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


    public static  void  deleteProductById(String id,DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReferenceProducts = db.collection(Constants.DB_PRODUCTS);
        collectionReferenceProducts.document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    dataBaseResult.onResult(false,"Deleted Successfully",null);
                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }

    public  static  void  updateProductById(Product product,DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReferenceProducts = db.collection(Constants.DB_PRODUCTS);
        collectionReferenceProducts.document(product.getId()).set(product).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    dataBaseResult.onResult(false,"Updated Successfully",null);
                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }

    public  static  void  updateOrderById(Order order,DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReferenceProducts = db.collection(Constants.DB_ORDERS);
        collectionReferenceProducts.document(order.getOrderid()).set(order).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    dataBaseResult.onResult(false,"Updated Successfully",null);
                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }
    public static   void  updateProfile(User user,DataBaseResult dataBaseResult)
    {

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReferenceUsers = db.collection(Constants.DB_USERS);
        collectionReferenceUsers.document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    dataBaseResult.onResult(false,"Account Updated",null);
                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }
    public static void  readAnalytics(String user_id,DataBaseResult dataBaseResult)
    {
        CollectionReference collectionReferenceAnalytics = FirebaseFirestore.getInstance().collection(Constants.DB_ANALYTICS);
        collectionReferenceAnalytics.document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        dataBaseResult.onResult(false,"Analytics Found",task.getResult().toObject(Analytics.class));

                    }
                    else
                    {
                        dataBaseResult.onResult(true,"No Analytics Found",null);

                    }

                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }


    public  static  void  loadProductsbyid(String id,DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_PRODUCTS);
        collectionReference.whereEqualTo("uid",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {

                    List<Product> productList=new ArrayList<>();
                    if(task.getResult().isEmpty())
                    {
                        dataBaseResult.onResult(true,"No Products Found",null);
                    }
                    else
                    {
                        QuerySnapshot result = task.getResult();
                        for(DocumentSnapshot documentSnapshot:result.getDocuments())
                        {
                            Product product = documentSnapshot.toObject(Product.class);
                            productList.add(product);
                        }

                        if(!productList.isEmpty())
                        {
                            dataBaseResult.onResult(false,"Products Found",productList);
                        }
                        else
                        {
                            dataBaseResult.onResult(true,"No Products Found",null);

                        }

                    }

                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }
    public  static  void  updateAnalyticVal(String uid,String fieldName,Long val)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_ANALYTICS);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(fieldName, FieldValue.increment(val));
        collectionReference.document(uid).update(hashMap);

    }

    public  static  void  loadAllProducts(DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_PRODUCTS);

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {

                    List<Product> productList=new ArrayList<>();
                    if(task.getResult().isEmpty())
                    {
                        dataBaseResult.onResult(true,"No Products Found",null);
                    }
                    else
                    {
                        QuerySnapshot result = task.getResult();
                        for(DocumentSnapshot documentSnapshot:result.getDocuments())
                        {
                            Product product = documentSnapshot.toObject(Product.class);
                            productList.add(product);
                        }

                        if(!productList.isEmpty())
                        {
                            dataBaseResult.onResult(false,"Products Found",productList);
                        }
                        else
                        {
                            dataBaseResult.onResult(true,"No Products Found",null);

                        }

                    }

                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }


    public  static  void  loadordersbyid(String id,DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_ORDERS);
        collectionReference.whereEqualTo("sellerid",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {

                    List<Order> orderArrayList=new ArrayList<>();
                    if(task.getResult().isEmpty())
                    {
                        dataBaseResult.onResult(true,"No Orders Found",null);
                    }
                    else
                    {
                        QuerySnapshot result = task.getResult();
                        for(DocumentSnapshot documentSnapshot:result.getDocuments())
                        {
                            Order order = documentSnapshot.toObject(Order.class);
                            orderArrayList.add(order);
                        }

                        if(!orderArrayList.isEmpty())
                        {
                            dataBaseResult.onResult(false,"Orders Found",orderArrayList);
                        }
                        else
                        {
                            dataBaseResult.onResult(true,"No Orders Found",null);

                        }

                    }

                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }

    public  static  void  loadOrderByIdAndStatus (String id, OrderStatus orderstatus, DataBaseResult dataBaseResult)
    {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_ORDERS);
        Query query = collectionReference.whereEqualTo("userid" , id);



        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {

                    List<Order> orderArrayList=new ArrayList<>();
                    if(task.getResult().isEmpty())
                    {
                        dataBaseResult.onResult(true,"No Orders Found",null);
                    }
                    else
                    {
                        QuerySnapshot result = task.getResult();
                        for(DocumentSnapshot documentSnapshot:result.getDocuments())
                        {
                            Order order = documentSnapshot.toObject(Order.class);

                            if(orderstatus==OrderStatus.PROCESSING)
                            {

                                if(order.getOrderStatus()!=OrderStatus.DELIVERED && order.getOrderStatus()!=OrderStatus.CANCELLED)
                                {
                                    orderArrayList.add(order);
                                }
                            }
                            if(orderstatus==OrderStatus.DELIVERED)
                            {
                               if(order.getOrderStatus()==OrderStatus.DELIVERED || order.getOrderStatus()==OrderStatus.CANCELLED)
                               {
                                   orderArrayList.add(order);
                               }
                            }
                        }

                        if(!orderArrayList.isEmpty())
                        {
                            dataBaseResult.onResult(false,"Orders Found",orderArrayList);
                        }
                        else
                        {
                            dataBaseResult.onResult(true,"No Orders Found",null);

                        }

                    }

                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });
    }
    public  static  void  addProduct(Product product,DataBaseResult dataBaseResult)
    {

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_PRODUCTS);
        DocumentReference document = collectionReference.document();
        product.setId(document.getId());
        document.set(product).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    dataBaseResult.onResult(false,"Product Uploaded Successfully",null);
                }
                else
                {
                    dataBaseResult.onResult(true,task.getException().getMessage(),null);
                }

            }
        });

    }


    public  static  void  addOrder (Order order, DataBaseResult dataBaseResult)
    {

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection(Constants.DB_ORDERS);
        DocumentReference document = collectionReference.document();
        order.setOrderid(document.getId());
        document.set(order).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete (@NonNull @NotNull Task<Void> task)
            {

                if(task.isSuccessful())
                {
                    dataBaseResult.onResult(false,"Order Uploaded Successfully",null);
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
            imageUploadListener.onUpload(false,"","https://cdn1.iconfinder.com/data/icons/user-pictures/101/malecostume-512.png");
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

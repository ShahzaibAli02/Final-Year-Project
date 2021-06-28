package com.example.digitalshop.Activities.Seller.Fragments;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.digitalshop.Enums.ProductAction;
import com.example.digitalshop.FireStoreDatabaseManager;
import com.example.digitalshop.Interfaces.DataBaseResult;
import com.example.digitalshop.Interfaces.ImageListener;
import com.example.digitalshop.Interfaces.ImageUploadListener;
import com.example.digitalshop.Model.Product;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.R;
import com.example.digitalshop.SharedPref;
import com.example.digitalshop.Utils.ProgressDialogManager;
import com.example.digitalshop.Utils.Util;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SellerAddProductFragment extends Fragment implements View.OnClickListener
{

    //Buttons
    Button  btnContactUs;
    Button  btnDismiss;
    Button  btnAddProduct;
    Button  btnClear;
    //Layouts
    LinearLayout linearLayoutHelp;
    //Spinners
    Spinner spinnerCategory;
    //EDitTexts
    EditText editTextName;
    EditText editTextPrice;
    EditText editTextDetail;
    TextView txtAddNewProduct;
    Button btndelete;
    int gridIndex=0;
    androidx.gridlayout.widget.GridLayout gridLayout;
    String [] images=new String[]{"","","","","",""};



     View view1;
    private static final String ARG_PARAM1 = "param1";
    private Product productToEdit;

    public SellerAddProductFragment () {
        // Required empty public constructor
    }
    public static SellerAddProductFragment newInstance (Product productToEdit ) {
        SellerAddProductFragment fragment = new SellerAddProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1 , productToEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            productToEdit = (Product) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_seller_add_product , container , false);
    }

    @Override
    public void onViewCreated (@NonNull @NotNull View view , @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        init(view);
        addItemInGrid();

        if(productToEdit!=null)
        {
            setVals();
        }
    }

    private void setVals ()
    {

        editTextName.setText(productToEdit.getName());
        editTextDetail.setText(productToEdit.getDetail());
        editTextPrice.setText(String.valueOf(productToEdit.getPrice()));
        images[0]=productToEdit.getImages().get(0);
        Picasso.get().load(productToEdit.getImages().get(0)).placeholder(R.drawable.loading_gif).into((ImageView) view1);


        txtAddNewProduct.setText("Edit Product");
        btnAddProduct.setText("Update");
        btndelete.setVisibility(View.VISIBLE);

    }

    private void init (@NotNull View view)
    {

        btnContactUs=view.findViewById(R.id.btnContactUs);
        btnDismiss=view.findViewById(R.id.btnDismiss);
        btnAddProduct=view.findViewById(R.id.btnAddProduct);
        btndelete=view.findViewById(R.id.btndelete);

        txtAddNewProduct=view.findViewById(R.id.txtAddNewProduct);

        btnClear=view.findViewById(R.id.btnClear);
        btnContactUs=view.findViewById(R.id.btnContactUs);

        linearLayoutHelp=view.findViewById(R.id.layoutHelp);

        gridLayout=view.findViewById(R.id.gridLayout);

        spinnerCategory=view.findViewById(R.id.spinnerCategory);

        editTextName=view.findViewById(R.id.editTextName);
        editTextPrice=view.findViewById(R.id.editTextPrice);
        editTextDetail=view.findViewById(R.id.editTextDetail);

        btndelete.setVisibility(View.GONE);

        for(View view1:new View[]{btnContactUs,btnDismiss,btnAddProduct,btnClear,btndelete})
            view1.setOnClickListener(this);
    }


    public  void  addItemInGrid()
    {

        view1 = getLayoutInflater().inflate(R.layout.layout_add_image ,gridLayout, false);

        View view2 = getLayoutInflater().inflate(R.layout.layout_add_plus_icon ,gridLayout, false);



        view1.setTag(gridIndex);


        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {

                selectImage(((ImageView)v), (Integer) v.getTag());
            }
        };



        if(gridIndex==0)
        {
            gridLayout.removeAllViews();
            gridLayout.addView(view1);
            gridLayout.addView(view2);

        }
        else if(gridIndex==4)
        {
            gridLayout.removeViewAt(gridIndex);
            gridLayout.addView(view1);

            View view3= getLayoutInflater().inflate(R.layout.layout_add_image ,gridLayout, false);
            view3.setTag(gridIndex+1);
            view3.setOnClickListener(onClickListener);
            gridLayout.addView(view3);
        }
        else
        {
            gridLayout.removeViewAt(gridIndex);
            gridLayout.addView(view1);
            gridLayout.addView(view2);
        }



        if(gridIndex!=5)
        {
            view2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v)
                {



                    addItemInGrid();

                }
            });

        }


        view1.setOnClickListener(onClickListener);
        gridIndex++;

    }

    private void selectImage (ImageView v , Integer tag)
    {

        Util.readImageFromGallery(getActivity() , new ImageListener()
        {
            @Override
            public void onImageLoaded (boolean error , Uri uri , Bitmap bitmap)
            {

                if(!error)
                {
                    Glide.with(getActivity()).asGif().load(R.drawable.loading_gif).into(v);

                    FireStoreDatabaseManager.uploadImage(uri , new ImageUploadListener()
                    {
                        @Override
                        public void onUpload (boolean Error , String Message , String url)
                        {

                            if(!Error)
                            {
                               Picasso.get().load(uri).into(v);
                               images[tag]=url;
                            }
                            else
                            {
                                Util.showCustomToast(getActivity(),Message,true);
                            }


                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick (View v)
    {

        if(v==btnAddProduct)
        {
            if(validate())
            {
                if(productToEdit==null)
                 uploadData(ProductAction.ADD);
                else
                  uploadData(ProductAction.UPDATE);
            }
        }

        if(v==btndelete)
        {
            uploadData(ProductAction.DELETE);
        }
        if(v==btnClear)
        {
            Clear();
        }
        if(v==btnContactUs)
        {
            Util.composeEmail(getActivity());
        }
        if(v==btnDismiss)
        {
            linearLayoutHelp.setVisibility(View.GONE);
        }


    }

    private void uploadData (ProductAction productAction)
    {

        AlertDialog progressDialog = ProgressDialogManager.getProgressDialog(getActivity(),new DoubleBounce(),R.color.colorPrimaryDark);
        progressDialog.show();
        Product product=getProduct();

        DataBaseResult dataBaseResult=new DataBaseResult()
        {
            @Override
            public void onResult (boolean error , String Message , Object data) {

                progressDialog.dismiss();
                Util.showCustomToast(getActivity(),Message,error);
                if(!error)
                {
                    Clear();
                }

            }
        };


        if(productAction==ProductAction.UPDATE)
        {
            FireStoreDatabaseManager.updateProductById(product ,dataBaseResult);
        }
        if(productAction==ProductAction.ADD)
        {
            FireStoreDatabaseManager.addProduct(product ,dataBaseResult);
        }
        if(productAction==ProductAction.DELETE)
        {
            FireStoreDatabaseManager.deleteProductById(product.getId() ,dataBaseResult);
        }




    }

    private Product getProduct ()
    {

        Product product=new Product();
        if(productToEdit!=null)
            product=productToEdit;

        User user= SharedPref.getUser(getActivity());
        product.setAddress(user.getAddress());
        product.setUploaderphone(user.getPhone());
        product.setUploadername(user.getName());
        product.setUploadershopname(user.getShopname());
        product.setUid(user.getUid());
        product.setName(editTextName.getText().toString());
        product.setDetail(editTextDetail.getText().toString());
        product.setCategory(spinnerCategory.getSelectedItem().toString());
        product.setPrice(Double.valueOf(editTextPrice.getText().toString()));
        product.setArrImages(images);
        product.setRating(0.0f);
        return product;
    }

    private boolean validate ()
    {
        if(Util.isEmpty(new EditText[]{editTextPrice,editTextName,editTextDetail}))
        {
            return false;
        }
        if(images[0].isEmpty())
        {
            Util.showSnackBar(getActivity(),"Please Select First Image");
            return false;
        }

        return true;
    }

    private void Clear ()
    {
        gridIndex=0;
        addItemInGrid();
        images=new String[]{"","","","",""};
        spinnerCategory.setSelection(0);
        btndelete.setVisibility(View.GONE);
        btnAddProduct.setText("Add Product");
        productToEdit=null;
        txtAddNewProduct.setText("Add New Product");
        for(EditText editText:new EditText[]{editTextPrice,editTextName,editTextDetail})
            editText.setText(null);

    }
}
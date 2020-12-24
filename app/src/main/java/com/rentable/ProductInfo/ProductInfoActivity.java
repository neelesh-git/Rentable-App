package com.rentable.ProductInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rentable.DataBaseHelper.IProductInfo;
import com.rentable.DataBaseHelper.IuserData;
import com.rentable.DataBaseHelper.ProductInfo;
import com.rentable.HomeScreen.HomeScreenNavigation.NavigationActivity;
import com.rentable.ProductCategory.BooksFeed;
import com.rentable.ProductCategory.CategoriesActivity;
import com.rentable.R;
import com.rentable.SessionContext;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;



public class ProductInfoActivity extends AppCompatActivity  {
    private EditText mItem,mItemDescription,mMinDeposit,mCost;
    private Button mProductInfoSubmit, mAddImage;
    private Spinner mCategory;
    private IProductInfo.CATEGORY  mCategoryValue;


    private boolean flag;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ImageView mImg, mImg2, mImg3, mImg1;
    int cam_num=1;
    Button mCamButton, mCamButton2, mCamButton3, mCamButton1, mUploadButton;
    ProgressBar mProgressBar;
    String currentPhotoPath;
    //ArrayList<String> mFilePaths = new ArrayList<>();
    HashMap<Integer, String> mFilePaths =  new HashMap<Integer,String>();
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    public void onBackPressed() {

            Intent intent = new Intent(ProductInfoActivity.this, NavigationActivity.class);
            startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        flag = true;
        mItem = findViewById(R.id.ItemID);
        mItemDescription = findViewById(R.id.ItemDescriptionID);
        mMinDeposit = findViewById(R.id.mindeposit);
        mCost = findViewById(R.id.cost);
        mProductInfoSubmit = findViewById(R.id.ProductInfoSubmit);
        mCategory = findViewById(R.id.category);

        mImg = findViewById(R.id.displayImageView);
        mImg1 = findViewById(R.id.displayImageView1);
        mImg2 = findViewById(R.id.displayImageView2);
        mImg3 = findViewById(R.id.displayImageView3);
        mCamButton = findViewById(R.id.cameraBtn);
        mCamButton1 = findViewById(R.id.cameraBtn1);
        mCamButton2 = findViewById(R.id.cameraBtn2);
        mCamButton3 = findViewById(R.id.cameraBtn3);
        mAddImage = findViewById(R.id.add_image);

        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");

        mCamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_num=1;
                askCamPermission(cam_num);
            }
        });

        mCamButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_num=2;
                askCamPermission(cam_num);
            }
        });

        mCamButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_num=3;
                askCamPermission(cam_num);
            }
        });

        mCamButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_num=4;
                askCamPermission(cam_num);
            }
        });




        //Spinner styling
        ArrayAdapter<IProductInfo.CATEGORY> dataAdapter;
        dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,IProductInfo.CATEGORY.values());
        //Dstyling dropdown layout
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(dataAdapter);

        Intent intent = getIntent();
        String filter = intent.getStringExtra("LABELTYPE");

        if(filter != null) {
            int spinnerPosition = IProductInfo.CATEGORY.valueOf(filter).ordinal();

            Log.e("hemanth", filter);


            mCategory.setSelection(spinnerPosition);
        }

        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose a Category")){
                    //do nothing
                }else{
                    mCategoryValue = (IProductInfo.CATEGORY )parent.getItemAtPosition(position);


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mProductInfoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("hello111", "entered upload file ");
                uploadFile();

            }
        });
        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductInfoActivity.this, CamLabel.class);
                startActivity(intent);
            }
        });


    }

    private void uploadFile(){
        if(mFilePaths.size() == 0){
            Toast.makeText(ProductInfoActivity.this, "Please capture images to upload", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (checkProdectDetails()) {
                int flag = 0;
                for (Map.Entry mapElement : mFilePaths.entrySet()) {
                    String key = (String) mapElement.getValue();

                    File f = new File(key);

                    if (f != null) {
                        final String url = System.currentTimeMillis() + "." + "jpg";
                        StorageReference fileReference = mStorageRef.child(url);
                        //System.out.println("URL!!!!!!!! " + fileReference.getDownloadUrl().toString());

                        fileReference.putFile(Uri.fromFile(f))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    //mProgressBar.setProgress(0);
                                                                }
                                                            }, 2
                                        );

                                        Toast.makeText(ProductInfoActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        uploadImages(url);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProductInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                //mProgressBar.setProgress((int) progress);

                            }
                        });

                    } else {
                        Toast.makeText(this, "Capture images to upload", Toast.LENGTH_SHORT).show();
                        //return;
                    }
                }

            }
        }
        Intent intent = new Intent(ProductInfoActivity.this, NavigationActivity.class);
        startActivity(intent);
    }

    private void askCamPermission(int cam_num) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent(cam_num);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent(cam_num);
            } else {
                Toast.makeText(this, "Camera permission is required to use camera.", Toast.LENGTH_SHORT).show();
                //return;
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File((mFilePaths.get(cam_num)));
                if (cam_num == 1)
                    Picasso.get().load(f).into(mImg);
                //mImg.setImageURI(Uri.fromFile(f));
                if (cam_num == 2)
                    Picasso.get().load(f).into(mImg1);
                //mImg1.setImageURI(Uri.fromFile(f));
                if (cam_num == 3)
                    Picasso.get().load(f).into(mImg2);
                //mImg2.setImageURI(Uri.fromFile(f));
                if (cam_num == 4)
                    Picasso.get().load(f).into(mImg3);
                //mImg3.setImageURI(Uri.fromFile(f));
            }
        }
    }

    private File createImageFile(int cam_num) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mFilePaths.put(cam_num,image.getAbsolutePath());
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int cam_num) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(cam_num);
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.rentable.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private boolean checkProdectDetails() {
        final String item = mItem.getText().toString().trim();
        final String cost = mCost.getText().toString().trim();
        final String mindeposit = mMinDeposit.getText().toString().trim();


        if (TextUtils.isEmpty(item)) {
            Toast.makeText(ProductInfoActivity.this, "Product Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(cost)) {
            Toast.makeText(ProductInfoActivity.this, "Cost cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mindeposit)) {
            Toast.makeText(ProductInfoActivity.this, "Minimum Deposit cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void uploadImages(String imgUrl){
        final IuserData userdata = SessionContext.getUserData();
        final String pattern = "yyyy-MM-dd";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        final String item = mItem.getText().toString().trim();
        final String cost = mCost.getText().toString().trim();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads/" + imgUrl);
        if (flag) {
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    // Pass it to Picasso to download, show in ImageView and caching

                    Log.e("CHECK:::::::::", "onSuccess: " + uri.toString());
                    String date = simpleDateFormat.format(new Date());
                    String productId = userdata.generateProductId();
                    ProductInfo productInfo = new ProductInfo(productId, item, cost, date, mCategoryValue, uri.toString(), SessionContext.getEmail().split("@")[0]);
                    userdata.addNewProduct(productId);
                    SessionContext.addProductInfo(productInfo, productId);
                    flag = false;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }

            });

        }
    }
}
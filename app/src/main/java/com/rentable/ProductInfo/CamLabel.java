package com.rentable.ProductInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.rentable.HomeScreen.HomeScreenNavigation.NavigationActivity;
import com.rentable.R;
import com.rentable.ViewProduct.RentActivity;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class CamLabel extends AppCompatActivity {
    String mLabel;

    CameraView cameraView;
    Button btnDetect;
    AlertDialog waitingDialog;

    @Override
    protected void onResume(){
        Log.e("CamLabel","onResume");
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause(){
        Log.e("CamLabel","onPause");
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CamLabel.this, ProductInfoActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("CamLabel","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cam_label);

        cameraView = (CameraView) findViewById(R.id.camera_view);
        btnDetect = (Button) findViewById(R.id.btn_detect);
        waitingDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Detecting...")
                .setCancelable(false).build();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
                Log.e("CamLabel","onEvent");

            }

            @Override
            public void onError(CameraKitError cameraKitError) {
                Log.e("CamLabel","onError");

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Log.e("CamLabel","onImage");
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();
                runDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
                Log.e("CamLabel","onVideo");

            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();

            }
        });
    }

    private void runDetector(Bitmap bitmap) {
        Log.e("CamLabel","runDetector");
        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(boolean internet) {
                Log.e("CamLabel","internetcheck");
                /*if(internet){
                    //if the internet is available, use cloud
                    Log.e("CamLabel","Cloud");
                    FirebaseVisionCloudImageLabelerOptions options = new FirebaseVisionCloudImageLabelerOptions.Builder()
                            .setConfidenceThreshold(0.7f)
                            .build();
                    FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
                            .getCloudImageLabeler(options);

                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>(){
                                @Override
                                public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels){
                                    Log.e("CamLabel","onSuccess");
                                    processDataResult(firebaseVisionImageLabels);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("CamLabel","onFail");
                                    Log.d("EDMTERROR", e.getMessage());
                                    //Log.d("EDMTERROR",e.getMessage());
                                }
                            });

                } */
                //else{
                Log.e("CamLabel","ondevice");
                FirebaseVisionOnDeviceImageLabelerOptions options = new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.6f)
                        .build();
                FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler(options);

                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                                Log.e("CamLabel","onSuccess");
                                processDataResult(firebaseVisionImageLabels);
                                Intent intent = new Intent(CamLabel.this, ProductInfoActivity.class);
                                intent.putExtra("LABELTYPE", mLabel);
                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("EDMTERROR", e.getMessage());
                                //Log.d("EDMTERROR",e.getMessage());
                            }
                        });

                // }

            }
        });

    }

    private void processDataResult(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
        Log.e("CamLabel","processDataResult");
        //for(FirebaseVisionImageLabel label: firebaseVisionImageLabels){

        //    Toast.makeText(this, "Cloud result: "+label.getText(), Toast.LENGTH_SHORT).show();
        //}
        FirebaseVisionImageLabel label = firebaseVisionImageLabels.get(0);
        mLabel = label.getText();
        mLabel.toUpperCase();
        if(mLabel.equalsIgnoreCase("MOBILE PHONE"))
            mLabel="MOBILE_PHONES";
        else if(mLabel.equalsIgnoreCase("MUSICAL INSTRUMENT"))
            mLabel="LAPTOPS";
        else if (mLabel.equalsIgnoreCase("VEHICLE"))
            mLabel="VEHICLES";
        else
            mLabel="ELECTRONICS";

        Log.e("Label: " , mLabel );
        if(waitingDialog.isShowing())
            waitingDialog.dismiss();
    }
}

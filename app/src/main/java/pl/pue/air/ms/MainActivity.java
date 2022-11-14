package pl.pue.air.ms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class MainActivity extends AppCompatActivity {


    EditText editText;
    Button button;
    ImageView imageView;

    private boolean mHasFlash;





    Switch flashControl;
    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // First check if device is supporting flashlight
        mHasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!mHasFlash) {
            // show message device doesn't support flash
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(getResources().getString(R.string.error));
            alert.setMessage(getResources().getString(R.string.message));
        }
        //






        editText = (EditText) findViewById(R.id.edittext);
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageview);

        flashControl = findViewById(R.id.switchFlashLight);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Toast.makeText(MainActivity.this, "This device has flash", Toast.LENGTH_SHORT).show();
                flashControl.setEnabled(true);
            } else {
                Toast.makeText(MainActivity.this, "This device has no flash", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "This device has no camera", Toast.LENGTH_SHORT).show();
        }
        flashControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        cameraManager.setTorchMode("0", false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    flashControl.setText("Flash ON");
                } else {
                    try {
                        cameraManager.setTorchMode("0", true);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    flashControl.setText("Flash OFF");

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE, 500, 500);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}



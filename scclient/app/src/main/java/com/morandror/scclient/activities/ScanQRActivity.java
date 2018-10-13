package com.morandror.scclient.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.morandror.scclient.R;
import com.morandror.scclient.models.QrCodeData;
import com.morandror.scclient.utils.JsonHandler;

import java.io.IOException;

public class ScanQRActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    QrCodeData qrCodeData;
    int closetId;

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        closetId = getIntent().getIntExtra(getString(R.string.closet_id), 0);

        surfaceView = (SurfaceView) findViewById(R.id.scannerView);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    String[] permissions = {Manifest.permission.CAMERA};

                    requestPermissions(permissions, 1);
                }

                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();

                if(qrCode.size() != 0){
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            qrCodeData = (QrCodeData) JsonHandler.getInstance().fromString(qrCode.valueAt(0).displayValue, QrCodeData.class);

                            Intent addItem = new Intent(getBaseContext(), AddItemActivity.class);
                            addItem.putExtra(getString(R.string.closet_id), closetId);
                            addItem.putExtra(getString(R.string.qrData), qrCodeData);
                            addItem.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(addItem);
                        }
                    };

                    runOnUiThread(r);
                }
            }
        });
    }
}

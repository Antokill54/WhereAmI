package com.example.whereami.whereami;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.whereami.whereami.CameraEngine;
import com.example.whereami.whereami.FocusBoxView;
import com.example.whereami.whereami.Tools;
import com.example.whereami.whereami.TessAsyncEngine;
/**
 * Created by Quentin on 11/12/2014.
 */
public class CaptureActivity  extends Activity implements SurfaceHolder.Callback, View.OnClickListener,
        Camera.PictureCallback, Camera.ShutterCallback {

    static final String TAG = "DBG_" + CaptureActivity.class.getName();

    Button shutterButton;
    Button focusButton;
    FocusBoxView focusBox;
    SurfaceView cameraFrame;
    CameraEngine cameraEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        //on force l'orientation portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(TAG, "Surface Created - starting camera");

        if (cameraEngine != null && !cameraEngine.isOn()) {
            cameraEngine.start();
        }

        if (cameraEngine != null && cameraEngine.isOn()) {
            Log.d(TAG, "Camera engine already on");
            return;
        }

        cameraEngine = CameraEngine.New(holder);
        cameraEngine.setContext(this);
        cameraEngine.start();

        Log.d(TAG, "Camera engine started");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraFrame = (SurfaceView) findViewById(R.id.camera_frame);
        shutterButton = (Button) findViewById(R.id.shutter_button);
        focusBox = (FocusBoxView) findViewById(R.id.focus_box);
        focusButton = (Button) findViewById(R.id.focus_button);

        shutterButton.setOnClickListener(this);
        focusButton.setOnClickListener(this);

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraFrame.setOnClickListener(this);

        if (cameraEngine != null && cameraEngine.isOn()) {
            cameraEngine.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (cameraEngine != null && cameraEngine.isOn()) {
            cameraEngine.stop();
        }

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.removeCallback(this);
    }

    @Override
    public void onClick(View v) {
        if(v == shutterButton){
            if(cameraEngine != null && cameraEngine.isOn()){
               // focusBox.adjustBox(); //on décale la box
                cameraEngine.takeShot(this, this, this);
            }
        }

        if(v == focusButton){
            if(cameraEngine!=null && cameraEngine.isOn()){
                cameraEngine.requestFocus();
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        Log.d(TAG, "Picture taken");

        if (data == null) {
            Log.d(TAG, "Got null data");
            return;
        }

        Bitmap bmp = Tools.getFocusedBitmap(this, camera, data, focusBox.getBox());

        Log.d(TAG, "Got bitmap");

        Log.d(TAG, "Initialization of TessBaseApi");

        new TessAsyncEngine().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, this, bmp);

    }

    @Override
    public void onShutter() {

    }

}
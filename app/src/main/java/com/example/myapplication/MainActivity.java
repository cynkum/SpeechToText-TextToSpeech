package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Queue;
import java.util.LinkedList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private ImageView micButton;
    private EditText editText;
    private String inputValue;
    private String TAG = "GeneratedQRCode";
    private ImageView qrImage;
    private AppCompatActivity activity;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private ImageView failed;
    private ImageView textFailed;
    Queue<Object> antrian = new LinkedList<>();
    private TextToSpeech textToSpeech;
    private EditText enterText;
    private SeekBar seekBarPitch;
    private SeekBar seekBarSpeed;
    private Button buttonSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
//---------------------------------------------------TEXT-TO-SPEECH-----------------------------------------------------------------------------
        buttonSpeak = findViewById(R.id.button_speak);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = textToSpeech.setLanguage(new Locale("id", "ID"));

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        buttonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        enterText = findViewById(R.id.edit_text);
        seekBarPitch = findViewById(R.id.seek_bar_pitch);
        seekBarSpeed = findViewById(R.id.seek_bar_speed);
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

//---------------------------------------------------SPEECH-TO-TEXT-----------------------------------------------------------------------------
        qrImage = findViewById(R.id.qr_image);
        activity = this;
        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.button);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//mulai activity yg meminta user berbicara dan mengirim melalui speech recognizer
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                //editText.setText("");
                editText.setText("Listening...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                editText.setText("");
            }

            @Override
            public void onError(int error) {
                //editText.setText("WARNING: Your voice isn't clear");
            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.mic);

                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));
                if (data.contains("satu")) {
                    inputValue = editText.getText().toString().trim();
                    if (inputValue.length() > 0 && failed == null && textFailed == null) {
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

                        try {
                            bitmap = qrgEncoder.encodeAsBitmap();
                            qrImage.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            Log.v(TAG, e.toString());
                        }
                    }else {
                        failed.setImageDrawable(null);
                        textFailed.setImageDrawable(null);
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

                        try {
                            bitmap = qrgEncoder.encodeAsBitmap();
                            qrImage.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            Log.v(TAG, e.toString());
                        }
                    }

                    }else if (data.contains("dua")) {
                    inputValue = editText.getText().toString().trim();
                    if (inputValue.length() > 0 && failed == null && textFailed == null) {
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

                        try {
                            bitmap = qrgEncoder.encodeAsBitmap();
                            qrImage.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            Log.v(TAG, e.toString());
                        }
                    }else {
                        failed.setImageDrawable(null);
                        textFailed.setImageDrawable(null);
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 4;

                        qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

                        try {
                            bitmap = qrgEncoder.encodeAsBitmap();
                            qrImage.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            Log.v(TAG, e.toString());
                        }
                    }
                }else if (data.contains("tiga")) {
                        inputValue = editText.getText().toString().trim();
                        if (inputValue.length() > 0 && failed == null && textFailed == null) {
                            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                            Display display = manager.getDefaultDisplay();
                            Point point = new Point();
                            display.getSize(point);
                            int width = point.x;
                            int height = point.y;
                            int smallerDimension = width < height ? width : height;
                            smallerDimension = smallerDimension * 3 / 4;

                            qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

                            try {
                                bitmap = qrgEncoder.encodeAsBitmap();
                                qrImage.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                Log.v(TAG, e.toString());
                            }
                        } else {
                            failed.setImageDrawable(null);
                            textFailed.setImageDrawable(null);
                            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                            Display display = manager.getDefaultDisplay();
                            Point point = new Point();
                            display.getSize(point);
                            int width = point.x;
                            int height = point.y;
                            int smallerDimension = width < height ? width : height;
                            smallerDimension = smallerDimension * 3 / 4;

                            qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

                            try {
                                bitmap = qrgEncoder.encodeAsBitmap();
                                qrImage.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                Log.v(TAG, e.toString());
                            }
                        }
                    } else {
                        qrImage.setImageDrawable(null);
                        inputValue = editText.getText().toString().trim();
                        if (inputValue.length() > 0) {
                            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                            Display display = manager.getDefaultDisplay();
                            Point point = new Point();
                            display.getSize(point);
                            int width = point.x;
                            int height = point.y;
                            int smallerDimension = width < height ? width : height;
                            smallerDimension = smallerDimension * 3 / 4;
                            failed = findViewById(R.id.failed);
                            failed.setImageResource(R.drawable.failed);
                            textFailed = findViewById(R.id.textfailed);
                            textFailed.setImageResource(R.drawable.textfailed);

                        }

                    }
                }
            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        //ngasitau kalo user nahan image trus sistem akan merekam
        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.mic);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });
    }

    //-----------------------TEXT-TO-SPEECH-----------------------------
    private void speak(){
        String text = enterText.getText().toString();
        float pitch = (float) seekBarPitch.getProgress()/50;
        if(pitch<0.1) pitch = 0.1f;
        float speed = (float) seekBarSpeed.getProgress()/50;
        if(speed<0.1) speed = 0.1f;

        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    //-------------------------------------------------------------------
    @Override
    protected void onDestroy() {
        //memberitahu service kalau sudah tidak dipakai
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
        speechRecognizer.destroy();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

}

//    findViewById(R.id.generate_barcode).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                inputValue = editText.getText().toString().trim();
//                if (inputValue.length() > 0) {
//                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
//                    Display display = manager.getDefaultDisplay();
//                    Point point = new Point();
//                    display.getSize(point);
//                    int width = point.x;
//                    int height = point.y;
//                    int smallerDimension = width < height ? width : height;
//                    smallerDimension = smallerDimension * 3 / 4;
//
//                    qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
//
//                    try {
//                        bitmap = qrgEncoder.encodeAsBitmap();
//                        qrImage.setImageBitmap(bitmap);
//                    } catch (WriterException e) {
//                        Log.v(TAG,e.toString());
//                    }
//                } else {
//                    editText.setError(getResources().getString(R.string.value_required));
//                }
//            }
//        });
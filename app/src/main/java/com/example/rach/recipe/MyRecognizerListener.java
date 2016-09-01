package com.example.rach.recipe;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rach on 2016/9/1.
 */


public class MyRecognizerListener extends Service implements RecognitionListener {
    private TextView textView2;

    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("RECOGNIZER", "onCreate");
        super.onStart(intent, startId);
    }

    @Override
    public void onResults(Bundle results) {
        List<String> resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        StringBuffer sb = new StringBuffer();
        for(String res: resList) {
            sb.append(res + "\n");
            break;
        }
        Log.d("RECOGNIZER", "onResults: " + sb.toString());
    }

    @Override
    public void onError(int error) {
        Log.d("RECOGNIZER", "Error Code: " + error);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }


}

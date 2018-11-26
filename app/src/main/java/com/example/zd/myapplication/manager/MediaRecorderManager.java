package com.example.zd.myapplication.manager;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class MediaRecorderManager {
    public static final String TAG = "MediaRecorderManager";
    private MediaRecorder mediaRecorder;
    private boolean isRecording;
    public static MediaRecorderManager mInstance;

    private MediaRecorderManager() {
        mediaRecorder = new MediaRecorder();
        // 设置音频录入源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录制音频的输出格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                // 发生错误，停止录制
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording=false;
            }
        });
    }

    public static MediaRecorderManager getInstance() {
        if (mInstance == null) {
            synchronized (MediaRecorderManager.class) {
                if (mInstance == null) {
                    mInstance = new MediaRecorderManager();
                }
            }
        }
        return mInstance;
    }

    public void startRecorder(String path) {
        try {
            File file = new File(path);
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecorder() {
        if (isRecording) {
            // 如果正在录音，停止并释放资源
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording=false;
        }
    }

}

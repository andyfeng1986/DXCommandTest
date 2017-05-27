package com.example.fenglei.dexcommendtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.dx.command.Main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by fenglei on 17/5/26.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.tv);
        Test test = new Test();
        tv.setText(test.hello());
        testDX();
    }

    private void testDX() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                prepareFile();
                executeDX();
            }
        });
    }

    private void prepareFile() {
        clearFilesDir();
        getFromAssets("mainactivity.txt");
        getFromAssets("test.txt");
        getFromAssets("hello.txt");
    }

    private void clearFilesDir() {
        File fileDirFile = getFilesDir();
        for(File file : fileDirFile.listFiles()) {
            if(file.exists()) {
                file.delete();
            }
        }
    }

    public void getFromAssets(String fileName){
        try {
            InputStream is= getResources().getAssets().open(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            String newfileName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1, fileName.length());
            newfileName = newfileName.replaceAll("txt", "class");
            File rawFile = new File(getFilesDir(), newfileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(rawFile);
                byte[] buffer = new byte[1024];
                int byteread;
                while ((byteread = bufferedInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, byteread);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bufferedInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeDX() {
        List<String> argList = new ArrayList<>();
        argList.add("--dex");
        argList.add("--no-strict");
        argList.add("--output=" + getFilesDir().getAbsolutePath() + "/Hello.dex");
        argList.add(getFilesDir().getAbsolutePath() + "/Hello.class");
//        argList.add("--help");
        String[] args = new String[argList.size()];
        argList.toArray(args);
        Main.main(args);
    }

}

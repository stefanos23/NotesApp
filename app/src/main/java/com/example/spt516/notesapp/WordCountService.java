package com.example.spt516.notesapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.Toast;

public class WordCountService extends Service {

    //protected MusicPlayer player = null;
    protected LocalBinder binder = new LocalBinder();
    Intent intent;

    public class LocalBinder extends Binder {

        public WordCountService getService() {
            return WordCountService.this;
        }

    }

    public WordCountService() {
    }

    @Override
    public IBinder onBind(Intent intent) {


        return binder;
    }

    @Override
    public void onCreate() {


    }



    private void toast(String sentence){
        Toast toast = Toast.makeText(
                this,
                sentence,
                Toast.LENGTH_LONG ); // Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int sid) {
        if ("stop".equals(intent.getAction())) {
            this.intent = intent;



        }
        return super.onStartCommand(intent, flags, 1);
    }


    public int count(String[] temp){
        //String temp[] = (String[])intent.getExtras().get("text");


        String las = "asd";
        int count=0;
        for(String a:temp){

            String words[]=a.split("[\\s|,|.]+");
            count += words.length;
        }

        return count;
    }
}

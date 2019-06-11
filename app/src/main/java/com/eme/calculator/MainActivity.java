package com.eme.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import cheema.calculater.CalculatorLib.SrbCalculator;
import cheema.calculater.CalculatorLib.Token;
import cheema.calculater.mymagic.KeyTables;
import cheema.calculater.mymagic.Magic;

import static cheema.calculater.CalculatorLib.Token.lastToken;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myinit();
        setListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void myinit(){
        this.activity=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RAD");
        toolbar.setTitleTextColor(Color.rgb(119,119,119));
        setSupportActionBar(toolbar);

        handler = new Handler();

        ansTv = (TextView)findViewById(R.id.ansTv);
        qusTv = (TextView)findViewById(R.id.qusTv);

        eqlBtn = (Button)findViewById(R.id.eqlBtn);
        delBtn = (Button)findViewById(R.id.delBtn);
        decBtn = (Button)findViewById(R.id.decBtn);

        magic = new Magic();

        toggleDynamic = true;

        asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getSupportActionBar().show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getSupportActionBar().hide();
                asyncTask=null;
            }
        };
        asyncTask.execute();

        stopWatch = new Thread(new Runnable() {
            @Override
            public void run() {
                for(counter = 0; counter<80; counter++) {
                    if(lock.get()) {
                        synchronized(stopWatch) {
                            try {
                                stopWatch.wait();
                            } catch (InterruptedException e) {}
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //     ansTv.append(Integer.toString(i) + ", ");
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {}
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setListeners(){
        menuviewListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportActionBar().isShowing()){
                    getSupportActionBar().hide();
                }
                else if(asyncTask!=null){
                    synchronized(stopWatch)
                    {
                        counter=0;
                        getSupportActionBar().show();
                    }
                }
                else{
                    asyncTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if(stopWatch.isAlive())
                                stopWatch.stop();
                            stopWatch.start();
                            while (stopWatch.isAlive()){}
                            return null;
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            getSupportActionBar().show();
                        }


package com.example.benz.mecamera;

import android.app.AlertDialog;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import me.leolin.shortcutbadger.ShortcutBadger;
import q.rorbin.badgeview.QBadgeView;

public class StartService extends JobService {

    public String token = FirebaseInstanceId.getInstance().getToken(); // get Token

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started"); //แสดงใน log เมื่อ job เริ่มทำงาน
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (jobCancelled) {
                    return;
                }

                try {
                    Thread.sleep(50000); // ทำงานทุกๆ 5 นาที
                    Log.d("Job", "Job finished");

                     UpdateToken();

                    run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                jobFinished(params, false);
            }
        }).start();
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    private void UpdateToken() {

        sharedpreferences = this.getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrlNotistore();

        final String Token = FirebaseInstanceId.getInstance().getToken();

        /// Update Token
        Ion.with(this)

                .load(Url + "UpdateToken.php")

                .setMultipartParameter("id_user", id_user)
                .setMultipartParameter("token", Token) //Token

                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                    }
                });


    }
}



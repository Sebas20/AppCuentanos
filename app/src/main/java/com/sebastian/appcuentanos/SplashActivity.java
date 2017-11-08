package com.sebastian.appcuentanos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sebastian.my_app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        ImageView img1 = (ImageView) findViewById(R.id.gif_logo_view);
        img1.setBackgroundResource(R.drawable.gif_logo);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img1.getBackground();
        frameAnimation.start();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ///Firebase
                FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                if (User != null) {
                    goMainActivity();
                }else {
                    goLoginActivity();
                }
                ///////////
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void goMainActivity() {
        Intent intent = new Intent(SplashActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }


}

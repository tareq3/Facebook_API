package com.mti.crazy_log;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;

public class KeyHash extends Activity {
    public void generateKeyHash(){
        try {
            PackageInfo packageInfo=getPackageManager().getPackageInfo("com.mti.crazy_log",PackageManager.GET_SIGNATURES);

            for(Signature signatureInfo :packageInfo.signatures){

                MessageDigest messageDigest=MessageDigest.getInstance("SHA");
                messageDigest.update(signatureInfo.toByteArray());
                Log.d("" +getClass().getName(), "KEY_HASH"+ Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

package com.grocerycustomer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.grocerycustomer.R;
import com.grocerycustomer.utils.SessionManager;
import com.grocerycustomer.utils.Utiles;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;

public class FirstActivity extends ActivityManagePermission {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sessionManager = new SessionManager(FirstActivity.this);
        new Handler().postDelayed(() -> {
            if (Utiles.internetChack()) {
                if (sessionManager.getBooleanData(SessionManager.login) || sessionManager.getBooleanData(SessionManager.isopen)) {
                    Intent i = new Intent(FirstActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(FirstActivity.this, InfoActivity.class);
                    startActivity(i);
                }
                finish();
            } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setMessage("Please Check Your Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("Exit", (dialog, id) -> {
                            Log.e("tem",dialog+""+id);
                            finish();
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }, 2000);

    }


}

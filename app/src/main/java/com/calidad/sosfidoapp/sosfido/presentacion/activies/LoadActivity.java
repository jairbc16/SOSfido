package com.calidad.sosfidoapp.sosfido.presentacion.activies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.calidad.sosfidoapp.sosfido.data.repositories.local.SessionManager;
import com.calidad.sosfidoapp.sosfido.R;

public class LoadActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        sessionManager = new SessionManager(getApplicationContext());
        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    verifyToken();

                } catch (Exception e) {
                    //nothing
                }
            }
        };
        t.start();
    }

    private void verifyToken() {
        if (sessionManager.isLogin()) {
            openActivity(HomeActivity.class);
        } else {
            openActivity(LoginActivity.class);
        }
    }

    private void openActivity(Class<?> activity) {

        Intent i = new Intent(LoadActivity.this, activity);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

package com.calidad.sosfidoapp.sosfido.Presentacion.Activies;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.calidad.sosfidoapp.sosfido.Presentacion.Fragments.ProfileFragment;
import com.calidad.sosfidoapp.sosfido.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ProfileFragment fragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.body);
        if(fragment==null){
            fragment = ProfileFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.body,fragment);
            transaction.commit();
        }
    }
    public void showMessageError(String message) {
        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        this.showMessageSnack(container, message, R.color.error_red);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void showMessageSnack(View container, String message, int colorResource) {
        if (container != null) {
            Snackbar snackbar = Snackbar
                    .make(container, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, colorResource));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG);

            toast.show();
        }

    }
    public void returnResult(){
        setResult(Activity.RESULT_OK);
    }
}

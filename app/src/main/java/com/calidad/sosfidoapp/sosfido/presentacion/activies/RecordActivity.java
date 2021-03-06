package com.calidad.sosfidoapp.sosfido.presentacion.activies;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.calidad.sosfidoapp.sosfido.presentacion.fragments.RecordFragment;
import com.calidad.sosfidoapp.sosfido.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar tb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        ButterKnife.bind(this);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecordFragment fragment = (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.body);
        if (fragment == null) {
            fragment = RecordFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.body, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

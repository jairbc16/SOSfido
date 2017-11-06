package com.calidad.sosfidoapp.sosfido.presentacion.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calidad.sosfidoapp.sosfido.R;

import butterknife.ButterKnife;

/**
 * Created by jairbarzola on 28/09/17.
 */

public class RecordFragment extends Fragment {


    public RecordFragment() {}
    public static RecordFragment newInstance() {return new RecordFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
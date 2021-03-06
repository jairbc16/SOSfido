package com.calidad.sosfidoapp.sosfido.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.calidad.sosfidoapp.sosfido.presentacion.fragments.ProfileFragment;
import com.calidad.sosfidoapp.sosfido.presentacion.fragments.RegisterFragment;
import com.calidad.sosfidoapp.sosfido.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by viniciusthiengo on 2/29/16.
 */
public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    // key
    public static final String FRAGMENT_KEY = "com.calidad.sosfidoapp.sosfido";
    @BindView(R.id.ln_gallery) ImageView lnGallery;
    @BindView(R.id.ln_camera) ImageView lnCamera;
    public ProfileFragment fragment2;
    public RegisterFragment fragment1;
    private int id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container);
        id= getArguments().getInt("IdFragment",1);
        ButterKnife.bind(this,view);
        return(view);
    }
    @OnClick({R.id.ln_camera,R.id.ln_gallery})
    public void onClickedView(View view){
                if(view.getId()==R.id.ln_camera) {
                    if (id == 1) {
                        fragment1 = (RegisterFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.body);
                        if (fragment1 != null) {
                            fragment1.openCamera();
                            dismiss();
                        }
                    } else {
                        fragment2 = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.body);
                        if (fragment2 != null) {
                            fragment2.openCamera();
                            dismiss();
                        }
                    }
                }else {
                    if (id == 1) {
                        fragment1 = (RegisterFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.body);
                        if (fragment1 != null) {
                            fragment1.openGallery();
                            dismiss();
                        }
                    } else {
                        fragment2 = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.body);
                        if (fragment2 != null) {
                            fragment2.openGallery();
                            dismiss();
                        }
                    }
                }

    }
}

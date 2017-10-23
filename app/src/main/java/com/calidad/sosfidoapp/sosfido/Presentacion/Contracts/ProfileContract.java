package com.calidad.sosfidoapp.sosfido.Presentacion.Contracts;

import com.calidad.sosfidoapp.sosfido.Data.Entities.PersonEntity;

/**
 * Created by jairbarzola on 2/10/17.
 */

public interface ProfileContract {

    //interfaces para el modelo Vista-Presentador
    interface View {
        void loadUser(PersonEntity personEntity,boolean t);
        void setLoadingIndicator(boolean active);
        void setMessageError(String error);
        void updateNav();
    }
    interface Presenter {
        void start();
        void uploadPhoto(String path);
        void changePhoto(String path);
    }
}
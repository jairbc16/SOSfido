package com.calidad.sosfidoapp.sosfido.Presentacion.Contracts;

import com.calidad.sosfidoapp.sosfido.Data.Entities.PersonEntity;

/**
 * Created by Jair Barzola on 22-Oct-17.
 */

public interface RegisterContract {
    //interfaces para el modelo Vista-Presentador
    interface View {
        void setLoadingIndicator(boolean active);
        void setMessageError(String error);
        void setDialogMessage(String message);
    }
    interface Presenter {
        void start(String location,String latitud,String longitude,String description,String image,String name,String phone);

    }
}

package com.calidad.sosfidoapp.sosfido.presentacion.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.*;
import android.widget.*;
import com.calidad.sosfidoapp.sosfido.data.entities.*;
import com.calidad.sosfidoapp.sosfido.data.entities.ResponseReport;
import com.calidad.sosfidoapp.sosfido.presentacion.activies.DetailMarkerActivity;
import com.calidad.sosfidoapp.sosfido.presentacion.activies.HomeActivity;
import com.calidad.sosfidoapp.sosfido.presentacion.contracts.HomeContract;
import com.calidad.sosfidoapp.sosfido.presentacion.presenters.HomePresenterImpl;
import com.calidad.sosfidoapp.sosfido.R;
import com.calidad.sosfidoapp.sosfido.utils.ProgressDialogCustom;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import java.util.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements OnMapReadyCallback, HomeContract.View,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMarkerClickListener {

    @BindView(R.id.mv_show_reports)
    MapView mapView;
    private GoogleMap googleMap;
    private ProgressDialogCustom mProgressDialogCustom;
    private HomeContract.Presenter presenter;
    private double latitude;
    private double longitude;
    private Unbinder unbinder;
    private final int CODE_LOCATION = 123;
    public int zoom = 16;
    public int size_maker = 95;
    public int min_time = 1200;
    public int min_distance = 0;
    public HashMap<Marker, ReportEntity> hashMapReport = new HashMap<Marker, ReportEntity>();
    private Timer timer;
    public LocationListener locListener;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);
        mProgressDialogCustom = new ProgressDialogCustom(getActivity(), "Actualizando...");
        presenter = new HomePresenterImpl(this, getContext());
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        int delay = 5000; // delay for 0 sec.
        int period = 15000; // repeat every 10 sec.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                presenter.loadReports();
            }
        }, delay, period);
        //control del gps
        locListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                // mover market real time ActualizarUbicacion(location);/ setLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //nothing
            }

            @Override
            public void onProviderEnabled(String s) {
                message("GPS Activado");
            }

            @Override
            public void onProviderDisabled(String s) {
                locationStart();
                message("GPS Desactivado");
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new CustomWindowAdapter());
        presenter.loadReports();
        myUbication();
    }


    private void myUbication() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_LOCATION);
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            actualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, min_time, min_distance, locListener);
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {

        if (requestCode == CODE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                // Permission granted.
                myUbication();
            } else {
                // User refused to grant permission. You can add AlertDialog here
                askForPermission();
            }
        }
    }

    private void askForPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_LOCATION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (mProgressDialogCustom != null) {
            if (active) {
                mProgressDialogCustom.show();
            } else {
                mProgressDialogCustom.dismiss();
            }
        }
    }

    @Override
    public void setMessageError(String error) {
        if(getActivity()!=null) {
            ((HomeActivity) getActivity()).showMessageError(error);
        }else{
            Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setDialogMessage(String message) {
        if(getActivity()!=null) {
            ((HomeActivity) getActivity()).showMessageError(message);
        }else{
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getReportsPoints(List<ResponseReport.ReportList> reportListsAbandoned, List<ResponseReport.ReportListMissing> reportListsMissing) {
        List<ReportEntity> reportEntityList = convertOneList(reportListsAbandoned, reportListsMissing);
        Bitmap abandoned = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.verde), size_maker, size_maker, false);
        Bitmap lost = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.naranja), size_maker, size_maker, false);
        for (ReportEntity entity : reportEntityList) {
            if (!entity.getLatitude().equals("") || !entity.getLongitude().equals("")) {
                LatLng latLng = new LatLng(Double.parseDouble(entity.getLatitude()), Double.parseDouble(entity.getLongitude()));

                if (entity.getTypeReport().equals("1")) {
                    hashMapReport.put(googleMap.addMarker(new MarkerOptions().position(latLng).title(entity.getIdReport())
                            .icon(BitmapDescriptorFactory.fromBitmap(lost))), entity);
                } else {
                        hashMapReport.put(googleMap.addMarker(new MarkerOptions().position(latLng).title(entity.getIdReport())
                                .icon(BitmapDescriptorFactory.fromBitmap(abandoned))), entity);
                }
            }
        }
        mapView.onResume();
    }


    private List<ReportEntity> convertOneList(List<ResponseReport.ReportList> reportListsAbandoned, List<ResponseReport.ReportListMissing> reportListsMissing) {
        List<ReportEntity> reportList = new ArrayList<>();
        for (ResponseReport.ReportListMissing entity : reportListsMissing) {
            reportList.add(new ReportEntity(entity.getId(), entity.getPlace().getLocation(), entity.getPlace().getLatitude(),
                    entity.getPlace().getLongitude(), entity.getDate(), entity.getReportImage(), entity.getPetName(), entity.getDescription(), "1"));
        }
        for (ResponseReport.ReportList entity : reportListsAbandoned) {
            reportList.add(new ReportEntity(entity.getId(), entity.getPlace().getLocation(), entity.getPlace().getLatitude(), entity.getPlace().getLongitude(),
                    entity.getDate(), entity.getReportImage(), "Desconocido", entity.getDescription(), "2"));
        }
        return reportList;
    }

    public void loadMap() {
        mapView.getMapAsync(this);
    }

    private void message(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    //actualizar la ubicacion
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mCamera(latitude, longitude);
        }
    }

    private void mCamera(double latitude, double longitude) {
        LatLng coordenadas = new LatLng(latitude, longitude);
        CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(coordenadas, zoom);
        googleMap.animateCamera(myUbication);
    }

    //activar los servicios del gps cuando esten apagados
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mCamera(latitude, longitude);
        return false;
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        ReportEntity data = hashMapReport.get(marker);
        Intent i = new Intent(getActivity(), DetailMarkerActivity.class);
        i.putExtra("Report",data);
        i.putExtra("tag","2");
        startActivity(i);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        marker.showInfoWindow();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                marker.showInfoWindow();
            }
        }, 450);
        return true;
    }

    protected class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.info_marker, null);
            ImageView imageView = v.findViewById(R.id.image_animal);
            TextView name = v.findViewById(R.id.pet_name);
            ReportEntity data = hashMapReport.get(marker);
            if (data != null) {
                name.setText(data.getNamePet());
                if (data.getPhoto().equals("Sin imagen")) {
                    Picasso.with(getContext()).load(R.drawable.mph).into(imageView);
                } else {
                    Picasso.with(getContext()).load(data.getPhoto()).into(imageView);
                }
            }
            return v;
        }
    }
}

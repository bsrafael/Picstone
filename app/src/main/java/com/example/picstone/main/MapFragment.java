package com.example.picstone.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.picstone.R;


import android.os.Bundle;
import android.widget.Toast;

import com.example.picstone.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private User user = null;


    private FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);


        // obter a localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        user = User.getInstance();

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Explorar");
    }


    @Override
    public void onMapReady(GoogleMap gm) {

        map = gm;
        map.getUiSettings().setZoomControlsEnabled(false);
        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        getLastKnownLocation();
//        createAllMarkers();

//        inicio
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent i = new Intent(getContext(), PinActivity.class);
                i.putExtra("idMarker", marker.getTag().toString());
                startActivity(i);
                return false;



            }

        });


    }


    /** Called when the user clicks a marker. */
//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//
//        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
//
//        Intent i = new Intent(getContext(), PinActivity.class);
//        i.putExtra("idMarker", marker.getTag().toString());
//        startActivity(i);
//
//        return true;
//    }


    private void getLastKnownLocation() {
        if (checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
                    user.setLocation(pos);
                }

                if (user.getDEBUG_SAMPLE_MARKER_PICTURE() != null)
                    createAllMarkers();
            }

        });
    }

    //TODO: properly create marker with it's params
    public void createMarker(@NonNull LatLng location, @Nullable String idPin) {
        Marker m = this.map.addMarker(new MarkerOptions()
            .position(location)
            .title("text"));
        if (idPin != null) {
            m.setTag(idPin); // salva um parâmetro no pin
        }
    }

    //TODO: Get all markers from API.
    public void createAllMarkers() {
        createMarker(user.getLocation(), "exemplo");

    }



}
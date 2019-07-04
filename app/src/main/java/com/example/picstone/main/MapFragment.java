package com.example.picstone.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.picstone.models.output.PostViewModel;
import com.example.picstone.network.ClientFactory;
import com.example.picstone.network.SoapstoneClient;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private User user = null;

    private List<PostViewModel> posts;

    private FusedLocationProviderClient fusedLocationClient;

    private SoapstoneClient client;

    private LocationListener locationListener;

    private final float MIN_DISTANCE_BETWEEN_REQUESTS = 10; // meters

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

        try
        {
            String token = user.getToken();
            client = ClientFactory.GetSoapstoneClient(token);
        }
        catch (Exception e)
        {
            // TODO on unauthorized
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (shouldMakeANewRequest(location)) // TODO aqui provavelmente precisa das permissoes tambem
                    createAllMarkers(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        return v;
    }

    private boolean shouldMakeANewRequest(Location location) {
        if (user.getLocation() == null)
            return true;

        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(user.getLocation().latitude);
        temp.setLongitude(user.getLocation().longitude);

        return location.distanceTo(temp) > MIN_DISTANCE_BETWEEN_REQUESTS;
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
                PostViewModel post = getPost(marker.getTag().toString());

                if (post != null) {
                    i.putExtra(PinActivity.ID_KEY, post.getId());
                    i.putExtra(PinActivity.PHOTO_KEY, post.getImageUrl());
                    i.putExtra(PinActivity.TEXT_KEY, post.getMessage());
                    i.putExtra(PinActivity.AUTHOR_KEY, post.getAuthor());
                    i.putExtra(PinActivity.DATA_KEY, post.getCreatedAt().toString());
                    startActivity(i);

                    // TODO reload posts? if yes just call getLastKnownLocation
                }

                return false;
            }

        });
    }

    private PostViewModel getPost(String id) {
        if (posts != null)
            for (PostViewModel post: posts) {
                if (post.getId().equals(id))
                    return post;
            }

        return null;
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

                    createAllMarkers(location);
                }
            }

        });
    }

    //TODO: properly create marker with it's params
    public void createMarker(@NonNull LatLng location, @Nullable String idPin) {
        Marker m = this.map.addMarker(new MarkerOptions()
            .position(location));
        if (idPin != null) {
            m.setTag(idPin); // salva um parâmetro no pin
        }
    }

    //TODO: Get all markers from API.
    public void createAllMarkers(final Location location) {
        Call<List<PostViewModel>> call = client.getPostsNearUser(location.getLatitude(), location.getLongitude(), 0, 500);

        call.enqueue(new Callback<List<PostViewModel>>() {
            @Override
            public void onResponse(Call<List<PostViewModel>> call, Response<List<PostViewModel>> response) {
                List<PostViewModel> viewModels = response.body();

                if (viewModels != null)
                    for (PostViewModel viewModel: viewModels) {
                        LatLng pinLocation = new LatLng(viewModel.getLatitude(), viewModel.getLongitude());
                        createMarker(pinLocation, viewModel.getId());
                    }

                posts = viewModels;

                LatLng pt = new LatLng(location.getLatitude(), location.getLongitude());
                user.setLocation(pt);
            }

            @Override
            public void onFailure(Call<List<PostViewModel>> call, Throwable t) {
                String message = t.getMessage();
                Toast.makeText(getContext(), "Erro ao carregar as mensagens. " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        createMarker(user.getLocation(), "exemplo");

    }



}
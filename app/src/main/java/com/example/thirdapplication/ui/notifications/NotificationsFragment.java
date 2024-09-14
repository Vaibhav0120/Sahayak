package com.example.thirdapplication.ui.notifications;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.thirdapplication.R;
import com.example.thirdapplication.databinding.FragmentNotificationsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NotificationsFragment extends Fragment implements OnMapReadyCallback {

    private FragmentNotificationsBinding binding;
    private GoogleMap mMap;
    private MapView mapView;
    private VideoView videoView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize and set up the MapView
        mapView = root.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Initialize and set up the VideoView
        videoView = root.findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.weather_clip);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(0.5f)); // Set playback speed to 0.5X
            videoView.start(); // Start video playback as soon as it's prepared
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (videoView != null) {
            videoView.start(); // Resume video playback
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (videoView != null) {
            videoView.pause(); // Pause video playback
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback(); // Stop video playback
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(37.4219999, -122.0862462))
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        // Optional: Add markers if needed
        // mMap.addMarker(new MarkerOptions()
        //        .position(new LatLng(37.4219999, -122.0862462))
        //        .title("Marker Title"));
    }
}

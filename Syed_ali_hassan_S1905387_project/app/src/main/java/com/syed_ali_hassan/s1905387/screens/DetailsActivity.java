//Syed_Ali_Hassan_S1905387
package com.syed_ali_hassan.s1905387.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syed_ali_hassan.s1905387.R;
import com.syed_ali_hassan.s1905387.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityDetailsBinding layoutBinding;
    private GoogleMap map;
    private Double mLat, mLong;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        SupportMapFragment mFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mFrag != null;
        mFrag.getMapAsync(this);

        setData();
    }

    private void setData() {
        intent = getIntent();
        mLat = Double.parseDouble(intent.getStringExtra("lat"));
        mLong = Double.parseDouble(intent.getStringExtra("lng"));

        String[] earthquakeDescTokens = intent.getStringExtra("description").split(";");
        float magnitude = Float.parseFloat(earthquakeDescTokens[4].split(":")[1].trim());
        int depth = Integer.parseInt(earthquakeDescTokens[3].split(":")[1].trim().split(" ")[0]);

        if (magnitude > 2)
            layoutBinding.tvMagnitude.setTextColor(this.getColor(R.color.red));
        else if (magnitude > 1)
            layoutBinding.tvMagnitude.setTextColor(this.getColor(R.color.yellow));
        else if (magnitude > 0)
            layoutBinding.tvMagnitude.setTextColor(this.getColor(R.color.green));

        layoutBinding.tvMagnitude.setText(magnitude + "");
        layoutBinding.tvLocation.setText(earthquakeDescTokens[1].split(":")[1]);
        layoutBinding.tvCoordinates.setText(intent.getStringExtra("lat") + "° S, " + intent.getStringExtra("lng") + "° E");
        layoutBinding.tvDepth.setText(depth + " km");
        layoutBinding.tvTime.setText(intent.getStringExtra("date"));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(mLat, mLong);
        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLong), 13f));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

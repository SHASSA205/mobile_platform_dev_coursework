//Syed_Ali_Hassan_S1905387
package com.syed_ali_hassan.s1905387.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.core.util.Pair;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.syed_ali_hassan.s1905387.R;
import com.syed_ali_hassan.s1905387.adapter.ListAdapter;
import com.syed_ali_hassan.s1905387.data.EarthQuakeModel;
import com.syed_ali_hassan.s1905387.data.FetchEarthQuakes;
import com.syed_ali_hassan.s1905387.data.RunnerClass;
import com.syed_ali_hassan.s1905387.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<EarthQuakeModel> arrayListCurrent = new ArrayList<>();
    private ActivityMainBinding layoutBinding;
    private EarthQuakeModel largestMagnitudeEarthquake, largestDepthEarthquake;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layoutBinding.getRoot());

        setListeners();
        getEarthQuakesList();
        getWorldEarthQuakes();
    }

    private void getDate() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        Calendar now = Calendar.getInstance();
        builder.setSelection(new Pair<>(now.getTimeInMillis(), now.getTimeInMillis()));
        picker.show(MainActivity.this.getSupportFragmentManager(), picker.toString());
        picker.addOnNegativeButtonClickListener(as -> picker.dismiss());
        picker.addOnPositiveButtonClickListener(date -> {
            Log.e("Date", date.toString());
            Date firstDate = new java.util.Date(date.first);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDateFirst = sdf.format(firstDate);
            Date secondDate = new java.util.Date(date.second);
            String formattedDateSecond = sdf.format(secondDate);
            layoutBinding.etSearch.setText(formattedDateFirst + "  -  " + formattedDateSecond);
            getListBetweenTwoDates(date.first, date.second);
        });
    }

    private void getListBetweenTwoDates(long dateStartPassed, long dateEndPassed) {
        ArrayList<EarthQuakeModel> filteredList = new ArrayList<>();
        for (int i = 0; i < arrayListCurrent.size(); i++) {
            String date = arrayListCurrent.get(i).getDate();

            try {

                SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss", Locale.getDefault());

                Date dateStart = inputFormat.parse(date);

                long dateLong = dateStart.getTime();


                if (dateStartPassed == dateEndPassed) {
                    if (dateStartPassed - 43200000 <= dateLong && dateLong <= dateEndPassed + 43200000) {
                        filteredList.add(arrayListCurrent.get(i));
                    }
                } else {
                    if (dateStartPassed <= dateLong && dateEndPassed >= dateLong) {
                        Log.e("dateLong", dateLong + "");
                        filteredList.add(arrayListCurrent.get(i));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        setEarthquakeAdapter(filteredList);
    }

    private void setListeners() {
        layoutBinding.ivRefresh.setOnClickListener(v -> {
            getEarthQuakesList();
            getWorldEarthQuakes();
        });
        layoutBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("SearchText", s.toString());
                onSearch(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        layoutBinding.viewHighestMag.setOnClickListener(v -> {
            if (largestDepthEarthquake != null) {
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("title", largestMagnitudeEarthquake.getTitle());
                intent.putExtra("description", largestMagnitudeEarthquake.getDescription());
                intent.putExtra("link", largestMagnitudeEarthquake.getLink());
                intent.putExtra("date", largestMagnitudeEarthquake.getDate());
                intent.putExtra("lat", largestMagnitudeEarthquake.getLat());
                intent.putExtra("lng", largestMagnitudeEarthquake.getLng());
                startActivity(intent);
            }
        });

        layoutBinding.viewHighestDepth.setOnClickListener(v -> {
            if (largestDepthEarthquake != null) {
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("title", largestDepthEarthquake.getTitle());
                intent.putExtra("description", largestDepthEarthquake.getDescription());
                intent.putExtra("link", largestDepthEarthquake.getLink());
                intent.putExtra("date", largestDepthEarthquake.getDate());
                intent.putExtra("lat", largestDepthEarthquake.getLat());
                intent.putExtra("lng", largestDepthEarthquake.getLng());
                startActivity(intent);
            }
        });

        layoutBinding.ivCalendar.setOnClickListener(v -> {
            getDate();
        });
    }

    private void onSearch(String text) {
        ArrayList<EarthQuakeModel> filteredList = new ArrayList<>();

        for (EarthQuakeModel item : arrayListCurrent) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        setEarthquakeAdapter(filteredList);
    }

    private void getEarthQuakesList() {
        layoutBinding.ivRefresh.setVisibility(View.GONE);
        layoutBinding.progressCircular.setVisibility(View.VISIBLE);
        RunnerClass runnerClass1 = new RunnerClass();
        runnerClass1.executeAsync(new FetchEarthQuakes("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml"), (data) -> {

            if (data != null) {
                layoutBinding.progressCircular.setVisibility(View.GONE);
                Log.d("EarthQuakeList", data.toString());
                arrayListCurrent = data;
                setEarthquakeAdapter(arrayListCurrent);
            } else {
                layoutBinding.progressCircular.setVisibility(View.GONE);
                layoutBinding.ivRefresh.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Internet Connectivity Issue", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getWorldEarthQuakes() {
        RunnerClass runnerClass1 = new RunnerClass();
        runnerClass1.executeAsync(new FetchEarthQuakes("http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml"), (data) -> {

            if (data != null) {
                Log.d("EarthQuakeList", data.toString());
                float largestMag = 0f;
                int largestDepth = 0;
                for (EarthQuakeModel earthquake : data) {
                    String[] earthquakeDataTokens = earthquake.getDescription().split(";");
                    float mag = Float.parseFloat(earthquakeDataTokens[4].split(":")[1].trim());
                    int depth = Integer.parseInt(earthquakeDataTokens[3].split(":")[1].trim().split(" ")[0]);
                    if (mag > largestMag) {
                        largestMagnitudeEarthquake = earthquake;
                        largestMag = mag;
                    }
                    if (depth > largestDepth) {
                        largestDepthEarthquake = earthquake;
                        largestDepth = depth;
                    }
                }
                layoutBinding.tvHighestMag.setText(largestMag + "");
                layoutBinding.tvHighestDepth.setText(largestDepth + " km");
            } else {
//                Toast.makeText(this, "Internet Connectivity Issue", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEarthquakeAdapter(ArrayList<EarthQuakeModel> Current) {
        RecyclerView rvIncidents = findViewById(R.id.rv_earthquakes);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ListAdapter recyclerAdapter = new ListAdapter(Current, this);
            rvIncidents.setAdapter(recyclerAdapter);
            rvIncidents.setLayoutManager(new LinearLayoutManager(this));
        } else {
            ListAdapter recyclerAdapter = new ListAdapter(Current, this);
            rvIncidents.setAdapter(recyclerAdapter);
            rvIncidents.setLayoutManager(new GridLayoutManager(this, 2));
        }

    }
}
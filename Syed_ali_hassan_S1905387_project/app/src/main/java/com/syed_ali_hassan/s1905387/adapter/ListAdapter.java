//Syed_Ali_Hassan_S190538
package com.syed_ali_hassan.s1905387.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.syed_ali_hassan.s1905387.R;
import com.syed_ali_hassan.s1905387.data.EarthQuakeModel;
import com.syed_ali_hassan.s1905387.screens.DetailsActivity;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<EarthQuakeModel> localEarthquakeList;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView location, mDate;
        public final TextView magnitude;
        public final CardView cvMain;

        public ViewHolder(View view) {
            super(view);
            location = view.findViewById(R.id.tv_location);
            mDate = view.findViewById(R.id.tv_time_date);
            magnitude = view.findViewById(R.id.tv_magnitude);
            cvMain = view.findViewById(R.id.card_main);
        }
    }

    public ListAdapter(ArrayList<EarthQuakeModel> dataSet, Context mContext) {
        localEarthquakeList = dataSet;
        context = mContext;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.earthquake_item_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String[] data = localEarthquakeList.get(position).getDescription().split(";");

        viewHolder.mDate.setText(data[0].split(":")[1]);
        viewHolder.location.setText(data[1].split(":")[1]);
        float magnitude = Float.parseFloat(data[4].split(":")[1].trim());
        if (magnitude > 2)
            viewHolder.magnitude.setTextColor(context.getColor(R.color.red));
        else if (magnitude > 1)
            viewHolder.magnitude.setTextColor(context.getColor(R.color.yellow));
        else if (magnitude > 0)
            viewHolder.magnitude.setTextColor(context.getColor(R.color.green));

        viewHolder.magnitude.setText(String.valueOf(magnitude));

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("title", localEarthquakeList.get(viewHolder.getAdapterPosition()).getTitle());
            intent.putExtra("description", localEarthquakeList.get(viewHolder.getAdapterPosition()).getDescription());
            intent.putExtra("link", localEarthquakeList.get(viewHolder.getAdapterPosition()).getLink());
            intent.putExtra("date", localEarthquakeList.get(viewHolder.getAdapterPosition()).getDate());
            intent.putExtra("lat", localEarthquakeList.get(viewHolder.getAdapterPosition()).getLat());
            intent.putExtra("lng", localEarthquakeList.get(viewHolder.getAdapterPosition()).getLng());
            context.startActivity(intent);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localEarthquakeList.size();
    }

}
package com.example.thirdapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thirdapplication.Domain.OngoingDomain;
import com.example.thirdapplication.R;

import java.util.ArrayList;

public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.Viewholder> {
    private ArrayList<OngoingDomain> ongoingDomainArrayList;
    private Context context;

    public OngoingAdapter(ArrayList<OngoingDomain> ongoingDomainArrayList) {
        this.ongoingDomainArrayList = ongoingDomainArrayList;
    }

    @NonNull
    @Override
    public OngoingAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflater = LayoutInflater.from(context).inflate(R.layout.viewholder_ongoing, parent, false);
        return new Viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingAdapter.Viewholder holder, int position) {
    holder.placeName.setText(ongoingDomainArrayList.get(position).getPlace());
    holder.IncidentDate.setText(ongoingDomainArrayList.get(position).getDate());
    holder.progress_percentage.setText(ongoingDomainArrayList.get(position).getProgressPercent()+"%");

    int drawableResourceId = context.getResources()
            .getIdentifier(ongoingDomainArrayList.get(position).getPicPath(), "drawable", context.getPackageName());

    Glide.with(context)
            .load(drawableResourceId)
            .into(holder.ongoingIcon);

        holder.progressBarOngoing.setProgress(ongoingDomainArrayList.get(position).getProgressPercent());

        holder.ongoingLayout.setBackgroundResource(R.drawable.mini_icon_bg_home);
        holder.placeName.setTextColor(context.getResources().getColor(R.color.darkblue));
        holder.IncidentDate.setTextColor(context.getResources().getColor(R.color.darkblue));
        holder.text_progress.setTextColor(context.getResources().getColor(R.color.darkblue));
        holder.progress_percentage.setTextColor(context.getResources().getColor(R.color.darkblue));
        holder.ongoingIcon.setColorFilter(ContextCompat.getColor(context, R.color.darkblue), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.progressBarOngoing.setProgressTintList(ContextCompat.getColorStateList(context, R.color.darkblue));
    }

    @Override
    public int getItemCount() {
        return ongoingDomainArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView placeName,IncidentDate,text_progress,progress_percentage;
        ImageView ongoingIcon;
        ProgressBar progressBarOngoing;
        ConstraintLayout ongoingLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ongoingLayout = itemView.findViewById(R.id.ongoingLayout);
            placeName = itemView.findViewById(R.id.placeName);
            IncidentDate = itemView.findViewById(R.id.IncidentDate);
            ongoingIcon = itemView.findViewById(R.id.ongoingIcon);
            text_progress = itemView.findViewById(R.id.text_progress);
            progressBarOngoing = itemView.findViewById(R.id.progressBarOngoing);
            progress_percentage = itemView.findViewById(R.id.progress_percentage);
        }
    }
}

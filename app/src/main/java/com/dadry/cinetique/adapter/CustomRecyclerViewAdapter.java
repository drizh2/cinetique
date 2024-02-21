package com.dadry.cinetique.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.dadry.cinetique.R;
import com.dadry.cinetique.UpdateActivity;
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.entity.Rate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<Film> films;
    Activity activity;

    Animation translate_anim;

    public CustomRecyclerViewAdapter(Activity activity, Context context, ArrayList<Film> films) {
        this.activity = activity;
        this.context = context;
        this.films = films;
    }

    @NonNull
    @NotNull
    @Override
    public CustomRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.film_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomRecyclerViewAdapter.MyViewHolder holder, int position) {
        Film film = films.get(position);
        holder.idText.setText(String.valueOf(position + 1));
        holder.titleText.setText(film.getTitle());

        if (film.isWatched()) {
            holder.ratingBar.setRating(film.getMark());

            Rate rate = Rate.getRateByInteger(film.getMark());
            holder.rateText.setText(rate.getStringValue());
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#958F8F"));
        } else {
            holder.ratingBar.setRating(0);

            holder.rateText.setText("Not watched yet");
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#EDF1C748"));
        }

        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("id", String.valueOf(film.getId()));
            intent.putExtra("title", String.valueOf(film.getTitle()));
            intent.putExtra("isWatched", String.valueOf(film.isWatched()));
            intent.putExtra("mark", String.valueOf(film.getMark()));
            intent.putExtra("review", String.valueOf(film.getReview()));

            activity.startActivityForResult(intent, 1);
        });

    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        TextView idText, titleText, rateText;
        RatingBar ratingBar;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            idText = itemView.findViewById(R.id.idText);
            titleText = itemView.findViewById(R.id.titleText);
            rateText = itemView.findViewById(R.id.rateText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}

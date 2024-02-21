package com.dadry.cinetique;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.util.DatabaseHelper;

public class AddFilmActivity extends AppCompatActivity {

    EditText titleInput, reviewInput;
    CheckBox isWatched;
    RatingBar ratingBar;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);

        titleInput = findViewById(R.id.titleInput);
        reviewInput = findViewById(R.id.reviewInput);
        isWatched = findViewById(R.id.isWatched);
        ratingBar = findViewById(R.id.ratingBar);
        saveButton = findViewById(R.id.saveButton);

        isWatched.setOnClickListener(v -> {
            ViewGroup.LayoutParams layoutParams = saveButton.getLayoutParams();

            if (isWatched.isChecked()) {
                changeTopMargin(500, View.VISIBLE, layoutParams);
            } else {
                changeTopMargin(40, View.GONE, layoutParams);
            }
        });

        saveButton.setOnClickListener(v -> {

            DatabaseHelper databaseHelper = new DatabaseHelper(AddFilmActivity.this);

            Film film = new Film();
            String title = String.valueOf(titleInput.getText());

            film.setTitle(title);
            film.setWatched(isWatched.isChecked());

            if (isWatched.isChecked()) {
                int mark = (int) ratingBar.getRating();
                String review = String.valueOf(reviewInput.getText());

                film.setMark(mark);
                film.setReview(review);
            }

            databaseHelper.addFilm(film);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void changeTopMargin(int value, int visibility, ViewGroup.LayoutParams layoutParams) {
        ratingBar.setVisibility(visibility);
        reviewInput.setVisibility(visibility);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layoutParams;
        params.setMargins(0, value, 0, 0);
        saveButton.setLayoutParams(params);
    }
}
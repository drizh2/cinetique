package com.dadry.cinetique;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.util.DatabaseHelper;

public class AddFilmActivity extends AppCompatActivity {

    EditText titleInput, reviewInput;
    CheckBox isWatched;
    RatingBar ratingBar;
    Button saveButton;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);

        titleInput = findViewById(R.id.titleInput);
        reviewInput = findViewById(R.id.reviewInput);
        isWatched = findViewById(R.id.isWatched);
        ratingBar = findViewById(R.id.ratingBar);
        saveButton = findViewById(R.id.saveButton);
        constraintLayout = findViewById(R.id.constraintLayout);

        isWatched.setOnClickListener(v -> {
            ViewGroup.LayoutParams layoutParams = saveButton.getLayoutParams();

            if (isWatched.isChecked()) {
                changeBottomMargin(View.VISIBLE, layoutParams, true);
            } else {
                changeBottomMargin(View.GONE, layoutParams, false);
            }
        });

        saveButton.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(AddFilmActivity.this);

            Film film = new Film();

            film.setTitle(String.valueOf(titleInput.getText()));

            film.setWatched(isWatched.isChecked());

            if (isWatched.isChecked()) {
                int mark = (int) ratingBar.getRating();

                film.setMark(mark);
                film.setReview(String.valueOf(reviewInput.getText()));
            }

            if (!film.getTitle().isEmpty() &&
                    (film.getMark() == null || film.getMark() != 0) &&
                    (film.getReview() == null || !film.getReview().isEmpty())
            ) {
                databaseHelper.addFilm(film);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "All fields should be filled!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void changeBottomMargin(int visibility, ViewGroup.LayoutParams layoutParams, boolean isWatched) {
        ratingBar.setVisibility(visibility);
        reviewInput.setVisibility(visibility);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        if (isWatched) {
            constraintSet.connect(R.id.saveButton, ConstraintSet.TOP, R.id.reviewInput, ConstraintSet.BOTTOM, 30);
        } else {
            constraintSet.connect(R.id.saveButton, ConstraintSet.TOP, R.id.isWatched, ConstraintSet.BOTTOM, 30);
        }

        constraintSet.applyTo(constraintLayout);
    }
}
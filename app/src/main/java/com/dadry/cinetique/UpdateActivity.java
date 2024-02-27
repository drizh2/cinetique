package com.dadry.cinetique;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.util.DatabaseHelper;

public class UpdateActivity extends AppCompatActivity {

    EditText titleInput, reviewInput;
    CheckBox isWatched;
    RatingBar ratingBar;
    Button saveButton, deleteButton;
    ConstraintLayout constraintLayout;

    String title;
    Long id;
    boolean isWatchedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        titleInput = findViewById(R.id.titleInput);
        reviewInput = findViewById(R.id.reviewInput);
        isWatched = findViewById(R.id.isWatched);
        ratingBar = findViewById(R.id.ratingBar);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        constraintLayout = findViewById(R.id.constraintLayout);

        getIntentData();

        if (isWatchedData) {
            changeBottomMargin(View.VISIBLE, saveButton.getLayoutParams(), true);
        } else {
            changeBottomMargin(View.GONE, saveButton.getLayoutParams(), false);
        }

        saveButton.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
            Film film = new Film();

            film.setId(id);
            film.setTitle(String.valueOf(titleInput.getText()));
            film.setWatched(isWatched.isChecked());

            if (isWatched.isChecked()) {
                film.setMark((int) ratingBar.getRating());
                film.setReview(String.valueOf(reviewInput.getText()));
            }

            if (!film.getTitle().isEmpty() &&
                    (film.getMark() == null || film.getMark() != 0) &&
                    (film.getReview() == null || !film.getReview().isEmpty())
            ) {
                db.updateFilm(film);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "All fields should be filled!", Toast.LENGTH_SHORT).show();
            }

        });

        deleteButton.setOnClickListener(v -> confirmDialog());

        isWatched.setOnClickListener(v -> {
            ViewGroup.LayoutParams layoutParams = saveButton.getLayoutParams();

            if (isWatched.isChecked()) {
                changeBottomMargin(View.VISIBLE, layoutParams, true);
            } else {
                changeBottomMargin(View.GONE, layoutParams, false);
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

    void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            this.id = Long.parseLong(getIntent().getStringExtra("id"));
            this.title = getIntent().getStringExtra("title");
             this.isWatchedData = Boolean.parseBoolean(getIntent().getStringExtra("isWatched"));

            if (isWatchedData) {
                int mark = Integer.parseInt(getIntent().getStringExtra("mark"));
                String review = getIntent().getStringExtra("review");

                this.ratingBar.setRating(mark);
                this.reviewInput.setText(review);
            }

            titleInput.setText(title);
            this.isWatched.setChecked(isWatchedData);

        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
            db.deleteRow(String.valueOf(id));
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {});

        builder.create().show();
    }
}
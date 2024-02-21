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
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.util.DatabaseHelper;

public class UpdateActivity extends AppCompatActivity {

    EditText titleInput, reviewInput;
    CheckBox isWatched;
    RatingBar ratingBar;
    Button saveButton, deleteButton;

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

        getIntentData();

        if (isWatchedData) {
            changeTopMargin(500, View.VISIBLE, saveButton.getLayoutParams());
        } else {
            changeTopMargin(40, View.GONE, saveButton.getLayoutParams());
        }

        saveButton.setOnClickListener(v -> {
            String titleText = String.valueOf(titleInput.getText());
            String reviewText = String.valueOf(reviewInput.getText());

            if(
                    !titleText.equals("") &&
                            (isWatched.isChecked() &&
                                    ratingBar.getRating() != 0 &&
                                    !reviewText.equals("")
                            )
            ) {
                Film film = new Film();

                film.setId(id);
                film.setTitle(titleText);
                film.setWatched(isWatched.isChecked());

                if (isWatched.isChecked()) {
                    film.setMark((int) ratingBar.getRating());
                    film.setReview(String.valueOf(reviewInput.getText()));
                }

                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
                db.updateFilm(film);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Enter all fields!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            confirmDialog();
        });

        isWatched.setOnClickListener(v -> {
            ViewGroup.LayoutParams layoutParams = saveButton.getLayoutParams();

            if (isWatched.isChecked()) {
                changeTopMargin(500, View.VISIBLE, layoutParams);
            } else {
                changeTopMargin(40, View.GONE, layoutParams);
            }
        });
    }

    private void changeTopMargin(int value, int visibility, ViewGroup.LayoutParams layoutParams) {
        ratingBar.setVisibility(visibility);
        reviewInput.setVisibility(visibility);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layoutParams;
        params.setMargins(0, value, 0, 0);
        saveButton.setLayoutParams(params);
    }

    void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            this.id = Long.parseLong(getIntent().getStringExtra("id"));
            this.title = getIntent().getStringExtra("title");
             this.isWatchedData = Boolean.parseBoolean(getIntent().getStringExtra("isWatched"));

            if (isWatchedData) {
                Integer mark = Integer.valueOf(getIntent().getStringExtra("mark"));
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
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);
                db.deleteRow(String.valueOf(id));
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }
}
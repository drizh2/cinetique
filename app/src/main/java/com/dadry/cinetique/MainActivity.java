package com.dadry.cinetique;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dadry.cinetique.adapter.CustomRecyclerViewAdapter;
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.util.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ImageView noDataImage;
    TextView noDataText;

    DatabaseHelper db;
    ArrayList<Film> films;
    CustomRecyclerViewAdapter adapter;

    boolean isBackPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noDataImage = findViewById(R.id.noDataImage);
        noDataText = findViewById(R.id.noDataText);

        addButton = findViewById(R.id.addButton);

        db = new DatabaseHelper(MainActivity.this);
        films = new ArrayList<>();

        storeDataInArray();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFilmActivity.class);
            startActivityForResult(intent, 1);
        });

        adapter = new CustomRecyclerViewAdapter(MainActivity.this, this, films);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

        if(isBackPressedOnce) {
            super.onBackPressed();
            return;
        }

        Toast.makeText(this, "Press one more time to exit", Toast.LENGTH_SHORT).show();
        isBackPressedOnce = true;

        new Handler().postDelayed(() -> isBackPressedOnce = false, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArray() {
        Cursor cursor = db.readAllData();

        if (cursor.getCount() == 0) {
            noDataImage.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.VISIBLE);
        } else {
            noDataImage.setVisibility(View.GONE);
            noDataText.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                Film film = new Film();

                Long id = Long.valueOf(cursor.getString(0));
                String title = cursor.getString(1);
                String isWatch = cursor.getString(2);
                boolean isWatched = isWatch.equals("1");

                if (isWatched) {
                    int mark = Integer.parseInt(cursor.getString(3));
                    String review = cursor.getString(4);

                    film.setMark(mark);
                    film.setReview(review);
                }

                film.setId(id);
                film.setTitle(title);
                film.setWatched(isWatched);

                films.add(film);
            }
            Toast.makeText(this, "Data Loaded!", Toast.LENGTH_SHORT).show();
        }
    }
}
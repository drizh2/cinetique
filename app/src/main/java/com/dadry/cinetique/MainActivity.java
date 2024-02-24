package com.dadry.cinetique;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dadry.cinetique.adapter.CustomRecyclerViewAdapter;
import com.dadry.cinetique.comparator.FilmComparatorBestFirst;
import com.dadry.cinetique.comparator.FilmComparatorWorstFirst;
import com.dadry.cinetique.entity.Film;
import com.dadry.cinetique.util.DatabaseHelper;
import com.dadry.cinetique.comparator.FilmComparatorNotWatchedFirst;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ImageView noDataImage;
    TextView noDataText;
    BottomNavigationView bottomNavigationView;

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

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.sort:
                        showPopUpMenu();
                        break;
                    case R.id.deleteAll:
                        confirmDialog();
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });
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

    private void showPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, bottomNavigationView);

        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.notWatched:
                    sort(new FilmComparatorNotWatchedFirst());
                    break;
                case R.id.worstFirst:
                    sort(new FilmComparatorWorstFirst());
                    break;
                case R.id.bestFirst:
                    sort(new FilmComparatorBestFirst());
                    break;
            }
            popupMenu.dismiss();
            return false;
        });

        popupMenu.show();
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all data?");
        builder.setMessage("Are you sure you want to delete all data?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            db.deleteAll();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {});

        builder.create().show();
    }

    private void sort(Comparator<Film> comparator) {
        films.sort(comparator);
        adapter = new CustomRecyclerViewAdapter(MainActivity.this, this, films);
        recyclerView.setAdapter(adapter);
    }
}
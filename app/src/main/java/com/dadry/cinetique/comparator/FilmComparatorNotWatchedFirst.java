package com.dadry.cinetique.comparator;

import com.dadry.cinetique.entity.Film;

import java.util.Comparator;

public class FilmComparatorNotWatchedFirst implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
        return Boolean.compare(o1.isWatched(), o2.isWatched());
    }
}

package com.dadry.cinetique.comparator;

import com.dadry.cinetique.entity.Film;

import java.util.Comparator;

public class FilmComparatorWorstFirst implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
        if (o1.getMark() == null) {
            return 1;
        } else if (o2.getMark() == null) {
            return -1;
        } else {
            return o1.getMark().compareTo(o2.getMark());
        }
    }
}

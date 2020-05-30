package com.example.todoapplication.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todoapplication.Models.Data;

@Database(entities = {Data.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}

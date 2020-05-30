package com.example.todoapplication.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapplication.Constants.Priority;
import com.example.todoapplication.Models.Data;

import java.util.List;

@Dao
public interface DataDao {
    @Query("SELECT * FROM tasks")
    List<Data> getData();


    @Query("SELECT * FROM tasks WHERE task LIKE :taskData")
    List<Data> loadAllByIds(String taskData);

    @Update
    void updateData(Data... d);

    @Insert
    void insertAll(Data... list);

    @Delete
    void delete(Data d);
}

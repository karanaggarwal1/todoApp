package com.example.todoapplication.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.todoapplication.Constants.Priority;

@Entity(tableName = "tasks")
public class Data {

    @ColumnInfo(name = "task")
    private String task;
    @ColumnInfo(name = "status")
    private boolean status;
    @TypeConverters(PriorityConverter.class)
    private Priority priority;
    @PrimaryKey
    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Data(String task, boolean status, Priority priority, long date) {
        this.task = task;
        this.status = status;
        this.priority = priority;
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}

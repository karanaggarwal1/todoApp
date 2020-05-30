package com.example.todoapplication.Models;

import androidx.room.TypeConverter;

import com.example.todoapplication.Constants.Priority;

public class PriorityConverter {
    @TypeConverter
    public static Priority toPriority(int priority){
        if (priority == Priority.HIGH.getCode()) {
            return Priority.HIGH;
        } else if (priority == Priority.MEDIUM.getCode()) {
            return Priority.MEDIUM;
        } else if (priority == Priority.LOW.getCode()) {
            return Priority.LOW;
        } else {
            throw new IllegalArgumentException("Could not recognize status");
        }
    }

    @TypeConverter
    public static int toInteger(Priority status) {
        return status.getCode();
    }
}

package com.example.todoapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.todoapplication.Adapters.SearchAdapter;
import com.example.todoapplication.Adapters.TodoAdapter;
import com.example.todoapplication.Constants.Priority;
import com.example.todoapplication.Database.AppDatabase;
import com.example.todoapplication.Models.Data;
import com.example.todoapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText et;
//    private CheckBox high;
//    private CheckBox medium;
//    private CheckBox low;
//    private CheckBox done;
    private Button button;
    private RecyclerView recyclerView;
    private List<Data> data;
    private AppDatabase db;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        et = findViewById(R.id.etSearch);
        button = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.searchList);
        db = Room.databaseBuilder(this, AppDatabase.class, "task_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = new ArrayList<>();
                if(et.getText().toString().isEmpty()){
                    data = db.dataDao().getData();
                } else {
                    data = db.dataDao().loadAllByIds("%" + et.getText().toString() + "%");
                }
                searchAdapter.updateReceiptsList(data);
            }
        });
    }

    private void init(){
        data = db.dataDao().getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Log.d("TodoApp", "init: " + data.size());
        searchAdapter = new SearchAdapter(data, db);
        recyclerView.setAdapter(searchAdapter);
    }
}
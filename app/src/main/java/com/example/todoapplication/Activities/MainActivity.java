package com.example.todoapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.todoapplication.Adapters.TodoAdapter;
import com.example.todoapplication.Constants.Priority;
import com.example.todoapplication.Database.AppDatabase;
import com.example.todoapplication.Models.Data;
import com.example.todoapplication.R;
import com.example.todoapplication.Activities.Callbacks.SwipeToDeleteCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private List<Data> data;
    private FloatingActionButton fab;
    private EditText et;
    private RadioGroup rg;
    private View v;
    private AlertDialog dialog;
    private AppDatabase db;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.data = db.dataDao().getData();
        switch(item.getItemId()){
            case R.id.sortPriority:
                if(!item.isChecked()){
                    item.setChecked(true);
                    Collections.sort(this.data, new Comparator<Data>() {
                        @Override
                        public int compare(Data o1, Data o2) {
                            if(o1.getPriority().getCode() != o2.getPriority().getCode()){
                                return o2.getPriority().getCode() - o1.getPriority().getCode();
                            }
                            return (int)(o1.getDate() - o2.getDate());
                        }
                    });
                } else {
                    item.setChecked(false);
                    Collections.sort(this.data, new Comparator<Data>() {
                        @Override
                        public int compare(Data o1, Data o2) {
                            return (int) (o1.getDate() - o2.getDate());
                        }
                    });
                }
                todoAdapter.updateReceiptsList(this.data);
                break;
            case R.id.action_search:
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableSwipeToDeleteAndUndo();
        constraintLayout = findViewById(R.id.constraintLayout);
        recyclerView = findViewById(R.id.todoList);
        db = Room.databaseBuilder(this, AppDatabase.class, "task_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        fab = findViewById(R.id.fab);
        init();
        enableSwipeToDeleteAndUndo();

        v = MainActivity.this.getLayoutInflater().inflate(R.layout.add_todo_dialog, null, false);
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(v)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                                String task = String.valueOf(taskEditText.getText());
                        et = v.findViewById(R.id.todoText);
                        rg = v.findViewById(R.id.priChoice);
                        int selectedId = rg.getCheckedRadioButtonId();
                        Priority p = null;
                        if(selectedId == R.id.choiceMedium){
                            p = Priority.MEDIUM;
                        }
                        if(selectedId == R.id.choiceHigh){
                            p = Priority.HIGH;
                        }
                        if(selectedId == R.id.choiceLow){
                            p = Priority.LOW;
                        }
                        Data d = new Data(et.getText().toString(), false, p, System.currentTimeMillis());
                        data.add(d);
                        db.dataDao().insertAll(d);
                        todoAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
            }
        });
    }

    private void init(){
        data = db.dataDao().getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Log.d("TodoApp", "init: " + data.size());
        todoAdapter = new TodoAdapter(data, db);
        recyclerView.setAdapter(todoAdapter);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Data item = todoAdapter.getArrayList().get(position);

                todoAdapter.removeItem(position);
                db.dataDao().delete(item);

                Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        todoAdapter.restoreItem(item, position);
                        db.dataDao().insertAll(item);
                        recyclerView.scrollToPosition(position);

                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
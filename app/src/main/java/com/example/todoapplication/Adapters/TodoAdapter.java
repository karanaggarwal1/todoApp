package com.example.todoapplication.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapplication.Constants.Priority;
import com.example.todoapplication.Database.AppDatabase;
import com.example.todoapplication.Models.Data;
import com.example.todoapplication.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder> {

    private List<Data> arrayList;
    private AppDatabase db;

    public TodoAdapter(List<Data> arrayList, AppDatabase db) {
        this.arrayList = arrayList;
        this.db = db;
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.task.setText(this.arrayList.get(position).getTask());
        holder.priority.setText(stringValue(this.arrayList.get(position).getPriority()));
        if(this.arrayList.get(position).isStatus()){
            holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.box.setChecked(true);
        } else {
            holder.box.setChecked(false);
        }
        holder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TodoAdapter.this.arrayList.get(position).setStatus(isChecked);
                db.dataDao().updateData(TodoAdapter.this.arrayList.get(position));
                if(isChecked){
                    holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.task.setPaintFlags(0);
                }
            }
        });
        Log.d("Adapter", "onBindViewHolder: "+this.arrayList.get(position).getTask());
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public class TodoHolder extends RecyclerView.ViewHolder {
        TextView task;
        TextView priority;
        CheckBox box;
        public TodoHolder(View itemView) {
            super(itemView);
            this.task = itemView.findViewById(R.id.tvTask);
            this.priority = itemView.findViewById(R.id.tvPriority);
            this.box = itemView.findViewById(R.id.boxCompleted);
        }
    }

    public void removeItem(int position){
        this.arrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateReceiptsList(List<Data> newlist) {
        arrayList.clear();
        arrayList.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public void restoreItem(Data item, int position) {
        this.arrayList.add(position, item);
        notifyItemInserted(position);
    }

    public List<Data> getArrayList() {
        return arrayList;
    }

    private String stringValue(Priority p){

        if(p == Priority.HIGH){
            return "HIGH";
        }

        if(p == Priority.MEDIUM){
            return "MEDIUM";
        }

        return "LOW";
    }
}

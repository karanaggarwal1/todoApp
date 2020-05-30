package com.example.todoapplication.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapplication.Constants.Priority;
import com.example.todoapplication.Database.AppDatabase;
import com.example.todoapplication.Models.Data;
import com.example.todoapplication.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>{

    private List<Data> arrayList;
    private AppDatabase db;

    public SearchAdapter(List<Data> arrayList, AppDatabase db) {
        this.arrayList = arrayList;
        this.db = db;
    }

    public void updateReceiptsList(List<Data> newlist) {
        arrayList.clear();
        arrayList.addAll(newlist);
        this.notifyDataSetChanged();
    }
    public void removeItem(int position){
        this.arrayList.remove(position);
        notifyItemRemoved(position);
    }


    public void restoreItem(Data item, int position) {
        this.arrayList.add(position, item);
        notifyItemInserted(position);
    }

    public List<Data> getArrayList() {
        return arrayList;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.search_item, parent, false);
        return new SearchHolder(itemView);
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

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.task.setText(this.arrayList.get(position).getTask());
        holder.priority.setText(stringValue(this.arrayList.get(position).getPriority()));
        if(this.arrayList.get(position).isStatus()){
            holder.task.setPaintFlags(holder.task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.box.setChecked(true);
        } else {
            holder.box.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        TextView task;
        TextView priority;
        CheckBox box;

        public SearchHolder(View itemView) {
            super(itemView);
            this.task = itemView.findViewById(R.id.tvTask);
            this.priority = itemView.findViewById(R.id.tvPriority);
            this.box = itemView.findViewById(R.id.boxCompleted);
        }
    }
}

package com.example.bucketlist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.grid, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(row);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.ViewHolder v, int i) {
        final Task item = taskList.get(i);
        (v).title.setText(item.getTitle());
        (v).description.setText(item.getDescription());

        v.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    v.title.setPaintFlags(v.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    v.description.setPaintFlags(v.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    v.title.setPaintFlags(v.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    v.description.setPaintFlags(v.description.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void swapList(List<Task> newList) {
        taskList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView title;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            title = itemView.findViewById(R.id.textView);
            description = itemView.findViewById(R.id.textView2);
        }
    }
}


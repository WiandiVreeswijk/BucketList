package com.example.bucketlist;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private List<Task> mTasks;
    private TaskClickListener taskClickListener;

    public TaskAdapter(TaskClickListener taskClickListener, List<Task> mTasks) {
        this.taskClickListener = taskClickListener;
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.grid, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder v, int i) {
        v.title.setText(mTasks.get(v.getAdapterPosition()).getTaskTitle());
        v.description.setText(mTasks.get(v.getAdapterPosition()).getTaskDescription());

        if (mTasks.get(v.getAdapterPosition()).getCheck()){
            v.title.setPaintFlags(v.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            v.description.setPaintFlags(v.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            v.checkBox.setChecked(Boolean.TRUE);
        }
        else{
            v.title.setPaintFlags(v.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            v.description.setPaintFlags(v.description.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            v.checkBox.setChecked(Boolean.FALSE);
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }


    public interface TaskClickListener{
        void onCheckboxClick(Task task);
    }
    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            description = itemView.findViewById(R.id.taskDescription);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskClickListener.onCheckboxClick(mTasks.get(getAdapterPosition()));
                }
            });
        }
    }
}

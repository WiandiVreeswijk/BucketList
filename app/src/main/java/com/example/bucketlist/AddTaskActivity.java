package com.example.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    TextView titleEdit;
    TextView descriptionEdit;
    Button create;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleEdit = findViewById(R.id.taskTitle);
        descriptionEdit = findViewById(R.id.taskDescription);
        create = findViewById(R.id.addButton);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Task task  = new Task(titleEdit.getText().toString(), descriptionEdit.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(MainActivity.TASK, task);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}


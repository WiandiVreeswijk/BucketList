package com.example.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddTaskActivity extends AppCompatActivity {
    private TextView taskTitle;
    private TextView taskDescription;
    private Button mAddButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDescription);
        mAddButton = findViewById(R.id.addButton);

        mAddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String titleText = taskTitle.getText().toString();
                String descriptionText = taskDescription.getText().toString();

                if(!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descriptionText)){
                    Task task = new Task(titleText,descriptionText,false);
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.NEW_TASK,task);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }
}

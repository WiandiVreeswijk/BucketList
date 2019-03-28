package com.example.bucketlist;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private RecyclerView mRecyclerView;
    private List<Task> taskList;
    private TaskAdapter mTaskAdapter;
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private TaskRoomDatabase db;
    private GestureDetector mGestureDetector;

    public static final int REQUEST_CODE = 1420;
    public static final String TASK = "Task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskList = new ArrayList<>();
        db = TaskRoomDatabase.getDatabase(this);

        mRecyclerView = findViewById(R.id.checkList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new TaskAdapter(taskList));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //Delete item with long click on the item
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = mRecyclerView.getChildAdapterPosition(child);
                    deleteTask(taskList.get(adapterPosition));
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(this);

        getAllTasks();
    }

    private void getAllTasks() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskList = db.dao().getAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }

    private void deleteTask(final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.dao().delete(task);
                getAllTasks();
            }
        });
    }

    private void insertTask(final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.dao().insert(task);
                getAllTasks();
            }
        });
    }

    private void deleteAllTasks(final List<Task> tasks) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.dao().delete(tasks);
                getAllTasks();
            }
        });
    }

    private void updateUI() {
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(taskList);
            mRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.swapList(taskList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            deleteAllTasks(taskList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Task newTask = data.getParcelableExtra(MainActivity.TASK);
                insertTask(newTask);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
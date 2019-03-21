package com.example.bucketlist;

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

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, TaskAdapter.TaskClickListener {

    private TaskAdapter mTaskAdapter;
    private RecyclerView mRecyclerView;
    private List<Task> taskList;
    private TaskRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private GestureDetector mGestureDetector;

    public static final String NEW_TASK = "NewTask";
    public static final int REQUEST_CODE = 4231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = TaskRoomDatabase.getDatabase(this);
        taskList = new ArrayList<>();
        initRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        getAllTasks();
    }

    private void initRecyclerView(){
        mTaskAdapter = new TaskAdapter(this,taskList);
        mRecyclerView = findViewById(R.id.checkList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mTaskAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View v = mRecyclerView.findChildViewUnder(e.getX(),e.getY());
                if(v != null){
                    int adapterPosition = mRecyclerView.getChildAdapterPosition(v);
                    deleteTask(taskList.get(adapterPosition));
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(this);
        getAllTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            deleteTasks(taskList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteTasks(final List<Task> tasks){
        executor.execute(new Runnable() {
            @Override
            public void run() {
               db.dao().delete(tasks);
               getAllTasks();
            }
        });
    }
    private void deleteTask(final Task task){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.dao().delete(task);
                getAllTasks();
            }
        });
    }
    private void updateTask(final Task task){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.dao().update(task);
                getAllTasks();
            }
        });
    }
    private void insertTasks(final Task task){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.dao().insert(task);
                getAllTasks();
            }
        });
    }

    private void updateUI(List<Task> tasks){
        taskList.clear();
        taskList.addAll(tasks);
        mTaskAdapter.notifyDataSetChanged();
    }
    private void getAllTasks(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = db.dao().getAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(tasks);
                    }
                });
            }
        });
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
    public void onCheckboxClick(final Task task) {
        if(task.getCheck()){
            task.setCheck(Boolean.FALSE);
        }else{
            task.setCheck(Boolean.TRUE);
        }
        updateTask(task);
        getAllTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(MainActivity.NEW_TASK);
                insertTasks(task);
                updateUI(taskList);
            }
        }
    }
}

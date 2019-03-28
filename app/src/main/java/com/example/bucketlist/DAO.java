package com.example.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Delete
    void delete(List<Task> taskList);

    @Query("SELECT * from task_table")
    List<Task> getAllTasks();


}

package com.example.bucketlist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName ="taskTable")
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "taskTitle")
    private String taskTitle;

    @ColumnInfo(name = "taskDescription")
    private String taskDescription;

    @ColumnInfo(name = "check")
    private Boolean check;

    public Task(String taskTitle, String taskDescription, Boolean check) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.check = check;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskTitle);
        dest.writeString(taskDescription);
        dest.writeValue(this.check);
        dest.writeLong(this.id);
    }
    protected Task(Parcel in){
        this.id = in.readLong();
        this.taskTitle = in.readString();
        this.taskDescription = in.readString();
        this.check = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

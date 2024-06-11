package com.example.todoapp.models;

public class TaskTodo {
    private String date;
    private String title;
    private String description;
    private String email;


    public TaskTodo(){}

    public TaskTodo(String date, String title, String description, String email){
        this.date = date;
        this.title = title;
        this.description = description;
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Task{" +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

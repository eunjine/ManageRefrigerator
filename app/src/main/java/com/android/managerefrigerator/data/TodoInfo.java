package com.android.managerefrigerator.data;

public class TodoInfo {
    private String todo;
    private boolean isChecked;

    public TodoInfo(String todo, boolean isChecked) {
        this.todo = todo;
        this.isChecked = isChecked;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

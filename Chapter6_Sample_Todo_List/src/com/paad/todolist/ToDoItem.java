package com.paad.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoItem {
  
  /** Get ToDo List item task name */
  public String getTask() {
    return task;
  }
  String task;
  
  /** Get ToDo List item creation date */
  public Date getCreated() {
    return created;    
  }
  Date created;

  public ToDoItem(String _task) {
    this(_task, new Date(java.lang.System.currentTimeMillis()));
  }
  
  public ToDoItem(String _task, Date _created) {
    task = _task;
    created = _created;
  }

  @Override
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");  
    return "(" + sdf.format(created) +  ") " + task; 
  }
}

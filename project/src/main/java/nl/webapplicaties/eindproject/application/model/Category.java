package nl.webapplicaties.eindproject.application.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Category {
    private String title;
    private HashMap<String, TaskList> taskLists;

    public Category(String title) {
        this.title = title;
        this.taskLists = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTaskList(String taskListTitle){
        for(String key : taskLists.keySet()){
            if(key.equalsIgnoreCase(taskListTitle)){
                return true;
            }
        }
        return false;
    }

    public TaskList findTaskListByTitle(String title){
        return taskLists.get(title);
    }

    public void addTaskList(TaskList taskList){
        taskLists.put(taskList.getTitle(), taskList);
    }

    public void deleteTaskList(String title, TaskList taskList) {
        taskLists.remove(title, taskList);
    }

    public ArrayList<TaskList> taskListsToArrayList() {
        ArrayList<TaskList> taskListArrayList = new ArrayList<>();
        for (String key : taskLists.keySet()) {
            taskListArrayList.add(taskLists.get(key));
        }
        taskListArrayList.sort(new Comparator<TaskList>() {
            @Override
            public int compare(TaskList o1, TaskList o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        return taskListArrayList;
    }
}

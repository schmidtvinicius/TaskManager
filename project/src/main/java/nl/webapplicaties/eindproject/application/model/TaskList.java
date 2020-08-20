package nl.webapplicaties.eindproject.application.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskList {

    private String title;
    private User owner;
    private HashMap<String, User> members;
    private HashMap<Integer, Task> tasks;

    public TaskList(String title){
        this.title = title;
        tasks = new HashMap<>();
        members = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<User> membersAsArrayList() {
        Collection<User> values = members.values();
        return new ArrayList<>(values);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Task> tasksAsArrayList(){
        ArrayList<Task> allTasks = new ArrayList<>();
        for(Integer taskId : tasks.keySet()){
            allTasks.add(tasks.get(taskId));
        }
        return allTasks;
    }

    public void addOwner(User creator){
        this.owner = creator;
    }

    public boolean isOwner(String username){
        return owner.getUsername().equals(username);
    }

    public String owner(){
        return owner.getUsername();
    }

    public Task findTaskById(int taskId){
        return tasks.get(taskId);
    }

    public void addTask(Task task){
        tasks.put(task.getTaskId(), task);
    }

    public void addMember(User user){
        members.put(user.getUsername(), user);
    }

    public void removeMember(String usernameToRemove){
        User user = members.remove(usernameToRemove);
        System.out.println(user.getUsername());
    }

    public boolean hasMember(String memberUsername){
        return members.containsKey(memberUsername);
    }

    public void deleteTask(int taskId){
        tasks.remove(taskId);
    }
}

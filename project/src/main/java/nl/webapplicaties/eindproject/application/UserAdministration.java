package nl.webapplicaties.eindproject.application;

import nl.webapplicaties.eindproject.application.exception.CategoryNotFoundException;
import nl.webapplicaties.eindproject.application.exception.TaskListNotFoundException;
import nl.webapplicaties.eindproject.application.exception.TaskNotFoundException;
import nl.webapplicaties.eindproject.application.exception.UserNotFoundException;
import nl.webapplicaties.eindproject.application.model.Category;
import nl.webapplicaties.eindproject.application.model.Task;
import nl.webapplicaties.eindproject.application.model.TaskList;
import nl.webapplicaties.eindproject.application.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAdministration {

    private HashMap<String, User> users;
    private static UserAdministration userAdministration;

    private UserAdministration() {
        users = new HashMap<>();
        init();
    }

    private void init() {
        User user1 = new User("Joy", "Joyce", "Richter", "480801@student.saxion.nl", "wachtwoord");
        User user2 = new User("Vini", "Vinícius", "Schmidt", "478336@student.saxion.nl", "qwerty123");
        users.put(user1.getUsername(), user1);
        users.put(user2.getUsername(), user2);
        user1.addCategory(new Category("School"));
        user1.addCategory(new Category("Boodschappen"));
        user1.addCategory(new Category("Sport"));
        TaskList taskList1 = new TaskList("Bla1");
        taskList1.addOwner(user1);

        Task task1 = new Task();
        task1.setTitle("Lopen");
        task1.setStatus(Task.STATUS_DOING);
        taskList1.addTask(task1);

        Task task2 = new Task();
        task2.setTitle("Rennen");
        task2.setStatus(Task.STATUS_TO_DO);
        taskList1.addTask(task2);

        TaskList taskList2 = new TaskList("Bla2");
        taskList2.addOwner(user1);

        Task task3 = new Task();
        task3.setTitle("Cu1");
        task3.setStatus(Task.STATUS_DONE);
        taskList2.addTask(task3);

        Task task4 = new Task();
        task4.setTitle("Cu2");
        task4.setStatus(Task.STATUS_DONE);
        taskList2.addTask(task4);

        TaskList taskList3 = new TaskList("Bla3");
        taskList3.addOwner(user1);
        user1.getCategoryByName("School").addTaskList(taskList1);
        user1.getCategoryByName("School").addTaskList(taskList2);
        user1.getCategoryByName("School").addTaskList(taskList3);
    }

    public static UserAdministration getInstance() {
        if (userAdministration == null) {
            userAdministration = new UserAdministration();
        }
        return userAdministration;
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public User findUser(String username){
        User user = UserAdministration.getInstance().getUser(username);
        if(user == null){
            throw new UserNotFoundException();
        }
        return user;
    }

    public Category findCategory(User user, String categoryTitle){
        Category category = user.getCategoryByName(categoryTitle);
        if(category == null){
            throw new CategoryNotFoundException();
        }
        return category;
    }

    public TaskList findTaskList(Category category, String taskListTitle){
        TaskList taskList = category.findTaskListByTitle(taskListTitle);
        if(taskList == null){
            throw new TaskListNotFoundException();
        }
        return taskList;
    }

    public Task findTask(TaskList taskList, int taskId){
        Task task = taskList.findTaskById(taskId);
        if(task == null){
            throw new TaskNotFoundException();
        }
        return task;
    }

    public boolean validateUser(String username, String password){
        User user = findUser(username);
        return user.getPassword().equals(password);
    }

    public ArrayList<String> findMatchingUsernames(String usernameToSearch){
        ArrayList<String> matchingUsernames = new ArrayList<>();
        usernameToSearch = usernameToSearch.toLowerCase();
        for(String username : users.keySet()){
            String usernameLowerCase = username.toLowerCase();
            if(usernameLowerCase.contains(usernameToSearch)){
                matchingUsernames.add(username);
            }
        }
        return matchingUsernames;
    }

    public boolean hasUser(String username) {
        for(String key : users.keySet()){
            if(key.equalsIgnoreCase(username)){
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
}

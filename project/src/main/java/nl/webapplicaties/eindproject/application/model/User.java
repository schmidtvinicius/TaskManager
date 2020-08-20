package nl.webapplicaties.eindproject.application.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class User {

    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private HashMap<String, Category> categories;

    public User(String username, String firstName, String lastName, String emailAddress, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.categories = new HashMap<>();
        this.categories.put("Shared", new Category("Shared"));
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Category> categoriesToArrayList() {
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        for (String title : categories.keySet()) {
            categoryArrayList.add(categories.get(title));
        }
        categoryArrayList.sort(new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        return categoryArrayList;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean hasCategory(String title){
        for(String key : categories.keySet()){
            if(key.equalsIgnoreCase(title)){
                return true;
            }
        }
        return false;
    }

    public Category getCategoryByName(String categoryName){
        return categories.get(categoryName);
    }

    public void addCategory(Category category) {
        categories.put(category.getTitle(), category);
    }

    public boolean deleteCategory(String title, Category category){
        return categories.remove(title, category);
    }

    public void addSharedTaskList(TaskList sharedTaskList){
        Category shared = categories.get("Shared");
        shared.addTaskList(sharedTaskList);
    }

    public void removeSharedTaskList(TaskList sharedTaskList){
        Category shared = categories.get("Shared");
        shared.deleteTaskList(sharedTaskList.getTitle(), sharedTaskList);
    }
}

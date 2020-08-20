package nl.webapplicaties.eindproject.application.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;

public class Task {

    public static final String STATUS_TO_DO = "To Do";
    public static final String STATUS_DOING = "Doing";
    public static final String STATUS_DONE = "Done";

    private static int lastId;

    private int taskId;
    private String title;
    private String description;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    public Task(){
        taskId = lastId;
        lastId++;
    }

    public static ArrayList<String> getStatuses(){
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add(STATUS_TO_DO);
        statuses.add(STATUS_DOING);
        statuses.add(STATUS_DONE);

        return statuses;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

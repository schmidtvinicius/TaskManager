package nl.webapplicaties.eindproject.application.controller;

import nl.webapplicaties.eindproject.application.UserAdministration;
import nl.webapplicaties.eindproject.application.exception.CategoryNotFoundException;
import nl.webapplicaties.eindproject.application.exception.TaskListNotFoundException;
import nl.webapplicaties.eindproject.application.exception.TaskNotFoundException;
import nl.webapplicaties.eindproject.application.exception.UserNotFoundException;
import nl.webapplicaties.eindproject.application.model.Category;
import nl.webapplicaties.eindproject.application.model.Task;
import nl.webapplicaties.eindproject.application.model.TaskList;
import nl.webapplicaties.eindproject.application.model.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping(path = "category/{categoryTitle}/tasklist/{taskListTitle}/")
public class TaskController {

    @GetMapping(path = "{taskId}/edit")
    public String getTask(@PathVariable("categoryTitle") String categoryTitle, @PathVariable("taskListTitle") String taskListTitle,
                          @PathVariable("taskId") int taskId, HttpSession session, Model model){
        if (isLoggedIn(session)) {
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                Task task = UserAdministration.getInstance().findTask(taskList, taskId);
                model.addAttribute("categoryTitle", categoryTitle);
                model.addAttribute("taskListTitle", taskListTitle);
                model.addAttribute("task", task);
                model.addAttribute("statuses", Task.getStatuses());
                return "Task_details";
            }catch(RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(path = "add/")
    public String createTask(@PathVariable("categoryTitle") String categoryTitle, @PathVariable ("taskListTitle") String taskListTitle,
                             HttpSession session, Model model){
        if (isLoggedIn(session)) {
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                model.addAttribute("taskList", taskList);
                model.addAttribute("category", category);
                model.addAttribute("statuses", Task.getStatuses());
                return "Create_task";
            }catch (RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping(path = "{taskId}/edit")
    public String saveChangesToTask(@PathVariable("categoryTitle") String categoryTitle, @PathVariable ("taskListTitle") String taskListTitle,
                                    @PathVariable("taskId") int taskId, String title, String description, String status,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dueDate, HttpSession session, Model model){
        if(isLoggedIn(session)){
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                Task task = UserAdministration.getInstance().findTask(taskList, taskId);
                task.setTitle(title);
                task.setDescription(description);
                task.setStatus(status);
                task.setDueDate(dueDate);
                return "redirect:/category/"+categoryTitle+"/tasklist/"+taskListTitle+"/";
            }catch (RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(path = "{taskId}/delete/")
    public String deleteTask(@PathVariable("categoryTitle") String categoryTitle, @PathVariable("taskListTitle") String taskListTitle,
                             @PathVariable("taskId") int taskId, HttpSession session, Model model){
        if (isLoggedIn(session)) {
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                taskList.deleteTask(taskId);
                return "redirect:/category/"+categoryTitle+"/tasklist/"+taskListTitle+"/";
            }catch(RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("username") != null;
    }
}

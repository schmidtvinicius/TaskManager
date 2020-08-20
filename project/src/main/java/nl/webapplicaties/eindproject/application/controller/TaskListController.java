package nl.webapplicaties.eindproject.application.controller;

import nl.webapplicaties.eindproject.application.UserAdministration;
import nl.webapplicaties.eindproject.application.model.Category;
import nl.webapplicaties.eindproject.application.model.Task;
import nl.webapplicaties.eindproject.application.model.TaskList;
import nl.webapplicaties.eindproject.application.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("category/{categoryTitle}/tasklist/")
public class TaskListController {

    @GetMapping(path = "")
    public String getTaskLists(@PathVariable("categoryTitle") String categoryTitle, HttpSession session, Model model){
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                model.addAttribute("category", category);
                return "Overview_tasklist";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }

    }

    @GetMapping(path = "{tasklistTitle}/")
    public String getTaskList(@PathVariable("categoryTitle") String categoryTitle, @PathVariable("tasklistTitle")
            String taskListTitle, HttpSession session, Model model){
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                model.addAttribute("tasklist", taskList);
                model.addAttribute("statuses", Task.getStatuses());
                model.addAttribute("username", user.getUsername());
                return "Tasks_overview";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(path = "{tasklistTitle}/members/")
    public String getMembers(HttpSession session, @PathVariable("categoryTitle") String categoryTitle,
                             @PathVariable("tasklistTitle") String taskListTitle, Model model){
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                model.addAttribute("taskList", taskList);
                model.addAttribute("username", user.getUsername());
                return "Members";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage");
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping(path = "{tasklistTitle}/members/add/")
    public String getAddMember(HttpSession session, @PathVariable("categoryTitle") String categoryTitle,
                             @PathVariable("tasklistTitle") String taskListTitle, Model model){
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                return "Add_member";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage");
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(path = "{tasklistTitle}/members/add/")
    public String postAddMember(HttpSession session, @PathVariable("categoryTitle") String categoryTitle,
                               @PathVariable("tasklistTitle") String tasklistTitle, String newMemberUsername, Model model){
        if(isLoggedIn(session)){
            try {
                newMemberUsername = newMemberUsername.trim();
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, tasklistTitle);
                if(taskList.hasMember(newMemberUsername)){
                    model.addAttribute("errorMessage", "Task list already has member");
                    return "Add_member";
                }
                if(taskList.isOwner(newMemberUsername)){
                    model.addAttribute("errorMessage", "You cannot add yourself as member");
                    return "Add_member";
                }
                User newMember = null;
                try {
                    newMember = UserAdministration.getInstance().findUser(newMemberUsername);
                } catch (RuntimeException e) {
                    model.addAttribute("errorMessage", e.getMessage());
                    return "Add_member";
                }
                taskList.addMember(newMember);
                newMember.addSharedTaskList(taskList);
                return "redirect:/category/"+categoryTitle+"/tasklist/"+tasklistTitle+"/";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(path = "{tasklistTitle}/members/delete/")
    public String deleteMember(HttpSession session, @PathVariable("categoryTitle") String categoryTitle,
                               @PathVariable("tasklistTitle") String tasklistTitle, @RequestParam("username") String usernameToDelete, Model model){
        if(isLoggedIn(session)){
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, tasklistTitle);
                User userToRemove = UserAdministration.getInstance().findUser(usernameToDelete);
                taskList.removeMember(usernameToDelete);
                userToRemove.removeSharedTaskList(taskList);
                return "redirect:/category/"+categoryTitle+"/tasklist/"+tasklistTitle+"/";
            }catch (RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(path = "{taskListTitle}/")
    public String addTask(@PathVariable("categoryTitle") String categoryTitle, @PathVariable("taskListTitle") String taskListTitle,
                                    HttpSession session, Task task, Model model){
        if(isLoggedIn(session)){
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                taskList.addTask(task);
                return "redirect:/category/"+categoryTitle+"/tasklist/"+taskListTitle+"/";
            }catch(RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(path = "add/")
    public String createTaskList(@PathVariable("categoryTitle") String categoryTitle, HttpSession session, Model model) {
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().getUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                model.addAttribute("categoryTitle", categoryTitle);
                return "Create_tasklist";
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(path = "")
    public String addTaskList(@PathVariable("categoryTitle") String categoryTitle, HttpSession session, String taskListTitle,
                              Model model){
        if(isLoggedIn(session)){
            try {
                taskListTitle = taskListTitle.trim();
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                if(category.hasTaskList(taskListTitle)){
                    model.addAttribute("errorMessage", "Task list already exists");
                    return "Create_tasklist";
                }
                TaskList newTaskList = new TaskList(taskListTitle);
                category.addTaskList(newTaskList);
                newTaskList.addOwner(user);
                return "redirect:/category/"+categoryTitle+"/tasklist/";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(path = "{tasklistTitle}/delete/")
    public String deleteTaskList(@PathVariable("categoryTitle") String categoryTitle, @PathVariable("tasklistTitle")
            String taskListTitle, HttpSession session, Model model) {
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                TaskList taskList = UserAdministration.getInstance().findTaskList(category, taskListTitle);
                ArrayList<User> members = taskList.membersAsArrayList();
                for(User member : members){
                    member.removeSharedTaskList(taskList);
                }
                category.deleteTaskList(taskListTitle, taskList);
                return "redirect:/category/"+categoryTitle+"/tasklist/";
            } catch (RuntimeException e) {
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

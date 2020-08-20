package nl.webapplicaties.eindproject.application.controller;

import nl.webapplicaties.eindproject.application.UserAdministration;
import nl.webapplicaties.eindproject.application.exception.CategoryAlreadyExistException;
import nl.webapplicaties.eindproject.application.exception.CategoryNotFoundException;
import nl.webapplicaties.eindproject.application.exception.UserNotFoundException;
import nl.webapplicaties.eindproject.application.model.Category;
import nl.webapplicaties.eindproject.application.model.TaskList;
import nl.webapplicaties.eindproject.application.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/category/")
public class CategoryController {

    @GetMapping(path = "")
    public String getCategories(HttpSession session, Model model) {
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                model.addAttribute("user", user);
                return "Homepage";
            } catch (RuntimeException e) {
                model.addAttribute("errormessage", e.getMessage());
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping(path = "add/")
    public String createCategory(HttpSession session, Model model) {
        if (isLoggedIn(session)) {
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                return "Create_category";
            } catch (Exception e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(path = "")
    public String addCategories(String categoryTitle, HttpSession session, Model model) {
        if(isLoggedIn(session)){
            try {
                categoryTitle = categoryTitle.trim();
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                if (user.hasCategory(categoryTitle)) {
                    throw new CategoryAlreadyExistException();
                }
                user.addCategory(new Category(categoryTitle));
                return "redirect:/category/";
            }catch (CategoryAlreadyExistException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "Create_category";
            }
            catch (RuntimeException e) {
                model.addAttribute("errormessage", e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(path = "{categoryTitle}/tasklist/delete/")
    public String deleteCategory(@PathVariable("categoryTitle") String categoryTitle, HttpSession session, Model model){
        if (isLoggedIn(session)) {
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                Category category = UserAdministration.getInstance().findCategory(user, categoryTitle);
                ArrayList<TaskList> taskListsToDelete = category.taskListsToArrayList();
                for(TaskList taskList : taskListsToDelete){
                    ArrayList <User> members = taskList.membersAsArrayList();
                    for(User member : members){
                        member.removeSharedTaskList(taskList);
                    }
                }
                user.deleteCategory(categoryTitle, category);
                return "redirect:/category/";
            } catch (RuntimeException e) {
                model.addAttribute("errormessage", e.getMessage());
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

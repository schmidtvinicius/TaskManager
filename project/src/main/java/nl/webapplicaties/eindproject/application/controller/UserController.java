package nl.webapplicaties.eindproject.application.controller;

import nl.webapplicaties.eindproject.application.UserAdministration;
import nl.webapplicaties.eindproject.application.exception.UserAlreadyExistsException;
import nl.webapplicaties.eindproject.application.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Controller
@RequestMapping("")
public class UserController {

    @GetMapping(path = "/tasklist/search/")
    public String getUserSearch(@RequestParam("q") String usernameToSearch, HttpSession session, Model model, HttpServletRequest request){
        if(isLoggedIn(session)){
            try {
                if(usernameToSearch.isEmpty()){
                    model.addAttribute("errorMessage", "No user found!");
                    return "Search_result";
                }
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                usernameToSearch = usernameToSearch.trim();
                ArrayList<String> matchingUsernames = UserAdministration.getInstance().findMatchingUsernames(usernameToSearch);
                if(matchingUsernames.isEmpty()){
                    model.addAttribute("errorMessage", "No user found!");
                    return "Search_result";
                }
                model.addAttribute("matchingUsernames", matchingUsernames);
                return "Search_result";
            } catch (RuntimeException e) {
                request.setAttribute("errorStatus", HttpStatus.NOT_FOUND);
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }else{
            model.addAttribute("errorMessage", "User must be logged in");
            return "redirect:/login";
        }
    }

    @GetMapping(path = "/settings")
    public String getSettings(HttpSession session, Model model){
        if(isLoggedIn(session)){
            try {
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                model.addAttribute("user", user);
                return "Settings_page";
            } catch (RuntimeException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(path = "/settings")
    public String saveSettings(HttpSession session, Model model, String firstName, String lastName, String emailAddress){
        if(isLoggedIn(session)){
            try{
                User user = UserAdministration.getInstance().findUser((String) session.getAttribute("username"));
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmailAddress(emailAddress);
                return "redirect:/category/";
            }catch (RuntimeException e){
                model.addAttribute("errorMessage", e.getMessage());
                return "error";
            }
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping(path = "")
    public String redirectLogin(HttpSession session){
        if(isLoggedIn(session)){
            return "redirect:/category/";
        }
        return "redirect:/login";
    }

    @GetMapping(path = "/login")
    public String getLogin(HttpSession session){
        if(isLoggedIn(session)){
            return "redirect:/category/";
        }
        return "Login";
    }

    @GetMapping(path = "/signup")
    public String getSignUp(HttpSession session){
        if(isLoggedIn(session)){
            return "redirect:/category/";
        }
        return "Sign_up";
    }

    @PostMapping(path = "/signUp")
    public String postSignUp(HttpSession session, String firstName, String lastName, String emailAddress, String username,
                             String password, Model model, HttpServletResponse response){
        try {
            username = username.trim();
            if(UserAdministration.getInstance().hasUser(username)){
                throw new UserAlreadyExistsException();
            }
            response.addCookie(updateCookie());
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(1800);
            UserAdministration.getInstance().addUser(new User(username, firstName, lastName, emailAddress, password));
            return "redirect:/category/";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "Sign_up";
        } catch (UnsupportedEncodingException e) {
            return "redirect:/category/";
        }
    }

    @PostMapping(path = "/login")
    public String postLogin(HttpSession session, HttpServletResponse response, String username, String password, Model model){
        try {
            if(UserAdministration.getInstance().validateUser(username, password)){
                response.addCookie(updateCookie());
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(1800);
                return "redirect:/category/";
            }else{
                model.addAttribute("errorMessage", "Password is invalid!");
                return "Login";
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "Login";
        } catch (UnsupportedEncodingException e) {

            // If the cookie is not created successfully, the user simply gets redirected to the homepage
            return "redirect:/category/";
        }
    }

    @PostMapping(path = "/logout")
    public String postLogout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    public Cookie updateCookie() throws UnsupportedEncodingException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();

        String date = dateFormat.format(cal.getTime());

        String encodedCookieValue = URLEncoder.encode(date, "UTF-8");
        return new Cookie("lastVisited", encodedCookieValue);
    }

    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("username") != null;
    }
}

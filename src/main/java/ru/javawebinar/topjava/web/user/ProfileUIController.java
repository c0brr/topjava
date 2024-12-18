package ru.javawebinar.topjava.web.user;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "profile";
        } else {
            try {
                super.update(userTo, SecurityUtil.authUserId());
            } catch (DataIntegrityViolationException e) {
                return processingDIVException(e, result, null);
            }
            SecurityUtil.get().setTo(userTo);
            status.setComplete();
            return "redirect:/meals";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                super.create(userTo);
            } catch (DataIntegrityViolationException e) {
                return processingDIVException(e, result, model);
            }
            status.setComplete();
            return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
        }
    }

    private String processingDIVException(DataIntegrityViolationException e, BindingResult result, ModelMap model) {
        if (e.getCause() instanceof ConstraintViolationException) {
            if (((ConstraintViolationException) e.getCause()).getConstraintName().equals("users_unique_email_idx")) {
                result.rejectValue("email", "409", "User with this email already exists");
            }
        }
        if (model != null) {
            model.addAttribute("register", true);
        }
        return "profile";
    }
}
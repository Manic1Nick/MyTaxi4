package ua.artcode.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.SecurityService;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.validator.UserValidator;

@Controller
public class ApplicationController {

    @Autowired
    UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your userphone or password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/registration_passenger", method = RequestMethod.GET)
    public String registrationPassenger(Model model) {
        model.addAttribute("userForm", new User());

        return "registration_passenger";
    }

    @RequestMapping(value = "/registration_passenger", method = RequestMethod.POST)
    public String registrationPassenger(@ModelAttribute("userForm") User userForm,
                                        BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration_passenger";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUserphone(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/registration_driver", method = RequestMethod.GET)
    public String registrationDriver(Model model) {
        model.addAttribute("userForm", new User());

        return "registration_driver";
    }

    @RequestMapping(value = "/registration_driver", method = RequestMethod.POST)
    public String registrationDriver(@ModelAttribute("userForm") User userForm,
                                     BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration_driver";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUserphone(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }
}

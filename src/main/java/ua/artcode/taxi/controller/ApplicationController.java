package ua.artcode.taxi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController {

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("ajax-login");
    }

    @RequestMapping("/register-passenger")
    public ModelAndView registerPassenger() {
        return new ModelAndView("ajax-register-passenger");
    }

    @RequestMapping("/register-driver")
    public ModelAndView registerDriver() {
        return new ModelAndView("ajax-register-driver");
    }
}

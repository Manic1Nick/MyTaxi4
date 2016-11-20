package ua.artcode.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.SecurityService;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.validator.OrderValidator;
import ua.artcode.taxi.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class ApplicationController {

    @Autowired
    UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private OrderValidator orderValidator;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your userphone or password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(@ModelAttribute("message") String message, Model model, Principal principal) {

        User user = userService.findByUserphone(principal.getName());
        model.addAttribute("currentUser", user);

        if (message.equals(""))
            message = null;

        model.addAttribute("message", message);

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
        model.addAttribute("message", "Welcome, " + userForm.getUsername());

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
        model.addAttribute("message", "Welcome, " + userForm.getUsername() + "!");

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/order/make", method = RequestMethod.GET)
    public String makeOrder(@ModelAttribute("orderForm") Order orderForm,
                            Model model, Principal principal, HttpServletRequest req) {

        model.addAttribute("orderForm", new Order());

        User passenger = userService.findByUserphone(principal.getName());
        model.addAttribute("currentUser", passenger);

        Order lastOrder = userService.getLastOrder(principal.getName());
        model.addAttribute("lastOrder", lastOrder);

        if (req.getParameter("id") != null) {
            Order copyOrder = userService.getOrderById(Long.parseLong(req.getParameter("id")));
            model.addAttribute("copyOrder", copyOrder);
        }

        return "make_order";
    }

    @RequestMapping(value = "/order/make", method = RequestMethod.POST)
    public String makeOrder(@ModelAttribute("orderForm") Order orderForm,
                            BindingResult bindingResult, Principal principal,
                            Model model) throws InputDataWrongException {

        User user = userService.findByUserphone(principal.getName());
        orderValidator.validate(orderForm, bindingResult);

        if (bindingResult.hasErrors())
            return "make_order";

        userService.save(orderForm, user);
        model.addAttribute("message", "Success! Your order id=" + orderForm.getId() + " was created.");

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/order/get", method = RequestMethod.GET)
    public String getOrderInfo(Model model, Principal principal, HttpServletRequest req) {

        User user = userService.findByUserphone(principal.getName());
        Order order = userService.getOrderById(Long.parseLong(req.getParameter("id")));

        model.addAttribute("currentUser", user);
        model.addAttribute("passenger", userService.findById(order.getIdPassenger()));
        model.addAttribute("driver", userService.findById(order.getIdDriver()));
        model.addAttribute("order", order);

        return "order_info";
    }
}

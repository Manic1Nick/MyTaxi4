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
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.SecurityService;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.validator.OrderValidator;
import ua.artcode.taxi.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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

        User user = userService.getByUserphone(principal.getName());
        model.addAttribute("currentUser", user);

        if (message.equals(""))
            message = null;

        model.addAttribute("message", message);

        return "welcome";
    }

    @RequestMapping(value = "/registration/passenger", method = RequestMethod.GET)
    public String registrationPassenger(Model model) {
        model.addAttribute("userForm", new User());

        return "registration_passenger";
    }

    @RequestMapping(value = "/registration/passenger", method = RequestMethod.POST)
    public String registrationPassenger(@ModelAttribute("userForm") User userForm,
                                        BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration_passenger";
        }

        userService.saveNewUser(userForm);
        securityService.autologin(userForm.getUserphone(), userForm.getPasswordConfirm());
        model.addAttribute("message", "Welcome, " + userForm.getUsername());

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/registration/driver", method = RequestMethod.GET)
    public String registrationDriver(Model model) {
        model.addAttribute("userForm", new User());

        return "registration_driver";
    }

    @RequestMapping(value = "/registration/driver", method = RequestMethod.POST)
    public String registrationDriver(@ModelAttribute("userForm") User userForm,
                                     BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration_driver";
        }

        userService.saveNewUser(userForm);
        securityService.autologin(userForm.getUserphone(), userForm.getPasswordConfirm());
        model.addAttribute("message", "Welcome, " + userForm.getUsername() + "!");

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/order/make", method = RequestMethod.GET)
    public String makeOrder(@ModelAttribute("orderForm") Order orderForm,
                            Model model, Principal principal, HttpServletRequest req) {

        model.addAttribute("orderForm", new Order());

        User passenger = userService.getByUserphone(principal.getName());
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

        User user = userService.getByUserphone(principal.getName());
        orderValidator.validate(orderForm, bindingResult);

        if (bindingResult.hasErrors())
            return "make_order";

        userService.saveNewOrder(orderForm, user);
        model.addAttribute("message", "Success! Your order id=" + orderForm.getId() + " was created.");

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/order/get", method = RequestMethod.GET)
    public String getOrderInfo(Model model, Principal principal, HttpServletRequest req) {

        User user = userService.getByUserphone(principal.getName());
        Order order = userService.getOrderById(Long.parseLong(req.getParameter("id")));

        model.addAttribute("currentUser", user);
        model.addAttribute("passenger", userService.getById(order.getIdPassenger()));
        model.addAttribute("driver", userService.getById(order.getIdDriver()));
        model.addAttribute("order", order);

        return "order_info";
    }

    @RequestMapping(value = "/order/get/all", method = RequestMethod.GET)
    public String getAllOrders(Model model, Principal principal) {

        List<Order> orders = userService.getListOrdersOfUser(principal.getName());
        model.addAttribute("listOrders", orders);

        User user = userService.getByUserphone(principal.getName());
        model.addAttribute("currentUser", user);

        return "history";
    }

    @RequestMapping(value = "/order/get/all-new", method = RequestMethod.GET)
    public String getAllNewOrders(Model model, Principal principal) {

        User user = userService.getByUserphone(principal.getName());

        if (user.getCar() == null) {
            model.addAttribute("error", "User is not driver.");
            return "wellcome";
        }

        List<Order> orders = userService.getListOrdersByOrderStatus(OrderStatus.NEW);
        try {
            Map<Long, Double> mapDistances =
                    userService.createMapOrdersWithDistancesToDriver(orders, user.getCurrentAddress());
            model.addAttribute("listOrders", orders);
            model.addAttribute("mapDistances", mapDistances);

        } catch (InputDataWrongException e) {
            e.printStackTrace();
            model.addAttribute("error", "Google API error. Check your internet connection.");
            return "wellcome";
        }

        return "new_orders";
    }

    @RequestMapping(value = "/order/take", method = RequestMethod.GET)
    public String takeOrderByDriver(Model model, Principal principal, HttpServletRequest req) {

        User user = userService.getByUserphone(principal.getName());
        Order takenOrder = userService.takeOrderByDriver(Long.parseLong(req.getParameter("id")), user);

        model.addAttribute("currentUser", user);
        model.addAttribute("passenger", userService.getById(takenOrder.getIdPassenger()));
        model.addAttribute("driver", userService.getById(takenOrder.getIdDriver()));
        model.addAttribute("order", takenOrder);

        return "order_info";
    }
}

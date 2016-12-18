package ua.artcode.taxi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.WrongStatusOrderException;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.repository.OrderRepository;
import ua.artcode.taxi.repository.RoleRepository;
import ua.artcode.taxi.repository.UserRepository;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

import java.io.IOException;
import java.util.*;

@Service(value = "service")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private double pricePerKilometer;
    private GoogleMapsAPI googleMapsAPI;

    public UserServiceImpl() {
        pricePerKilometer = Constants.PRICE_PER_KILOMETER_UAH;
        googleMapsAPI = new GoogleMapsAPIImpl();
    }

    @Override
    public void saveNewUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        createRoleIfNotFound("ROLE_USER");
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    @Override
    public void saveNewOrder(Order order, User user) throws InputDataWrongException {
        order.setId(null);
        order.setOrderStatus(OrderStatus.NEW);
        order.setTimeCreate(new Date());
        order.setIdPassenger(user.getId());
        order.setDistance(calculateDistance(order.getFrom(), order.getTo()));
        order.setPrice(calculatePrice(order.getDistance()));
        orderRepository.save(order);

        user.setLastOrderId(order.getId());
        int quantityOrders = user.getQuantityOrders();
        user.setQuantityOrders(++quantityOrders);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public User getByUserphone(String userphone) {
        return userRepository.findByUserphone(userphone);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id);
    }

    /*@Override
    public User updateUser(Long id, User newUser) {
        User oldUser = userRepository.findById(id);

        oldUser.setId(newUser.getId());
        oldUser.setUserphone(newUser.getUserphone());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setPasswordConfirm(newUser.getPasswordConfirm());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setHomeAddress(newUser.getHomeAddress());
        oldUser.setCar(newUser.getCar());
        //oldUser.setCurrentAddress(newUser.getCurrentAddress());
        oldUser.setRoles(newUser.getRoles());
        oldUser.setLastOrderId(newUser.getLastOrderId());
        oldUser.setQuantityOrders(newUser.getQuantityOrders());

        return userRepository.save(oldUser);
    }*/

    @Override
    public Order getLastOrder(String userphone) {
        User user = userRepository.findByUserphone(userphone);

        if (user.getLastOrderId() != null)
            return orderRepository.findById(user.getLastOrderId());

        return null;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /*@Override
    public Order updateOrder(Long id, Order newOrder) {
        Order oldOrder = orderRepository.findById(id);

        oldOrder.setId(newOrder.getId());
        oldOrder.setTimeCreate(newOrder.getTimeCreate());
        oldOrder.setTimeTaken(newOrder.getTimeTaken());
        oldOrder.setTimeCancelled(newOrder.getTimeCancelled());
        oldOrder.setTimeClosed(newOrder.getTimeClosed());
        oldOrder.setOrderStatus(newOrder.getOrderStatus());
        oldOrder.setFrom(newOrder.getFrom());
        oldOrder.setTo(newOrder.getTo());
        oldOrder.setIdPassenger(newOrder.getIdPassenger());
        oldOrder.setIdDriver(newOrder.getIdDriver());
        oldOrder.setDistance(newOrder.getDistance());
        oldOrder.setPrice(newOrder.getPrice());
        oldOrder.setMessage(newOrder.getMessage());

        return orderRepository.save(oldOrder);
    }*/

    @Override
    public Order calculateOrder(Order baseOrder) throws InputDataWrongException {

        int distance = calculateDistance(baseOrder.getFrom(), baseOrder.getTo());
        baseOrder.setDistance(distance);
        baseOrder.setPrice(calculatePrice(distance));

        return baseOrder;
    }

    @Override
    public List<Order> getListOrdersOfUser(String userphone) {
        User user = userRepository.findByUserphone(userphone);

        if (user.getHomeAddress() != null)
            return orderRepository.findByIdPassenger(user.getId());

        if (user.getCar() != null)
            return orderRepository.findByIdDriver(user.getId());

        return null;
    }

    @Override
    public List<Order> getListOrdersByOrderStatus(OrderStatus orderStatus) {

        return orderRepository.findByOrderStatus(orderStatus);
    }

    @Override
    public Map<Long, Double> createMapOrdersIdDistancesKmToUser(List<Order> orders)
            throws InputDataWrongException, IOException {

        Location locationDriver = googleMapsAPI.getCurrentLocation();

        Map <Long, Double> mapOfDistances = new HashMap<>();
        for (Order order : orders) {
            Location locationPassenger = googleMapsAPI.findLocation(order.getFrom().separateByCommas());

            int distanceInMeters = (int) googleMapsAPI.getDistance(locationDriver, locationPassenger);
            double distanceInKm = (double) (Math.round(distanceInMeters / 100))/10;

            mapOfDistances.put(order.getId(), distanceInKm);
        }

        return mapOfDistances;
    }

    @Override
    public Map<Long, User> getMapUsersFromUserOrders(List<Order> orders, boolean passenger) {

        Map<Long, User> users = new HashMap<>();
        for (Order order : orders) {
            User user = passenger ? getById(order.getIdDriver()) : getById(order.getIdPassenger());
            users.put(order.getId(), user);
        }

        return users;
    }

    @Override
    public User updateCurrentAddressOfUser(User user) throws IOException, InputDataWrongException {

        Location currentLocation = googleMapsAPI.getCurrentLocation();
        Address currentAddress = getAddressFromLocation(currentLocation);

        user.setCurrentAddress(currentAddress);

        return user;
    }

    @Override
    public Order takeOrderByDriver(Long orderId, User user)
            throws OrderNotFoundException, WrongStatusOrderException {

        Order order = orderRepository.findById(orderId);
        if (order == null)
            throw new OrderNotFoundException("Order not found in data base");

        else if (user.isActive())
            throw new OrderNotFoundException("You have active orders now");

        else if (order.getOrderStatus() != OrderStatus.NEW)
            throw new WrongStatusOrderException("This order has wrong status (not NEW)");

        order.setIdDriver(user.getId());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setTimeTaken(new Date());
        orderRepository.save(order);

        user.setLastOrderId(order.getId());
        int quantityOrders = user.getQuantityOrders();
        user.setQuantityOrders(++quantityOrders);
        user.setActive(true);
        userRepository.save(user);

        return order;
    }

    @Override
    public Order cancelOrder(Long orderId, User user)
            throws OrderNotFoundException, WrongStatusOrderException {

        Order order = orderRepository.findById(orderId);
        if (order == null)
            throw new OrderNotFoundException("Order not found in data base");

        else if (user.getCar() != null && order.getOrderStatus() != OrderStatus.IN_PROGRESS)
            throw new WrongStatusOrderException(
                    "You can cancelled orders only with status IN_PROGRESS");

        else if (user.getCar() != null && !order.getId().equals(user.getLastOrderId()))
            throw new WrongStatusOrderException(
                    "You can cancelled only your orders");

        else if (user.getHomeAddress() != null &&
                    (order.getOrderStatus() != OrderStatus.NEW &&
                        order.getOrderStatus() != OrderStatus.IN_PROGRESS))
            throw new WrongStatusOrderException(
                    "You can cancelled orders only with status NEW or IN_PROGRESS");

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setTimeCancelled(new Date());
        orderRepository.save(order);

        deactivateUsersInOrder(order);

        return order;
    }

    @Override
    public Order closeOrder(Long orderId, User user)
            throws OrderNotFoundException, WrongStatusOrderException {

        Order order = orderRepository.findById(orderId);
        if (order == null)
            throw new OrderNotFoundException("Order not found in data base");

        else if (!order.getId().equals(user.getLastOrderId()))
            throw new WrongStatusOrderException(
                    "You can closed only your orders");

        else if (order.getOrderStatus() != OrderStatus.IN_PROGRESS)
            throw new WrongStatusOrderException(
                    "You can closed orders only with status IN_PROGRESS");

        order.setOrderStatus(OrderStatus.CLOSED);
        order.setTimeClosed(new Date());
        orderRepository.save(order);

        deactivateUsersInOrder(order);

        return order;
    }


    /*@Override
    public User deleteUser(String accessToken) throws WrongStatusOrderException {

        User user = accessKeys.get(accessToken);

        //check open orders of user (NEW or IN_PROGRESS)
        List<Order> ordersNew = orderDao.getOrdersByStatus(OrderStatus.NEW);
        List<Order> ordersInProgress = orderDao.getOrdersByStatus(OrderStatus.IN_PROGRESS);

        for (Order order : ordersNew) {
            if (user.getId().equals(order.getIdPassenger())) {
                throw new WrongStatusOrderException
                        ("Can't delete. You have orders with status NEW");
            }
        }

        for (Order order : ordersInProgress) {
            if (user.getId().equals(order.getIdPassenger()) ||
                                    user.getId().equals(order.getIdDriver())) {
                throw new WrongStatusOrderException
                        ("Can't delete. You have orders with status IN_PROGRESS");
            }
        }

        User deleteUser = userDao.deleteUser(user.getId());
        accessKeys.remove(accessToken);

        return deleteUser;
    }*/

    @Transactional
    private void deactivateUsersInOrder(Order order) {

        User passenger = getById(order.getIdPassenger());
        passenger.setActive(false);
        userRepository.save(passenger);

        User driver = getById(order.getIdDriver());
        if (driver != null) {
            driver.setActive(false);
            userRepository.save(driver);
        }
    }

    @Transactional
    private Role createRoleIfNotFound(String name) {

        for (Role role : roleRepository.findAll()) {
            if (role.getName().equals(name))
                return role;
        }
        return roleRepository.save(new Role(name));
    }

    @Transactional
    private int calculateDistance(Address from, Address to) throws InputDataWrongException {

        Location locationFrom = googleMapsAPI.findLocation(from.separateByCommas());
        Location locationTo = googleMapsAPI.findLocation(to.separateByCommas());

        return ((int) googleMapsAPI.getDistance(locationFrom, locationTo) / 1000);
    }

    @Transactional
    private int calculatePrice(int distance) {
        return (int) pricePerKilometer * distance + Constants.FEE_FOR_FILING_TAXI_UAH;
    }

    @Transactional
    private Address getAddressFromLocation(Location location) {

        Address address = new Address(location);

        String formattedAddress = location.getFormattedAddress();
        String[] addressArray = formattedAddress.split(",");

        if (addressArray.length >= 4) {
            address.setStreet(addressArray[0].trim());
            address.setHouseNum(addressArray[1].trim());
            address.setCity(addressArray[2].trim());
            address.setCountry(addressArray[3].trim());
        }

        return address;
    }

    @Transactional
    private Address addLocationToAddress(Address address) throws InputDataWrongException {

        Location location = googleMapsAPI.findLocation(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNum());

        address.setLocation(location);

        return address;
    }
}

package ua.artcode.taxi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.repository.OrderRepository;
import ua.artcode.taxi.repository.RoleRepository;
import ua.artcode.taxi.repository.UserRepository;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

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
        order.setOrderStatus(OrderStatus.NEW);
        order.setTimeCreate(new Date());
        order.setIdPassenger(user.getId());
        order.setDistance(calculateDistance(order.getFrom(), order.getTo()));
        order.setPrice(calculatePrice(order.getDistance()));
        orderRepository.save(order);

        user.setLastOrderId(order.getId());
        int quantityOrders = user.getQuantityOrders();
        user.setQuantityOrders(++quantityOrders);
        userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByUserphone(String userphone) {
        return userRepository.findByUserphone(userphone);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User newUser) {
        User oldUser = userRepository.findById(id);

        oldUser.setId(newUser.getId());
        oldUser.setUserphone(newUser.getUserphone());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setPasswordConfirm(newUser.getPasswordConfirm());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setHomeAddress(newUser.getHomeAddress());
        oldUser.setCar(newUser.getCar());
        oldUser.setCurrentAddress(newUser.getCurrentAddress());
        oldUser.setRoles(newUser.getRoles());
        oldUser.setLastOrderId(newUser.getLastOrderId());
        oldUser.setQuantityOrders(newUser.getQuantityOrders());

        return userRepository.save(oldUser);
    }

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

    @Override
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
        oldOrder.setDistanceToDriver(newOrder.getDistanceToDriver());

        return orderRepository.save(oldOrder);
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
    public Map<Long, Double> createMapOrdersWithDistancesToDriver(
            List<Order> orders, Address addressDriver)
            throws InputDataWrongException {

        Location locationDriver = getLocationFromAddress(addressDriver);

        Map <Long, Double> mapOfDistances = new HashMap<>();
        for (Order order : orders) {
            Location locationPassenger = getLocationFromAddress(order.getFrom());

            int distanceInMeters = new Distance(locationDriver, locationPassenger).calculateDistance();
            double distanceInKm = (double) (Math.round(distanceInMeters / 100))/10;

            mapOfDistances.put(order.getId(), distanceInKm);
        }

        return mapOfDistances;
    }

    @Override
    public Order takeOrderByDriver(Long orderId, User user) {

        Order order = orderRepository.findById(orderId);

        order.setIdDriver(user.getId());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setTimeTaken(new Date());

        /*if (checkOrdersOfUser(user, OrderStatus.IN_PROGRESS))
            throw new DriverOrderActionException("Driver has orders IN_PROGRESS already");

        if (inProgress == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (inProgress.getOrderStatus() != OrderStatus.NEW) {
            throw new WrongStatusOrderException("This order has wrong status (not NEW)");
        }*/

        user.setLastOrderId(order.getId());
        int quantityOrders = user.getQuantityOrders();
        user.setQuantityOrders(++quantityOrders);
        updateUser(user.getId(), user);

        return updateOrder(orderId, order);
    }


    /*@Override
    public Order cancelOrder(Long orderId) throws OrderNotFoundException, WrongStatusOrderException {

        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (foundOrder.getOrderStatus().equals(OrderStatus.CLOSED) ||
                    foundOrder.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new WrongStatusOrderException("This order has been CLOSED or CANCELLED already");
        }
        foundOrder.setOrderStatus(OrderStatus.CANCELLED);
        foundOrder.setTimeCancelled(new Date());

        return orderDao.update(foundOrder);
    }*/

    /*@Override
    public Order closeOrder(String accessToken, Long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (user.getIdentifier() == UserIdentifier.D && foundOrder.getIdDriver() <= 0) {
            throw new DriverOrderActionException("This order is not your order");

        } else if (foundOrder.getOrderStatus() != OrderStatus.IN_PROGRESS) {
            throw new WrongStatusOrderException("This order has wrong status (not IN_PROGRESS)");
        }

        foundOrder.setOrderStatus(OrderStatus.CLOSED);
        foundOrder.setTimeClosed(new Date());

        return orderDao.update(foundOrder);
    }*/

    /*@Override
    public Order takeOrder(String accessToken, Long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order inProgress = orderDao.findById(orderId);

        if (checkOrdersOfUser(user, OrderStatus.IN_PROGRESS))
            throw new DriverOrderActionException("Driver has orders IN_PROGRESS already");

        if (inProgress == null) {
            throw new OrderNotFoundException("Order not found in data base");

        } else if (inProgress.getOrderStatus() != OrderStatus.NEW) {
            throw new WrongStatusOrderException("This order has wrong status (not NEW)");
        }

        inProgress.setIdDriver(user.getId());
        inProgress.setOrderStatus(OrderStatus.IN_PROGRESS);
        inProgress.setTimeTaken(new Date());

        return orderDao.update(inProgress);
    }*/



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

    @Override
    public Address getUserLocation() {

        return new Address(Constants.USER_LOCATION_PATH);
    }

    @Transactional
    private Location getLocationFromAddress(Address address) throws InputDataWrongException {

        return googleMapsAPI.findLocation(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNum()
        );
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
        Location location = getLocationFromAddress(from);
        Location location1 = getLocationFromAddress(to);

        return ((int) googleMapsAPI.getDistance(location, location1) / 1000);
    }

    @Transactional
    private int calculatePrice(int distance) {
        return (int) pricePerKilometer * distance + Constants.FEE_FOR_FILING_TAXI_UAH;
    }


    private class Distance implements Comparable {

        private Location fromLocation;
        private Location toLocation;
        private GoogleMapsAPI googleMapsAPI;

        private int averageSpeedKmH;
        private int timeInMin;

        private Distance(Location fromLocation, Location toLocation) {
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            googleMapsAPI = new GoogleMapsAPIImpl();
            averageSpeedKmH = Constants.AVERAGE_SPEED_KM_H;
        }

        public Location getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(Location fromLocation) {
            this.fromLocation = fromLocation;
        }

        public Location getToLocation() {
            return toLocation;
        }

        public void setToLocation(Location toLocation) {
            this.toLocation = toLocation;
        }

        private int calculateDistance() throws InputDataWrongException {
            return (int) googleMapsAPI.getDistance(fromLocation, toLocation);
        }

        private int calculateDistanceInKm() throws InputDataWrongException {
            int distanceInMeters = (int) googleMapsAPI.getDistance(fromLocation, toLocation);
            return distanceInMeters/1000;
        }

        public void setSpeedKmH(int speedKmH) {
            this.averageSpeedKmH = speedKmH;
        }

        public int getTimeInMin() throws InputDataWrongException {
            return (this.calculateDistance() / 1000) / this.averageSpeedKmH;
        }

        @Override
        public int compareTo(Object o) {

            Distance tmp = (Distance)o;
            try {
                double distance1 = this.googleMapsAPI.getDistance(fromLocation, toLocation);
                double distance2 = tmp.googleMapsAPI.getDistance(fromLocation, toLocation);
                return distance1 - distance2 > 0 ? 1 : -1 ;

            } catch (InputDataWrongException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

}

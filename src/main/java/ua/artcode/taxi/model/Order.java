package ua.artcode.taxi.model;

import com.sun.org.apache.xml.internal.serialize.Serializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    private Long id;
    private Date timeCreate;
    private Date timeTaken;
    private Date timeCancelled;
    private Date timeClosed;
    private OrderStatus orderStatus;
    private Address from;
    private Address to;
    private Long idPassenger;
    private Long idDriver;
    private int distance;
    private int price;
    private String message;
    private int distanceToDriver;

    public Order() {
    }

    public Order(Address from, Address to, Long idPassenger, int distance, int price, String message) {
        this.from = from;
        this.to = to;
        this.idPassenger = idPassenger;
        this.distance = distance;
        this.price = price;
        this.message = message;
    }

    public Order(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimeCancelled() {
        return timeCancelled;
    }

    public void setTimeCancelled(Date timeCancelled) {
        this.timeCancelled = timeCancelled;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(Date timeClosed) {
        this.timeClosed = timeClosed;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_id")
    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_id")
    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public Long getIdPassenger() {
        return idPassenger;
    }

    public void setIdPassenger(Long idPassenger) {
        this.idPassenger = idPassenger;
    }

    public Long getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(Long idDriver) {
        this.idDriver = idDriver;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Transient
    public int getDistanceToDriver() {
        return distanceToDriver;
    }

    public void setDistanceToDriver(int distanceToDriver) {
        this.distanceToDriver = distanceToDriver;
    }

    public String toShortViewJS() {
        return "id: " + id +
                ",<br /> create: " + timeCreate +
                ",<br /> status: " + orderStatus +
                ",<br /> from: " + from.separateByCommas() +
                ",<br /> to: " + to.separateByCommas() +
                ",<br /> distance: " + distance + " km" +
                ",<br /> price: " + price + " uah";
    }

    public String toStringForViewShort() {
        return "id " + id +
                ", price " + price + "uah";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}


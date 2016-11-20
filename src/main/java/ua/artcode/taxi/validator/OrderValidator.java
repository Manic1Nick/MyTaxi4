package ua.artcode.taxi.validator;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;

@Component
public class OrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Order order = (Order) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "from.country", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "from.city", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "from.street", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "from.houseNum", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "to.country", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "to.city", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "to.street", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "to.houseNum", "NotEmpty");
    }
}

package ua.artcode.taxi.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userphone", "NotEmpty");
        if (user.getUserphone().length() != 13 ||
                !user.getUserphone().startsWith("+380")) {
            errors.rejectValue("userphone", "Type.userForm.userphone");
        }
        if (userService.findByUserphone(user.getUserphone()) != null) {
            errors.rejectValue("userphone", "Duplicate.userForm.userphone");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getUsername().length() <= 0) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        /*if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }*/

        //validation home address for passenger
        if (user.getHomeAddress() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.country", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("homeAddress.country", "Size.userForm.country");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.city", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("homeAddress.city", "Size.userForm.city");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.street", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("homeAddress.street", "Size.userForm.street");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.houseNum", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("homeAddress.houseNum", "Size.userForm.housenumber");
            }

        } else if (user.getCar() != null) {
            //validation car for driver
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "car.type", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("car.type", "Size.userForm.cartype");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "car.model", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("car.model", "Size.userForm.carmodel");
            }

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "car.number", "NotEmpty");
            if (user.getPassword().length() <= 0) {
                errors.rejectValue("car.number", "Size.userForm.carnumber");
            }
        }
    }
}

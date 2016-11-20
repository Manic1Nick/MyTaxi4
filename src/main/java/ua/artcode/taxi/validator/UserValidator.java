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

        if (user.getHomeAddress() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.country", "NotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.city", "NotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.street", "NotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homeAddress.houseNum", "NotEmpty");
        }

        if (user.getCar() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "car.type", "NotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "car.model", "NotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "car.number", "NotEmpty");
        }

    }
}

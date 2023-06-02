package mx.bl;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class TaskValidator {
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Task.class);
    }

    public void validate(Object o, Errors errors) {
        Task t = (Task) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"date","date" ," required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","name"," required");
    }
}

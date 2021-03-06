package net.sf.jcommon.geo;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IbanValidator.class)
@Documented
public @interface Iban {
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
    
	String message() default "iban.invalid";
	boolean ignoreWhitespace() default true;
}
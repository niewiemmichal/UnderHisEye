package pl.niewiemmichal.underhiseye.commons.annotations;

import javax.annotation.security.RolesAllowed;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RolesAllowed({"REGISTRANT"})
public @interface IsRegistrant {}

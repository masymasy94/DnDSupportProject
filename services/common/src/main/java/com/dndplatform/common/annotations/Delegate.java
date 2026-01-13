package com.dndplatform.common.annotations;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CDI Qualifier annotation to mark delegate beans and injection points.
 * <p>
 * Can be used on:
 * <ul>
 *   <li>Classes - to mark a bean as a delegate implementation</li>
 *   <li>Fields - to inject a delegate bean</li>
 *   <li>Constructor/Method parameters - to inject a delegate bean</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>
 * // Mark the delegate implementation
 * {@literal @}Delegate
 * {@literal @}ApplicationScoped
 * public class LoginDelegate implements LoginResource {
 *     // implementation
 * }
 *
 * // Inject into a field
 * public class LoginResourceImpl {
 *     {@literal @}Inject
 *     {@literal @}Delegate
 *     private LoginResource delegate;
 * }
 *
 * // Inject via constructor
 * public class LoginResourceImpl {
 *     private final LoginResource delegate;
 *
 *     {@literal @}Inject
 *     public LoginResourceImpl({@literal @}Delegate LoginResource delegate) {
 *         this.delegate = delegate;
 *     }
 * }
 * </pre>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface Delegate {

    /**
     * Supports inline instantiation of the {@link Delegate} qualifier.
     * Useful for programmatic bean lookup.
     */
    final class Literal extends AnnotationLiteral<Delegate> implements Delegate {
        public static final Literal INSTANCE = new Literal();

        private static final long serialVersionUID = 1L;
    }
}
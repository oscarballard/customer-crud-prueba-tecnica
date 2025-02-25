package org.acme.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        
        for (ConstraintViolation<?> violation : violations) {
            String message = violation.getMessage();
            errors.put("error", message);
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errors)
                .build();
    }
}

package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;

import static ru.javawebinar.topjava.util.exception.ErrorType.DATA_NOT_FOUND;

//@ControllerAdvice(annotations = Controller.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandlerUI {

    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);



    @ExceptionHandler(DataIntegrityViolationException.class)
    public String conflict(HttpServletRequest req, DataIntegrityViolationException e, Model model) {
        if (e.getCause().getCause().toString().endsWith("USERS_UNIQUE_EMAIL_IDX"))
            e = new DataIntegrityViolationException("User with this email already exists");
        logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
        model.addAttribute("register", true);
        model.addAttribute("uniqEmail", e.getMessage());
        return "profile";
    }

    private static void logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
    }

}

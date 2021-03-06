package com.insframe.server.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;

/**
 * General error handler for the application.
 */
@ControllerAdvice
class ExceptionHandler {
	
	@Autowired
    private MessageSource messageSource;
	
	/**
	 * Handle exceptions thrown by handlers.
	 */
	@org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)	
	public ModelAndView exception(Exception exception, WebRequest request) {
		ModelAndView modelAndView = new ModelAndView("error/general");
		modelAndView.addObject("errorMessage", Throwables.getRootCause(exception));
		return modelAndView;
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(InspectionObjectAccessException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
	public JSONErrorMessage inspectionObjectNotFound(HttpServletRequest req, InspectionObjectAccessException ex) {
		String errorMessage = messageSource.getMessage(ex.getMessageId(), ex.getArgs(), LocaleContextHolder.getLocale());
        String errorURL = req.getRequestURL().toString();
         
        return new JSONErrorMessage(errorMessage, errorURL);
    }
}
package com.yicj.study.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class RequestValidateExceptionHandle {

    /**
     * 校验错误拦截处理
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Message validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        String message = "";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.stream().peek(p ->{
                FieldError fieldError = (FieldError) p;
                log.error("Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() +
                        "},errorMessage{" + fieldError.getDefaultMessage() + "}");
            }) ;
            if (errors.size() > 0) {
                FieldError fieldError = (FieldError) errors.get(0);
                message = fieldError.getDefaultMessage();
            }
        }
        return Message.fail("".equals(message) ? "请填写正确信息" : message);
    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public Message parameterTypeException(HttpMessageConversionException exception) {
        log.error(exception.getCause().getLocalizedMessage());
        return Message.fail("类型转换错误");
    }
}
package com.exam.myblogs.exception;


import com.exam.myblogs.dto.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 权限校验
     * MethodArgumentNotValidException捕获实体校验异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED) //因为前后端分离 返回一个状态 一般是401 没有权限
    @ExceptionHandler(value = ShiroException.class)//捕获运行时异常ShiroException是大部分异常的父类
    public Result handler(ShiroException e){
        log.error("运行时异常：------------------{}",e);
        return Result.error(401,e.getMessage(),null);
    }

    /**
     * 运行时异常
     * MethodArgumentNotValidException捕获实体校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)//因为前后端分离 返回一个状态
    @ExceptionHandler(value = RuntimeException.class)//捕获运行时异常
    public Result handler(RuntimeException e){
        log.error("运行时异常：------------------{}",e);
        return Result.error(e.getMessage());
    }


    /**
     * 实体校验异常
     * MethodArgumentNotValidException捕获实体校验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)//捕获运行时异常
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体捕获异常  ：-----------------{}",e);
        BindingResult bindingException = e.getBindingResult();
        //多个异常顺序抛出异常
        ObjectError objectError = bindingException.getAllErrors().stream().findFirst().get();
        return Result.error(objectError.getDefaultMessage());
    }

    /**
     * 断言异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)//处理 RuntimeException 异常时，向客户端返回 400 Bad Request 状态码，以此告知客户端请求存在问题。
    @ExceptionHandler(value = IllegalArgumentException.class)//如果该表达式的值为 false，就会抛出 AssertionError 异常。
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常:------------------>{}",e);
        return Result.error(e.getMessage());
    }
}

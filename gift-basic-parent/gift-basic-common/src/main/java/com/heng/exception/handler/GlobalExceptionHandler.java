package com.heng.exception.handler;/**
 * @author shkstart
 * @create 2023-04-25 19:10
 */

import com.heng.exception.BusinessException;
import com.heng.exception.ResponseCode;
import com.heng.util.AjaxResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @Auther:Jarvis
 * @Date:2023年04月2023/4/25日19:10
 * @Description: 全局异常处理
 */
@RestControllerAdvice //执行controller方法前后做事
public class GlobalExceptionHandler {

    //对BusinessException进行处理,相当于catch(BusinessException e)

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public AjaxResult businessExceptionHandler(BusinessException e) {
        e.printStackTrace();
        return AjaxResult.me().setSuccess(false)
                .setCode(e.getCode()).setMessage(e.getMessage());
    }

    /**
     * 参数校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        //获取bindingResult
        BindingResult bindingResult = e.getBindingResult();
        //获取所有的字段错误
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        //拼接每个字段错误的提示信息,中间用逗号分隔
        StringBuilder stringBuilder = new StringBuilder();
        fieldErrors.forEach(fieldError -> {
            stringBuilder.append(fieldError.getDefaultMessage() + ",");
        });
        //去除最后一个逗号
        String message = stringBuilder.substring(0, stringBuilder.length() - 1);
        return AjaxResult.me().setSuccess(false).setCode(ResponseCode.RESPONSE_CODE_400.getCode()).setMessage(message);
    }

    /**
     * 系统异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult exceptionHandler(Exception e) {
        e.printStackTrace();
        return AjaxResult.me().setSuccess(false).setCode(ResponseCode.RESPONSE_CODE_500.getCode()).setMessage(ResponseCode.RESPONSE_CODE_500.getMessage());
    }

}

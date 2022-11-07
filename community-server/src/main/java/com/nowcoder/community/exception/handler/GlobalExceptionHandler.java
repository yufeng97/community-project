package com.nowcoder.community.exception.handler;

import com.nowcoder.community.constant.HttpStatus;
import com.nowcoder.community.exception.BadRequestException;
import com.nowcoder.community.exception.NotFoundException;
import com.nowcoder.community.exception.UnAuthorizationException;
import com.nowcoder.community.exception.UserException;
import com.nowcoder.community.util.CommonResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 未授权异常
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(UnAuthorizationException.class)
    public CommonResult<Boolean> handleUnAuthorizationException(UnAuthorizationException e, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error("请求地址'{}',token过期'{}'", requestURI, e.getMessage());
        return CommonResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "token过期，请重新登录");
    }

    /**
     * 非法请求异常
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    public CommonResult<String> handleBadRequestException(BadRequestException e, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("请求地址'{}',请求非法'{}'", requestURI, e.getMessage());
        return CommonResult.fail(HttpServletResponse.SC_BAD_REQUEST, "请求非法");
    }

    @ExceptionHandler(NotFoundException.class)
    public CommonResult<String> handleNotFoundException(NotFoundException e, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        log.error("请求地址'{}',资源不存在'{}'", requestURI, e.getMessage());
        return CommonResult.fail(HttpServletResponse.SC_NOT_FOUND, "资源不存在");
    }

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult<String> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return CommonResult.fail(HttpServletResponse.SC_FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                    HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return CommonResult.fail(e.getMessage());
    }

    /**
     * 方法参数无效异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return CommonResult.fail(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(UserException.class)
    public CommonResult<String> handleServiceException(UserException e) {
        log.error(e.getMessage(), e);
        String message = e.getMessage();
        return CommonResult.fail(HttpServletResponse.SC_BAD_REQUEST, message);
    }

//    /**
//     * 业务异常
//     */
//    @ExceptionHandler(ServiceException.class)
//    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
//    {
//        log.error(e.getMessage(), e);
//        Integer code = e.getCode();
//        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());

//    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public CommonResult<String> handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return CommonResult.fail(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return CommonResult.fail(e.getMessage());
    }
//    /**
//     * 自定义验证异常
//     */
//    @ExceptionHandler(BindException.class)
//    public AjaxResult handleBindException(BindException e)
//    {
//        log.error(e.getMessage(), e);
//        String message = e.getAllErrors().get(0).getDefaultMessage();
//        return AjaxResult.error(message);

//    }

//    /**
//     * 演示模式异常
//     */
//    @ExceptionHandler(DemoModeException.class)
//    public AjaxResult handleDemoModeException(DemoModeException e)
//    {
//        return AjaxResult.error("演示模式，不允许操作");
//    }


}


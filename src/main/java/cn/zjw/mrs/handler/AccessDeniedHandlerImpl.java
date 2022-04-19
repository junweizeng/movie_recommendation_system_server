package cn.zjw.mrs.handler;

import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zjw
 * @Classname AccessDeniedHandlerImpl
 * @Date 2022/4/11 21:31
 * @Description 自定义异常处理，当授权失败时会处理异常
 */

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result result = new Result(HttpStatus.FORBIDDEN.value(), "您的权限不足");
        String json = JSON.toJSONString(result);
        // 处理异常
        WebUtils.renderString(response, json);
    }
}

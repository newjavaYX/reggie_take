package com.example.reggie.filters;

import com.alibaba.fastjson.JSON;
import com.example.reggie.common.BeasContext;
import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 =(HttpServletRequest) request;
        HttpServletResponse response1 =(HttpServletResponse) response;
//        定义不需要拦截的请求
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/backend/plugins/axios/axios.min.map",
        };
        //获取请求路径
        String requestURI = request1.getRequestURI();
        //调用方法比较
        boolean check = check(urls, requestURI);
        //如不需要处理直接放行
        if (check) {
            log.info("拦截到请求: " + requestURI);
            chain.doFilter(request1, response1);
            return;
        //否则判断是否登入
        }else{
            log.info("拦截到请求: " + requestURI);
            log.info("判断是否登入");
            Object employee = request1.getSession().getAttribute("employee");
            Object user = request1.getSession().getAttribute("user");
            if (employee != null) {
                log.info("用户以登入 id为：{}",employee);
                //将用户id保存到threadLocal中
                BeasContext.setCurrentId((Long) employee);
                chain.doFilter(request1, response1);
                return;
            }
            if (user != null) {
                log.info("用户以登入 id为：{}",user);
                //将用户id保存到threadLocal中
                BeasContext.setCurrentId((Long) user);
                chain.doFilter(request1, response1);
                return;
            }
        }
        log.info("拦截请求并发送错误消息会前端");
        response1.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
        }

    /**
     * 比较请求路径是否为需要拦截的路径
     * @param urls
     * @param requestURL
     * @return
     */
    public static boolean check(String[] urls,String requestURL){
        for (String url : urls) {
            boolean flag = PATH_MATCHER.match(url, requestURL);
            if(flag) {
                return true;
            }
        }
        return false;
    }
}

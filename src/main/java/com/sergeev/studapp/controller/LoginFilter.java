package com.sergeev.studapp.controller;

import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.ApplicationException;
import com.sergeev.studapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
    static final String LOGIN_COOKIE = "MY_SESSION_COOKIE";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        final String path = req.getRequestURI().substring(req.getContextPath().length());
        User user = (User) session.getAttribute("user");
        Cookie loginCookie = null;

        if (user != null && (path.contains("new") || path.contains("change") || path.contains("admin"))) {
            if (user.getRole() == User.Role.ADMIN) {
                chain.doFilter(request, response);
                return;
            } else if (user.getRole() == User.Role.TEACHER && path.contains("mark")) {
                chain.doFilter(request, response);
                return;
            } else {
                resp.sendRedirect("/");
                return;
            }
        }

        if (req.getCookies() == null) {
            resp.sendRedirect("/login");
            return;
        }

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(LOGIN_COOKIE)) {
                loginCookie = cookie;
                break;
            }
        }

        if (loginCookie != null) {
            if (user == null) {
                try {
                    user = UserService.getByToken(loginCookie.getValue());
                } catch (ApplicationException e) {
                    LOG.info("Broken cookie.", e);
                }
                req.getSession().setAttribute("user", user);
            }
        }

        if (path.startsWith("/login")) {
            if (user == null) {
                chain.doFilter(request, response);
                return;
            }
            resp.sendRedirect("/");
            return;
        }

        if (path.contains(".css") || path.contains(".js")) {
            chain.doFilter(request, response);
            return;
        }

        if (user == null) {
            resp.sendRedirect("/login");
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
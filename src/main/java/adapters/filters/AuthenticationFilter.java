package adapters.filters;

import core.exceptions.adapters.UnauthenticatedException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/habits", "/users", "/blocks", "/users/list", "/habits/list"})
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest sessionRequest = (HttpServletRequest) request;
        HttpSession session = sessionRequest.getSession(false);

        if (session == null) {
            try {
                throw new UnauthenticatedException();
            } catch (UnauthenticatedException e) {
                throw new ServletException(e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
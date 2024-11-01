package core.aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UserAuditAspect {
    @Pointcut("within(adapters.controller..*)")
    public void userActions() {

    }

    @Around("userActions() && args(request, response,..)")
    public void logUserAction(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getMethod();
        String userId = request.getSession().getAttribute("id").toString();

        System.out.println("User with ID: " + userId + " performed action: " + action);
    }
}
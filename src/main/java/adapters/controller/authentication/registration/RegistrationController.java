package adapters.controller.authentication.registration;

import adapters.controller.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.iternal.DataDto;
import common.dto.request.authentication.RegistrationDto;
import common.dto.response.ResponseDto;
import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;
import core.exceptions.usecase.InvalidUserInformationException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usecase.authentication.registration.Registration;
import usecase.authentication.registration.impl.JdbcRegistration;
import usecase.session.SessionCreator;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationController extends HttpServlet {
    private Registration registration;
    private UserDao userDao;
    private ObjectMapper objectMapper;
    private Validator<RegistrationDto> validator;

    @Override
    public void init() {
        this.registration = new JdbcRegistration(new JdbcUserDao(new DatabaseUtils()));
        this.userDao = new JdbcUserDao(new DatabaseUtils());
        this.objectMapper = new ObjectMapper();
        this.validator = new RegistrationRequestValidator();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            RegistrationDto registrationDto = objectMapper.readValue(request.getReader(), RegistrationDto.class);
            validator.validate(registrationDto);
            if (registration.isSuccess(registrationDto)) {
                new SessionCreator().createSession(request, userDao, registrationDto.getEmail());
            }

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(201);
            out.print(objectMapper.writeValueAsString(new ResponseDto<>(
                    DataDto.builder()
                            .message("Пользователь зарегистрирован")
                            .build()
            )));
            out.flush();
        } catch (InvalidUserInformationException e) {
            throw new RuntimeException(e);
        } catch (BadRequestException | ForbiddenException e) {
            throw new ServletException(e);
        }
    }
}
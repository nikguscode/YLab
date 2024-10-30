package adapters.controller.exception;

import adapters.controller.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.dto.response.ErrorDto;
import core.LocalDateTimeFormatter;
import core.exceptions.adapters.ForbiddenException;
import core.exceptions.adapters.UnauthenticatedException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ClientExceptionHandler")
public class ClientExceptionHandler extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleExceptions(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleExceptions(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleExceptions(request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleExceptions(request, response);
    }

    private void handleExceptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        String jsonResponse;
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String exceptionDescription = throwable != null ? throwable.getMessage() : "Unknown exception";

        if (throwable instanceof UnauthenticatedException) {
            jsonResponse = (exceptionDescription != null)
                    ? generateJsonResponseForUnauthenticatedException(exceptionDescription)
                    : generateJsonResponseForUnauthenticatedException();
            response.setStatus(401);
        } else if (throwable instanceof ForbiddenException) {
            jsonResponse = (exceptionDescription != null)
                    ? generateJsonResponseForForbiddenException(exceptionDescription)
                    : generateJsonResponseForForbiddenException();
            response.setStatus(403);
        } else {
            jsonResponse = generateJsonResponseForUnexpectedError();
            response.setStatus(500);
        }

        out.print(jsonResponse);
        out.flush();
    }

    private String generateJsonResponseForUnauthenticatedException() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(401)
                        .result(Constants.UNAUTHENTICATED)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(Constants.USER_IS_NOT_AUTHENTICATED)
                        .build()
        );
    }

    private String generateJsonResponseForUnauthenticatedException(String exceptionDescription) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(401)
                        .result(Constants.UNAUTHENTICATED)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(exceptionDescription)
                        .build()
        );
    }

    private String generateJsonResponseForForbiddenException() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(403)
                        .result(Constants.FORBIDDEN)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(Constants.USER_NOT_AUTHORIZED)
                        .build()
        );
    }

    private String generateJsonResponseForForbiddenException(String exceptionDescription) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(403)
                        .result(Constants.FORBIDDEN)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(exceptionDescription)
                        .build()
        );
    }

    private String generateJsonResponseForBadRequestException() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(400)
                        .result(Constants.BAD_REQUEST)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(Constants.INCORRECT_REQUEST_SYNTAX)
                        .build()
        );
    }

    private String generateJsonResponseForBadRequestException(String exceptionDescription) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(400)
                        .result(Constants.BAD_REQUEST)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(exceptionDescription)
                        .build()
        );
    }

    private String generateJsonResponseForUnexpectedError() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                ErrorDto.builder()
                        .code(500)
                        .result(Constants.API_ERROR)
                        .dateAndTime(LocalDateTimeFormatter.formatWithSeconds())
                        .description(Constants.API_UNEXPECTED_ERROR)
                        .build()
        );
    }
}
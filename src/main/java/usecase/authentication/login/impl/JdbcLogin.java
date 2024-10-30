package usecase.authentication.login.impl;

import static usecase.authentication.Constants.*;

import common.dto.request.authentication.LoginDto;
import core.entity.User;
import common.enumiration.Role;
import infrastructure.dao.user.UserDao;
import lombok.RequiredArgsConstructor;
import usecase.authentication.login.Login;

import java.time.LocalDateTime;

/**
 * Класс, реализующий локальный вход пользователя, без использования базы данных
 */
@RequiredArgsConstructor
public class JdbcLogin implements Login {
    private final UserDao userDao;

    /**
     * Проверяет учетные данные пользователя в базе данных
     *
     * @param loginDto DTO с данными для входа
     * @return <b>true</b> если данные верны, иначе <b>false</b>
     */
    @Override
    public boolean isSuccess(LoginDto loginDto) {
        User user = userDao.get(loginDto.getEmail());

        if (validate(user, loginDto)) {
            user.setAuthorized(true);
            user.setAuthorizationDate(LocalDateTime.now());
            userDao.edit(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Используется для проверки введённых данных на валидность. Проверяет следующее:
     * <ul>
     *     <li>Существование пользователя по указанной электронной почте</li>
     *     <li>Отсутствие блокировки учётной записи</li>
     *     <li>Указанный пароль совпадает с паролем при регистрации</li>
     * </ul>
     * @param user сущность пользователя
     * @param loginDto dto, переносящий пользовательский ввод
     * @return <b>true</b>: если все проверки пройдены, иначе <b>false</b>
     */
    private boolean validate(User user, LoginDto loginDto) {
        if (user == null) {
            System.out.println(LOGIN_USER_NOT_FOUND);
            return false;
        }

        if (user.getRole() == Role.BLOCKED) {
            System.out.println(LOGIN_USER_BLOCKED);
            return false;
        }

        String correctPassword = user.getPassword();
        String currentPassword = loginDto.getPassword();

        if (!correctPassword.equals(currentPassword)) {
            System.out.println(LOGIN_INCORRECT_DATA);
            return false;
        }

        return true;
    }
}
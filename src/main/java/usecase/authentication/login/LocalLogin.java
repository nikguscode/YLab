package usecase.authentication.login;

import static usecase.authentication.Constants.*;

import infrastructure.dto.LoginDto;
import core.entity.User;
import core.enumiration.Role;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dao.user.UserDao;

import java.time.LocalDateTime;

/**
 * Класс, реализующий локальный вход пользователя, без использования базы данных
 */
public class LocalLogin implements Login {

    /**
     * Содержит реализацию входа в учётную запись через проверку данных из {@link infrastructure.UserDatabase#database
     * database}, где в качестве ключа используется почта пользователя
     * @param loginDto dto, хранящий и переносящий пользовательский ввод
     * @return <b>true</b>: вход выполнен успешено, иначе <b>false</b>
     */
    @Override
    public boolean isSuccess(LoginDto loginDto) throws InterruptedException {
        UserDao userDao = new LocalUserDao();
        User user = userDao.get(loginDto.getEmail());

        if (validate(user, loginDto)) {
            user.setAuthorized(true);
            user.setAuthorizationDate(LocalDateTime.now());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод, используемый для проверки введённых данных на валидность. Проверяет следующее:
     * <ul>
     *     <li>Существование пользователя по указанной электронной почте</li>
     *     <li>Отсутствие блокировки учётной записи</li>
     *     <li>Указанный пароль совпадает с паролем при регистрации</li>
     * </ul>
     * @param user сущность пользователя
     * @param loginDto dto, переносящий пользовательский ввод
     * @return <b>true</b>: если все проверки пройдены, иначе <b>false</b>
     */
    private boolean validate(User user, LoginDto loginDto) throws InterruptedException {
        if (user == null) {
            System.out.println(LOGIN_USER_NOT_FOUND);
            Thread.sleep(500);
            return false;
        }

        if (user.getRole() == Role.BLOCKED) {
            System.out.println(LOGIN_USER_BLOCKED);
            Thread.sleep(500);
            return false;
        }

        String correctPassword = user.getPassword();
        String currentPassword = loginDto.getPassword();

        if (!correctPassword.equals(currentPassword)) {
            System.out.println(LOGIN_INCORRECT_DATA);
            Thread.sleep(500);
            return false;
        }

        return true;
    }
}
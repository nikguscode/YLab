package usecase.authentication.registration;

import core.entity.User;
import core.enumiration.Role;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dao.user.UserDao;
import infrastructure.dto.RegistrationDto;

import java.time.LocalDateTime;
import java.util.HashMap;

import static usecase.authentication.Constants.REGISTRATION_EMAIL_ALREADY_EXISTS;

/**
 * Класс, реализующий локальную регистрацию пользователя, без использования базы данных
 */
public class LocalRegistration implements Registration {
    /**
     * Содержит реализацию создания учётной записи, где в качестве учётной записи выступает сущность {@link User}
     *
     * @param registrationDto dto, хранящий и переносящий пользовательский ввод
     * @return <b>true</b>: создание учётной записи выполнено успешно, иначе <b>false</b>
     * @throws InvalidUserInformationException возникает в том случае, если пользователь ввёл некорректные данные при регистрации,
     *                                         корректность обеспечивается за счёт:
     *                                         <ul>
     *                                             <li>{@link User#validateEmail(String) validateEmail()}</li>
     *                                             <li>{@link User#validateUsername(String) validateUsername()}</li>
     *                                             <li>{@link User#validatePassword(String) validatePassword()}</li>
     *                                         </ul>
     * @throws InterruptedException            стандартное исключение, вызываемое из-за задержки вывода
     */
    @Override
    public boolean isSuccess(RegistrationDto registrationDto) throws InvalidUserInformationException, InterruptedException {
        UserDao userDao = new LocalUserDao();

        if (validate(registrationDto, userDao)) {
            userDao.add(
                    User.builder()
                            .email(registrationDto.getEmail())
                            .username(registrationDto.getUsername())
                            .password(registrationDto.getPassword())
                            .role(Role.ADMINISTRATOR)
                            .isAuthorized(true)
                            .habits(new HashMap<>())
                            .registrationDate(LocalDateTime.now())
                            .authorizationDate(LocalDateTime.now())
                            .build()
            );

            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод, используемый для проверки введённых данных на валидность. Проверяет следующее:
     * <li>Свободна ли указанная почта</li>
     * @param registrationDto dto, хранящий и переносящий пользовательский ввод
     * @param userDao интерфейс для CRUD запросов к базе данных
     * @return <b>true</b>: если все проверки пройдены, иначе <b>false</b>
     * @throws InterruptedException стандартное исключение, вызываемое из-за задержки вывода
     */
    private boolean validate(RegistrationDto registrationDto, UserDao userDao) throws InterruptedException {
        String currentEmail = registrationDto.getEmail();
        if (userDao.get(currentEmail) != null) {
            System.out.println(REGISTRATION_EMAIL_ALREADY_EXISTS);
            Thread.sleep(500);
            return false;
        }

        return true;
    }
}
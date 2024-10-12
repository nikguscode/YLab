package usecase.authentication.registration;

import infrastructure.dto.RegistrationDto;
import core.exceptions.InvalidUserInformationException;

/**
 * Интерфейс для регистрации пользователем новой учётной записи
 */
public interface Registration {
    /**
     * Метод для регистрации новой учётной записи
     * @param registrationDto dto, хранящий и переносящий пользовательский ввод
     * @return <b>true</b>: успешная регистрация, иначе <b>false</b>
     */
    boolean register(RegistrationDto registrationDto) throws InvalidUserInformationException, InterruptedException;
}

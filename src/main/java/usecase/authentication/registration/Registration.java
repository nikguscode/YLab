package usecase.authentication.registration;

import common.dto.request.authentication.RegistrationDto;
import core.exceptions.usecase.InvalidUserInformationException;

/**
 * Интерфейс для регистрации пользователем новой учётной записи
 */
public interface Registration {
    /**
     * Метод для регистрации новой учётной записи
     * @param registrationDto dto, хранящий и переносящий пользовательский ввод
     * @return <b>true</b>: успешная регистрация, иначе <b>false</b>
     */
    boolean isSuccess(RegistrationDto registrationDto) throws InvalidUserInformationException;
}

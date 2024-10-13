package usecase.authentication.login;

import infrastructure.dto.LoginDto;

/**
 * Интерфейс для входа пользователя в учётную запись
 */
public interface Login {
    /**
     * Метод для входа пользователя в свою учётную запись
     * @param loginDto dto, хранящий и переносящий пользовательский ввод
     * @return <b>true</b>: вход выполнен успешно, иначе <b>false</b>
     */
    boolean isSuccess(LoginDto loginDto) throws InterruptedException;
}

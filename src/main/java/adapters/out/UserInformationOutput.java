package adapters.out;

import adapters.console.Constants;
import core.entity.User;

/**
 * Вспомогательный класс, испольузуемый для вывода информации о пользователе
 */
public class UserInformationOutput {
    public void output(User user) {
        System.out.println(Constants.USER_INFORMATION);
        System.out.println("# | Идентификатор: " + user.getId());
        System.out.println("# | Электронная почта: " + user.getEmail());
        System.out.println("# | Пароль: " + user.getPassword());
        System.out.println("# | Имя пользователя: " + user.getUsername());
        System.out.println("# | Права доступа: " + user.getRole().getValue());
        System.out.println("0. Вернуться назад");
    }
}
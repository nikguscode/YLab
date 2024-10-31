package adapters.controller.user.validatorimpl;

import adapters.controller.Validator;
import common.dto.request.user.UserEditDto;
import common.enumiration.Role;
import core.entity.User;
import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.user.impl.JdbcUserDao;
import infrastructure.dao.user.UserDao;

/**
 * Класс, проверяющий параметры запроса на валидность и формирует сущность для изменения
 */
public class UserEditValidator implements Validator<UserEditDto> {
    private final UserDao userDao;

    public UserEditValidator() {
        this.userDao = new JdbcUserDao(new DatabaseUtils());
    }

    /**
     * Проверяет параметры запроса пользователя на валидность
     *
     * @param userEditDto dto, содержащий данные запроса
     * @throws BadRequestException возникает в случае некорректных параметров запроса
     * @throws ForbiddenException  возникает в случае, если прав пользователя недостаточно для запроса
     */
    @Override
    public void validate(UserEditDto userEditDto) throws BadRequestException, ForbiddenException {
        if (userEditDto.getEmail() == null && userEditDto.getUsername() == null && userEditDto.getPassword() == null) {
            throw new BadRequestException();
        }

        if (userEditDto.getEmail() != null && userEditDto.getEmail().isEmpty() ||
                userEditDto.getUsername() != null && userEditDto.getUsername().isEmpty() ||
                userEditDto.getPassword() != null && userEditDto.getPassword().isEmpty()) {
            throw new BadRequestException();
        }

        if (userEditDto.getRequiredUserId() != null && userEditDto.getSessionUserId() != null) {
            User sessionUser = userDao.get(userEditDto.getSessionUserId());

            if (!sessionUser.getRole().equals(Role.ADMINISTRATOR)) {
                throw new ForbiddenException();
            }
        }
    }
}
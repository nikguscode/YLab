package adapters.controller.user.validatorimpl;

import adapters.controller.Validator;
import common.dto.request.user.UserDeleteDto;
import core.exceptions.adapters.BadRequestException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.user.impl.JdbcUserDao;
import infrastructure.dao.user.UserDao;

public class UserDeleteValidator implements Validator<UserDeleteDto> {
    private final UserDao userDao;

    public UserDeleteValidator() {
        this.userDao = new JdbcUserDao(new DatabaseUtils());
    }

    @Override
    public void validate(UserDeleteDto userDeleteDto) throws BadRequestException {
        if (userDeleteDto.getSessionUserId() == 0) {
            throw new BadRequestException();
        }
    }
}
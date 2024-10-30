package adapters.controller.administrator.userblock;

import adapters.controller.Validator;
import common.dto.request.administrator.UserBlockDto;
import core.exceptions.adapters.BadRequestException;

public class UserBlockValidator implements Validator<UserBlockDto> {
    @Override
    public void validate(UserBlockDto userBlockDto) throws BadRequestException {
        if (userBlockDto == null || userBlockDto.getUserId() == null) {
            throw new BadRequestException();
        }
    }
}
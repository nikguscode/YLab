package adapters.controller.authentication.login;

import adapters.controller.Validator;
import common.dto.request.authentication.LoginDto;
import core.exceptions.adapters.BadRequestException;

public class LoginRequestValidator implements Validator<LoginDto> {
    @Override
    public void validate(LoginDto loginDto) throws BadRequestException {
        if (loginDto.getEmail() == null || loginDto.getPassword() == null) {
            throw new BadRequestException();
        }
    }
}

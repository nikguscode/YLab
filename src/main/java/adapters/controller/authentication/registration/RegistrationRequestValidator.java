package adapters.controller.authentication.registration;

import adapters.controller.Validator;
import common.dto.request.authentication.RegistrationDto;
import core.exceptions.adapters.BadRequestException;

public class RegistrationRequestValidator implements Validator<RegistrationDto> {
    @Override
    public void validate(RegistrationDto registrationDto) throws BadRequestException {
        if (registrationDto.getEmail() == null || registrationDto.getUsername() == null
                || registrationDto.getPassword() == null) {
            throw new BadRequestException();
        }
    }
}
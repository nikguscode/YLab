package adapters.controller;

import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;

public interface Validator<T> {
    void validate(T dtoClass) throws BadRequestException, ForbiddenException;
}
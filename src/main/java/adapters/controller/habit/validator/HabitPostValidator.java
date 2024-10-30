package adapters.controller.habit.validator;

import adapters.controller.Validator;
import common.dto.request.habit.HabitPostDto;
import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;

public class HabitPostValidator implements Validator<HabitPostDto> {
    @Override
    public void validate(HabitPostDto dtoClass) throws BadRequestException, ForbiddenException {
        if (dtoClass == null || dtoClass.getTitle() == null || dtoClass.getDescription() == null ||
                dtoClass.getFrequency() == null || dtoClass.getMarkDateAndTime() == null) {
            throw new BadRequestException();
        }

        if (dtoClass.getTitle().isEmpty() || dtoClass.getDescription().isEmpty() || dtoClass.getMarkDateAndTime().isEmpty()) {
            throw new BadRequestException();
        }
    }
}
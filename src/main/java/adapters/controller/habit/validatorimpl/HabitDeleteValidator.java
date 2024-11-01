package adapters.controller.habit.validatorimpl;

import adapters.controller.Validator;
import common.dto.request.habit.HabitDeleteDto;
import common.enumiration.Role;
import core.entity.Habit;
import core.entity.User;
import core.exceptions.adapters.BadRequestException;
import core.exceptions.adapters.ForbiddenException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.impl.JdbcHabitDao;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import infrastructure.dao.habitmarkhistory.impl.JdbcHabitMarkHistoryDao;
import infrastructure.dao.user.impl.JdbcUserDao;
import infrastructure.dao.user.UserDao;

public class HabitDeleteValidator implements Validator<HabitDeleteDto> {
    private final UserDao userDao;
    private final HabitMarkHistoryDao habitMarkHistoryDao;
    private final HabitDao habitDao;

    public HabitDeleteValidator() {
        DatabaseUtils databaseUtils = new DatabaseUtils();
        this.userDao = new JdbcUserDao(databaseUtils);
        this.habitMarkHistoryDao = new JdbcHabitMarkHistoryDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, habitMarkHistoryDao);
    }

    @Override
    public void validate(HabitDeleteDto dtoClass) throws BadRequestException, ForbiddenException {
        if (dtoClass == null || dtoClass.getHabitId() == null) {
            throw new BadRequestException();
        }

        Habit habitThatMustBeDeleted = habitDao.get(dtoClass.getHabitId());
        User sessionUser = userDao.get(dtoClass.getSessionUserId());
        if (habitThatMustBeDeleted.getUserId() != sessionUser.getId() && !sessionUser.getRole().equals(Role.ADMINISTRATOR)) {
            throw new ForbiddenException();
        }
    }
}
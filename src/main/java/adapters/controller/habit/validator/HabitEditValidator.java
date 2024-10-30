package adapters.controller.habit.validator;

import adapters.controller.Validator;
import common.dto.request.habit.HabitEditDto;
import core.entity.User;
import core.exceptions.adapters.BadRequestException;
import infrastructure.DatabaseUtils;
import infrastructure.dao.habit.HabitDao;
import infrastructure.dao.habit.JdbcHabitDao;
import infrastructure.dao.habitmarkhistory.HabitMarkHistoryDao;
import infrastructure.dao.habitmarkhistory.JdbcHabitMarkHistoryDao;
import infrastructure.dao.user.JdbcUserDao;
import infrastructure.dao.user.UserDao;

public class HabitEditValidator implements Validator<HabitEditDto> {
    private final UserDao userDao;
    private final HabitMarkHistoryDao habitMarkHistoryDao;
    private final HabitDao habitDao;

    public HabitEditValidator() {
        DatabaseUtils databaseUtils = new DatabaseUtils();
        this.userDao = new JdbcUserDao(databaseUtils);
        this.habitMarkHistoryDao = new JdbcHabitMarkHistoryDao(databaseUtils);
        this.habitDao = new JdbcHabitDao(databaseUtils, habitMarkHistoryDao);
    }

    @Override
    public void validate(HabitEditDto dtoClass) throws BadRequestException {
        if (dtoClass.getHabitId() == null && dtoClass.getTitle() == null && dtoClass.getDescription() == null &&
                dtoClass.getIsCompleted() == null && dtoClass.getFrequency() == null) {
            throw new BadRequestException();
        }
//
//        User sessisonUserId = userDao.get(dtoClass.get)
//        if (dtoClass.getHabitId() != )
    }
}
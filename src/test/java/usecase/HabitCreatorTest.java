package usecase;


import core.entity.Habit;
import core.entity.User;
import core.enumiration.Frequency;
import core.exceptions.InvalidFrequencyConversionException;
import core.exceptions.InvalidHabitInformationException;
import core.exceptions.InvalidUserInformationException;
import infrastructure.dao.user.LocalUserDao;
import infrastructure.dto.HabitDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HabitCreatorTest {
    @InjectMocks
    private HabitCreator habitCreator;

    @Mock
    private Scanner scanner;

    @Mock
    private LocalUserDao localUserDao;

    @Before
    public void setUp() throws InvalidUserInformationException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_Correct_Habit() throws InvalidFrequencyConversionException, InterruptedException, InvalidHabitInformationException {
        // Подготовка данных, которые будут возвращаться при вызове scanner.nextLine()
        when(scanner.nextLine())
                .thenReturn("Заголовок", "Описание", "08:00 10/10/2024", "1");

        // Создание и настройка мокируемого пользователя
        User mockUser = mock(User.class);
        UUID mockUserId = UUID.randomUUID();
        when(mockUser.getId()).thenReturn(mockUserId);

        // Мокирование LocalUserDao
        when(localUserDao.get("test@gmail.com")).thenReturn(mockUser);

        // Создание привычки
        Habit habit = habitCreator.create(scanner, "test@gmail.com");

        // Проверка результатов
        assertNotNull(habit);
        assertEquals(Frequency.EVERY_DAY, habit.getFrequency());
        assertEquals(LocalDateTime.of(2024, 10, 10, 8, 0), habit.getCreationDateAndTime());
        assertEquals(mockUserId, habit.getUserId()); // Проверка, что ID пользователя установлен правильно
    }

}
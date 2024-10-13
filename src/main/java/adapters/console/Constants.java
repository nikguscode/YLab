package adapters.console;

/**
 * Константы, используемые в консоли, в качестве меню
 */
public class Constants {
    public static final String START_MENU = """
            ╔═══════════════════════════╗
            ║       Habit Tracker       ║
            ╚═══════════════════════════╝
            1. Войти в учётную запись
            2. Регистрация учётной записи
            -----------------------------
            Выберите опцию:\s""";

    public static final String AUTHENTICATION_MENU = """
            ╔═══════════════════════════╗
            ║          Login            ║
            ╚═══════════════════════════╝\s""";

    public static final String REGISTRATION_MENU = """
            ╔═══════════════════════════╗
            ║       Registration        ║
            ╚═══════════════════════════╝\s""";

    public static final String MAIN_MENU = """
            ╔═══════════════════════════╗
            ║           Menu            ║
            ╚═══════════════════════════╝
            1. Управление привычками
            2. Статистика пользователя
            3. Настройки учётной записи
            0. Выйти из учётной записи
            -----------------------------
            Выберите опцию:\s""";

    public static final String ADMINISTRATOR_MENU = """
            ╔═══════════════════════════╗
            ║           Menu            ║
            ╚═══════════════════════════╝
            1. Управление привычками
            2. Статистика пользователя
            3. Настройки учётной записи
            4. Панель администратора
            0. Выйти из учётной записи
            -----------------------------
            Выберите опцию:\s""";

    public static final String USER_SETTINGS = """
            ╔═══════════════════════════╗
            ║       User Settings       ║
            ╚═══════════════════════════╝
            1. Изменить имя
            2. Изменить пароль
            3. Изменить электронную почту
            4. Информация об учётной записи
            5. Удалить учётную запись
            0. Вернуться в главное меню
            -------------------------------
            Выберите опцию:\s""";

    public static final String USER_INFORMATION = """
            ╔═══════════════════════════╗
            ║     User Information      ║
            ╚═══════════════════════════╝\s""";


    public static final String HABIT_MENU = """
            ╔═══════════════════════════╗
            ║         Habit Menu        ║
            ╚═══════════════════════════╝
            1. Список привычек
            2. Добавить привычку
            0. Вернуться в главное меню
            -----------------------------
            Выберите опцию:\s""";

    public static final String HABIT_LIST = """
            ╔═══════════════════════════╗
            ║         Habit List        ║
            ╚═══════════════════════════╝\s""";

    public static final String HABIT_SORT = """
            ╔═══════════════════════════╗
            ║         Habit Sort        ║
            ╚═══════════════════════════╝
            1. По статусу (выполнена или нет)
            2. По дате и времени начала отсчёта
            3. По дате и времени последней отметки
            4. По частоте отметки
            0. Вернуться назад
            --------------------------------------
            Выберите опцию: \s""";

    public static final String HABIT_FILTER = """
            ╔═══════════════════════════╗
            ║        Habit Filter       ║
            ╚═══════════════════════════╝
            1. Показать только выполненные
            2. Показать только невыполненные
            3. Показать по определённой частоте
            0. Вернуться назад
            -----------------------------------
            Выберите опцию: \s""";

    public static final String SELECTED_HABIT_SETTINGS = """
            ╔═══════════════════════════╗
            ║  Selected Habit Settings  ║
            ╚═══════════════════════════╝\s""";

    public static final String HABIT_EDIT_MENU = """
            ╔═══════════════════════════╗
            ║        Habit Edit         ║
            ╚═══════════════════════════╝
            1. Изменить название
            2. Изменить описание
            3. Изменить частоту
            4. Удалить привычку
            0. Вернуться назад
            --------------------
            Выберите опцию:\s""";

    public static final String HABIT_STATISTICS_MENU = """
            ╔═══════════════════════════╗
            ║      Habit Statistics     ║
            ╚═══════════════════════════╝
            
            --------------------
            Выберите опцию:\s""";

    public static final String FILTER_FOOTER = """
            --------------------------------------------------------------------
            
            #0. Вернуться назад | #1. Сортировка | #2. Фильтрация
            # Чтобы открыть настройки конкретной привычки, укажите идентификатор
            Выберите опцию: \s""";

    public static final String FREQUENCY_LIST = """
            Доступная частота для привычки:
            1. Ежедневно
            2. Еженедельно
            3. Ежемесячно
            4. Раз в три месяца
            5. Раз в полгода
            6. Ежегодно
            -------------------
            Выберите опцию: \s""";
}
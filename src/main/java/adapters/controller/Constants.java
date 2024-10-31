package adapters.controller;

public class Constants {
    /**
     * Состояния ответов
     */
    public static final String SUCCEEDED = "Succeeded";
    public static final String ALREADY_AUTHENTICATED = "Already authenticated";
    public static final String UNAUTHENTICATED = "Unauthenticated";
    public static final String BAD_REQUEST = "Bad request";
    public static final String FORBIDDEN = "Forbidden";
    public static final String API_ERROR = "API Error";

    /**
     * Подробная информация ответов
     */
    public static final String USER_ACCOUNT_WAS_DELETED_SUCCESSFULLY = "Учётная запись удалена";
    public static final String USER_INFORMATION = "Информация о пользователе";
    public static final String INCORRECT_REQUEST_SYNTAX = "Некорректные параметры запроса, обратитесь к документации по" +
            "ссылке: https://github.com/nikguscode/YLab";
    public static final String USER_IS_NOT_AUTHENTICATED = "Пользователь не аутентифицирован, необходимо войти или создать аккаунт";
    public static final String USER_SUCCESSFULLY_AUTHENTICATED = "Пользователь аутентифицирован";
    public static final String USER_AUTHENTICATION_DATA_INCORRECT = "Проверьте введённую почту и пароль";
    public static final String USER_ALREADY_AUTHENTICATED = "Пользователь уже аутентифицирован для текущего сеанса";
    public static final String ALL_HABITS = "Список привычек пользователя";
    public static final String API_UNEXPECTED_ERROR = "API столкнулся с неожиданной ошибкой, обратитесь к разработчику";
    public static final String HABIT_DATA_CHANGED = "Данные привычки изменены";
    public static final String HABIT_CREATED = "Привычка создана";
    public static final String HABIT_DELETED = "Привычка удалена";
    public static final String USER_DATA_CHANGED = "Данные учётной записи изменены";
    public static final String USER_NOT_AUTHORIZED = "Недостаточно прав";
    public static final String USER_LIST = "Список пользователей";
    public static final String USER_BLOCKED = "Пользователь заблокирован";
    public static final String USER_UNBLOCKED = "Пользователь разблокирован";
}

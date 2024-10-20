//package usecase.authentication;
//
//import core.entity.User;
//import core.exceptions.InvalidUserInformationException;
//import infrastructure.dao.user.LocalUserDao;
//import infrastructure.dto.RegistrationDto;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import usecase.authentication.registration.LocalRegistration;
//import usecase.authentication.registration.Registration;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//
//
//public class RegistrationTest {
//    private final Registration registration = new LocalRegistration();
//
//    @Test
//    @DisplayName("Register With Incorrect Email")
//    public void register_With_Incorrect_Email() {
//        RegistrationDto registrationDto = RegistrationDto.builder()
//                .email("incorrectgmail.com")
//                .username("user")
//                .password("1")
//                .build();
//        assertThatThrownBy(() -> registration.isSuccess(registrationDto))
//                .isInstanceOf(InvalidUserInformationException.class);
//    }
//
//    @Test
//    @DisplayName("Register With Blank Data")
//    public void register_With_Blank_Data() {
//        RegistrationDto registrationDto = RegistrationDto.builder()
//                .email("")
//                .username("")
//                .password("")
//                .build();
//        assertThatThrownBy(() -> registration.isSuccess(registrationDto))
//                .isInstanceOf(InvalidUserInformationException.class);
//    }
//
//    @Test
//    @DisplayName("Account Already Exists")
//    public void account_Already_Exists() throws InvalidUserInformationException {
//        String existingEmail = "emailAlreadyExists@gmail.com";
//        RegistrationDto registrationDto = RegistrationDto.builder()
//                .email(existingEmail)
//                .username("username")
//                .password("password")
//                .build();
//        new LocalUserDao().add(User.builder().email(existingEmail).build());
//
//        boolean result = registration.isSuccess(registrationDto);
//        Assertions.assertThat(result).isFalse();
//    }
//}
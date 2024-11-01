package common.dto.response.user;

import core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInformationDtoMapper {
    UserInformationDtoMapper INSTANCE = Mappers.getMapper(UserInformationDtoMapper.class);

    @Mapping(target = "registrationDate", source = "registrationDate", dateFormat = "HH:mm:ss dd-MM-yyyy")
    @Mapping(target = "authorizationDate", source = "authorizationDate", dateFormat = "HH:mm:ss dd-MM-yyyy")
    UserInformationDto userToUserInformationDto(User user);
}

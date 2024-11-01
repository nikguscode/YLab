//package common.dto.response.habit.informationdto;
//
//import core.entity.Habit;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper
//public interface HabitInformationDtoMapper {
//    HabitInformationDtoMapper INSTANCE = Mappers.getMapper(HabitInformationDtoMapper.class);
//
//    @Mapping(target = "creationDateAndTime", source = "creationDateAndTime", dateFormat = "HH:mm:ss dd-MM-yyyy")
//    @Mapping(target = "lastMarkDateAndTime", source = "lastMarkDateAndTime", dateFormat = "HH:mm:ss dd-MM-yyyy")
//    @Mapping(target = "nextMarkDateAndTime", source = "nextMarkDateAndTime", dateFormat = "HH:mm:ss dd-MM-yyyy")
//    HabitInformationDto habitToHabitInformationDto(Habit habit);
//}
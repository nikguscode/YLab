package common.dto.response.habit.listdto;

import core.entity.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HabitListDtoMapper {
    HabitListDtoMapper INSTANCE = Mappers.getMapper(HabitListDtoMapper.class);

    @Mapping(target = "habitId", source = "id")
    @Mapping(target = "creationDateAndTime", source = "creationDateAndTime", dateFormat = "HH:mm:ss dd-MM-yyyy")
    @Mapping(target = "lastMarkDateAndTime", source = "lastMarkDateAndTime", dateFormat = "HH:mm:ss dd-MM-yyyy")
    @Mapping(target = "nextMarkDateAndTime", source = "nextMarkDateAndTime", dateFormat = "HH:mm:ss dd-MM-yyyy")
    HabitListDto habitToHabitListDto(Habit habit);
}

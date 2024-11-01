package common.dto.iternal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized @Builder
public class DataDto<T> {
    String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T responseData;
}

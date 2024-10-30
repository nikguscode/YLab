package common.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized @Builder
public class ErrorDto <T> {
    int code;
    String result;
    String dateAndTime;
    String description;
}
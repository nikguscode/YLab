package common.dto.response;

import adapters.controller.Constants;
import common.dto.iternal.DataDto;
import core.LocalDateTimeFormatter;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@Jacksonized @Builder
public class ResponseDto<T> {
    String result;
    String dateAndTime;
    DataDto<T> data;

    public ResponseDto(DataDto<T> data) {
        this.result = Constants.SUCCEEDED;
        this.dateAndTime = LocalDateTimeFormatter.formatWithSeconds();
        this.data = data;
    }

    public static class ResponseDtoBuilder<T> {
        public ResponseDto<T> build() {
            if (this.dateAndTime == null) {
                this.dateAndTime = LocalDateTimeFormatter.formatWithSeconds();
            }

            return new ResponseDto<>(result, dateAndTime, data);
        }
    }
}
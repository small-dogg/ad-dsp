package com.addsp.api.adgroup.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * 광고그룹 요일별 스케줄 수정 요청.
 */
public record UpdateScheduleRequest(
        @NotNull(message = "월요일 설정은 필수입니다.")
        Boolean mon,

        @NotNull(message = "화요일 설정은 필수입니다.")
        Boolean tue,

        @NotNull(message = "수요일 설정은 필수입니다.")
        Boolean wed,

        @NotNull(message = "목요일 설정은 필수입니다.")
        Boolean thu,

        @NotNull(message = "금요일 설정은 필수입니다.")
        Boolean fri,

        @NotNull(message = "토요일 설정은 필수입니다.")
        Boolean sat,

        @NotNull(message = "일요일 설정은 필수입니다.")
        Boolean sun
) {
}

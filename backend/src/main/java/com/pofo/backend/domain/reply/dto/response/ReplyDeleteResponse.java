package com.pofo.backend.domain.reply.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)  // 빈 객체라도 JSON으로 변환
public class ReplyDeleteResponse {
}

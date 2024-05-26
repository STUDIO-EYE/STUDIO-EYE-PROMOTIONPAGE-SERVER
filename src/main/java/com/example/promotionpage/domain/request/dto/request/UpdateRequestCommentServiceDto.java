package com.example.promotionpage.domain.request.dto.request;

import com.example.promotionpage.domain.request.domain.State;

public record UpdateRequestCommentServiceDto(
        String answer,
        State state
) {

}

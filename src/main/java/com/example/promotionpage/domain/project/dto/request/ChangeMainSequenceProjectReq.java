package com.example.promotionpage.domain.project.dto.request;

import lombok.Data;

@Data
public class ChangeMainSequenceProjectReq {

    private Long projectId;

    private Integer mainSequence;
}

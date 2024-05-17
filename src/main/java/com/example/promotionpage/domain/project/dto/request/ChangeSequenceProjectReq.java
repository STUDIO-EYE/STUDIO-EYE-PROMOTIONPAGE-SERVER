package com.example.promotionpage.domain.project.dto.request;

import lombok.Data;

@Data
public class ChangeSequenceProjectReq {

    private Long projectId;

    private Integer sequence;
}

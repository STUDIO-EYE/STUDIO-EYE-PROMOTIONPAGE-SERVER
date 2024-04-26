package com.example.promotionpage.domain.ceo.application;

import com.example.promotionpage.domain.ceo.dao.CeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CeoService {
    private final CeoRepository ceoRepository;
}

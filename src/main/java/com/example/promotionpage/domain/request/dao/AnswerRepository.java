package com.example.promotionpage.domain.request.dao;

import com.example.promotionpage.domain.request.domain.Answer;
import com.example.promotionpage.domain.request.domain.Request;
import com.example.promotionpage.domain.request.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}

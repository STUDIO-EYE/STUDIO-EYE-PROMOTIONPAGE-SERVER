package com.example.promotionpage.domain.client.dao;

import com.example.promotionpage.domain.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

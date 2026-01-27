package ua.tqs.opportunity4ua.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs.opportunity4ua.entity.Token;

public interface TokenRepository extends JpaRepository<Token, String> {
}

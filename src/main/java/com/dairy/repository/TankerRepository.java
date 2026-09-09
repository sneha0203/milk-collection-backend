package com.dairy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.Tanker;

public interface TankerRepository extends JpaRepository<Tanker, Long> {
}

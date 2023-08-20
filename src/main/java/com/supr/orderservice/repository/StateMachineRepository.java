package com.supr.orderservice.repository;

import com.supr.orderservice.entity.statemachine.StateMachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateMachineRepository extends JpaRepository<StateMachineEntity, Long> {
}

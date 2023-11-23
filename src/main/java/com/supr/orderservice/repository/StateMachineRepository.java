package com.supr.orderservice.repository;

import com.supr.orderservice.entity.statemachine.StateEntity;
import com.supr.orderservice.entity.statemachine.StateMachineEntity;
import com.supr.orderservice.enums.EntityTypeEnum;
import com.supr.orderservice.enums.OrderItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateMachineRepository extends JpaRepository<StateMachineEntity, Long> {

    @Query("SELECT sm FROM StateMachineEntity sm " +
            "WHERE sm.stateMachineTypeEntity.type = :stateMachineType " +
            "AND sm.stateMachineTypeEntity.entityType = :entityType " +
            "AND sm.stateChangeEventEntity.event = :eventName " +
            "AND sm.fromState.systemInternalStatus = :fromState")
   public  Optional<StateMachineEntity> fetchNextState(
            @Param("stateMachineType") String stateMachineType,
            @Param("eventName") String eventName,
            @Param("entityType") EntityTypeEnum entityType,
            @Param("fromState") OrderItemStatus fromState
    );
}

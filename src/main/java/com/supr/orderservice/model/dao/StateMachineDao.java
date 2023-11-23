package com.supr.orderservice.model.dao;

import com.supr.orderservice.entity.statemachine.StateChangeEventEntity;
import com.supr.orderservice.entity.statemachine.StateEntity;
import com.supr.orderservice.entity.statemachine.StateMachineEntity;
import com.supr.orderservice.entity.statemachine.StateMachineTypeEntity;
import com.supr.orderservice.enums.EntityTypeEnum;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.repository.StateMachineRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StateMachineDao {
    private final StateMachineRepository stateMachineRepository;

    public StateMachineDao(final StateMachineRepository stateMachineRepository) {
        this.stateMachineRepository = stateMachineRepository;
    }

    public Optional<StateMachineEntity> getStateMachine1(final String stateMachineType,
                                                         final OrderItemStatus fromState,
                                                         final String stateChangeEvent,
                                                         final EntityTypeEnum entityTypeEnum) {
        return stateMachineRepository.fetchNextState(stateMachineType, stateChangeEvent, entityTypeEnum, fromState);
    }

    public Optional<StateMachineEntity> getStateMachine(final String stateMachineType,
                                                        final OrderItemStatus fromState,
                                                        final String stateChangeEvent,
                                                        final EntityTypeEnum entityTypeEnum) {

        StateMachineTypeEntity stateMachineTypeEntity = new StateMachineTypeEntity();
        stateMachineTypeEntity.setType(stateMachineType);
        stateMachineTypeEntity.setEntityType(entityTypeEnum);

        StateEntity fromStateEntity = new StateEntity();
        fromStateEntity.setSystemInternalStatus(fromState);

        StateChangeEventEntity stateChangeEventEntity = new StateChangeEventEntity();
        stateChangeEventEntity.setEvent(stateChangeEvent);

        StateMachineEntity stateMachineEntity = new StateMachineEntity();
        stateMachineEntity.setStateMachineTypeEntity(stateMachineTypeEntity);
        stateMachineEntity.setFromState(fromStateEntity);
        stateMachineEntity.setStateChangeEventEntity(stateChangeEventEntity);

        Optional<StateMachineEntity> stateMachineOptional =
                stateMachineRepository.findOne(Example.of(stateMachineEntity));
        if (stateMachineOptional.isPresent()) {
            StateMachineEntity stateMachine = stateMachineOptional.get();
        }
        return stateMachineOptional;
    }


}

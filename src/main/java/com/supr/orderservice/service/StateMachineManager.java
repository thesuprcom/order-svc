package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderBaseEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.entity.statemachine.StateEntity;
import com.supr.orderservice.entity.statemachine.StateMachineEntity;
import com.supr.orderservice.enums.EntityTypeEnum;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.dao.StateMachineDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StateMachineManager {
  private final StateMachineDao stateMachineDao;

  public StateMachineManager(final StateMachineDao stateMachineDao) {
    this.stateMachineDao = stateMachineDao;
  }

  public void moveToNextState(OrderEntity order,
                              String stateMachineType,
                              String stateChangeEvent) {

    Optional<StateEntity> optionalNextState = getNextState(stateMachineType,
        order.getStatus(),
        stateChangeEvent,
        EntityTypeEnum.ORDER);

    moveToNextState(order, optionalNextState);
  }

  public void moveToNextState(OrderItemEntity orderItemVO,
                              String stateMachineType,
                              String stateChangeEvent) {

    Optional<StateEntity> optionalNextState = getNextState(stateMachineType,
        orderItemVO.getStatus(),
        stateChangeEvent,
        EntityTypeEnum.ORDER_ITEM);

    moveToNextState(orderItemVO, optionalNextState);
  }

  private void moveToNextState(final OrderBaseEntity orderBaseEntity, final Optional<StateEntity> optionalNextState) {
    if (optionalNextState.isPresent()) {
      StateEntity nextState = optionalNextState.get();
      orderBaseEntity.setStatus(nextState.getSystemInternalStatus());
      orderBaseEntity.setExternalStatus(nextState.getUserVisibleStatus());
    } else {
      throw new OrderServiceException(ErrorEnum.NO_TRANSITION_FOUND, HttpStatus.NOT_FOUND);
    }
  }

  private Optional<StateEntity> getNextState(String stateMachineType,
                                             OrderItemStatus fromState,
                                             String stateChangeEvent,
                                             EntityTypeEnum entityTypeEnum) {
    log.info("State machine type: {}, from state: {}, state change event: {}", stateMachineType,
        fromState, stateChangeEvent);

    Optional<StateMachineEntity> optionalStateMachineEntity = stateMachineDao.getStateMachine1(stateMachineType,
        fromState,
        stateChangeEvent,
        entityTypeEnum);
  
    return optionalStateMachineEntity.map(StateMachineEntity::getToState);
  
  }
}

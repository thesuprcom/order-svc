package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.entity.GreetingCardEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.entity.TransactionEntity;
import com.supr.orderservice.enums.CouponInventoryOperationType;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.GiftSentOption;
import com.supr.orderservice.enums.GreetingCardStatus;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.OrderItemEvent;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.StateChangeReason;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.enums.TransactionStatus;
import com.supr.orderservice.exception.BadRequestException;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.CouponDetails;
import com.supr.orderservice.model.GreetingCard;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.OrderSummaryDTO;
import com.supr.orderservice.model.Product;
import com.supr.orderservice.model.SavedCardDetails;
import com.supr.orderservice.model.UserCartDTO;
import com.supr.orderservice.model.request.CheckItemDetailsRequest;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.request.PurchaseOrderRequest;
import com.supr.orderservice.model.request.UpdateQuantityRequest;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.model.response.ProductDataResponse;
import com.supr.orderservice.model.response.PurchaseOrderResponse;
import com.supr.orderservice.model.response.SellerSkuResponse;
import com.supr.orderservice.repository.CardDetailsRepository;
import com.supr.orderservice.repository.GreetingCardRepository;
import com.supr.orderservice.repository.OrderItemRepository;
import com.supr.orderservice.service.CouponInventoryManagementService;
import com.supr.orderservice.service.GreetingCardService;
import com.supr.orderservice.service.OrderInventoryManagementService;
import com.supr.orderservice.service.OrderItemService;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.service.PaymentGatewayService;
import com.supr.orderservice.service.PurchaseOrderService;
import com.supr.orderservice.service.SenderOrderService;
import com.supr.orderservice.service.StateMachineManager;
import com.supr.orderservice.service.TransactionService;
import com.supr.orderservice.service.external.CartServiceClient;
import com.supr.orderservice.service.external.InventoryServiceClient;
import com.supr.orderservice.utils.ApplicationUtils;
import com.supr.orderservice.utils.CardDetailsUtility;
import com.supr.orderservice.utils.CouponUtility;
import com.supr.orderservice.utils.DateUtils;
import com.supr.orderservice.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.supr.orderservice.enums.CouponInventoryUpdateType.USER_LEVEL_AND_COUPON_LEVEL;
import static com.supr.orderservice.utils.Constants.COUPON_DISCOUNT_IS_NOT_APPLICABLE_ON_THIS_PAYMENT_METHOD;
import static com.supr.orderservice.utils.DateUtils.getScheduledTimestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartServiceClient cartServiceClient;
    private final TransactionService transactionService;
    private final CardDetailsUtility cardDetailsUtility;
    private final SenderOrderService senderOrderService;
    private final GreetingCardService greetingCardService;
    private final CardDetailsRepository cardDetailsRepository;
    private final GreetingCardRepository greetingCardRepository;
    private final StateMachineManager stateMachineManager;
    private final InventoryServiceClient inventoryServiceClient;
    private final PaymentGatewayService paymentGatewayService;
    private final OrderInventoryManagementService orderInventoryManagementService;
    private final CouponInventoryManagementService couponInventoryManagementService;

    @Override
    public PurchaseOrderResponse purchaseOrder(PurchaseOrderRequest request) {
        try {
            OrderEntity order = createOrderEntity(request);
            final OrderEntity finalOrder = order;
            final TransactionEntity transaction = transactionService.createTransaction(finalOrder);
            order.setTransaction(transaction);
            List<OrderItemEntity> orderItemEntities = generateOrderItems(finalOrder, request);
            orderItemRepository.saveAll(orderItemEntities);
            order.setOrderItemEntities(orderItemEntities);
            GreetingCardEntity greetingCard = greetingCardRepository.save(fetchGreetingCard(finalOrder, request));
            order.setGreetingCard(greetingCard);
            if (request.isScheduledOrder()) {
                order.setOrderScheduled(true);
                order.setScheduledDate(getScheduledTimestamp(request.getScheduleDate()));
            }
            order = orderService.save(order);
            SavedCardDetails savedCardDetails = paymentGatewayService.paymentDetails(order.getUserId());
            PurchaseOrderResponse response = new PurchaseOrderResponse();
            response.setOrderId(order.getOrderId());
            response.setAmountPayable(order.getTotalAmount());
            response.setSavedCards(savedCardDetails.getSavedCards());
            return response;
        }catch (Exception exception){
            log.error("Error while creating the order: {}", exception);
            throw new OrderServiceException("Unable to create the order");
        }
    }


    @Override
    public PaymentProcessingResponse placeOrder(ProcessPaymentRequest request) {
        OrderEntity order = senderOrderService.fetchOrder(request.getOrderId());
        PaymentProcessingResponse processTransactionResponse = transactionService.processTransaction(order, request);
        performPostPaymentAuthActionAndUpdateItemQuantity(request, order, StateMachineType.SENDER.name()
                , OrderChangeEvent.SENDER_PLACE_ORDER.name());
        return processTransactionResponse;
    }

    private OrderEntity createOrderEntity(PurchaseOrderRequest request) {
        OrderEntity order = createOrder(request.getOrderId());
        order.setStatus(OrderItemStatus.ANY);

        UserCartDTO userCartDTO = request.getUserCartDTO();
        order.setReceiverEmail(userCartDTO.getReceiverEmail());
        order.setReceiverPhone(userCartDTO.getReceiverPhone());
        order.setReceiver(userCartDTO.getReceiver());
        order.setShippingAddress(request.getUserCartDTO().getReceiverAddress());
        order.setBillingAddress(userCartDTO.getBillingAddress());
        order.setPaymentMode(PaymentMode.Card);
        order.setCountryCode(request.getCountryCode());
        order.setCurrencyCode(request.getCurrencyCode());
        order.setGiftSentOption(userCartDTO.getGiftSentThrough());
        OrderPrice orderPrice = fetchOrderPrice(request);
        order.setTotalAmount(ApplicationUtils.roundUpToTwoDecimalPlaces(getPayableAmount(orderPrice)));
        order.setPrice(orderPrice);
        order.setSellerId(userCartDTO.getSellerInfo().getStoreId());
        order.setUserId(userCartDTO.getUserId());
        order.setSellerInfo(userCartDTO.getSellerInfo());
        order.setSender(userCartDTO.getSender());
        order.setIpAddress(request.getIpAddress());
        order.setCouponDetails(userCartDTO.getCouponDetails());
        order.setOrderPlacedTime(DateUtils.getCurrentDateTimeUTC());
        order.setOrderType(OrderType.SENDER);
        order.setStatus(OrderItemStatus.CREATED);
        order.setExternalStatus(ExternalStatus.CREATED);
        return orderService.save(order);
    }

    private OrderPrice fetchOrderPrice(PurchaseOrderRequest request) {
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setTotalPrice(request.getUserCartDTO().getPriceDetails().getTotalPrice());
        orderPrice.setTotalShipping(request.getUserCartDTO().getPriceDetails().getTotalShipping());
        orderPrice.setTotalVat(request.getUserCartDTO().getPriceDetails().getTotalTax());
        return orderPrice;
    }

    private OrderEntity createOrder(String orderId) {
        OrderEntity order;
        if (orderId != null) {
            order = senderOrderService.fetchOrder(orderId);
            final OrderItemStatus orderStatus = order.getStatus();
            if (orderStatus == OrderItemStatus.ACCEPTED || orderStatus == OrderItemStatus.PLACED) {
                throw new OrderServiceException(ErrorEnum.ORDER_IS_BEING_PROCESSED);
            }
            if (orderStatus != OrderItemStatus.CREATED ||
                    order.getTransaction().getStatus() != TransactionStatus.CREATED) {
                log.info("Existing order id: {} has status: {}, hence generating new order id.", order.getOrderId(),
                        orderStatus);
                return generateNewOrder();
            }
            return order;
        } else {
            return generateNewOrder();
        }
    }

    private OrderEntity generateNewOrder() {
        OrderEntity order = new OrderEntity();
        order.setOrderId(ApplicationUtils.generateOrderId());
        return order;
    }

    private BigDecimal getPayableAmount(OrderPrice orderPrice) {
        return orderPrice.getTotalPrice();
    }

    private GreetingCardEntity fetchGreetingCard(OrderEntity order, PurchaseOrderRequest request) {
        deleteAllExistingGreetingCardItems(order.getId());
        GreetingCard greetingCardReq = request.getUserCartDTO().getGreetingCard();
        GreetingCardEntity greetingCard = new GreetingCardEntity();
        greetingCard.setOrder(order);
        greetingCard.setGreetingCardMsg(greetingCardReq.getGreetingCardMsg());
        greetingCard.setGreetingCardCode(greetingCardReq.getGreetingCardCode());
        greetingCard.setGreetingCardName(greetingCardReq.getGreetingCardName());
        greetingCard.setGreetingCardOccasion(greetingCardReq.getGreetingCardOccasion());
        greetingCard.setGreetingCardStatus(GreetingCardStatus.CREATED);
        greetingCard.setGreetingCardImage(greetingCardReq.getGreetingCardImage());
        greetingCard.setGreetingCardImageUrl(greetingCardReq.getGreetingCardImageUrl());
        greetingCard.setReceiverName(greetingCardReq.getReceiverName());
        return greetingCard;
    }

    private List<OrderItemEntity> generateOrderItems(OrderEntity order, PurchaseOrderRequest request) {
        deleteAllExistingOrderItems(order.getId());

        List<OrderItemEntity> orderItems = new ArrayList<>();
        int suffix = 1;
        final UserCartDTO userCartDTO = request.getUserCartDTO();
        List<ItemInfo> items = userCartDTO.getGiftItems();
        CheckItemDetailsRequest checkItemDetailsRequest = new CheckItemDetailsRequest();
        checkItemDetailsRequest.setSkus(items.stream().map(ItemInfo::getSkus).collect(Collectors.toList()));
        Map<String, Product> productDataResponse =
                inventoryServiceClient.fetchSellerSkuDetails(order.getCountryCode(), checkItemDetailsRequest);
        for (ItemInfo cartInfo : items) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setCountryCode(request.getCountryCode());
            orderItem.setStatus(OrderItemStatus.CREATED);
            orderItem.setOrderItemId(order.getOrderId() + "-" + String.format("%03d", suffix++));
            orderItem.setPskuCode(cartInfo.getPsku());
            orderItem.setItemInfo(cartInfo);
            orderItem.setSellerId(userCartDTO.getSellerInfo().getStoreId());
            orderItem.setOrderItemQuantity(cartInfo.getQuantity());
            orderItem.setCouponDetails(request.getUserCartDTO().getCouponDetails());
            orderItem.setBrandId(request.getUserCartDTO().getSellerInfo().getBrandId());
            orderItem.setBrandId(cartInfo.getBrandCode());
            orderItem.setProductId(cartInfo.getSkus());
            orderItem.setProductTitle(cartInfo.getGiftTitle());
            orderItem.setProductBrand(cartInfo.getBrandCode());
            orderItem.setProductFamily(cartInfo.getBrandName());
            orderItem.setImages(cartInfo.getGiftImages());
            orderItem.setParentSku(cartInfo.getPsku());
            orderItem.setPrice(fetchItemPrice(cartInfo, userCartDTO));
            orderItem.setTotalPrice(orderItem.getPrice().getTotalPrice());
            orderItem.setCouponDetails(userCartDTO.getCouponDetails());
            orderItem.setStatus(OrderItemStatus.CREATED);
            orderItem.setExternalStatus(ExternalStatus.CREATED);
            orderItems.add(orderItem);
        }
        List<OrderItemEntity> outOfStockItems = OrderUtils.validateStock(productDataResponse, orderItems);
        if (outOfStockItems.size() > 0) {
            throw new OrderServiceException("Few items in the cart are OUT_OF_STOCK");
        }
        return OrderUtils.updatePriceFromCatalogService(productDataResponse, orderItems);
    }

    private OrderPrice fetchItemPrice(ItemInfo cartInfo, UserCartDTO userCartDTO) {
        OrderSummaryDTO orderSummary = userCartDTO.getOrderSummary();
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setTotalPrice(cartInfo.getSalePrice().multiply(cartInfo.getQuantity()));
        orderPrice.setTotalShipping(cartInfo.getShippingPrice());
        return orderPrice;
    }

    private void deleteAllExistingOrderItems(final Long orderId) {
        if (orderId != null) {
            orderItemService.deleteAllOrderItems(orderId);
        }
    }

    private void deleteAllExistingGreetingCardItems(Long orderId) {
        if (orderId != null) {
            greetingCardService.deleteGreetingCard(orderId);
        }
    }

    private void performPostPaymentAuthActionAndUpdateItemQuantity(ProcessPaymentRequest request, OrderEntity order,
                                                                   String stateMachineType, String orderChangeEvent) {
        performActionPostPaymentAuthorization(order, request.getPaymentModeSelected(), stateMachineType, orderChangeEvent);
        OrderEntity orderEntity = orderService.save(order);
        if (orderEntity.isOrderScheduled()) {
            stateMachineManager.moveToNextState(order, StateMachineType.SENDER.name(),
                    OrderChangeEvent.SENDER_ORDER_SCHEDULED.name());
            orderEntity.getOrderItemEntities().forEach(orderItemEntity ->
                    stateMachineManager.moveToNextState(orderItemEntity, StateMachineType.SENDER.name(),
                            OrderChangeEvent.SENDER_ORDER_SCHEDULED.name()));
        }
    }

    private void performActionPostPaymentAuthorization(final OrderEntity order, PaymentMode paymentMode,
                                                       String stateMachineType, String orderChangeEvent) {
        order.setSubscriptionPending(false);
        order.setPaymentMode(paymentMode);
        orderService.changeOrderState(order, stateMachineType, orderChangeEvent, true,
                StateChangeReason.PAYMENT_AUTHORIZATION_SUCCESSFULLY.getReason());

        final String userId = order.getUserId();
        log.info("Incrementing the order in progress count for user: {} and order: {}", userId, order.getOrderId());
        cardDetailsUtility.addOrderIdToPaymentPendingOrder(userId, order.getOrderId());

        performCouponActionsPostPaymentAuthorization(order, paymentMode);
        order.setOrderPlacedTime(DateUtils.getCurrentDateTimeUTC());
        orderInventoryManagementService.updateStoreOrderQuantity(order, UpdateQuantityRequest.OperationType.DEC);
        clearCart(order);
    }

    private void performCouponActionsPostPaymentAuthorization(final OrderEntity order, final PaymentMode paymentMode) {
        final CouponDetails couponDetails = order.getCouponDetails();

        String maskedCardNumber = cardDetailsRepository.findFirstByUserId(order.getUserId())
                .map(CardDetailsEntity::getPaymentInfo)
                .orElse(null);

        if (CouponUtility.isCouponAppliedToOrder(couponDetails)) {
            final boolean invalidPaymentOptionSelectedForCoupon =
                    CouponUtility.isInvalidPaymentOptionSelectedForCoupon(couponDetails, paymentMode, maskedCardNumber);

            if (invalidPaymentOptionSelectedForCoupon) {
                throw new BadRequestException(COUPON_DISCOUNT_IS_NOT_APPLICABLE_ON_THIS_PAYMENT_METHOD);
            }

            couponInventoryManagementService.updateCouponInventory(order, CouponInventoryOperationType.INCREMENT,
                    USER_LEVEL_AND_COUPON_LEVEL);
        }
    }

    private void clearCart(final OrderEntity order) {
        try {
            cartServiceClient.clearCart(order.getOrderId());
        } catch (Exception exception) {
            log.info("Exception occurred while clearing cart.", exception);
        }
    }

}

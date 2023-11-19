package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.GreetingCardEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.exception.AuthenticationException;
import com.supr.orderservice.exception.BadRequestException;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.CustomerOrderDetail;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.Product;
import com.supr.orderservice.model.UserDetails;
import com.supr.orderservice.model.UserInfo;
import com.supr.orderservice.model.request.CheckItemDetailsRequest;
import com.supr.orderservice.model.request.GiftConfirmRequest;
import com.supr.orderservice.model.request.ProcessPaymentRequest;
import com.supr.orderservice.model.request.ReceiverPlaceOrderRequest;
import com.supr.orderservice.model.request.SwapGiftRequest;
import com.supr.orderservice.model.request.UserDetailRequest;
import com.supr.orderservice.model.request.UserOtpRequest;
import com.supr.orderservice.model.request.UserOtpVerifyRequest;
import com.supr.orderservice.model.request.VerifyGiftOtpRequest;
import com.supr.orderservice.model.request.VerifyGiftRequest;
import com.supr.orderservice.model.response.AcceptGiftResponse;
import com.supr.orderservice.model.response.CustomerOrderResponse;
import com.supr.orderservice.model.response.FetchGreetingResponse;
import com.supr.orderservice.model.response.GiftConfirmResponse;
import com.supr.orderservice.model.response.GiftSwapOptionsResponse;
import com.supr.orderservice.model.response.ItemDetailResponse;
import com.supr.orderservice.model.response.OpenGreetingResponse;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.model.response.ProductDataResponse;
import com.supr.orderservice.model.response.ReceiverPlaceOrderResponse;
import com.supr.orderservice.model.response.SellerSkuResponse;
import com.supr.orderservice.model.response.SwapGiftResponse;
import com.supr.orderservice.model.response.UserDetailResponse;
import com.supr.orderservice.model.response.VerifyGiftResponse;
import com.supr.orderservice.model.response.ViewGiftResponse;
import com.supr.orderservice.model.response.WalletResponse;
import com.supr.orderservice.repository.GreetingCardRepository;
import com.supr.orderservice.repository.OrderItemRepository;
import com.supr.orderservice.repository.OrderRepository;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.service.ReceiverOrderService;
import com.supr.orderservice.service.InvoiceService;
import com.supr.orderservice.service.PurchaseOrderService;
import com.supr.orderservice.service.StateMachineManager;
import com.supr.orderservice.service.WalletService;
import com.supr.orderservice.service.external.CustomerServiceClient;
import com.supr.orderservice.service.external.InventoryServiceClient;
import com.supr.orderservice.utils.ApplicationUtils;
import com.supr.orderservice.utils.CardDetailsUtility;
import com.supr.orderservice.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.supr.orderservice.enums.OrderChangeEvent.GIFT_SWAPPED_LOWER;
import static com.supr.orderservice.enums.OrderChangeEvent.RECEIVER_SWAPPED_GIFT;
import static com.supr.orderservice.utils.Constants.ACCEPTED_GIFT_SWAP;
import static com.supr.orderservice.utils.Constants.ACCEPTED_GIFT_WITHOUT_SWAP;
import static com.supr.orderservice.utils.OrderUtils.reCalculatePrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiverOrderServiceImpl implements ReceiverOrderService {
    private final InvoiceService invoiceService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final WalletService walletService;
    private final CardDetailsUtility cardDetailsUtility;
    private final CustomerServiceClient customerService;
    private final StateMachineManager stateMachineManager;
    private final OrderItemRepository orderItemRepository;
    private final PurchaseOrderService purchaseOrderService;
    private final GreetingCardRepository greetingCardRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Override
    public FetchGreetingResponse fetchGreeting(String orderId) {
        log.info("Request to fetch the Greeting for order: {}", orderId);
        OrderEntity orderEntity = fetchSenderOrderDetail(orderId);
        GreetingCardEntity greetingCard = greetingCardRepository.findByOrderId(orderEntity.getId()).orElseThrow(() ->
                new BadRequestException("No greeting card found for the Order"));
        changeTheOrderStatus(orderEntity, OrderChangeEvent.RECEIVER_CLICKED_GIFT.name());
        return OrderUtils.fetchGreetingResponse(greetingCard, orderEntity);
    }

    @Override
    public OpenGreetingResponse openGreeting(String orderId) {
        log.info("Request to open the Greeting card for order: {}", orderId);
        OrderEntity orderEntity = fetchSenderOrderDetail(orderId);
        GreetingCardEntity greetingCard = greetingCardRepository.findByOrderId(orderEntity.getId()).orElseThrow(() ->
                new BadRequestException("No greeting card found for the Order"));

        changeTheOrderStatus(orderEntity, OrderChangeEvent.RECEIVER_OPEN_GIFT.name());

        return OpenGreetingResponse.builder().greetingMessage(greetingCard.getGreetingCardMsg())
                .senderName(orderEntity.getSender().getFirstName()).orderId(orderEntity.getOrderId()).build();
    }

    @Override
    public ViewGiftResponse viewGift(String orderId) {
        log.info("Request to view the gift items for order: {}", orderId);
        OrderEntity order = fetchReceiverOrderDetail(orderId);
        List<OrderItemEntity> orderItemList = orderItemRepository.findAllByOrderIdIn(List.of(order.getId()));
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new BadRequestException("No gift Item present against the order id");
        }
        return OrderUtils.fetchViewGiftResponse(order, orderItemList);
    }

    @Override
    public ItemDetailResponse fetchItemDetails(String orderId, String itemId, String sellerId) {
        log.info("Request to fetch item details the gift item: {} for order: {}", itemId, orderId);
        OrderEntity orderEntity = fetchReceiverOrderDetail(orderId);
        ItemInfo itemInfo;
        try {
            log.info("Calling inventory service to fetch details of item: {} for order: {}", itemId, orderId);
            itemInfo = inventoryServiceClient.fetchItemDetails(itemId, sellerId, orderEntity.getCountryCode());
        } catch (Exception e) {
            log.error("Unable to fetch the item details!!", e);
            throw new BadRequestException("Unable to fetch the item details!!");
        }
        return OrderUtils.fetchItemDetails(orderEntity, itemInfo);
    }

    @Override
    public GiftSwapOptionsResponse fetchSwapOptions(String orderId, String sku) {
        log.info("Request to fetch the gift swap options for order: {}", orderId);
        OrderEntity orderEntity = fetchReceiverOrderDetail(orderId);
        String totalPrice = orderEntity.getTotalAmount().toString();
        List<ItemInfo> itemInfo;
        try {
            log.info("Calling inventory service to fetch swap options for orderId: {}", orderId);
            itemInfo = inventoryServiceClient.fetchItemDetailForSwap(sku, totalPrice, 0, 10);
        } catch (Exception e) {
            log.error("Unable to fetch the item details!!", e);
            throw new BadRequestException("Unable to fetch the item details!!");
        }
        return OrderUtils.fetchSwapOptions(orderEntity, itemInfo);
    }

    @Override
    public SwapGiftResponse swapGift(SwapGiftRequest swapGiftRequest) {
        log.info("Request to swap the gift for order: {}", swapGiftRequest.getOrderId());
        OrderEntity orderEntity = fetchSenderOrderDetail(swapGiftRequest.getOrderId());
        List<OrderItemEntity> orderItemEntityList =
                orderItemRepository.findAllByOrderIdIn(List.of(orderEntity.getId()));
        List<OrderItemEntity> orderItems = OrderUtils.fetchOrderItemEntity(swapGiftRequest.getSwappedItemInfo(),
                orderEntity, orderItemEntityList.size());
        OrderPrice newOrderPrice = reCalculatePrice(orderItems);
        int priceCompareResult = newOrderPrice.getTotalPrice().compareTo(orderEntity.getPrice().getTotalPrice());

        changeTheOrderStatus(orderEntity, priceCompareResult < 0 ? GIFT_SWAPPED_LOWER.name() : RECEIVER_SWAPPED_GIFT.name());
        orderItemRepository.saveAll(orderItems);
        orderEntity.setGiftSwapped(true);
        orderEntity.setReceiverOrderPrice(newOrderPrice);
        orderEntity.setReceiverTotalAmount(newOrderPrice.getTotalPrice());
        OrderEntity savedOrderEntity = orderRepository.save(orderEntity);
        if (priceCompareResult < 0) {
            log.info("Since swappedTotal amount is lower hence going for wallet refund: {} for userId :{}",
                    savedOrderEntity.getOrderId(), savedOrderEntity.getUserId());
            BigDecimal priceDifference = orderEntity.getTotalAmount().subtract(newOrderPrice.getTotalPrice());
            processWalletRefund(savedOrderEntity.getSender(), priceDifference, savedOrderEntity);
        }
        return OrderUtils.getchSwapGiftResponse(savedOrderEntity, orderItems);
    }


    @Override
    public AcceptGiftResponse acceptGift(String orderId) {
        log.info("Request to accept the gift for order: {}", orderId);
        OrderEntity orderEntity = fetchSenderOrderDetail(orderId);
        List<OrderItemEntity> orderItemEntityList =
                orderItemRepository.findAllByStatusAndOrderIdIn(OrderItemStatus.PLACED, List.of(orderEntity.getId()));
        stateMachineManager.moveToNextState(orderEntity, StateMachineType.SENDER.name(),
                OrderChangeEvent.RECEIVER_ACCEPTED_GIFT.name());
        orderItemEntityList.forEach(orderItemEntity -> {
            stateMachineManager.moveToNextState(orderItemEntity, StateMachineType.SENDER.name(),
                    OrderChangeEvent.RECEIVER_ACCEPTED_GIFT.name());
        });
        return OrderUtils.fetchAcceptGiftResponse(orderItemEntityList, orderEntity);
    }

    @Override
    public ReceiverPlaceOrderResponse receiverPlaceOrder(ReceiverPlaceOrderRequest placeOrderRequest) {
        boolean shouldTriggerReceiverPayment = false;
        log.info("Request to place order: {}", placeOrderRequest);
        OrderEntity order = fetchSenderOrderDetail(placeOrderRequest.getOrderId());
        order.setShippingAddress(placeOrderRequest.getAddress());
        order.setReceiverEmail(placeOrderRequest.getEmailId());
        order.setReceiverPhone(placeOrderRequest.getPhoneNumber());
        if (order.getReceiverTotalAmount() == null || order.getReceiverTotalAmount().compareTo(BigDecimal.ZERO) == 0 ||
                order.getTotalAmount().compareTo(order.getReceiverTotalAmount()) < 0) {
            stateMachineManager.moveToNextState(order, OrderType.RECEIVER.name(), ACCEPTED_GIFT_WITHOUT_SWAP);
            CompletableFuture.runAsync(() -> invoiceService.generateReceiverInvoice(order));
            CompletableFuture.runAsync(() -> invoiceService.generateSenderInvoice(order));
        } else {
            shouldTriggerReceiverPayment = true;
            stateMachineManager.moveToNextState(order, OrderType.RECEIVER.name(), ACCEPTED_GIFT_SWAP);
        }
        List<OrderItemEntity> orderItems =
                orderItemRepository.findAllByStatusAndOrderIdIn(OrderItemStatus.PLACED, List.of(order.getId()));
        return OrderUtils.fetchReceiverPlaceOrderResponse(shouldTriggerReceiverPayment, order, orderItems);
    }

    @Override
    public PaymentProcessingResponse receiverOrderPayment(ProcessPaymentRequest processPaymentRequest) {
        log.info("Request to make receiver payment for order: {}", processPaymentRequest);
        try {
            return purchaseOrderService.placeOrder(processPaymentRequest);
        } catch (Exception exception) {
            log.info("Exception occurred in place order.", exception);
            OrderEntity order = orderRepository.findByOrderId(processPaymentRequest.getOrderId());
            cardDetailsUtility.removeOrderIdFromPaymentPendingOrder(order.getUserId(), order.getOrderId());
            throw new OrderServiceException(ErrorEnum.USER_FRIENDLY_SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public VerifyGiftResponse verifyGift(VerifyGiftRequest verifyGiftRequest) {
        UserOtpRequest userOtpRequest = new UserOtpRequest();
        VerifyGiftResponse userOtpResponse = new VerifyGiftResponse();
        if (verifyGiftRequest.getOrderId() == null) {
            throw new BadRequestException("OrderId cannot be empty ");
        }
        if (verifyGiftRequest.getEmailId() == null && verifyGiftRequest.getPhoneNumber() == null) {
            throw new BadRequestException("EmailId and PhoneNumber cannot be empty ");
        }
        OrderEntity orderEntity = fetchSenderOrderDetail(verifyGiftRequest.getOrderId());
        UserInfo receiver = orderEntity.getReceiver();
        userOtpRequest.setCountryCode(verifyGiftRequest.getCountryCode());
        if (verifyGiftRequest.getEmailId() != null) {
            userOtpRequest.setEmailId(verifyGiftRequest.getEmailId());
            userOtpResponse.setEmailId(verifyGiftRequest.getEmailId());
        } else {
            userOtpRequest.setPhoneNumber(verifyGiftRequest.getPhoneNumber());
            userOtpResponse.setPhoneNumber(verifyGiftRequest.getPhoneNumber());
        }
        userOtpRequest.setClientReference(UUID.randomUUID().toString());
        userOtpResponse = customerService.generateOtp(userOtpRequest);
        receiver.setUserId(userOtpResponse.getData().getPublicId());
        orderEntity.setReceiver(receiver);
        orderService.save(orderEntity);
        userOtpResponse.setOrderId(orderEntity.getOrderId());
        userOtpResponse.setClientReference(userOtpRequest.getClientReference());
        return userOtpResponse;
    }

    @Override
    public VerifyGiftResponse verifyGiftOtp(VerifyGiftOtpRequest verifyGiftOtpRequest) {
        UserOtpVerifyRequest request = new UserOtpVerifyRequest();
        request.setOtpCode(verifyGiftOtpRequest.getOtpCode());
        request.setEmailId(verifyGiftOtpRequest.getEmailId());
        request.setClientReference(verifyGiftOtpRequest.getClientReference());
        request.setPhoneNumber(verifyGiftOtpRequest.getPhoneNumber());
        request.setVerifyToken(verifyGiftOtpRequest.getVerifyToken());
        request.setPublicId(verifyGiftOtpRequest.getPublicId());
        VerifyGiftResponse verifyGiftResponse = customerService.verifyOtp(request, "receiver");
        if (verifyGiftResponse.success) {
            OrderEntity orderEntity = fetchSenderOrderDetail(verifyGiftOtpRequest.getOrderId());
            verifyGiftResponse.setFrom(orderEntity.getSender().getFirstName() + " " + orderEntity.getSender().getLastName());
            verifyGiftResponse.setTo(orderEntity.getReceiver().getFirstName() + " " + orderEntity.getReceiver().getLastName());
            verifyGiftResponse.setOrderId(orderEntity.getOrderId());
            createReceiver(orderEntity, verifyGiftOtpRequest);
        } else {
            throw new AuthenticationException("Cannot verify the User otp");
        }
        return verifyGiftResponse;
    }

    @Override
    public GiftConfirmResponse confirmGift(String orderId, GiftConfirmRequest request) {
        OrderEntity orderEntity = fetchSenderOrderDetail(orderId);
        CheckItemDetailsRequest checkItemDetailsRequest = new CheckItemDetailsRequest();
        checkItemDetailsRequest.setSkus(request.getGiftItems().stream().map(ItemInfo::getSkus).collect(Collectors.toList()));
        Map<String, Product> productDataResponse = getSellerSkuResponse(orderId, orderEntity,
                checkItemDetailsRequest);
        List<OrderItemEntity> outOfStockItems = OrderUtils.validateStock(productDataResponse,
                orderEntity.getOrderItemEntities());
        if (outOfStockItems.size() > 0) {
            return GiftConfirmResponse.builder().orderId(orderId).outOfStockItems(outOfStockItems)
                    .isOutOfStock(true).build();
        }
        if (!request.isGiftSwapped()) {
            return GiftConfirmResponse.builder().orderId(orderId).isOutOfStock(false).build();
        } else {
            processItemVariantChanges(request, orderEntity, productDataResponse);
            return GiftConfirmResponse.builder().orderId(orderId).isOutOfStock(false).build();
        }

    }

    private void processItemVariantChanges(GiftConfirmRequest request, OrderEntity orderEntity,
                                           Map<String, Product> productDataResponse) {
        List<OrderItemEntity> availableOrderItem = OrderUtils.updatePriceFromCatalogService(productDataResponse,
                orderEntity.getOrderItemEntities());
        OrderPrice orderPrice = reCalculatePrice(availableOrderItem);
        int isOrderPriceChanged = orderEntity.getPrice().getTotalPrice().compareTo(orderPrice.getTotalPrice());
        if (isOrderPriceChanged == 0) {
            log.info("Receiver has chosen to get a variant equal to order price for orderId: {}",
                    orderEntity.getOrderId());
        } else if (isOrderPriceChanged < 0) {
            log.info("Receiver has chosen to get a variant with lower price for order: {}", orderEntity.getOrderId());
            BigDecimal priceDifference = orderEntity.getTotalAmount().subtract(orderPrice.getTotalPrice());
            orderEntity.setPrice(orderPrice);
            orderEntity.setOrderItemEntities(availableOrderItem);
            orderRepository.save(orderEntity);
            processWalletRefund(orderEntity.getSender(), priceDifference, orderEntity);
        } else {
            log.info("Receiver has chosen to get a variant with higher price for orderId: {}", orderEntity.getOrderId());
            BigDecimal extraPriceToBePaid = orderPrice.getTotalPrice().subtract(orderEntity.getTotalAmount());
            orderEntity.setReceiverTotalAmount(extraPriceToBePaid);
            orderPrice.setTotalPrice(extraPriceToBePaid);
            orderEntity.setReceiverOrderPrice(orderPrice);
            orderEntity.setOrderItemEntities(availableOrderItem);
            orderRepository.save(orderEntity);
        }
    }


    public List<CustomerOrderResponse> orderHistory(String userId) {
        List<ExternalStatus> externalStatus = ApplicationUtils.getUserOrderHistoryExternalStatus();
        List<OrderEntity> orderEntityList = orderRepository.findByUserIdAndExternalStatusIn(userId, externalStatus);
        Map<ExternalStatus, List<OrderEntity>> ordersByStatus = orderEntityList.stream()
                .collect(Collectors.groupingBy(OrderEntity::getExternalStatus));

        return ordersByStatus.entrySet().stream()
                .map(entry -> {
                    CustomerOrderResponse response = new CustomerOrderResponse();
                    List<CustomerOrderDetail> customerOrderDetails = entry.getValue().stream().map(order -> {
                        CustomerOrderDetail customerOrderDetail = new CustomerOrderDetail();
                        return customerOrderDetail.customerOrderDetail(order);
                    }).collect(Collectors.toList());
                    response.setStatus(entry.getKey().name());
                    response.setCustomerOrderDetails(customerOrderDetails);
                    response.setNoOfOrders(entry.getValue().size());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<OrderEntity> fetchOrderDetail(String orderId) {
        return orderService.findByOrderId(orderId);
    }

    public OrderEntity fetchSenderOrderDetail(String orderId) {
        OrderEntity orderEntity = orderService.fetchSenderOrder(orderId);
        if (orderEntity == null) {
            throw new BadRequestException("Invalid order Id in the request");
        }
        return orderEntity;
    }

    public OrderEntity fetchReceiverOrderDetail(String orderId) {
        OrderEntity orderEntity = orderService.fetchReceiverOrder(orderId);
        if (orderEntity == null) {
            throw new BadRequestException("Invalid order Id in the request");
        }
        return orderEntity;
    }

    private void changeTheOrderStatus(OrderEntity order, String orderChangeEvent) {
        stateMachineManager.moveToNextState(order, StateMachineType.RECEIVER.name(), orderChangeEvent);
        order.getOrderItemEntities().forEach(orderItemEntity ->
                stateMachineManager.moveToNextState(orderItemEntity, StateMachineType.RECEIVER.name(),
                        orderChangeEvent));
    }

    @Retryable(value = {OrderServiceException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    private void processWalletRefund(UserInfo userDetails, BigDecimal refundAmount, OrderEntity orderEntity) {
        try {
            WalletResponse walletResponse = walletService.processRefund(userDetails, refundAmount, orderEntity);
        } catch (Exception exception) {
            log.error("Error while processing wallet refund for the order:{} and for userId:{}",
                    orderEntity.getOrderId(), userDetails.getUserId());
            throw new OrderServiceException(exception.getMessage());
        }
    }

    private void createReceiver(OrderEntity orderEntity, VerifyGiftOtpRequest verifyGiftOtpRequest) {
        UserDetailRequest request = new UserDetailRequest();
        request.setFirstName(orderEntity.getReceiver().getFirstName());
        request.setLastName(orderEntity.getReceiver().getLastName());
        request.setPublicId(verifyGiftOtpRequest.getPublicId());
        request.setEmailId(orderEntity.getReceiverEmail());
        request.setCountryCode(verifyGiftOtpRequest.getCountryCode());
        request.setEmailVerificationRequired(false);
        UserDetailResponse userDetailResponse = customerService.updateUserDetails(request);
        UserInfo userDetails = orderEntity.getReceiver();
        userDetails.setFirstName(userDetailResponse.getData().getFirstName());
        userDetails.setLastName(userDetailResponse.getData().getLastName());
        userDetails.setUserId(userDetailResponse.getData().getPublicId());
        userDetails.setEmailId(userDetailResponse.getData().getEmailId());
        userDetails.setPhoneNumber(userDetailResponse.getData().getPhoneNumber());
        userDetails.setProfileUrl(userDetailResponse.getData().getProfileUrl());
        orderEntity.setReceiver(userDetails);
        orderService.save(orderEntity);
    }

    private Map<String, Product> getSellerSkuResponse(String orderId, OrderEntity orderEntity,
                                                      CheckItemDetailsRequest checkItemDetailsRequest) {
        Map<String, Product> productDataResponse = new HashMap<>();
        try {
            productDataResponse = inventoryServiceClient.fetchSellerSkuDetails(orderEntity.getCountryCode(), checkItemDetailsRequest);
        } catch (Exception exception) {
            log.error("Error while calling the catalog service to fetch teh item price info for orderId: {}, " +
                    "exception: {}", orderId, exception);
            throw new OrderServiceException("Unable to call catalog service");
        }
        return productDataResponse;
    }

}

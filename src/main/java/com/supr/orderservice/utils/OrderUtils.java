package com.supr.orderservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supr.orderservice.entity.GreetingCardEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.GreetingCard;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.ItemPrice;
import com.supr.orderservice.model.Misc;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.Product;
import com.supr.orderservice.model.ViewGiftItem;
import com.supr.orderservice.model.response.AcceptGiftResponse;
import com.supr.orderservice.model.response.FetchGreetingResponse;
import com.supr.orderservice.model.response.GiftSwapOptionsResponse;
import com.supr.orderservice.model.response.ItemDetailResponse;
import com.supr.orderservice.model.response.ProductDataResponse;
import com.supr.orderservice.model.response.ReceiverPlaceOrderResponse;
import com.supr.orderservice.model.response.SellerSkuResponse;
import com.supr.orderservice.model.response.SwapGiftResponse;
import com.supr.orderservice.model.response.ViewGiftResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static FetchGreetingResponse fetchGreetingResponse(GreetingCardEntity greetingCard, OrderEntity order) {
        return FetchGreetingResponse.builder().greetingMessage(greetingCard.getGreetingCardMsg())
                .from(ApplicationUtils.fetchFullName(order.getSender()))
                .to(ApplicationUtils.fetchFullName(order.getReceiver()))
                .senderName(order.getSender().getFirstName())
                .greetingEnvelopUrl(greetingCard.getGreetingEnvelopUrl())
                .greetingCardImage(greetingCard.getGreetingCardImage()).orderId(order.getOrderId()).build();
    }

    public static ViewGiftResponse fetchViewGiftResponse(OrderEntity order, List<OrderItemEntity> orderItemList) {
        return ViewGiftResponse.builder().senderName(order.getSender().getFirstName())
                .items(fetchViewGiftItem(orderItemList)).orderId(order.getOrderId()).build();
    }

    private static List<ViewGiftItem> fetchViewGiftItem(List<OrderItemEntity> orderItemList) {
        return orderItemList.stream().map(orderItemEntity -> ViewGiftItem.builder()
                .itemImage(orderItemEntity.getImages())
                .productBrand(orderItemEntity.getBrandId()).productId(orderItemEntity.getProductId())
                .productFullTitle(orderItemEntity.getProductFullTitle())
                .productTitle(orderItemEntity.getProductTitle()).pskuCode(orderItemEntity.getPskuCode())
                .quantity(orderItemEntity.getOrderItemQuantity())
                .description(orderItemEntity.getProductDescription()).warranty(orderItemEntity.getWarranty())
                .build()).collect(Collectors.toList());
    }

    public static ItemDetailResponse fetchItemDetails(OrderEntity orderEntity, ItemInfo itemInfo) {
        return ItemDetailResponse.builder().itemInfo(itemInfo).orderId(orderEntity.getOrderId()).build();
    }

    public static GiftSwapOptionsResponse fetchSwapOptions(OrderEntity orderEntity, List<ItemInfo> itemInfo) {
        return GiftSwapOptionsResponse.builder().itemInfos(itemInfo).orderId(orderEntity.getOrderId()).build();
    }

    public static List<OrderItemEntity> fetchOrderItemEntity(List<ItemInfo> items, OrderEntity order, int index) {
        List<OrderItemEntity> orderItems = new ArrayList<>();
        int suffix = index;
        for (ItemInfo cartInfo : items) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setCountryCode(order.getCountryCode());
            orderItem.setStatus(OrderItemStatus.PLACED);
            orderItem.setOrderItemId(order.getOrderId() + "-" + String.format("%03d", suffix++));
            orderItem.setPskuCode(cartInfo.getPsku());
            orderItem.setItemInfo(cartInfo);
            orderItem.setSellerId(orderItem.getSellerId());
            orderItem.setPrice(cartInfo.getItemPriceDetails());
            orderItem.setOrderItemQuantity(cartInfo.getQuantity());
            orderItem.setSellerId(cartInfo.getSellerId());
            orderItem.setBrandId(cartInfo.getBrandCode());
            orderItem.setProductId(cartInfo.getPsku());
            orderItem.setPartnerSku(cartInfo.getSkus());
            orderItem.setProductTitle(cartInfo.getGiftTitle());
            orderItem.setProductBrand(cartInfo.getBrandName());
            orderItem.setImages(cartInfo.getGiftImages());
            orderItem.setPrice(cartInfo.getItemPriceDetails());
            orderItem.setTotalPrice(cartInfo.getItemPriceDetails().getTotalPrice());
            orderItem.setCouponDetails(order.getCouponDetails());
            orderItem.setExternalStatus(ExternalStatus.GIFT_SWAPPED);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    public static OrderEntity modifyOrderForGiftItemSwap(OrderEntity order,
                                                         List<OrderItemEntity> orderItems) {
        order.setReceiverOrderPrice(reCalculatePrice(orderItems));
        order.setReceiverTotalAmount(fetchTotalAmountForOrder(order));
        return order;
    }

    private static BigDecimal fetchTotalAmountForOrder(OrderEntity order) {
        return order.getPrice().getTotalPrice();
    }

    public static OrderPrice reCalculatePrice(List<OrderItemEntity> orderItems) {
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal totalWalletPrice= new BigDecimal(0);
        BigDecimal finalPrice= new BigDecimal(0);
        BigDecimal totalTax= new BigDecimal(0);
        BigDecimal totalDiscount= new BigDecimal(0);
        BigDecimal totalCouponDiscount= new BigDecimal(0);
        BigDecimal totalShipping= new BigDecimal(0);

        for (OrderItemEntity orderItem : orderItems) {
            OrderPrice orderPrice = orderItem.getPrice();
            totalWalletPrice = totalWalletPrice.add(orderPrice.getTotalWalletPrice());
            totalPrice = totalPrice.add(orderPrice.getTotalPrice());
            finalPrice = finalPrice.add(orderPrice.getFinalPrice());
            totalDiscount = totalDiscount.add(orderPrice.getTotalDiscount());
            totalShipping = totalShipping.add(orderPrice.getTotalShipping());
            totalTax = totalTax.add(orderPrice.getTotalTax());
        }
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setTotalPrice(totalPrice);
        orderPrice.setFinalPrice(finalPrice);
        orderPrice.setTotalWalletPrice(totalWalletPrice);
        orderPrice.setTotalDiscount(totalDiscount);
        orderPrice.setTotalShipping(totalShipping);
        orderPrice.setTotalTax(totalTax);
        orderPrice.setTotalCouponDiscount(totalCouponDiscount);
        return orderPrice;
    }

    public static SwapGiftResponse getchSwapGiftResponse(OrderEntity savedOrderEntity,
                                                         List<OrderItemEntity> orderItems) {
        return SwapGiftResponse.builder().items(fetchItemInfos(orderItems)).orderId(savedOrderEntity.getOrderId())
                .isGiftSwapped(true).build();
    }

    public static List<ItemInfo> fetchItemInfos(List<OrderItemEntity> orderItems) {
        return orderItems.stream().map(orderItemEntity ->
                objectMapper.convertValue(orderItemEntity, ItemInfo.class)).collect(Collectors.toList());
    }

    public static AcceptGiftResponse fetchAcceptGiftResponse(List<OrderItemEntity> orderItems, OrderEntity orderEntity) {
        return AcceptGiftResponse.builder().items(fetchItemInfos(orderItems)).orderId(orderEntity.getOrderId())
                .shouldInitiateReceiverPayment(shouldInitiateReceiverPayment(orderEntity)).build();
    }

    private static boolean shouldInitiateReceiverPayment(OrderEntity orderEntity) {
        return orderEntity.getReceiverTotalAmount() != null &&
                orderEntity.getReceiverTotalAmount().compareTo(orderEntity.getTotalAmount()) > 0;
    }

    public static ReceiverPlaceOrderResponse fetchReceiverPlaceOrderResponse(boolean shouldTriggerReceiverPayment,
                                                                             OrderEntity order,
                                                                             List<OrderItemEntity> orderItems) {
        return ReceiverPlaceOrderResponse.builder().items(fetchItemInfos(orderItems))
                .shouldTriggerReceiverPayment(shouldTriggerReceiverPayment)
                .subTotalAmount(fetchSwappedAmount(order))
                .vat(order.getPrice().getTotalTax().toString())
                .swappedProductAmount(fetchSwappedAmount(order))
                .totalPrice(fetchTotalPrice(order)).build();
    }

    private static String fetchTotalPrice(OrderEntity order) {
        return order.getReceiverTotalAmount().subtract(order.getTotalAmount()).toString();
    }

    private static String fetchSwappedAmount(OrderEntity order) {
        return order.getReceiverTotalAmount().subtract(order.getTotalAmount()).toString();
    }

    public static GreetingCard fetchGreetingCard(GreetingCardEntity greetingCardEntity) {
        return objectMapper.convertValue(greetingCardEntity, GreetingCard.class);
    }

    public static String fetchTime(Timestamp updatedAtTimestamp) {
        Instant updatedAtInstant = updatedAtTimestamp.toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(updatedAtInstant, now);

        if (duration.toMinutes() < 1) {
            return "just now";
        } else if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (duration.toDays() < 1) {
            long hours = duration.toHours();
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else {
            long days = duration.toDays();
            return days + (days == 1 ? " day ago" : " days ago");
        }
    }

    public static ExternalStatus fetchExternalStatus(String status) {
        if ("OPEN".equalsIgnoreCase(status)) {
            return ExternalStatus.PLACED;
        } else if ("Shipped".equalsIgnoreCase(status)) {
            return ExternalStatus.SHIPPED;
        } else if ("Closed".equalsIgnoreCase(status)) {
            return ExternalStatus.CANCELLED;
        } else {
            return ExternalStatus.CREATED;
        }
    }

    public static List<OrderItemEntity> validateStock(Map<String, Product> productDataResponse,
                                                      List<OrderItemEntity> orderItemEntities) {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        Map<String, OrderItemEntity> orderItemEntityMap =
                orderItemEntities.stream().collect(Collectors.toMap(OrderItemEntity::getPskuCode, Function.identity()));
        Optional<Product> productOptional =
                productDataResponse.entrySet().stream().filter(entry -> entry.getValue().isInventoryTrack() &&
                                entry.getValue().getStock() >= orderItemEntityMap.get(entry.getKey()).getOrderItemQuantity().intValue())
                        .map(Map.Entry::getValue)
                        .findFirst();
        if (productOptional.isPresent()) {
            orderItemEntityList.add(orderItemEntityMap.get(productOptional.get().getSku()));
            throw new OrderServiceException("Stock not available for item");
        }
        return orderItemEntityList;
    }

    public static List<OrderItemEntity> updatePriceFromCatalogService(Map<String, Product> productDataResponse,
                                                                      List<OrderItemEntity> orderItemEntities) {
        List<OrderItemEntity> orderItemEntityList;
        Map<String, OrderItemEntity> orderItemEntityMap =
                orderItemEntities.stream().collect(Collectors.toMap(OrderItemEntity::getProductId, Function.identity()));
        orderItemEntityList = productDataResponse.values().stream().map(product -> {
            OrderItemEntity orderItem = orderItemEntityMap.get(product.getSku());
            orderItem.setTotalPrice(product.getMisc().getSalePrice().setScale(2, RoundingMode.HALF_UP));
            OrderPrice itemPrice = fetchOrderItemPrice(orderItem.getPrice(), product.getMisc());
            orderItem.setPrice(itemPrice);
            return orderItem;
        }).collect(Collectors.toList());
        return orderItemEntityList;

    }

    private static OrderPrice fetchOrderItemPrice(OrderPrice price, Misc misc) {
        price.setTotalPrice(misc.getSalePrice().setScale(2, RoundingMode.HALF_UP));
        price.setTotalTax(misc.getVatRate().setScale(2, RoundingMode.HALF_UP));
        price.setTotalPrice(misc.getSalePrice().setScale(2, RoundingMode.HALF_UP));
        price.setTotalShipping(misc.getShippingChargeExVat().setScale(2, RoundingMode.HALF_UP));
        return price;
    }
}

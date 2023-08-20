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
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.ViewGiftItem;
import com.supr.orderservice.model.response.AcceptGiftResponse;
import com.supr.orderservice.model.response.FetchGreetingResponse;
import com.supr.orderservice.model.response.GiftSwapOptionsResponse;
import com.supr.orderservice.model.response.ItemDetailResponse;
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
            orderItem.setPskuCode(cartInfo.getPskuCode());
            orderItem.setItemInfo(cartInfo);
            orderItem.setStoreId(orderItem.getStoreId());
            orderItem.setPrice(cartInfo.getItemPriceDetails());
            orderItem.setOrderItemQuantity(cartInfo.getQuantity());
            orderItem.setSellerId(order.getSellerId());
            orderItem.setBrandId(order.getBrandId());
            orderItem.setProductId(cartInfo.getProductId());
            orderItem.setParentProductId(cartInfo.getParentProductId());
            orderItem.setWarranty(cartInfo.getWarranty());
            orderItem.setPartnerSku(cartInfo.getPartnerSku());
            orderItem.setProductTitle(cartInfo.getProductTitle());
            orderItem.setProductColour(cartInfo.getProductColour());
            orderItem.setProductFullTitle(cartInfo.getProductFullTitle());
            orderItem.setProductBrand(cartInfo.getProductBrand());
            orderItem.setProductFamily(cartInfo.getProductFamily());
            orderItem.setProductSubtype(cartInfo.getProductSubtype());
            orderItem.setProductType(cartInfo.getProductType());
            orderItem.setImages(cartInfo.getImages());
            orderItem.setParentSku(cartInfo.getParentSku());
            orderItem.setPrice(cartInfo.getItemPriceDetails());
            orderItem.setTotalPrice(cartInfo.getTotalAmount());
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
        BigDecimal totalMrp = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal totalOfferPrice = new BigDecimal(0);
        BigDecimal totalDiscount = new BigDecimal(0);
        BigDecimal totalShipping = new BigDecimal(0);
        BigDecimal totalVat = new BigDecimal(0);
        BigDecimal totalCommission = new BigDecimal(0);
        BigDecimal totalBufferAmount = new BigDecimal(0);
        BigDecimal totalCouponDiscount = new BigDecimal(0);
        BigDecimal totalMerchantCouponDiscount = new BigDecimal(0);
        for (OrderItemEntity orderItem : orderItems) {
            OrderPrice orderPrice = orderItem.getPrice();
            totalMrp = totalMrp.add(orderPrice.getTotalMrp());
            totalPrice = totalPrice.add(orderPrice.getTotalPrice());
            totalOfferPrice = totalOfferPrice.add(orderPrice.getTotalOfferPrice());
            totalDiscount = totalDiscount.add(orderPrice.getTotalDiscount());
            totalShipping = totalShipping.add(orderPrice.getTotalShipping());
            totalVat = totalVat.add(orderPrice.getTotalVat());
            totalCommission = totalCommission.add(orderPrice.getTotalCommission());
            totalBufferAmount = totalBufferAmount.add(orderPrice.getTotalBufferAmount());
            totalCouponDiscount = totalCouponDiscount.add(orderPrice.getTotalCouponDiscount());
            totalMerchantCouponDiscount = totalMerchantCouponDiscount.add(orderPrice.getTotalMerchantCouponDiscount());
        }
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setTotalMrp(totalMrp);
        orderPrice.setTotalPrice(totalPrice);
        orderPrice.setTotalOfferPrice(totalOfferPrice);
        orderPrice.setTotalDiscount(totalDiscount);
        orderPrice.setTotalShipping(totalShipping);
        orderPrice.setTotalVat(totalVat);
        orderPrice.setTotalCommission(totalCommission);
        orderPrice.setTotalBufferAmount(totalBufferAmount);
        orderPrice.setTotalCouponDiscount(totalCouponDiscount);
        orderPrice.setTotalMerchantCouponDiscount(totalMerchantCouponDiscount);
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
                .vat(order.getPrice().getTotalVat().toString())
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

    public static List<OrderItemEntity> validateStock(List<SellerSkuResponse> sellerSkuResponses,
                                                      List<OrderItemEntity> orderItemEntities) {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        Map<String, OrderItemEntity> orderItemEntityMap =
                orderItemEntities.stream().collect(Collectors.toMap(OrderItemEntity::getPskuCode, Function.identity()));
        Optional<SellerSkuResponse> sellerSkuResponseOptional =
                sellerSkuResponses.stream().filter(sellerSkuResponse -> sellerSkuResponse.getOffers().isInventoryTrack() &&
                        orderItemEntityMap.get(sellerSkuResponse.getSku()).getOrderItemQuantity().intValue() >
                                sellerSkuResponse.getOffers().getStock()).findFirst();
        if (sellerSkuResponseOptional.isPresent()) {
            orderItemEntityList.add(orderItemEntityMap.get(sellerSkuResponseOptional.get().getSku()));
            throw new OrderServiceException("Stock not available for item");
        }
        return orderItemEntityList;
    }

    public static List<OrderItemEntity> updatePriceFromCatalog(List<SellerSkuResponse> sellerSkuResponses,
                                                               List<ItemInfo> orderItemEntities) {
        List<OrderItemEntity> orderItemEntityList;
        Map<String, ItemInfo> orderItemEntityMap =
                orderItemEntities.stream().collect(Collectors.toMap(ItemInfo::getPskuCode, Function.identity()));
        orderItemEntityList = sellerSkuResponses.stream().map(sellerSkuResponse -> {
            OrderItemEntity orderItem = new OrderItemEntity();
            ItemInfo itemInfo = orderItemEntityMap.get(sellerSkuResponse.getOffers().getPskuCode());
            orderItem = objectMapper.convertValue(itemInfo,OrderItemEntity.class);
            orderItem.setTotalPrice(sellerSkuResponse.getOffers().getMisc().getSalePrice().setScale(2, RoundingMode.HALF_UP));
            OrderPrice itemPrice = fetchOrderItemPrice(orderItem.getPrice(), sellerSkuResponse.getOffers().getMisc());
            orderItem.setPrice(itemPrice);
            orderItem.setStatus(OrderItemStatus.ANOTHER_VARIANT);
            return orderItem;
        }).collect(Collectors.toList());
        return orderItemEntityList;

    }

    public static List<OrderItemEntity> updatePriceFromCatalogService(List<SellerSkuResponse> sellerSkuResponses,
                                                               List<OrderItemEntity> orderItemEntities) {
        List<OrderItemEntity> orderItemEntityList;
        Map<String, OrderItemEntity> orderItemEntityMap =
                orderItemEntities.stream().collect(Collectors.toMap(OrderItemEntity::getPskuCode, Function.identity()));
        orderItemEntityList = sellerSkuResponses.stream().map(sellerSkuResponse -> {
            OrderItemEntity orderItem = orderItemEntityMap.get(sellerSkuResponse.getOffers().getPskuCode());
            orderItem.setTotalPrice(sellerSkuResponse.getOffers().getMisc().getSalePrice().setScale(2, RoundingMode.HALF_UP));
            OrderPrice itemPrice = fetchOrderItemPrice(orderItem.getPrice(), sellerSkuResponse.getOffers().getMisc());
            orderItem.setPrice(itemPrice);
            return orderItem;
        }).collect(Collectors.toList());
        return orderItemEntityList;

    }

    private static OrderPrice fetchOrderItemPrice(OrderPrice price, ItemPrice itemPrice) {
        price.setTotalPrice(itemPrice.getSalePrice().setScale(2, RoundingMode.HALF_UP));
        price.setTotalVat(itemPrice.getVatRate().setScale(2, RoundingMode.HALF_UP));
        price.setTotalPrice(itemPrice.getSalePrice().setScale(2, RoundingMode.HALF_UP));
        price.setTotalShipping(itemPrice.getShippingChargeExVat().setScale(2, RoundingMode.HALF_UP));
        return price;
    }
}

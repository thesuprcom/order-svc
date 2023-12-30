package com.supr.orderservice.entity;

import com.supr.orderservice.enums.GreetingCardStatus;
import com.supr.orderservice.model.ImageUrl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@Audited
@Builder
@NoArgsConstructor
@Table(name = "order_greeting_card_item")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GreetingCardEntity extends BaseEntity{

    @Column(name = "greeting_card_code")
    private String greetingCardCode;
    @Column(name = "sender_name")
    private String senderName;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_first_name")
    private String receiverFirstName;
    @Column(name = "receiver_last_name")
    private String receiverLastName;
    @Column(name = "greeting_card_occasion")
    private String greetingCardOccasion;
    @Column(name = "greeting_card_msg")
    private String greetingCardMsg;
    @Type(type = "json")
    @Column(name = "gift_image",columnDefinition = "json")
    private ImageUrl greetingCardImage;
    @Enumerated(EnumType.STRING)
    @Column(name = "greeting_card_status")
    private GreetingCardStatus greetingCardStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

}

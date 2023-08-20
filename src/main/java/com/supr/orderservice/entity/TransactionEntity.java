package com.supr.orderservice.entity;

import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.TransactionStatus;
import com.supr.orderservice.enums.TransactionType;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Audited
@Builder
@NoArgsConstructor
@Table(name = "transaction_data")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionEntity extends BaseEntity {

    @NaturalId
    private String transactionId;

    private BigDecimal amount;

    private BigDecimal amountPayable;

    private String currency;

    @NotAudited
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private OrderEntity order;

    private Long pgOrderId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentModeSelected;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private PaymentGatewayResponse paymentGatewayResponse;
}


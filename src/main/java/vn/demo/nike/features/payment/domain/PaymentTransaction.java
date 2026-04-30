package vn.demo.nike.features.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import vn.demo.nike.features.order.domain.Order;
import vn.demo.nike.features.payment.domain.enums.PaymentProvider;
import vn.demo.nike.features.payment.domain.enums.PaymentStatus;
import vn.demo.nike.shared.domain.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment_transaction",
        indexes = {
                @Index(name = "idx_txn_ref", columnList = "txnRef"),
                @Index(name = "idx_order_id", columnList = "order_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_txn_ref", columnNames = "txnRef")
        }
)
public class PaymentTransaction extends BaseEntity {

    /**
     * Internal order reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    /**
     * vnp_TxnRef (unique per request)
     */
    @Column(nullable = false, length = 100)
    private String txnRef;

    /**
     * Amount in VND
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Internal business status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    /**
     * vnp_ResponseCode
     */
    @Column(length = 10)
    private String responseCode;

    /**
     * vnp_TransactionStatus
     */
    @Column(length = 10)
    private String transactionStatus;

    /**
     * vnp_TransactionNo (VNPay side)
     */
    @Column(length = 20)
    private String transactionNo;

    /**
     * vnp_BankCode
     */
    @Column(length = 20)
    private String bankCode;

    /**
     * vnp_PayDate
     */
    private LocalDateTime payDate;

    /**
     * vnp_ExpireDate
     */
    private LocalDateTime expireDate;

    /**
     * IP của client (fraud/debug)
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * Idempotency flag cho IPN
     */
    @Column(nullable = false)
    private boolean ipnProcessed;

    /**
     * Raw data (audit/debug)
     */
    @Lob
    private String rawRequestPayload;

    @Lob
    private String rawResponsePayload;

    /**
     * Lý do fail (mapping từ responseCode)
     */
    private String failureReason;
}

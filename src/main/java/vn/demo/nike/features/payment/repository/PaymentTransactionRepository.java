package vn.demo.nike.features.payment.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import vn.demo.nike.features.payment.domain.PaymentTransaction;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       select pt
       from PaymentTransaction pt
       where pt.txnRef = :txnRef
       """)
    Optional<PaymentTransaction> findByTxnRef(String txnRef);

    List<PaymentTransaction> findByOrder_IdOrderByCreateDateDesc(long order_Id);
}

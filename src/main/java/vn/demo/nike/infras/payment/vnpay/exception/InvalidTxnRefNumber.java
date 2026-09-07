package vn.demo.nike.infras.payment.vnpay.exception;

public class InvalidTxnRefNumber extends RuntimeException {
    public InvalidTxnRefNumber(int random) {
        super(String.format("Invalid TxnRefNumber: %d", random));
    }
}

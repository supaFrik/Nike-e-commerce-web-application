package vn.demo.nike.infras.payment.vnpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class VNPayIpnResponse {
    private final String RspCode;
    private final String Message;
}

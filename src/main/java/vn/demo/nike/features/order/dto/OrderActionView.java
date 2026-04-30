package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderActionView {
    String label;
    String href;
    String kind;
    boolean available;
    String hint;
}
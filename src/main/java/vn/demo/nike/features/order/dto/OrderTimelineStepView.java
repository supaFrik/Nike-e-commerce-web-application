package vn.demo.nike.features.order.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderTimelineStepView {
    String label;
    String description;
    boolean completed;
    boolean current;
}

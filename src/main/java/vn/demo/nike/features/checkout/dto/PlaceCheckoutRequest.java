package vn.demo.nike.features.checkout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceCheckoutRequest {
    @NotNull
    private String paymentMethod;

    @NotNull
    private String shippingMethod;

    @Size(max = 500)
    private String note;

    private Long addressId;

    @NotBlank
    @Size(max = 255)
    private String recipientName;

    @NotBlank
    @Size(max = 50)
    private String phone;

    @NotBlank
    @Size(max = 255)
    private String line1;

    @Size(max = 255)
    private String line2;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @Size(max = 30)
    private String postalCode;

    @NotBlank
    @Size(max = 100)
    private String country;
}

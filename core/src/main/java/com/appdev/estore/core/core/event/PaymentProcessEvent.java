package com.appdev.estore.core.core.event;

import com.appdev.estore.core.core.payment.PaymentDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentProcessEvent {

    private String paymentId;
    private String orderId;
    private PaymentDetails paymentDetails;

}

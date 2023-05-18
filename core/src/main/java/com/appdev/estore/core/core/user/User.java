package com.appdev.estore.core.core.user;

import com.appdev.estore.core.core.payment.PaymentDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;
}

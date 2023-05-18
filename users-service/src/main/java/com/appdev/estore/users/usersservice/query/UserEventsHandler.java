package com.appdev.estore.users.usersservice.query;

import com.appdev.estore.core.core.data.FetchUserPaymentDetailsQuery;
import com.appdev.estore.core.core.payment.PaymentDetails;
import com.appdev.estore.core.core.user.User;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {


    @QueryHandler
    public User returnUserDetails(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery) {

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("CardNumber")
                .cvv("333")
                .name("ANDREI RAMANOUSKI")
                .validUntilMonth(06)
                .validUntilYear(2025)
                .build();

        User userRest = User.builder()
                .firstName("Andrei")
                .lastName("Ramanouski")
                .userId(fetchUserPaymentDetailsQuery.getUserId())
                .paymentDetails(paymentDetails)
                .build();
        return userRest;
    }
}

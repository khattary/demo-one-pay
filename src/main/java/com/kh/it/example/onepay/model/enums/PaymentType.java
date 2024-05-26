package com.kh.it.example.onepay.model.enums;

import lombok.Getter;

@Getter
public enum PaymentType {
	
	CREDIT_CARD("Credit Card"),
	GIFT_CARD("Gift Card"),
	PAYPAL("PayPal");

    private final String value;
    
    PaymentType(String value) {
        this.value = value;
    }
}


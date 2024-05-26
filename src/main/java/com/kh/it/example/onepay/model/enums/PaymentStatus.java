package com.kh.it.example.onepay.model.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
	
	NEW("New"),
	AUTHORIZED("Authorized"),
	CAPTURED("Captured");

    private final String value;
    
    PaymentStatus(String value) {
        this.value = value;
    }
}

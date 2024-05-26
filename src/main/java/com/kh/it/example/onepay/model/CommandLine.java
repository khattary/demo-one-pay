package com.kh.it.example.onepay.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="COMMAND_LINES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandLine {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal productPrice;
	
	@Column(nullable = false)
	private Integer quantity;

}

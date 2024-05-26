package com.kh.it.example.onepay.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="COMMANDS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CommandLine> commandLines = new ArrayList<>();
	
	private BigDecimal totalPrice;
	
	@PrePersist
    public void updateTotalPrice() {
        totalPrice = commandLines.stream()
        		.map(cd-> cd.getProductPrice()
        				.multiply(BigDecimal.valueOf(cd.getQuantity())))
        		.reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

package com.kh.it.example.onepay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.it.example.onepay.model.Transaction;
import com.kh.it.example.onepay.service.TransactionService;

@RequestMapping("/one-pay/transactions")
@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/")
	public ResponseEntity<Iterable<Transaction>> getTransactions() {
		return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") long id) {
		return new ResponseEntity<>(transactionService.findTransactionById(id), HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
		try {
			return new ResponseEntity<>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") long id,
			@RequestBody Transaction transaction) {
		transaction.setId(id);
		return new ResponseEntity<>(transactionService.updateTransaction(transaction), HttpStatus.OK);
	}

}

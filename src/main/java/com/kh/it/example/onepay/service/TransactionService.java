package com.kh.it.example.onepay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.it.example.onepay.exception.ResourceNotFoundException;
import com.kh.it.example.onepay.exception.UnauthorizedResourceModificationException;
import com.kh.it.example.onepay.model.Transaction;
import com.kh.it.example.onepay.model.enums.PaymentStatus;
import com.kh.it.example.onepay.repository.CommandRepository;
import com.kh.it.example.onepay.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private CommandRepository commandRepository;

	/**
	 * Return all transactions in DB
	 * 
	 * @return Iterable<Transaction> of all DB transaction
	 */
	public Iterable<Transaction> getAllTransactions() {
		return this.transactionRepository.findAll();
	}

	/**
	 * Create a new Transaction
	 * 
	 * @param transaction to be created
	 * @return db created transaction
	 */
	public Transaction createTransaction(Transaction transaction) {
		transaction.setId(null);
		transaction.setStatus(PaymentStatus.NEW);

		if (null != transaction.getCommand()) {
			transaction.setCommand(commandRepository.save(transaction.getCommand()));
		}

		return this.transactionRepository.save(transaction);
	}

	/**
	 * Update a Transaction retrieved by its id.
	 * 
	 * @param transaction to be updated
	 * @return db updated transaction
	 * @throws ResourceNotFoundException 
	 * 									if transaction not found,
	 *         UnauthorizedResourceModificationException
	 *                                   if it is not possible to change the status
	 */
	public Transaction updateTransaction(Transaction transaction) {
		Transaction currentTransaction = transactionRepository.findById(transaction.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Transaction not found with id: " + transaction.getId()));

		if (!transaction.getStatus().equals(currentTransaction.getStatus())) {
			if (PaymentStatus.CAPTURED.equals(currentTransaction.getStatus())) {
				throw new UnauthorizedResourceModificationException(
						"it is not possible to change the status of a CAPTURED transaction");
			}
			if (PaymentStatus.CAPTURED.equals(transaction.getStatus())
					&& !PaymentStatus.AUTHORIZED.equals(currentTransaction.getStatus())) {
				throw new UnauthorizedResourceModificationException(
						"it is not possible to change the transaction status to CAPTURED if the transaction is not in AUTHORIZED status.");
			}
		}

		applyTransactionChanges(transaction, currentTransaction);

		return this.transactionRepository.save(currentTransaction);
	}

	/**
	 * Retrieves a transaction by its id.
	 * 
	 * @param id of transaction
	 * @return Transaction
	 * @throws ResourceNotFoundException if not found
	 */
	public Transaction findTransactionById(Long id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
	}

	private void applyTransactionChanges(Transaction transaction, Transaction currentTransaction) {
		currentTransaction
				.setStatus(transaction.getStatus() != null ? transaction.getStatus() : currentTransaction.getStatus());
		currentTransaction
				.setType(transaction.getType() != null ? transaction.getType() : currentTransaction.getType());

	}

}

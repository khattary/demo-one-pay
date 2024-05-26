package com.kh.it.example.onepay.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kh.it.example.onepay.exception.UnauthorizedResourceModificationException;
import com.kh.it.example.onepay.model.Command;
import com.kh.it.example.onepay.model.CommandLine;
import com.kh.it.example.onepay.model.Transaction;
import com.kh.it.example.onepay.model.enums.PaymentStatus;
import com.kh.it.example.onepay.model.enums.PaymentType;
import com.kh.it.example.onepay.repository.CommandRepository;
import com.kh.it.example.onepay.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private CommandRepository commandRepository;
	
	@InjectMocks
	private TransactionService transactionService;
	
	@Captor
	ArgumentCaptor<Transaction> transactionCaptor;
	
	@Captor
	ArgumentCaptor<Command> commandCaptor;

	@Test
	public void updateTransaction_shouldThrows_when_newStatusIs_CAPTURED_and_currentStatusIsNot_AUTHORIZED() {
		Transaction bdTransaction = jUnitTransaction(1L, PaymentStatus.NEW, PaymentType.PAYPAL);
		Transaction updatedTransaction = jUnitTransaction(1L, PaymentStatus.CAPTURED, PaymentType.PAYPAL);

		when(transactionRepository.findById(1L)).thenReturn(Optional.of(bdTransaction));

		assertThrows(UnauthorizedResourceModificationException.class,
				() -> transactionService.updateTransaction(updatedTransaction),
				"it is not possible to change the transaction status to CAPTURED if the transaction is not in AUTHORIZED status");
	}

	@Test
	public void updateTransaction_shouldThrows_when_changeTransactionStatus_given_CAPTURED_Transaction() {
		Transaction bdTransaction = jUnitTransaction(1L, PaymentStatus.CAPTURED, PaymentType.PAYPAL);
		Transaction updatedTransaction = jUnitTransaction(1L, PaymentStatus.AUTHORIZED, PaymentType.PAYPAL);

		when(transactionRepository.findById(1L)).thenReturn(Optional.of(bdTransaction));

		assertThrows(UnauthorizedResourceModificationException.class,
				() -> transactionService.updateTransaction(updatedTransaction),
				"it is not possible to change the status of a CAPTURED transaction");
	}
	
	@Test
	public void updateTransaction_shouldUpdateTransactionStatusAndType() {
		Transaction bdTransaction = jUnitTransaction(1L, PaymentStatus.AUTHORIZED, PaymentType.CREDIT_CARD);
		Transaction updatedTransaction = jUnitTransaction(1L, PaymentStatus.CAPTURED, PaymentType.PAYPAL);
		
		when(transactionRepository.findById(1L)).thenReturn(Optional.of(bdTransaction));
		when(transactionRepository.save(transactionCaptor.capture())).thenReturn(bdTransaction);
		
		transactionService.updateTransaction(updatedTransaction);
		
		assertThat(transactionCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.CAPTURED);
		assertThat(transactionCaptor.getValue().getType()).isEqualTo(PaymentType.PAYPAL);
		
	}
	
	@Test
	public void createTransaction_shouldCreateTranscationInNewStatus_and_ItsCommand() {
		Transaction transaction = jUnitTransaction(null, null, PaymentType.PAYPAL);
		
		when(transactionRepository.save(transactionCaptor.capture())).thenReturn(transaction);
		when(commandRepository.save(commandCaptor.capture())).thenReturn(new Command());
		
		transactionService.createTransaction(transaction);
		
		assertThat(transactionCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.NEW);
		assertThat(transactionCaptor.getValue().getType()).isEqualTo(PaymentType.PAYPAL);
		
		assertThat(commandCaptor.getValue().getTotalPrice()).isEqualTo(BigDecimal.valueOf(497.5));
		assertThat(commandCaptor.getValue().getCommandLines().size()).isEqualTo(2);
	}

	private Transaction jUnitTransaction(Long id, PaymentStatus status, PaymentType type) {
		return new Transaction(id, type, status, jUnitCommand(), null);
	}

	private Command jUnitCommand() {

		List<CommandLine> commandLines = List.of(
				new CommandLine(null, "Vélo", BigDecimal.valueOf(120.5), 4),
				new CommandLine(null, "Gants", BigDecimal.valueOf(15.5), 1));

		return new Command(null, commandLines, BigDecimal.valueOf(497.5));
	}

}

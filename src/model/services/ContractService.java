package model.services;

import java.time.LocalDate;

import model.entities.Contract;
import model.entities.Installment;

public class ContractService {

	private OnlinePaymentService onlinePaymentService;
	
	public ContractService(OnlinePaymentService onlinePaymentService) {
		this.onlinePaymentService = onlinePaymentService;
	}

	public void processContract(Contract contract, int months) {
		
		double basicQuota = contract.getTotalValue() / months;
		for(int i = 1; i <= months; i++) {
			LocalDate dueDate = contract.getDate().plusMonths(i);
			
			//juros
			double interest = onlinePaymentService.interest(basicQuota, i);
			
			//taxa de pagamento
			double fee = onlinePaymentService.paymentFee(basicQuota + interest);
			
			//valor da parcela
			double quota = basicQuota + interest + fee;
			
			contract.getInstalments().add(new Installment(dueDate, quota));
		}
	}
	
}

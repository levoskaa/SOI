package banking;

import banking.Banking.ChargeCardRequest;
import banking.Banking.ChargeCardResponse;

public class BankingService implements IBankingService {
    @Override
    public ChargeCardResponse chargeCard(ChargeCardRequest request) {
	boolean result = false;
	if (request.getAmount() > 0
		&& request.getCardNumber().length() % 2 == 0) {
	    result = true;
	}
	return ChargeCardResponse.newBuilder().setSuccess(result).build();
    }
}

package banking;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import banking.Banking.ChargeCardRequest;
import banking.Banking.ChargeCardResponse;

@Consumes({ "application/json", "application/x-protobuf" })
@Produces({ "application/json", "application/x-protobuf" })
public interface IBankingService {
    @POST
    @Path("ChargeCard")
    ChargeCardResponse chargeCard(ChargeCardRequest request);
}

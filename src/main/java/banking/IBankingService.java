package banking;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("banking")
@Consumes("application/x-protobuf")
@Produces("application/x-protobuf")
public interface IBankingService {
    @POST
    @Path("ChargeCard")
    Response ChargeCard(String cardNumber, int amount);
}

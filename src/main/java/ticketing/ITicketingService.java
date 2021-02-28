package ticketing;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("ticketing")
@Consumes("application/x-protobuf")
@Produces("application/x-protobuf")
public interface ITicketingService {
    @POST
    @Path("GetMovies")
    Response getMovies(int year);

    @POST
    @Path("BuyTickets")
    Response buyTickets(int movieId, int count, String cardNumber);

    @POST
    @Path("GetTickets")
    Response getTickets();
}

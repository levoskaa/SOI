package ticketing;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ticketing.Ticketing.BuyTicketsRequest;
import ticketing.Ticketing.BuyTicketsResponse;
import ticketing.Ticketing.GetMoviesRequest;
import ticketing.Ticketing.GetMoviesResponse;
import ticketing.Ticketing.GetTicketsRequest;
import ticketing.Ticketing.GetTicketsResponse;

@Consumes({ "application/json", "application/x-protobuf" })
@Produces({ "application/json", "application/x-protobuf" })
public interface ITicketingService {
    @POST
    @Path("GetMovies")
    GetMoviesResponse getMovies(GetMoviesRequest request);

    @POST
    @Path("BuyTickets")
    BuyTicketsResponse buyTickets(BuyTicketsRequest request);

    @POST
    @Path("GetTickets")
    GetTicketsResponse getTickets(GetTicketsRequest request);
}

package ticketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import banking.Banking.ChargeCardRequest;
import banking.Banking.ChargeCardResponse;
import banking.IBankingService;
import movies.IMoviesService;
import movies.Movies;
import movies.Movies.MovieIdList;
import ticketing.Ticketing.BuyTicketsRequest;
import ticketing.Ticketing.BuyTicketsResponse;
import ticketing.Ticketing.GetMoviesRequest;
import ticketing.Ticketing.GetMoviesResponse;
import ticketing.Ticketing.GetTicketsRequest;
import ticketing.Ticketing.GetTicketsResponse;
import ticketing.Ticketing.Ticket;

public class TicketingService implements ITicketingService {
    private static String BankingAddress;
    private static String MoviesAddress;
    private static Map<Integer, Integer> soldTickets = new HashMap<>();

    private static String getBankingAddress() {
	if (BankingAddress == null) {
	    // Load the address of the banking service from the configuration
	    // file
	    BankingAddress = System.getProperty("microservices.banking.url");
	}
	return BankingAddress;
    }

    private static String getMoviesAddress() {
	if (MoviesAddress == null) {
	    // Load the address of the banking service from the configuration
	    // file
	    MoviesAddress = System.getProperty("microservices.movies.url");
	}
	return MoviesAddress;
    }

    private IBankingService getBankingService() {
	String bankingAddress = TicketingService.getBankingAddress();
	if (bankingAddress == null)
	    return null;
	try {
	    // Create the Resteasy provider:
	    ResteasyProviderFactory instance = ResteasyProviderFactory
		    .getInstance();
	    ResteasyClient client = new ResteasyClientBuilder()
		    .providerFactory(instance).build();
	    ResteasyWebTarget target = client.target(bankingAddress);
	    // Get a typed interface:
	    IBankingService bankingService = target
		    .proxy(IBankingService.class);
	    return bankingService;
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return null;
    }

    private IMoviesService getMoviesService() {
	String moviesAddress = TicketingService.getMoviesAddress();
	if (moviesAddress == null)
	    return null;
	try {
	    // Create the Resteasy provider:
	    ResteasyProviderFactory instance = ResteasyProviderFactory
		    .getInstance();
	    ResteasyClient client = new ResteasyClientBuilder()
		    .providerFactory(instance).build();
	    ResteasyWebTarget target = client.target(moviesAddress);
	    // Get a typed interface:
	    IMoviesService moviesService = target.proxy(IMoviesService.class);
	    return moviesService;
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return null;
    }

    @Override
    public GetMoviesResponse getMovies(GetMoviesRequest request) {
	IMoviesService moviesService = getMoviesService();
	MovieIdList movieIds = moviesService.findMovie(request.getYear(),
		"Title");
	List<Ticketing.Movie> result = new ArrayList<>();
	for (int id : movieIds.getIdList()) {
	    Movies.Movie movie = moviesService.getMovieById(id);
	    Ticketing.Movie ticketingMovie = Ticketing.Movie.newBuilder()
		    .setId(id).setTitle(movie.getTitle()).build();
	    result.add(ticketingMovie);
	}
	return GetMoviesResponse.newBuilder().addAllMovie(result).build();
    }

    @Override
    public BuyTicketsResponse buyTickets(BuyTicketsRequest request) {
	IBankingService bankingService = getBankingService();
	ChargeCardRequest payRequest = ChargeCardRequest.newBuilder()
		.setCardNumber(request.getCardNumber())
		.setAmount(request.getCount() * 10).build();
	ChargeCardResponse payResponse = bankingService.chargeCard(payRequest);
	int movieId = request.getMovieId();
	int newCount = request.getCount();
	if (payResponse.getSuccess()) {
	    if (soldTickets.containsKey(movieId)) {
		newCount += soldTickets.get(movieId);
	    }
	    soldTickets.put(movieId, newCount);
	}
	return BuyTicketsResponse.newBuilder()
		.setSuccess(payResponse.getSuccess()).build();
    }

    @Override
    public GetTicketsResponse getTickets(GetTicketsRequest request) {
	List<Ticket> tickets = soldTickets.entrySet().stream()
		.map(entry -> Ticket.newBuilder().setMovieId(entry.getKey())
			.setCount(entry.getValue()).build())
		.collect(Collectors.toList());
	return GetTicketsResponse.newBuilder().addAllTicket(tickets).build();
    }
}

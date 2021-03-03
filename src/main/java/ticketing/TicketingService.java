package ticketing;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import banking.IBankingService;
import movies.IMoviesService;
import ticketing.Ticketing.BuyTicketsRequest;
import ticketing.Ticketing.BuyTicketsResponse;
import ticketing.Ticketing.GetMoviesRequest;
import ticketing.Ticketing.GetMoviesResponse;
import ticketing.Ticketing.GetTicketsRequest;
import ticketing.Ticketing.GetTicketsResponse;

public class TicketingService implements ITicketingService {
    private static String BankingAddress;
    private static String MoviesAddress;

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
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public BuyTicketsResponse buyTickets(BuyTicketsRequest request) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public GetTicketsResponse getTickets(GetTicketsRequest request) {
	// TODO Auto-generated method stub
	return null;
    }
}

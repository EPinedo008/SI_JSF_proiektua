package eredua.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import businessLogic.BLFacade;
import domain.Driver;
import domain.EgoeraRideRequest;
import domain.Profile;
import domain.Ride;
import domain.RideRequest;
import domain.Traveller;

@Named("rideRequests")
@ViewScoped
public class RideRequestsBean implements Serializable {


    private BLFacade blfacade;
    private List<RideRequest> requests;
    private RideRequest selectedRequest;
    private EgoeraRideRequest state;

    @Inject
    private UserBean userBean;

    private Profile user;

    public RideRequestsBean() {}

    @PostConstruct
    public void init() {
        blfacade = FacadeBean.getBusinessLogic();
        user = userBean.getUser();
        loadRequests();
    }

    public void loadRequests() {
        if (user instanceof Traveller) {
            requests = blfacade.getRideRequestsTraveller(user);
        } else if (user instanceof Driver) {
        	 List<RideRequest> erreserbak = new ArrayList<>();
             List<RideRequest> requestsOfRide;

             for (Ride ride : blfacade.getRidesDriver((Driver) user)) {
                 requestsOfRide = new ArrayList<>();
                 for (RideRequest request : blfacade.getRidesRequestsOfRide(ride)) {
                     if (request.getState().equals(EgoeraRideRequest.TRATATU_GABE)) {
                         requestsOfRide.add(request);
                     }
                 }
                 Collections.sort(requestsOfRide, Collections.reverseOrder());
                 erreserbak.addAll(requestsOfRide);
             }

             requests = erreserbak;
             if (!requests.isEmpty()) {
                 selectedRequest = requests.get(0);
             }
        }
    }

    public void acceptRequest(RideRequest r) {
        blfacade.onartuEdoDeuseztatuErreserba(r,true);
        loadRequests();
    }

    public void denyRequest(RideRequest r) {
    	blfacade.onartuEdoDeuseztatuErreserba(r,false);
        loadRequests();
    }

    public List<RideRequest> getRequests() {
        return requests;
    }

    public RideRequest getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(RideRequest selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

	public BLFacade getBlfacade() {
		return blfacade;
	}

	public void setBlfacade(BLFacade blfacade) {
		this.blfacade = blfacade;
	}

	public EgoeraRideRequest getState() {
		return state;
	}

	public void setState(EgoeraRideRequest state) {
		this.state = state;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public Profile getUser() {
		return user;
	}

	public void setUser(Profile user) {
		this.user = user;
	}

	public void setRequests(List<RideRequest> requests) {
		this.requests = requests;
	}
    
    
    
}

package eredua.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Profile;
import domain.Ride;
import domain.RideRequest;

@Named("rides")
@ViewScoped
public class RidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private BLFacade blfacade;
    private List<Ride> rides; 
    private List<RideRequest> requests;
    private Ride selectedRide;

    @Inject
    private UserBean userBean;

    private Profile user;

    public RidesBean() {}

    @PostConstruct
    public void init() {
        blfacade = FacadeBean.getBusinessLogic();
        user = userBean.getUser();

        if (user instanceof Driver) {
            loadDriverRides();
        } else {
            loadTravellerRequests();
        }
    }

    private void loadDriverRides() {
    	user = userBean.getUser();
        rides = blfacade.getRidesDriver((Driver) user);
    }

    private void loadTravellerRequests() {
    	user = userBean.getUser();
        requests = blfacade.getRideRequestsTraveller(user);
    }

    public void selectRide(Ride r) {
        selectedRide = r;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public List<RideRequest> getRequests() {
        return requests;
    }

    public Ride getSelectedRide() {
        return selectedRide;
    }

    public Profile getUser() {
        return user;
    }

    public void refresh() {
        if (user instanceof Driver) {
            loadDriverRides();
        } else {
            loadTravellerRequests();
        }
    }
}

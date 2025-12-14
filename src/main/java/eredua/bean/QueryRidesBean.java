package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Ride;
import domain.RideRequest;
import domain.Traveller;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("queryRides")
@SessionScoped
public class QueryRidesBean implements Serializable {
    private List<Ride> rides;
    private BLFacade blfacade;
    private String departCity;
    private String arrivalCity;
    private Date data;
    private List<String> departCities = new ArrayList<>();
    private List<String> arrivalCities = new ArrayList<>();
    private Ride selectedRide;
    private Traveller traveller;
    private Integer seats;
    @Inject
    private UserBean userBean;
    
    private List<Ride> ridesGidariData;
    private List<Driver> gidariak;
    private Driver selectDriver;
    

    public QueryRidesBean() {
        blfacade = FacadeBean.getBusinessLogic();
        data = new Date();
       // traveller = (Traveller) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    public List<Ride> getRides() {
        if (departCity == null || arrivalCity == null || data == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eremu bat hutsa dago"));
            return null;
        } else {
            rides = blfacade.getRides(departCity, arrivalCity, data);
            return rides;
        }
    }

    public List<String> getDepartCities() {
        departCities = blfacade.getDepartCities();
        if (departCity == null && !departCities.isEmpty()) {
            departCity = departCities.get(0);
        }
        return departCities;
    }

    public List<String> getArrivalCities() {
        if (departCity != null) {
            arrivalCities = blfacade.getDestinationCities(departCity);
            if (!arrivalCities.isEmpty()) {
                arrivalCity = arrivalCities.get(0);
            }
        }
        return arrivalCities;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDepartCity() {
        return departCity;
    }

    public void setDepartCity(String departCity) {
        this.departCity = departCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public void onDateSelect(SelectEvent event) {
        getRides();
    }

    public Ride getSelectedRide() {
        return selectedRide;
    }

    public void setSelectedRide(Ride selectedRide) {
        this.selectedRide = selectedRide;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public void erreserbatu() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (selectedRide == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aukeratu bidai bat.", null));
            return;
        }
        if (seats == null || seats <= 0 || seats > selectedRide.getnPlaces()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sartu zenbaki balioduna.", null));
            return;
        }
        traveller=(Traveller)userBean.getUser();
        RideRequest r = new RideRequest(new Date(), selectedRide, traveller, seats, departCity, arrivalCity);
        try {
            blfacade.erreserbatu(r);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ondo erreserbatu da.", null));
            float cost = selectedRide.getPrice() * seats;
            float currentWallet = userBean.getWallet();
            userBean.setWallet(currentWallet - cost);
        
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea: " + e.getMessage(), null));
        }
    }
    
    
    
    public List<Ride> getRidesGidariData() {
    	ridesGidariData = blfacade.getRidesGidariData(selectDriver, data);
		return ridesGidariData;
	}

	public void setRidesGidariData(List<Ride> ridesGidariData) {
		this.ridesGidariData = ridesGidariData;
	}

	public List<Driver> getGidariak() {
    	gidariak = blfacade.getGidariak();
        
		return gidariak;
	}

	public void setGidariak(List<Driver> gidariak) {
		this.gidariak = gidariak;
	}

	public Driver getSelectDriver() {
		return selectDriver;
	}

	public void setSelectDriver(Driver selectDriver) {
		this.selectDriver = selectDriver;
	}

	public String gidariakErakutsi() {
	    return "Gidariak?faces-redirect=true";
	}
    public String lortuBidaiak() {
    	return "RideGidariData?faces-redirect=true";
    }
    
    
    
    
    
}

package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Profile;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Date;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("createRide")
@SessionScoped
public class CreateRideBean implements Serializable {
	private BLFacade blfacade;
    private String departCity;
    private String arrivalCity;
    private Date data;
    private int numSeats;
    private float price;
    private String email;
    private Ride ride;
    @Inject
    private UserBean userBean;
    private Profile user;
    
    public CreateRideBean() {
    	
	}
    
    @PostConstruct
	public void init() {
		this.user = userBean.getUser();

		if (this.user == null) {
			System.out.println("¡Acceso no autorizado!");


		}

	}
    public BLFacade getBlfacade() {
		return blfacade;
	}


	public void setBlfacade(BLFacade blfacade) {
		this.blfacade = blfacade;
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


	public Date getData() {
		return data;
	}


	public void setData(Date data) {
		this.data = data;
	}


	public int getNumSeats() {
		return numSeats;
	}


	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	public Ride getRide() { 
		return ride;
	}

	public void createRide() {
		if(departCity==null||arrivalCity==null||data==null||numSeats==0||price==0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Errorea Ride-ak sortzean"));
		}else {
			try {
				blfacade = FacadeBean.getBusinessLogic();
				System.out.println(user.getUser());
				ride=blfacade.createRide(departCity, arrivalCity, data, numSeats, price, user.getUser());
				
				System.out.println("Ridea" +ride);
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ondo sortu da ondorengo ride-a" + ride));
			} catch (RideMustBeLaterThanTodayException e) {
				System.out.println("Gaur baino beranduago izan behar da");
			} catch (RideAlreadyExistException e) {
				System.out.println("Existitzen da jada");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ride already exists"));
			}
		}
	}
	
	
	public void onDateSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: " + event.getObject()));
	}
    

    
    
}

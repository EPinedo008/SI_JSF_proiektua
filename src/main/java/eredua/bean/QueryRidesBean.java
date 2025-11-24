package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Ride;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("queryRides")
@ViewScoped
public class QueryRidesBean implements Serializable {
	private List<Ride> rides;
	private BLFacade blfacade;
    private String departCity;
    private String arrivalCity;
    private Date data;
	private List<String> departCities = new ArrayList<String>();
	private List<String> arrivalCities = new ArrayList<String>();

	public QueryRidesBean() {
		blfacade = FacadeBean.getBusinessLogic();
		data=new Date();

	}

	public List<Ride> getRides() {
		if (departCity == null || arrivalCity == null || data == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eremu bat hutsa dago"));
			return null;
		} else {
			rides = blfacade.getRides(departCity, arrivalCity, data);
			System.out.println(rides);
			return rides;
		}
	}

	public List<String> getDepartCities() {
		departCities = new ArrayList<String>();
		departCities = blfacade.getDepartCities();
		if (departCity == null && departCities.size() > 0) {
			departCity = departCities.get(0);
		}
		System.out.println("From:" + departCities);
		return departCities;
	}

	public List<String> getArrivalCities() {
		arrivalCities = new ArrayList<String>();
		if (departCity != null) {
			arrivalCities = blfacade.getDestinationCities(departCity);
			if (arrivalCities.size() > 0) {
				arrivalCity = arrivalCities.get(0);
				
			}
			System.out.println("To:" + arrivalCities);
		}
		return arrivalCities;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date date) {
		this.data = date;
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
		this.getRides();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(""));
	}

	

}

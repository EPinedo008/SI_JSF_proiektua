package domain;

import java.io.*;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@SuppressWarnings("serial")

@Entity
public class Ride implements Serializable {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rideNumber;
	private String nondik;
	private String nora;
	private int nPlaces;
	private Date date;
	private float price;
	@ManyToOne(fetch=FetchType.EAGER)
	private Driver driver;  
	@OneToMany(mappedBy="ride", fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<RideRequest> requests;
	private EgoeraRide egoera;
	public Ride(){
		super();
	}
public void addRequest(RideRequest t) {
		
		requests.add(t);
		
	}
	public Ride(Integer rideNumber, String nondik, String nora, Date date, int nPlaces, float price, Driver driver) {
		super();
		this.rideNumber = rideNumber;
		this.nondik = nondik;
		this.nora = nora;
		this.nPlaces = nPlaces;
		this.date=date;
		this.price=price;
		this.driver = driver;
	}

	

	public Ride(String nondik, String nora,  Date date, int nPlaces, float price, Driver driver) {
		super();
		this.nondik = nondik;
		this.nora = nora;
		this.nPlaces = nPlaces;
		this.date=date;
		this.price=price;
		this.driver = driver;
	}
	
	/**
	 * Get the  number of the ride
	 * 
	 * @return the ride number
	 */
	public Integer getRideNumber() {
		return rideNumber;
	}

	
	/**
	 * Set the ride number to a ride
	 * 
	 * @param ride Number to be set	 */
	
	public void setRideNumber(Integer rideNumber) {
		this.rideNumber = rideNumber;
	}


	/**
	 * Get the origin  of the ride
	 * 
	 * @return the origin location
	 */

	public String getNondik() {
		return nondik;
	}


	/**
	 * Set the origin of the ride
	 * 
	 * @param origin to be set
	 */	
	
	public void setNondik(String origin) {
		this.nondik = origin;
	}

	/**
	 * Get the destination  of the ride
	 * 
	 * @return the destination location
	 */

	public String getNora() {
		return nora;
	}


	/**
	 * Set the origin of the ride
	 * 
	 * @param destination to be set
	 */	
	public void setNora(String destination) {
		this.nora = destination;
	}

	/**
	 * Get the free places of the ride
	 * 
	 * @return the available places
	 */
	
	/**
	 * Get the date  of the ride
	 * 
	 * @return the ride date 
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Set the date of the ride
	 * 
	 * @param date nora be set
	 */	
	public void setDate(Date date) {
		this.date = date;
	}

	
	public float getnPlaces() {
		return nPlaces;
	}

	/**
	 * Set the free places of the ride
	 * 
	 * @param  nPlaces places to be set
	 */

	public void setBetMinimum(int nPlaces) {
		this.nPlaces = nPlaces;
	}

	/**
	 * Get the driver associated to the ride
	 * 
	 * @return the associated driver
	 */
	public Driver getDriver() {
		return driver;
	}

	/**
	 * Set the driver associated to the ride
	 * 
	 * @param driver to associate to the ride
	 */
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	

	public String toString(){
		return rideNumber+";"+";"+nondik+";"+nora+";"+date;  
	}
	public List<RideRequest> getRequests() {
		return requests;
	}
	public void setRequests(List<RideRequest> requests) {
		this.requests = requests;
	}
	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}
	public EgoeraRide getEgoera() {
		return egoera;
	}
	public void setEgoera(EgoeraRide egoera) {
		this.egoera = egoera;
	}

	public void deuseztatuSeatKopuruBainaHandiagoa(RideRequest r) {
		
		for (RideRequest request : requests) {
			
			if (request.getSeats() >this.getnPlaces()
					&& request.getState().equals(EgoeraRideRequest.TRATATU_GABE)&&request.getId()!=r.getId()) {
				request.setState(EgoeraRideRequest.REJECTED);
				request.setWhenDecided(new Date());
				Traveller t = request.getTraveller();	
				t.gehituDirua(request.getPrezioa());

			}
		}
	}


	
}

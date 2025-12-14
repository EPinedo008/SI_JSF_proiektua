package businessLogic;

import java.util.Date;
import java.util.List;

//import domain.Booking;
import domain.Ride;
import domain.RideContainer;
import domain.RideRequest;
import domain.Driver;
import domain.Profile;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
 
/**
 * Interface that specifies the business logic.
 */

public interface BLFacade  {
	  
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	 public List<String> getDepartCities();
	
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	 public List<String> getDestinationCities(String from);


	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driver to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;
	
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	 public List<Ride> getRides(String from, String to, Date date);
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	 public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);
	
	
	 public Profile register(Profile p, String type);
	 public Profile login(String user, String password);
		
	 public List<Ride> getRidesDriver(Profile p) ;
	 public List<RideRequest> getRideRequestsTraveller(Profile p);
	 
	 
	 public void erreserbatu(RideRequest r);
	 public float getMoney(Profile p);
	 
	 public void sartuDirua(float dirua, Profile p);
	 public void kenduDirua(float dirua, Profile p) ;
	 public void onartuEdoDeuseztatuErreserba(RideRequest request, boolean onartuta);
	 public List<RideRequest> getRidesRequestsOfRide(Ride ride);
	 
	 
	 public List<Driver> getGidariak();
	 public List<Ride> getRidesGidariData(Driver driver, Date date);
}

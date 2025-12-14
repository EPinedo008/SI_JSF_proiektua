package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;

import configuration.ConfigXML;
import configuration.JPAUtil;
import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.RideContainer;
import domain.RideRequest;
import domain.Traveller;
import domain.Driver;
import domain.Profile;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new HibernateDataAccess();
		    
		// 

		
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		//ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
     public List<String> getDepartCities(){
    	//dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		 
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	 public List<String> getDestinationCities(String from){
		//dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		 
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		//dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		 
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	 
	public List<Ride> getRides(String from, String to, Date date){
		//dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		 
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		//dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		 
		return dates;
	}
	
	
	
	public Profile register(Profile profile, String type) {

		Profile p = dbManager.register(profile, type);
		return p;
	}


	public Profile login(String user, String password) {

		Profile p = dbManager.login(user, password);
		return p;
	}
	
	public List<Ride> getRidesDriver(Profile p) {
		List<Ride> rides = dbManager.getRidesDriver((Driver)p);
		return rides;
	}
	public List<RideRequest> getRideRequestsTraveller(Profile p) {
		List<RideRequest> rr = dbManager.getRideRequestsTraveller((Traveller)p);
		return rr;
	}
	public void erreserbatu(RideRequest r) {
		dbManager.erreserbatu(r);
	}
	public float getMoney(Profile p) {
		return dbManager.getMoney(p);
	}
	public void sartuDirua(float dirua, Profile p) {
		dbManager.sartuDirua(dirua,p);
	}
	public void kenduDirua(float dirua, Profile p) {
		dbManager.kenduDirua(dirua,p);
	}
	public void onartuEdoDeuseztatuErreserba(RideRequest request, boolean onartuta) {
		dbManager.onartuEdoDeuseztatuErreserba(request,onartuta);
	}
	public List<RideRequest> getRidesRequestsOfRide(Ride ride) {
		return dbManager.getRidesRequestsOfRide(ride);
	}
	
	
	public List<Driver> getGidariak(){
		return dbManager.getGidariak();
	}
	public List<Ride> getRidesGidariData(Driver driver, Date date){
		return dbManager.getRidesGidariData(driver,date);
	}
	
	
}


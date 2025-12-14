package dataAccess;

import configuration.*;
import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Driver;
import domain.EgoeraRide;
import domain.EgoeraRideRequest;
import domain.Profile;
import domain.Ride;
import domain.RideContainer;
import domain.RideRequest;
import domain.Traveller;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */

public class HibernateDataAccess {

	public HibernateDataAccess() {

	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.nondik FROM Ride r ORDER BY r.nondik",
					String.class);
			List<String> cities = query.getResultList();
			db.getTransaction().commit();
			return cities;
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String nondik) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			TypedQuery<String> query = db
					.createQuery("SELECT DISTINCT r.nora FROM Ride r WHERE r.nondik=?1 ORDER BY r.nora", String.class);
			query.setParameter(1, nondik);
			List<String> arrivingCities = query.getResultList();
			db.getTransaction().commit();
			return arrivingCities;
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String nondik, String nora, Date date, int nPlaces, float price, String driverUser)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			System.out.println(">> HibernateDataAccess: createRide=> nondik= " + nondik + " nora= " + nora + " driver="
					+ driverUser + " date " + date);
			try {
				if (new Date().compareTo(date) > 0) {
					throw new RideMustBeLaterThanTodayException("CreateRideGUI.ErrorRideMustBeLaterThanToday");
				}

				Driver driver = db.find(Driver.class, driverUser);

				if (driver.doesRideExists(nondik, nora, date)) {
					db.getTransaction().rollback();
					throw new RideAlreadyExistException(
							"Existitzen da jada");
				}
				System.out.println("crea addRide");
				Ride ride = driver.addRide(nondik, nora, date, nPlaces, price);
				// next instruction can be obviated
				db.persist(driver);
				db.getTransaction().commit();

				return ride;
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				System.out.println("Null");
				db.getTransaction().rollback();
				return null;
			}
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				System.out.println("Rollback");
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String nondik, String nora, Date date) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			System.out.println(">> DataAccess: getRides=> nondik= " + nondik + " nora= " + nora + " date " + date);

			List<Ride> res = new ArrayList<Ride>();
			TypedQuery<Ride> query = db
					.createQuery("SELECT r FROM Ride r WHERE r.nondik=?1 AND r.nora=?2 AND r.date=?3", Ride.class);
			query.setParameter(1, nondik);
			query.setParameter(2, nora);
			query.setParameter(3, date);
			List<Ride> rides = query.getResultList();
			for (Ride ride : rides) {
				res.add(ride);
			}
			db.getTransaction().commit();
			return res;
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String nondik, String nora, Date date) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			System.out.println(">> DataAccess: getEventsMonth");
			List<Date> res = new ArrayList<Date>();

			Date firstDayMonthDate = UtilDate.firstDayMonth(date);
			Date lastDayMonthDate = UtilDate.lastDayMonth(date);

			TypedQuery<Date> query = db.createQuery(
					"SELECT DISTINCT r.date FROM Ride r WHERE r.nondik=?1 AND r.nora=?2 AND r.date BETWEEN ?3 and ?4",
					Date.class);

			query.setParameter(1, nondik);
			query.setParameter(2, nora);
			query.setParameter(3, firstDayMonthDate);
			query.setParameter(4, lastDayMonthDate);
			List<Date> dates = query.getResultList();
			for (Date d : dates) {
				res.add(d);
			}
			db.getTransaction().commit();
			return res;
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	public Profile register(Profile p, String type) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();
			Profile u = db.find(Profile.class, p.getUser());
			Profile user;
			if (u != null) {
				db.getTransaction().commit();

				return null;
			} else {

				user = createTravellerOrDriver(p, type);

				db.persist(user);

				System.out.println("Ondo gorde da");
				db.getTransaction().commit();
				return user;
			}

		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}

	}

	private Profile createTravellerOrDriver(Profile p, String type) {
		String email = p.getEmail();
		String name = p.getName();
		String surname = p.getSurname();
		String username = p.getUser();
		String password = p.getPassword();
		String telf = p.getTelf();

		if (type.equals("Traveller")) {
			return new Traveller(email, name, surname, username, password, telf);
		} else {
			return new Driver(email, name, surname, username, password, telf);
		}
	}

	public Profile login(String user, String password) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			Profile p = db.find(Profile.class, user);

			if (p == null) {
				db.getTransaction().commit();
				return null;

			} else {
				if (p.getPassword().equals(password)) {
					db.getTransaction().commit();
					return p;
				} else {
					db.getTransaction().commit();
					return null;
				}
			}

		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}

	}

	public List<Ride> getRidesDriver(Driver d) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();
			List<Ride> res = new ArrayList<>();
			TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.driver=?1  ", Ride.class);
			query.setParameter(1, d);
			List<Ride> rides = query.getResultList();
			for (Ride ride : rides) {
				res.add(ride);
			}

			return res;

		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	public List<RideRequest> getRideRequestsTraveller(Traveller t) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();
			List<RideRequest> res = new ArrayList<>();
			TypedQuery<RideRequest> query = db.createQuery("SELECT r FROM RideRequest r WHERE r.traveller=?1  ",
					RideRequest.class);
			query.setParameter(1, t);
			List<RideRequest> rides = query.getResultList();
			for (RideRequest ride : rides) {
				res.add(ride);
			}

			return res;

		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}
	public RideRequest erreserbatu(RideRequest request) {
	    EntityManager db = JPAUtil.getEntityManager();
	    
	    try {
	        db.getTransaction().begin();
	        
	        Ride rideManaged = db.find(Ride.class, request.getRide().getRideNumber());
	        Traveller travellerManaged = db.find(Traveller.class, request.getTraveller().getUser());
	        
	       if (!eserlekuKopEgokia(rideManaged, request)) {
	            db.getTransaction().commit(); 
	            return null;
	        }

	        float totalCost = rideManaged.getPrice() * request.getSeats();

	        if (travellerManaged.getWallet() < totalCost) {
	             throw new Exception("Ez daukazu diru nahikorik.");
	        }
	        travellerManaged.kenduDirua(totalCost); 
	       RideRequest newRequest = travellerManaged.addRequest(
	            request.getWhenRequested(), 
	            rideManaged, 
	            request.getSeats(), 
	            request.getFromRequested(), 
	            request.getToRequested()
	        );
	        
	        newRequest.setState(EgoeraRideRequest.TRATATU_GABE);
	        rideManaged.addRequest(newRequest); 
	    
	        db.getTransaction().commit();

	        return newRequest;
	    } catch (Exception ex) {
	        if (db.getTransaction().isActive()) {
	            db.getTransaction().rollback();
	        }
	        throw new RuntimeException("Errorea erreserbatzen: " + ex.getMessage(), ex);
	    } finally {
	        db.close();
	    }
	}
	private boolean eserlekuKopEgokia(Ride rd, RideRequest request) {

		int seats = request.getSeats();

		return rd.getnPlaces() >= seats;
	}


	public float getMoney(Profile profile) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			Profile p = db.find(Profile.class, profile.getUser());
			return p.getWallet();
		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	public void sartuDirua(float dirua, Profile user) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();
			Profile u = db.find(Profile.class, user.getUser());
			u.gehituDirua(dirua);
			db.getTransaction().commit();
		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}

	}

	public void kenduDirua(float dirua, Profile user) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			Profile u = db.find(Profile.class, user.getUser());

			u.kenduDirua(dirua);

			db.getTransaction().commit();
		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}

	}

	public List<RideRequest> getRidesRequestsOfRide(Ride ride) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			Ride r = db.find(Ride.class, ride.getRideNumber());

			return r.getRequests();
		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	public void onartuEdoDeuseztatuErreserba(RideRequest request, boolean onartuta) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			RideRequest r = db.find(RideRequest.class, request.getId());
			Ride ride = r.getRide();

			if (onartuta) {
				onartuErreserba(ride, r);
			} else {
				deuseztatuErreserba(r);
			}
			r.setWhenDecided(new Date());
			db.getTransaction().commit();

		} catch (

		Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	private void onartuErreserba(Ride ride, RideRequest r) {
		ride.setnPlaces((int) ride.getnPlaces() - r.getSeats());
		r.setState(EgoeraRideRequest.ACCEPTED);
		ride.deuseztatuSeatKopuruBainaHandiagoa(r);
		if (ride.getnPlaces() == 0) {
			ride.setEgoera(EgoeraRide.TOKIRIK_GABE);
		}
	}

	private void deuseztatuErreserba(RideRequest r) {
		Traveller t = r.getTraveller();
		t.gehituDirua(r.getPrezioa());
		r.setState(EgoeraRideRequest.REJECTED);

	}

	public List<Driver> getGidariak() {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();
			List<Driver> res = new ArrayList<Driver>();
			TypedQuery<Driver> query = db
					.createQuery("SELECT d FROM Driver d", Driver.class);
			
			List<Driver> gidariak = query.getResultList();
			for (Driver gidari : gidariak) {
				res.add(gidari);
			}
			db.getTransaction().commit();
			return res;
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}
	
	public List<Ride> getRidesGidariData(Driver driver, Date date) {
		EntityManager db = JPAUtil.getEntityManager();
		try {
			db.getTransaction().begin();

			List<Ride> res = new ArrayList<Ride>();
			TypedQuery<Ride> query = db
					.createQuery("SELECT r FROM Ride r WHERE r.driver=?1 AND r.date=?2", Ride.class);
			query.setParameter(1, driver);
			query.setParameter(2, date);
			List<Ride> rides = query.getResultList();
			for (Ride ride : rides) {
				res.add(ride);
			}
			db.getTransaction().commit();
			return res;
		} catch (Exception ex) {
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
			throw ex;
		} finally {
			db.close();
		}
	}

	
	
}

package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@Entity
public class Driver extends Profile implements Serializable {
	 
	
	@OneToMany(mappedBy="driver", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Ride> rides=new ArrayList<Ride>();
	
	public Driver(String email, String name, String surname, String user, String password, String telefono) {
		super(email, name, surname, user, password, telefono);
	}
	public Driver(String user, String email) {
		super(user,email);
	}

	public Driver() {
		super();
	}
	
	
	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Ride addRide(String nondik, String nora, Date date, int nPlaces, float price)  {
        Ride ride=new Ride(nondik,nora,date,nPlaces,price, this);
        rides.add(ride);
        return ride;
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param nondik the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String nondik, String nora, Date date)  {	
		for (Ride r:rides)
			if ( (java.util.Objects.equals(r.getNondik(),nondik)) && (java.util.Objects.equals(r.getNora(),nora)) && (java.util.Objects.equals(r.getDate(),date)) )
			 return true;
		
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		return true;
	}

	public Ride removeRide(String nondik, String nora, Date date) {
		boolean found=false;
		int index=0;
		Ride r=null;
		while (!found && index<=rides.size()) {
			r=rides.get(++index);
			if ( (java.util.Objects.equals(r.getNondik(),nondik)) && (java.util.Objects.equals(r.getNora(),nora)) && (java.util.Objects.equals(r.getDate(),date)) )
			found=true;
		}
			
		if (found) {
			rides.remove(index);
			return r;
		} else return null;
	}
	
}

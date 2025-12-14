package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


@Entity
public class Traveller extends Profile implements Serializable 
{

	@OneToMany(targetEntity = RideRequest.class, mappedBy = "traveller",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<RideRequest> requests;
	
	public Traveller(String email, String name, String surname, String user, String password, String telf) {
		super(email, name, surname, user, password, telf);
		requests = new ArrayList<RideRequest>();
	}
	
	public Traveller(String user, String email) {
		super(user, email);
		requests = new ArrayList<RideRequest>();
	}
	
    public Traveller() {
    	super();
    }
	public List<RideRequest> getRequests() {
		return requests;
	}

	public boolean jadanikErreserbatuta(RideRequest rideRequest) {
		for (RideRequest r : requests) {
			if (rideRequest.equals(r)) {
				return true;
			}
		}
		return false;
	}

	public RideRequest addRequest(Date when,Ride ride,  int seats, String requestFrom, String requestTo) {
		RideRequest request= new RideRequest(when,ride,this,seats,requestFrom,requestTo);
		requests.add(request);
		return request;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traveller other = (Traveller) obj;
		if (!this.getUser().equals(other.getUser()))
			return false;
		return true;
	}
	
	public void ezabatuRideRequest(RideRequest r) {
		this.getRequests().remove(r);
	}
	
	
	public RideRequest lortuLehenRequestBidaiakoa(Ride ride) {
		for(RideRequest request: requests) {
			
			if(request.getRide().getRideNumber().equals(ride.getRideNumber())) {
			
				return request;
			}
		}
		return null;
	}
	public void doneNotDoneErreserbaBerdinak(boolean eginda,Ride ride,Driver d) {
		if(eginda) {
		for(RideRequest request: requests) {
			if(request.getRide().getRideNumber().equals(ride.getRideNumber())&&request.getState().equals(EgoeraRideRequest.ACCEPTED)) {
				
				float diruSartu = request.getPrezioa();
				d.gehituDirua(diruSartu);

				//d.gehituMezuaTransaction(7, diruSartu, request); 
				
				
				request.setState(EgoeraRideRequest.DONE);
				request.setBidaiaEsandaTrue();
			}
		}
		}else {
			for(RideRequest request: requests) {
				if(request.getRide().getRideNumber().equals(ride.getRideNumber())&&request.getState().equals(EgoeraRideRequest.ACCEPTED)) {
					float diruSartu = request.getPrezioa();
					this.gehituDirua(diruSartu);
				//	this.gehituMezuaTransaction(4, diruSartu, request);
					request.setState(EgoeraRideRequest.NOT_DONE);
					request.setBidaiaEsandaTrue();
				}
			}
		}
	
	}
	
	
}

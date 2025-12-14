package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlID;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Driver;
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

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("register")
@SessionScoped
public class RegisterBean implements Serializable {
	private BLFacade blfacade;

	private String email;
	private String name;
	private String surname;
	@Id
	private String user;
	private float wallet;
	private String password;
	private String repeatPassword;
	private String tlf;
	private String type;
	
	@Inject
    private UserBean userBean;
	
    public RegisterBean() {
    	
	}
    
    
    public BLFacade getBlfacade() {
		return blfacade;
	}


	public void setBlfacade(BLFacade blfacade) {
		this.blfacade = blfacade;
	}

	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public float getWallet() {
		return wallet;
	}


	public void setWallet(float wallet) {
		this.wallet = wallet;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getTlf() {
		return tlf;
	}


	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	

	public String getRepeatPassword() {
		return repeatPassword;
	}


	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}


	public void register() {
	    FacesContext context = FacesContext.getCurrentInstance();

	    if (user == null || user.isBlank() ||
	        name == null || name.isBlank() ||
	        surname == null || surname.isBlank() ||
	        email == null || email.isBlank() ||
	        password == null || password.isBlank() ||
	        repeatPassword == null || repeatPassword.isBlank() ||
	        tlf == null || tlf.isBlank()) {

	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Mesedez, dena bete.", null));
	        return;
	    }
	    if(!password.equals(repeatPassword)) {
	    	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Pasahitzak ez dute koinziditzen.", null));
	    }
	    try {
	    	blfacade = FacadeBean.getBusinessLogic();
	    	
	        Profile p = new Driver(email, name, surname, user, password, tlf);
	        Profile registeredUser = blfacade.register(p, type);

	        if (registeredUser != null) {
	        	userBean.resetUser();
            	userBean.sortuUser(registeredUser);
	            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
	                    "Kaixo " + registeredUser.getName(), null));
	            
	            if ("Traveller".equals(type)) {
	                context.getExternalContext().redirect("Traveller.xhtml");
	            } else if ("Driver".equals(type)) {
	                context.getExternalContext().redirect("Driver.xhtml");
	            }

	        } else {
	            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "User hori jadanik existitzen da.", null));
	        }

	    } catch (Exception e) {
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Errorea: " + e.getMessage(), null));
	        e.printStackTrace();
	    }
	}

	


    
    
}

package eredua.bean;

import java.io.Serializable;

import javax.persistence.Id;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Profile;
import domain.Traveller;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("userBean")
@SessionScoped
public class UserBean implements Serializable {

    private BLFacade blfacade;
    private Profile user;
	private String email;
	private String name;
	private String surname;
	@Id
	private String username;
	private float wallet;
	private String password;
	private String tlf;
	private Float amount;
	private String type;
	private Traveller traveller;
    private Driver driver;
	
	
    public UserBean() {
    	blfacade = FacadeBean.getBusinessLogic();
    }
    
    
    public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
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

	public Profile getUser() {
		return user;
	}

	public void setUser(Profile user) {
		this.user = user;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public void resetUser() {
	    this.user = null;
	    this.type = null;
	    this.wallet = 0;
	    this.username = null;
	    this.name = null;
	    this.surname = null;
	    this.password = null;
	    this.tlf = null;
	    this.amount = null;
	    this.traveller = null;
	    this.driver = null;
	}

	public void sortuUser(Profile p) {
		email=p.getEmail();
		name=p.getName();
		surname=p.getSurname();
		user=p;
		wallet=p.getWallet();
		password=p.getPassword();
		tlf=p.getTelf();
		username=p.getUser();
		if(p instanceof Driver) {
			type="Driver";
			this.driver = (Driver)p;
		}else {
			type="Traveller";
			this.traveller = (Traveller)p;
			System.out.println(traveller);
			
		}
		blfacade = FacadeBean.getBusinessLogic();
		
	}
	
	
	public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void depositOrWithdraw() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (amount == null || amount <= 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kantitate txikia", null));
            return;
        }

        try {
            if ("Traveller".equals(type) && traveller != null) {
                blfacade.sartuDirua(amount, traveller);
                wallet = blfacade.getMoney(traveller);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ondo sartu da dirua", null));
            } else if ("Driver".equals(type) && driver != null) {
                if (amount > wallet) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dirua falta da", null));
                    return;
                }
                blfacade.kenduDirua(amount, driver);
                wallet = blfacade.getMoney(driver);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ondo atera da dirua", null));
            }
            amount = null;
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
            e.printStackTrace();
        }
    }
	
	public String backButton() {
	    if ("Traveller".equalsIgnoreCase(this.type)) {
	        return "Traveller?faces-redirect=true";
	    } 
	    else if ("Driver".equalsIgnoreCase(this.type)) {
	        return "Driver?faces-redirect=true";
	    }

	    return "Menu?faces-redirect=true";
	}
	
}

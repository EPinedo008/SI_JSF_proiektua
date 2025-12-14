package eredua.bean;

import java.io.Serializable;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Profile;
import domain.Traveller;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("login")
@RequestScoped
public class LoginBean implements Serializable {
    @Inject
    private UserBean userBean;

    private BLFacade blfacade;

    private String username;
    private String password;
    private Profile currentUser;

    public LoginBean() {
        
    }

    public BLFacade getBlfacade() {
        return blfacade;
    }

    public void setBlfacade(BLFacade blfacade) {
        this.blfacade = blfacade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Profile currentUser) {
        this.currentUser = currentUser;
    }


    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Mesedez, dena bete.", null));
            return;
        }

        try {
            blfacade = FacadeBean.getBusinessLogic();
            Profile user = blfacade.login(username, password);

            if (user != null) {
            	userBean.resetUser();
            	userBean.sortuUser(user);
	            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
	                    "Kaixo " + user.getName(), null));
	            
	            if (user instanceof Traveller) {
	                context.getExternalContext().redirect("Traveller.xhtml");
	            } else if (user instanceof Driver) {
	                context.getExternalContext().redirect("Driver.xhtml");
	            }

	        } else {
	            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                    "User edo pasahitza gaizki.", null));
	        }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error en login: " + e.getMessage(), null));
            e.printStackTrace();
        }
    }


    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentUser = null;
        username = null;
        password = null;

        try {
            context.getExternalContext().redirect("Login.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

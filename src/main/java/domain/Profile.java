package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSeeAlso;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class Profile implements Serializable {

	private String email;
	private String name;
	private String surname;
	
	@Id
	private String user;
	private float wallet;
	private String password;
	private String telf;
	
	
	public Profile(String email, String name, String surname, String user, String password, String telefono) {
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.user = user;
		this.password = password;
		this.wallet = 0;
		this.telf = telefono;
	}
 
	public Profile(String user, String email) {
		this.user = user;
		this.email = email;
	}
	public Profile() {
		
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getUser() {
		return user;
	}

	public float getWallet() {
		return wallet;
	}

	public String getPassword() {
		return password;
	}

	/*public float getRating() {
		rating=0;
		if(balorazioLista!=null&&balorazioLista.size()>0) {
			for( int i:balorazioLista ) {
				rating=rating+i;
			}
			rating= rating/balorazioKop;
		}
		return rating;
	}
	public int getRatingLuzera() {
		if(balorazioLista!=null) {
			return balorazioLista.size();
		}else {
			return 0;
		}
	}*/

	public String getTelf() {
		return telf;
	}

	public String toString() {
		return (user +" "+ name+" " + surname);
	}

	public void setWallet(float dirua) {
		this.wallet = dirua;
	}

	public void gehituDirua(float dirua) {
		wallet = wallet + dirua;

	}

	public void kenduDirua(float dirua) {
		wallet = wallet - dirua;

	}
	public String getFullName() {
		return this.getName()+" "+this.getSurname();
	}
	/*public List<Erreklamazioa> getErreklamazioak() {
		return erreklamazioak;
	}

	public void setErreklamazioak(List<Erreklamazioa> erreklamazioak) {
		this.erreklamazioak = erreklamazioak;
	}
	
	public void addErreklamazioa(Erreklamazioa e) {
		erreklamazioak.add(e);
	}
	public void gehituErreklamazioa(Profile nori, String deskripzioa,float prezioa, RideRequest r) {
		Erreklamazioa erreklamazioa= new Erreklamazioa(this, nori, deskripzioa, prezioa, r);
		this.addErreklamazioa(erreklamazioa);
	}
	
	public void addErreklamazioMezu(int i,Ride r,Erreklamazioa erreklamazio) {
		Mezua mezu= new Mezua(i,this,r,erreklamazio);
		this.getMezuList().add(mezu);
		
	}
	public List<Mezua> getMezuList() {
		if (this.mezuList == null) {
	        this.mezuList = new Vector<Mezua>();
	    }
		return mezuList;
	}

	public void addMezua(Mezua mezua) {
	    mezuList.add(mezua);
	}

	public void gehituMezuaTransaction(int i, float kantitatea, RideRequest erreserba) {
		Mezua m = new Mezua(i, kantitatea, erreserba, this);
		this.addMezua(m);
	
	}

	public void gehituMezuaTransaction(int i, float kantitatea) {
		Mezua m = new Mezua(i, kantitatea, this);
			this.addMezua(m);
			System.out.println(this.getMezuList());
			
		}
	public int getBalorazioKop() {
		return balorazioKop;
	}
	public List<Integer>getBalorazioLista(){
		return balorazioLista;
	}
	public void addBalorazioa(int balorazioa) {
		balorazioLista.add(balorazioa);
		balorazioKop++;
		
	}
	
	public float kalkulatuBalorazioMedia() {
		float batura=0;
		if(balorazioLista!=null) {
		for(int a: balorazioLista) {
			batura=batura+a;
		}
		
		if(balorazioKop==0) {
			return 0;
		}else {
		return (batura/balorazioKop);
		}
		}else {
			return 0;
		}
	}*/
}

	
	


package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import controllers.MainController;

public abstract class Agent implements Comparable<Agent>{

	private String matricule, nom, prenom;
	private int code;
	private Hashtable<Integer, Tache> lesTaches;
	private boolean absent;
	
	private static Hashtable<String, Agent> lesAgents = new Hashtable<String, Agent>();
	
	public Agent(String matricule, String nom, String prenom, int code) {
		this.matricule = matricule;
		this.nom = nom;
		this.prenom = prenom;
		this.code = code;
		this.absent = false;
		this.lesTaches = new Hashtable<Integer, Tache>();
		lesAgents.put(matricule, this);
	}	
	
	public abstract TrancheHoraire getHoraire();
	public abstract Duree getNbHeure();
	public abstract void setNbHeure(Duree nbHeure);
	
	public boolean estDisponible(Horaire debut, Horaire fin){
		TrancheHoraire thAgent = getHoraire();
		TrancheHoraire thTache = new TrancheHoraire(debut, fin);
		boolean res = false;
		if(thTache.dansTrancheHoraire(thAgent)){
			Enumeration<Tache> liste = lesTaches.elements();
			res = true;
			while(liste.hasMoreElements()){
				Tache t = (Tache)liste.nextElement();
				TrancheHoraire th = new TrancheHoraire(t.getDebut(), t.getFin());
				if(thTache.intersection(th) != null)
					res = false;
			}
		}
		return res;
	}
	
	private boolean resteTempsTravail(Tache t){
		if(getNbHeure().compareTo(t.getDuree()) >= 0)
			return true;
		else
			return false;
	}
	
	public boolean affecterTache(Tache t) {
		if(estDisponible(t.getDebut(), t.getFin()) && resteTempsTravail(t)){
			getLesTaches().put(t.getIdTache(), t);
			setNbHeure(getNbHeure().retirer(t.getDuree()));
			Tache.listeTachesNonAffectees().remove(t.getIdTache());
			t.setAgent(this);
			return true;
		}
		else{
			Tache.listeTachesNonAffectees().put(t.getIdTache(), t);
			return false;
		}		
	}
	
	public void affectationTachesAccueil(){
		ArrayList<Tache> taches = Tache.trier(lesTaches);
		Duree free;
		Horaire debut, fin;
		if(taches.size() == 0){
			affecterTache(new TacheAccueil("Accueil", getHoraire().getDebutTrancheHoraire(), getHoraire().getFinTrancheHoraire()));
		}
		else{
			// Debut
			free = taches.get(0).getDebut().retrait(getHoraire().getDebutTrancheHoraire());
			if(free.dureeEnMinutes() >= 30)
				affecterTache(new TacheAccueil("Accueil", getHoraire().getDebutTrancheHoraire(), taches.get(0).getDebut()));
			
			// Ensemble des t�ches
			for (int i = 1; i < taches.size()-1; i++) {
				free = taches.get(i).getDebut().retrait(taches.get(i-1).getFin());
				if(free.dureeEnMinutes() >= 30)
					affecterTache(new TacheAccueil("Accueil", taches.get(i-1).getFin(), taches.get(i).getDebut()));
			}
			
			// Fin
			free = getHoraire().getFinTrancheHoraire().retrait(taches.get(taches.size()-1).getFin());
			if(free.dureeEnMinutes() >= 30)
				affecterTache(new TacheAccueil("Accueil", taches.get(taches.size()-1).getFin(), getHoraire().getFinTrancheHoraire()));	
			
		}
	}
	
	public static void reaffecterTache(Tache t){
		
	}
	
	public void desaffecterTache(Tache t){
		if(lesTaches.containsKey(t.getIdTache())){
			lesTaches.remove(t);
			if(t.getDuree().dureeEnMinutes() >= 30){
				affecterTache(new TacheAccueil("Accueil", t.getDebut(), t.getFin()));
			}
		}
	}
	
	public String getMatricule() {
		return matricule;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public int getCode() {
		return code;
	}

	public Hashtable<Integer, Tache> getLesTaches() {
		return lesTaches;
	}	
	
	public static Hashtable<String, Agent> getLesAgents() {
		return lesAgents;
	}

	public void resetTache(){
		ArrayList<Tache> listeT = Tache.trier(getLesTaches());
		for(Tache t : listeT){
			Tache.listeTachesNonAffectees().put(t.getIdTache(), t);
		}
		getLesTaches().clear();
	}
	
	public Horaire getHRepas(){
		Horaire horaire;
		do {
			int h = (int) (Math.random()*(14-11)+11);
			int m = (int) (Math.random()*59);
			if(h == 11)
				m = (int) (Math.random()*29+30);
			horaire = new Horaire(h, m);
		} while(!getHoraire().contient(horaire) || !getHoraire().contient(horaire.ajout(new Duree(1,0))) );
		return horaire;
	}
	
	public String toString(){
		String res = this.matricule +" - "+ this.nom + this.prenom + " - "+ getHoraire() + "\n\nListe des t�ches affect�es :\n";
		ArrayList<Tache> liste = Tache.trier(lesTaches);
		for(Tache t : liste){
			res += "\t"+ t +"\n";
		}
		return res;
	}
	
	public boolean isAbsent() {
		return absent;
	}

	public void setAbsent(boolean absent) {
		this.absent = absent;
	}

	public static ArrayList<Agent> trier(Hashtable h){
		ArrayList<Agent> liste = new ArrayList<Agent>(h.values());
		Collections.sort(liste);
		return liste;
	}
	
    public int compareTo(Agent a){    	
		return getHoraire().getDebutTrancheHoraire().compareTo(a.getHoraire().getDebutTrancheHoraire());
    }
	
	
	
}
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import models.*;
import vue.FenetreAccueil;

public class MainController implements ActionListener{
	
	private FenetreAccueil vueAccueil;

	public MainController (FenetreAccueil vueAccueil) {
		this.vueAccueil = vueAccueil;
		importationFichiers();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void importationFichiers(){
		try{
			BufferedReader entree = new BufferedReader	(new FileReader("./ressources/avions16-v1.txt"));
			String ligne= null;
			while ((ligne = entree.readLine())!=null){			
				StringTokenizer decoupe = new StringTokenizer(ligne);
				new Avion(decoupe.nextToken(), decoupe.nextToken(), Integer.parseInt(decoupe.nextToken()));
			}
			
			entree = new BufferedReader	(new FileReader("./ressources/AgentsMiTemps-16-v1.txt"));
			ligne= null;
			while ((ligne = entree.readLine())!=null){			
				StringTokenizer decoupe = new StringTokenizer(ligne);
				new AgentMiTemps(decoupe.nextToken(), decoupe.nextToken(), decoupe.nextToken(), Integer.parseInt(decoupe.nextToken()));
			}
			
			entree = new BufferedReader	(new FileReader("./ressources/AgentsTempsPlein-16-v1.txt"));
			ligne= null;
			while ((ligne = entree.readLine())!=null){			
				StringTokenizer decoupe = new StringTokenizer(ligne);
				new AgentTempsPlein(decoupe.nextToken(), decoupe.nextToken(), decoupe.nextToken(), Integer.parseInt(decoupe.nextToken()));
			}
			
			entree = new BufferedReader	(new FileReader("./ressources/ProgrammeVolsDeparts16-v2.txt"));
			ligne= null;
			while ((ligne = entree.readLine())!=null){			
				StringTokenizer decoupe = new StringTokenizer(ligne);
				new VolDepart(
						decoupe.nextToken(), 
						new Horaire(Integer.parseInt(decoupe.nextToken()), Integer.parseInt(decoupe.nextToken())),
						decoupe.nextToken(),
						Avion.lesAvions.get(decoupe.nextToken())
					);
			}
			
			entree = new BufferedReader	(new FileReader("./ressources/ProgrammeVolsArrivees16-v2.txt"));
			ligne= null;
			while ((ligne = entree.readLine())!=null){			
				StringTokenizer decoupe = new StringTokenizer(ligne);
				new VolArrivee(
						decoupe.nextToken(), 
						new Horaire(Integer.parseInt(decoupe.nextToken()), Integer.parseInt(decoupe.nextToken())),
						decoupe.nextToken(),
						Avion.lesAvions.get(decoupe.nextToken())
					);
			}
			System.out.println("Gg men");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}

}
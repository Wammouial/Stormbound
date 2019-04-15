/**
 * Cette classe contient les methodes statiques de tous les effets du jeu.
 */
package components;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author William
 *
 */
public class Effects {
	
	public static void bladestorm (EffectParameters params) {
		for(Unit[] line : params.getMatrice()) {
			for (Unit unit : line) {
				if (unit != null) {
					if (unit.getPlayer() != params.getCurrentPlayer().getNumber()) {
						unit.lessPower(1);
					}				
				}
			}
		}
	}
	
	public static void execution (EffectParameters params) {
		
		//On vérifie tout d'abord qu'il y a bien une cible pour lancer ce sort
		boolean possible = false;
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() != params.getCurrentPlayer().getNumber()) {
						possible = true;
					}
				}
			}
		}
		if (!(possible)) { throw new NoTargetEffectException(); }
		
		//Puis on lance si les coords sont bien les bonnes
		int[] coords = params.getUnitPos();
		if (coords == null) {
			throw new WrongEffectParametersException();
		}
		
		if (params.getMatrice()[coords[0]][coords[1]] != null) {
			if (params.getMatrice()[coords[0]][coords[1]].getPlayer() != params.getCurrentPlayer().getNumber()) {
					
				params.getMatrice()[coords[0]][coords[1]].lessPower(3);
			
			} else { throw new WrongEffectParametersException(); }
		} else { throw new WrongEffectParametersException(); }
	}
	
	public static void felflares (EffectParameters params) {
		ArrayList<int[]> surrounding = new ArrayList<int[]>();
		int[] unitCoords = params.getUnitPos();
		
		// On verifie si les cases autours sont des ennemis
		if (unitCoords[0] > 0) { if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]] != null) { // En haut
			if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]].getPlayer() != params.getCurrentPlayer().getNumber()) {
				surrounding.add(new int[] {unitCoords[0]-1, unitCoords[1]} );
			}
		}}
		
		if (unitCoords[0] < Constants.height-1) { if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]] != null) { // En bas
			if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]].getPlayer() != params.getCurrentPlayer().getNumber()) {
				surrounding.add(new int[] {unitCoords[0]+1, unitCoords[1]} );
			}
		}}
		
		if (unitCoords[1] > 0) { if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1] != null) { // A gauche
			if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1].getPlayer() != params.getCurrentPlayer().getNumber()) {
				surrounding.add(new int[] {unitCoords[0], unitCoords[1]-1} );
			}
		}}
		
		if (unitCoords[1] < Constants.width-1) { if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1] != null) { // A droite
			if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1].getPlayer() != params.getCurrentPlayer().getNumber()) {
				surrounding.add(new int[] {unitCoords[0], unitCoords[1]+1} );
			}
		}}
		
		// Si la liste n'est pas vide on prend une coord au hasard
		if (!(surrounding.isEmpty())) {
			Collections.shuffle(surrounding);
			
			int[] choosed = surrounding.get(0);
			params.getMatrice()[choosed[0]][choosed[1]].lessPower(2); // Finalement
		} else {
		}
	}
	
	public static void emerald_towers (EffectParameters params) {
		
		int[] coords = params.getUnitPos();
		int i = coords[0];
		int j = coords[1];
		
		if (params.getCurrentPlayer().getNumber() == 1) {
			for(int x = i-1; x >= 0; x--) {
				if (params.getMatrice()[x][j] != null) {
					if (params.getMatrice()[x][j].getPlayer() == 1) {
						params.getMatrice()[x][j].addPower(2);
					}
				}
			}
		}
		
		if (params.getCurrentPlayer().getNumber() == 2) {
			for(int x = i+1; x < Constants.height; x++) {
				if (params.getMatrice()[x][j] != null) {
					if (params.getMatrice()[x][j].getPlayer() == 2) {
						params.getMatrice()[x][j].addPower(2);
					}
				}
			}
		}
	}
	
	public static void joust_champions (EffectParameters params) {
		if (params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]] != null) {
			if (params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]].getPlayer() != params.getCurrentPlayer().getNumber()) {
				params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]].lessPower(3);
			}
		}
	}
	
	public static void personal_servers (EffectParameters params) {
		ArrayList<int[]> friends = new ArrayList<int[]>();
		Unit[][] mat = params.getMatrice();
		
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (mat[i][j] != null) {
					if (mat[i][j].getPlayer() == params.getCurrentPlayer().getNumber()) {
						friends.add(new int[] {i, j});
					}
				}
			}
		}
		// On enleve la position de la carte qu'on vient de jouer elle meme
		friends.remove(params.getUnitPos());
		// Ensuite on melange la liste et on prend le premier element
		if (!(friends.isEmpty())) {
			Collections.shuffle(friends);
			int[] choosed = friends.get(0);
			
			mat[choosed[0]][choosed[1]].addPower(2);
		}
	}
	
	public static void siegebreakers (EffectParameters params) {
		ArrayList<int[]> surrounding = new ArrayList<int[]>();
		int[] unitCoords = params.getUnitPos();
		
		// On verifie si les cases autours sont des ennemis
		if (unitCoords[0] > 0) { if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]] != null) { // En haut
			if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]].getPlayer() != params.getCurrentPlayer().getNumber()) {
				if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]].getType().equals(Constants.TYPE_STRUCTURE)) {
					surrounding.add(new int[] {unitCoords[0]-1, unitCoords[1]} );
				}
			}
		}}
		
		if (unitCoords[0] < Constants.height-1) { if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]] != null) { // En bas
			if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]].getPlayer() != params.getCurrentPlayer().getNumber()) {
				if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]].getType().equals(Constants.TYPE_STRUCTURE)) {
				surrounding.add(new int[] {unitCoords[0]+1, unitCoords[1]} );
				}
			}
		}}
		
		if (unitCoords[1] > 0) { if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1] != null) { // A gauche
			if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1].getPlayer() != params.getCurrentPlayer().getNumber()) {
				if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1].getType().equals(Constants.TYPE_STRUCTURE)) {
				surrounding.add(new int[] {unitCoords[0], unitCoords[1]-1} );
				}
			}
		}}
		
		if (unitCoords[1] < Constants.width-1) { if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1] != null) { // A droite
			if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1].getPlayer() != params.getCurrentPlayer().getNumber()) {
				if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1].getType().equals(Constants.TYPE_STRUCTURE)) {
					surrounding.add(new int[] {unitCoords[0], unitCoords[1]+1} );
				}
			}
		}}
		
		if (!(surrounding.isEmpty())) {
			Collections.shuffle(surrounding);
			
			int[] choosed = surrounding.get(0);
			params.getMatrice()[choosed[0]][choosed[1]].lessPower(4); // Finalement
		}
	}
	
	public static void call_for_aid(EffectParameters params) {
		
		//On vérifie tout d'abord qu'il y a bien une cible pour lancer ce sort
		boolean possible = false;
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() != params.getCurrentPlayer().getNumber()) {
						possible = true;
					}
				}
			}
		}
		if (!(possible)) { throw new NoTargetEffectException(); }
		
		String newType = "";
		if (params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]] != null) {
			if (params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]].getPlayer() == params.getCurrentPlayer().getNumber()) {
				newType = (params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]].getUnitType().equals("")) ? "Knight" : params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]].getUnitType();
			} else {
				throw new WrongEffectParametersException();
			}
		} else {
			throw new WrongEffectParametersException();
		}
		
		Unit newUnit = new Unit("Aids", 0, 0, params.getCurrentPlayer().getNumber(), 3, "Common",EffectDescription.None, newType);
		int[] unitCoords = params.getUnitPos();
		
		// On verifie si les cases autours sont des ennemis
		if (unitCoords[0] > 0) { if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]] != null) { // En haut
			if (params.getMatrice()[unitCoords[0]-1][unitCoords[1]] == null) {
				params.getMatrice()[unitCoords[0]-1][unitCoords[1]] = (Unit) newUnit.clone();
			}
		}}
		
		if (unitCoords[0] < Constants.height-1) { if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]] != null) { // En bas
			if (params.getMatrice()[unitCoords[0]+1][unitCoords[1]] == null) {
				params.getMatrice()[unitCoords[0]+1][unitCoords[1]] = (Unit) newUnit.clone();
			}
		}}
		
		if (unitCoords[1] > 0) { if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1] != null) { // A gauche
			if (params.getMatrice()[unitCoords[0]][unitCoords[1]-1] == null) {
				params.getMatrice()[unitCoords[0]][unitCoords[1]-1] = (Unit) newUnit.clone();
			}
		}}
		
		if (unitCoords[1] < Constants.width-1) { if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1] != null) { // A droite
			if (params.getMatrice()[unitCoords[0]][unitCoords[1]+1] == null) {
				params.getMatrice()[unitCoords[0]][unitCoords[1]+1] = (Unit) newUnit.clone();
			}
		}}
	}
	
	public static void confinement(EffectParameters params) {
		//On vérifie tout d'abord qu'il y a bien une cible pour lancer ce sort
		boolean possible = false;
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() != params.getCurrentPlayer().getNumber()) {
						possible = true;
					}
				}
			}
		}
		if (!(possible)) { throw new NoTargetEffectException(); }
	
		Unit u = params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]];
		if (u != null) {
			if (u.getPlayer() != params.getCurrentPlayer().getNumber()) {
				if (u.getPower() > 5 ) {
					u.lessPower(u.getPower() - 5);
				} 
			} else {
				throw new WrongEffectParametersException();
			}
		} else {
			throw new WrongEffectParametersException();
		}
	}
	
	public static void flooding_the_gates(EffectParameters params) {
		int i;
		
		if (params.getCurrentPlayer().getNumber() == 1) {
			i = Constants.height - 1;
		} else {
			i = 0;
		}
		
		for (int j = 0; j < Constants.width; j++) {
			if (params.getMatrice()[i][j] != null) {
				params.getMatrice()[i][j].lessPower(3);
			}
		}
	}
	
	public static void kindreds_grace(EffectParameters params) {
		//On vérifie tout d'abord qu'il y a bien une cible pour lancer ce sort
		boolean possible = false;
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() == params.getCurrentPlayer().getNumber()) {
						possible = true;
					}
				}
			}
		}
		if (!(possible)) { throw new NoTargetEffectException(); }
		
		// On vérifie que la cible est la bonne et on retient sa faction
		Unit u = params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]];
		String faction;
		
		if (u != null) {
			if (u.getPlayer() == params.getCurrentPlayer().getNumber()) {
				faction = u.getFaction();
				u.addPower(5);
			} else {
				throw new NoTargetEffectException();
			}
		} else {
			throw new NoTargetEffectException();
		}
		
		//ON ajoute 2 de power à toutes les cartes aliées du même type
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() == params.getCurrentPlayer().getNumber()) {
						if (params.getMatrice()[i][j].getFaction().equals(faction)) {
							params.getMatrice()[i][j].addPower(2);
						}
					}
				}
			}
		}
	}
	
	public static void needle_blast(EffectParameters params) {
		ArrayList<Unit> possibles = new ArrayList<Unit>();
		
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() != params.getCurrentPlayer().getNumber()) {
						possibles.add(params.getMatrice()[i][j]);
					}
				}
			}
		}
		
		if (possibles.isEmpty()) {
			throw new NoTargetEffectException();
		}
		
		// Meme si un seul ennemi et pas deux lui inflige juste 2 points de dégats
		if (possibles.size() == 1) {
			possibles.get(0).lessPower(2);
		// Si plus mélange et prend les deux premiers de la liste
		} else {
			Collections.shuffle(possibles);
			possibles.get(0).lessPower(2);
			possibles.get(1).lessPower(2);
		}
	}
	
	public static void potion_of_growth(EffectParameters params) {
		//On vérifie tout d'abord qu'il y a bien une cible pour lancer ce sort
		boolean possible = false;
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {
					if (params.getMatrice()[i][j].getPlayer() == params.getCurrentPlayer().getNumber()) {
						possible = true;
					}
				}
			}
		}
		if (!(possible)) { throw new NoTargetEffectException(); }
		
		Unit u = params.getMatrice()[params.getUnitPos()[0]][params.getUnitPos()[1]];
		
		if (u != null) {
			if (u.getPlayer() == params.getCurrentPlayer().getNumber()) {
				u.addPower(3);
			} else {
				throw new WrongEffectParametersException();
			}
		} else {
			throw new WrongEffectParametersException();
		}
	}
	
	public static void summon_militia(EffectParameters params) {
		ArrayList<int[]> possibles = new ArrayList<int[]>();
		
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] == null) {
					possibles.add(new int[] {i, j});
				}
			}
		}
		
		if (possibles.isEmpty()) {
			throw new NoTargetEffectException();
		}
		Collections.shuffle(possibles);
		
		params.getMatrice()[possibles.get(0)[0]][possibles.get(0)[1]] = new Unit("Knight", 0, 0, params.getCurrentPlayer().getNumber(), 4, "Common", EffectDescription.None, "Knight");
	}
	
	public static void wallim(EffectParameters params) {
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {	
					if (params.getMatrice()[i][j].getPlayer() != params.getCurrentPlayer().getNumber()) {
						params.getMatrice()[i][j].lessPower(5);
					}
				}
			}
		}
	}
	
	public static void leo(EffectParameters params) {
		for(int i = 0; i < Constants.height; i++) {
			for(int j = 0; j < Constants.width; j++) {
				if (params.getMatrice()[i][j] != null) {	
					if (params.getMatrice()[i][j].getPlayer() == params.getCurrentPlayer().getNumber()) {
						params.getMatrice()[i][j].addPower(5);
					}
				}
			}
		}
	}
	
	public static void carine(EffectParameters params) {
		params.getCurrentPlayer().setHp(20);
	}
	
}





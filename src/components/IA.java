package components;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class IA extends Player {
	
	public IA(String name, int number, Deck deck) {
		super(name, number, deck);
	}
	
	/* Methode principale qui renvoie un tableau de int correspondant à un indice de carte, une action et des coordonnees. */
	public int[] chooseAction(Board board, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects, boolean changeMade) {
		int[] result = new int[] { -1, -1, -1, -1 };
		
		/* On regarde tout d'abord si il existe au moins une carte coutant trop cher pour l'IA pour ensuite
		   pouvoir en choisir une à échanger si ca n'a pas été déjà fait pendant ce tour. */
		if(needChange() && !(changeMade)) {
			
			result[0] = whatChange(); // Peut etre 1,2,3,4 et represente l'indice de la carte la plus chere.
			result[1] = 2; // Numéro de l'action consistant à échanger une carte.
			return result; // Les deux derniers indices de result n'auront pas d'incidence sur l'échange car pas de coord donc on laisse à -1.
		}
		
		// Ensuite on regarde si l'IA a assez de mana pour jouer une carte et si non elle passe son tour
		if(!(canPlayMinOne())) {
			result[1] = 0;  // Numéro de l'action consistant à passer son tour.
			return result;  // Pas besoin de modifier plus result car si il y a 0 le tour est direct passé donc les autres valeurs restent -1.
		}
		
		// On cherche maintenant la carte permettant de diminuer la force totale des monstres de l'adversaire au maximum.
		int[] bestCardToPlay = bestCardLessPowerOpponent(board, otherPlayer, allEffects);
		
		// Si l'indice 3 de bestCardToPlay est différent de -1 alors il y a une carte qui a été sélectionnée.
		if(bestCardToPlay[3] != -1) {
			result[0] = bestCardToPlay[3]; // Indice de la carte à jouer.
			result[1] = 1;                 // Numéro de l'action consistant à jouer une carte.
			result[2] = bestCardToPlay[1]; // Correspond à la coord i où jouer la carte.
			result[3] = bestCardToPlay[2]; // Correspond à la coord j où jouer la carte.
			return result;
		}
		
		// Si on arrive ici c'est qu'il y a une carte à jouer mais qu'elle n'aura pas d'impact sur la force totale de l'adversaire.
		int[] lastChance = lastChancePlay(board);
		
		//Si on peut jouer quelque part.
		if(lastChance[0] != -1) {
			result[0] = lastChance[0];  // Indice de la carte à jouer.
			result[1] = 1;              // Numéro de l'action consistant à jouer une carte.
			result[2] = lastChance[1];  // i.
			result[3] = lastChance[2];  // j.
		}
		
		// Si on est là rien à jouer du tout et on retourne le numéro de passage de tour.
		result[1] = 0;  // Numéro de l'action consistant à passer son tour.
		return result;
	}
	
	
	
	
	
	
	
	
	
	private boolean canPlayMinOne() {
		for(int x = 0; x < 4; x++) {
			if (hand[x] != null ) {
				if (hand[x].getMana() <= super.getTurnMana()) {
					return true;
				}
			}	
		}
		return false;
	}
	
	
	private int testCardPosition(Board b, Card c, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects, int i, int j) {
		Board bCopy = b.clone();
		boolean result = false;
		try {
			result = bCopy.playCard(c, this, otherPlayer, allEffects, new int[] {i, j});
		} catch (WrongEffectParametersException wE) {
			return -1;
		}
		if(!(result)) {
			return -2;
		} else {
			return bCopy.getPlayerBoardPower(otherPlayer.getNumber());
		}
	}
	
	
	private int[] bestCardLessPowerOpponent(Board b, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects) {
		boolean nextCard = false;
		int[] maximum_numCard = new int[] {-1, -1, -1, -1};
		//  Avec { m, i, j, x } ou m est le power min de l'adversaire i et j sont la ligne et la colonne et x l'indice de la carte dans la main
		
		for(int x = 0; x < 4; x++) { // Pour chaque carte dans la main
			
			if(hand[x] != null) {
				
				if(hand[x].enoughMana(super.getTurnMana())) {
					
					if(hand[x].getType().equals(Constants.TYPE_SPELL)) {
					
						int spellPowerResult =  -1;
						for(int i = 0; i < Constants.height; i++) {
							
							if (nextCard) { break; }
							
							for(int j = 0; j < Constants.width; j++) {
								spellPowerResult = testCardPosition(b, hand[x], otherPlayer, allEffects, i, j);
								
								if(spellPowerResult == -2) {
									nextCard = true;
									break;
								
								} else if (spellPowerResult == -1) {
									continue;
								
								} else {
									if (maximum_numCard[0] == -1) { // 1ere carte a etre testee
										maximum_numCard[0] = spellPowerResult;
										maximum_numCard[1] = i;
										maximum_numCard[2] = j;
										maximum_numCard[3] = x;
									
									} else {
										if (maximum_numCard[0] > spellPowerResult) {
											maximum_numCard[0] = spellPowerResult;
											maximum_numCard[1] = i;
											maximum_numCard[2] = j;
											maximum_numCard[3] = x;
										}
									}
								}
							}
						}
						
						if (nextCard) {
							nextCard = false;
							continue;
						}
					
					} else { // If Unit
						
						int unitPowerResult =  -1;
						for(int[] coord : b.validPosition(super.getNumber())) {
							
							int i = coord[0];
							int j = coord[1];
							
							unitPowerResult = testCardPosition(b, hand[x], otherPlayer, allEffects, i, j);
							
							if(unitPowerResult == -2) {
								break;
							
							} else if (unitPowerResult == -1) {
								continue;
							
							} else {
								if (maximum_numCard[0] == -1) { // 1ere carte a etre testee
									maximum_numCard[0] = unitPowerResult;
									maximum_numCard[1] = i;
									maximum_numCard[2] = j;
									maximum_numCard[3] = x;
								
								} else {
									if (maximum_numCard[0] > unitPowerResult) {
										maximum_numCard[0] = unitPowerResult;
										maximum_numCard[1] = i;
										maximum_numCard[2] = j;
										maximum_numCard[3] = x;
									}
								}
							}
						}
					}
				}
			}
		}
		return maximum_numCard;
	}
	
	
	private boolean needChange() {
		for(int x = 0; x < 4; x++) {
			if(hand[x] != null) {
				if(hand[x].getMana() > super.getTurnMana()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private int whatChange() {
		int result = 0;
		int max = 0;
		
		for(int x = 0; x < 4; x++) {
			
			if (hand[x] != null) {
				if(hand[x].getMana() > max) {
					
					max = hand[x].getMana();
					result = x;
				}
			}
		}
		return result;
	}
	
	
	private int[] lastChancePlay(Board b) {
		int[] result = new int[] { -1, -1, -1 };
		// Où { a, b, c } avec a l'indice de la carte, b et c égaux aux coords i et j.
		
		for(int x = 0; x < 4; x++) {
			if (hand[x] != null) {
				if(hand[x].enoughMana(super.getTurnMana())) {
					if(!(hand[x].getType().equals(Constants.TYPE_SPELL))) {
						ArrayList<int[]> goodCoords = b.validPosition(super.getNumber());
						if(!(goodCoords.isEmpty())) {
							int max = 0;
							for(int[] coord : goodCoords) { // On cherche la position où la ligne de front est la plus loin.
								if (coord[0] > max) {
									max = coord[0];
								}
							}
							for(int[] coord : goodCoords) {
								if (coord[0] == max) {
									result[0] = x;
									result[1] = coord[0];
									result[2] = coord[1];
									return result;
								}
							}
						}
					}
				}
			}
		}
		// Au pire on retourne result avec tout à -1 pour montrer qu'on ne peut pas jouer.
		return result;
	}
	
	
	
	/*
	private int[] bestForTwoTurns(Board b, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects) {
		
		int[] result = new int[] { -1, -1, -1, -1 };
		
		for(int x = 0; x < 4; x++) {
			
			if(hand[x] != null) {
				
				
			}
		}
	}
	*/  // A améliorer plus tard
	

}

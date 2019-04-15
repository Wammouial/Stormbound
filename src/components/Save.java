package components;

import java.io.Serializable;
import java.util.HashSet;

public class Save implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String player1;
	private String player2;

	private String deckname1;
	private String deckname2;
	private String deckname3;

	private String deckname4;
	private String deckname5;
	private String deckname6;

	private HashSet<String> chest;

	private String[] currentdeck1;
	private String[] currentdeck2;
	
	private String[] deck1;
	private String[] deck2;
	private String[] deck3;
	private String[] deck4;
	private String[] deck5;
	private String[] deck6;

	public Save() {
		 player1 = "Player1" ;
		 player2 = "Player2";
		 deckname1 = "Base";
		 deckname2 = "Perso1";
		 deckname3 = "Perso2";
		 deckname4 = "BaseJ2";
		 deckname5 = "Perso4";
		 deckname6 = "Perso5";
		 chest = new HashSet<>();
		 deck1 = new String[]{"Bladestorm", "Felflares", "Heroic Soldiers", "Emerald Towers",
				 "Execution", "Fort of Ebonrock", "Gifted Recruits", "Joust Champions",
				 "Personal Servers", "Siegebreakers", "Veterans of War", "Warfront Runners" };
		 deck2 = new String[]{null, null, null, null,null, null, null, null,
				 null, null, null, null};
		 deck3 = new String[]{null, null, null, null,null, null, null, null,
				 null, null, null, null};
		 deck4 = new String[]{"Bladestorm", "Felflares", "Heroic Soldiers", "Emerald Towers",
						 "Execution", "Fort of Ebonrock", "Gifted Recruits", "Joust Champions",
						 "Personal Servers", "Siegebreakers", "Veterans of War", "Warfront Runners" };
		 deck5 = new String[]{null, null, null, null,null, null, null, null,
				 null, null, null, null};
		 deck6 = new String[]{null, null, null, null,null, null, null, null,
				 null, null, null, null};
		 currentdeck1 = deck1;
		 currentdeck2 = deck4;
		 chest.add("Bladestorm");
		 chest.add("Felflares");
		 chest.add("Heroic Soldiers");
		 chest.add("Emerald Towers");
		 chest.add("Execution");
		 chest.add("Fort of Ebonrock");
		 chest.add("Gifted Recruits");
		 chest.add("Joust Champions");
		 chest.add("Personal Servers");
		 chest.add("Siegebreakers");
		 chest.add("Veterans of War");
		 chest.add("Warfront Runners");
	
		 
	}
	
	@Override
	public String toString() {
		String chaine = "[";
		for (String card : deck1) {
			chaine += card;
			chaine += ",";
		}
		chaine += "]";
		return chaine;
	}

	public String[] getCurrentDeck1() {
		return currentdeck1;
	}

	public void setCurrentDeck1(String[] deck1) {
		this.currentdeck1 = deck1;
	}
	
	public String[] getCurrentDeck2() {
		return currentdeck2;
	}

	public void setCurrentDeck2(String[] deck2) {
		this.currentdeck2 = deck2;
	}
	
	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public String getDeckname1() {
		return deckname1;
	}

	public void setDeckname1(String deckname1) {
		this.deckname1 = deckname1;
	}

	public String getDeckname2() {
		return deckname2;
	}

	public void setDeckname2(String deckname2) {
		this.deckname2 = deckname2;
	}

	public String getDeckname3() {
		return deckname3;
	}

	public void setDeckname3(String deckname3) {
		this.deckname3 = deckname3;
	}

	public String getDeckname4() {
		return deckname4;
	}

	public void setDeckname4(String deckname4) {
		this.deckname4 = deckname4;
	}

	public String getDeckname5() {
		return deckname5;
	}

	public void setDeckname5(String deckname5) {
		this.deckname5 = deckname5;
	}

	public String getDeckname6() {
		return deckname6;
	}

	public void setDeckname6(String deckname6) {
		this.deckname6 = deckname6;
	}

	public HashSet<String> getChest() {
		return chest;
	}

	public void setChest(HashSet<String> chest) {
		this.chest = chest;
	}
	
	public void addToChest(String cardname) {
		chest.add(cardname);
	}

	public String[] getDeck1() {
		return deck1;
	}

	public void setDeck1(String[] deck1) {
		this.deck1 = deck1;
	}

	public String[] getDeck2() {
		return deck2;
	}

	public void setDeck2(String[] deck2) {
		this.deck2 = deck2;
	}

	public String[] getDeck3() {
		return deck3;
	}

	public void setDeck3(String[] deck3) {
		this.deck3 = deck3;
	}

	public String[] getDeck4() {
		return deck4;
	}

	public void setDeck4(String[] deck4) {
		this.deck4 = deck4;
	}

	public String[] getDeck5() {
		return deck5;
	}

	public void setDeck5(String[] deck5) {
		this.deck5 = deck5;
	}

	public String[] getDeck6() {
		return deck6;
	}

	public void setDeck6(String[] deck6) {
		this.deck6 = deck6;
	}
	
	
}

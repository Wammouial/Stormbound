/**
 * 
 */
package components;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author W
 *
 */
public class Player {
	private final String name;
	private int number;
	private int mana;
	private int turnMana;
	public Deck deck;
	public Card[] hand;
	public int hp;

	
	public Player(String name, int number, Deck deck) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.number = number;
		this.deck = deck;
		hand = this.deck.getHand();
		hp = 10;
		mana = 3;
		turnMana = mana;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public void lessHp(int n) {
		hp -= n;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMana() {
		return mana;
	}
	
	public void addManaEndTurn() {
		mana += 1;
	}
	
	public int getTurnMana() {
		return turnMana;
	}
	
	public void initTurnMana() {
		turnMana = mana;
	}
	
	public void lessTurnMana(int n) {
		turnMana -= n;
	}
	
	/* Remplit la main du joueur */
	public void fillHand() {
		for(int i = 0; i < 4; i++) {
			if (hand[i] == null) {
				hand[i] = deck.draw();
			}
		}
	}
	
	public boolean isDead() {
		return hp <= 0;
	}
	
	public String recapLife() {
		return "Il reste " + hp + " points de vie � " + name + ";";
	}
	
	public String winCard(Map<String, Card> cardsMap) {
		// Common     74/100
		// Rare       20/100
		// Epic       05/100
		// Legendary  01/100
		ArrayList<String> bigArray = new ArrayList<String>();
		
		// We create a big array with 74 Strings for a card if she is common etc.
		for(String s : cardsMap.keySet()) {
			String rarity = cardsMap.get(s).getRarity();
			
			int i;
			if (rarity.equals("Common")) {
				i = 74;
			} else if (rarity.equals("Rare")) {
				i = 20;
			} else if (rarity.equals("Epic")) {
				i = 5;
			} else {
				i = 1;
			}
			
			while (i > 0) {
				bigArray.add(s);
				i--;
			}
		}
		return bigArray.get(ThreadLocalRandom.current().nextInt(bigArray.size()));
	}
}

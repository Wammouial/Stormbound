/**
 * 
 */
package components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;


/**
 * @author W
 *
 */
public class Deck {
	private ArrayList<Card> cards;
	private ArrayList<Card> copyFull; /* When the deck is empty and we need to reset it. */
	
	/* Deck name in param. */
	public Deck(String deckName, String[] namesArray, int player, Map<String, Card> allCards) {
		// TODO Auto-generated constructor stub
		cards = new ArrayList<Card>();
		copyFull = new ArrayList<Card>();
		
		for(String name : namesArray) {
			cards.add(allCards.get(name).clone());
			copyFull.add(allCards.get(name).clone());
		}
		
		for(Card c : cards) {
			c.setPlayer(player);
		}
		
		for(Card c : copyFull) {
			c.setPlayer(player);
		}
		shuffle();
	}
	
	/* Si le deck est vide, recree une copie du dek plein et le melange */
	private void ifDeckEmpty() {
		if (cards.size() == 0) {
			for(Card c : copyFull) {
				cards.add(c.clone());
			}
			shuffle();
		}
	}
	
	/* Melange le deck */
	public void shuffle() {
		Random random = new Random();
		Collections.shuffle(cards, random);
	}
	
	/* Pioche une carte du deck */
	public Card draw() {
		int indice = cards.size() - 1;
		Card c = cards.get(indice);
		cards.remove(indice);
		ifDeckEmpty(); // Verif
		return c;
	}
	
	/* Remet une carte dans le deck */
	public void setBack(Card c) {
		cards.add(c);
	}
	
	/* Remet une carte dans le deck, melange et en renvoie une nouvelle piochee */
	public Card changeCard(Card c) {
		setBack(c);
		shuffle();
		return draw();
	}
	
	/* Retourne la main de depart piochee du deck */
	public Card[] getHand() {
		Card[] hand = { draw(), draw(), draw(), draw() } ;
		return hand;
	}
}

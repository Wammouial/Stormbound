/**
 * 
 */
package components;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/* Ici toutes donnees et regles du jeu */

/**
 * @author W
 *
 */
public class GameData {
	private static final Map<String, Card> allCards = Creator.createCards();
	private final Map<String, Consumer<EffectParameters>> allEffects; 
	private Player p1;
	private Player p2;
	private Board board;
	private Scanner reader;
	
	public GameData(boolean iA,Save save) {
		// TODO Auto-generated constructor stub
		allEffects = Creator.createEffects();
		p1 = new Player(save.getPlayer1(), 1, new Deck("Base", save.getCurrentDeck1(), 1, allCards));
		
		if (iA) {
			p2 = new IA("IA", 2, new Deck("Base", save.getCurrentDeck2(), 2, allCards));
		} else {
			p2 = new Player(save.getPlayer2(), 2, new Deck("Base", save.getCurrentDeck2(), 2, allCards));
		}
		
		board = new Board();
		reader = new Scanner(System.in);
		
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Player getP1() {
		return p1;
	}
	
	public Player getP2() {
		return p2;
	}
	
	public Scanner getReader() {
		return reader;
	}
	
	public  Map<String, Consumer<EffectParameters>> getAlleffects() {
		return allEffects;
	}
	
	public static Map<String, Card> getAllcards() {
		return allCards;
	}
}

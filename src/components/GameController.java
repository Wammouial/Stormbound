/**
 * 
 */
package components;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

/* Boucle principale plus events */

/**
 * @author W
 *
 */
public class GameController {

	/*
	 * Tant que la valeur entree par l'utilisateur n'est pas dans l'array chooses,
	 * lui redemande un nombre
	 */
	public static int consoleInput(Integer[] chooses, String message, Scanner reader) {
		int n = -1000;

		while (!(Arrays.asList(chooses).contains(n))) {
			System.out.print("\n" + message);
			if (reader.hasNext()) {
				if (reader.hasNextInt()) {
					n = reader.nextInt();
				} else {
					reader.next(); // On vide le buffer du scanner si pas int.
				}
			}
		}
		return n;
	}

	/* Choix du joueur quand c'est son tour */
	public static int[] consoleInputHand(Scanner reader) {
		/* Le joueur selectionne une carte. */
		Integer[] chooses1 = { 0, 1, 2, 3, 4 };
		System.out.println(
				"[ ? ]Entrez 0 pour passer votre tour ou entrez un chiffre correspondant ï¿½ une carte pour la sï¿½lectionner.");
		int choice1 = consoleInput(chooses1, "Numï¿½ro de la carte que vous voulez sï¿½lectionner : ", reader);

		if (choice1 == 0) {
			return new int[] { 0, 0 };
		}

		Integer[] chooses2 = { 0, 1, 2, 3 };
		/* 0 : annuler, 1 : jouer la carte, 2 : avoir un descriptif de la carte. */
		System.out.println(
				"\n[ ? ]Entrez 0 pour annuler et passer votre tour, 1 pour jouer la carte sï¿½lectionnï¿½e, 2 pour l'ï¿½changer et 3 pour lire ses dï¿½tails.");
		int choice2 = consoleInput(chooses2, "Numï¿½ro de l'action que vous voulez effectuer : ", reader);

		return new int[] { choice1, choice2 };
	}

	/* Entree de coords */
	public static int[] consoleInputCoord(Scanner reader) {
		Integer[] i = { 0, 1, 2, 3, 4 };
		int choice1 = consoleInput(i, "Entrez le numï¿½ro de la ligne ou vous voulez jouer (commence ï¿½ 0 en haut) : ",
				reader);

		Integer[] j = { 0, 1, 2, 3 };
		int choice2 = consoleInput(j,
				"Entrez le numï¿½ro de la colonne ou vous voulez jouer (commence ï¿½ 0 ï¿½ gauche) : ", reader);

		return new int[] { choice1, choice2 };
	}

	/* Affiche la main du joueur */
	public static void consoleOutputHand(Card[] hand) {
		String tirets = new String(new char[60]).replace("\0", "-"); // == "-" * 60

		System.out.println("\n" + tirets + "\nVoici votre main : ");
		for (int i = 0; i < 4; i++) {
			if (hand[i] != null) {
				System.out.print(i + 1 + " : " + hand[i] + " : " + hand[i].getName() + ";\n");
			} else {
				System.out.print(i + 1 + " : " + hand[i] + ";\n");
			}
		}
		System.out.print("\n");
	}

	/* Affichage en cas de victoire d'un joueur */
	public static void consoleOutputWin(Player player) {
		String s = "";
		String tirets = new String(new char[60]).replace("\0", "-");
		s += tirets + "\n";
		s += "|                                                          |\n";
		s += "|                                                          |\n";
		s += "|                    " + player.getName() + " a gagnï¿½ !                    |\n";
		s += "|                                                          |\n";
		s += "|                                                          |\n";
		s += tirets + "\n";
		System.out.println(s);
	}

	/* Main du jeu pour la console */
	public static void consoleMain() {
		Save save;
		try {
			FileInputStream fis = new FileInputStream("save.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			save = (Save) ois.readObject(); // 4
			ois.close();
		} catch (Exception e) {
			System.out.println("aucun fichier trouvé");
			save = new Save();
		}
		GameData gD = new GameData(false, save);
		Scanner reader = gD.getReader();
		Board board = gD.getBoard();
		Player p1 = gD.getP1();
		Player p2 = gD.getP2();
		Map<String, Consumer<EffectParameters>> allEffects = gD.getAlleffects();

		Player currentPlayer = p1;
		Player otherPlayer = p2;

		System.out.println("[ * ] La partie commence !\n");

		// Boucle principale
		while (true) {

			// On vï¿½rifie si quelqun a gagnï¿½
			if (p1.isDead() || p2.isDead()) { // Fin de partie
				if (p1.isDead()) {
					GameController.consoleOutputWin(p2);
				}
				if (p2.isDead()) {
					GameController.consoleOutputWin(p1);
				}
				break;
			}

			System.out.println("[ * ] " + p1.recapLife());
			System.out.println("[ * ] " + p2.recapLife());
			board.playerTurn(currentPlayer, otherPlayer, reader, allEffects);

			currentPlayer = (currentPlayer == p1) ? p2 : p1; // Change the player each turn
			otherPlayer = (currentPlayer == p1) ? p2 : p1;
		}

		reader.close();
	}

	public static void iAConsoleMain() {
		Save save;
		try {
			FileInputStream fis = new FileInputStream("save.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			save = (Save) ois.readObject(); // 4
			ois.close();
		} catch (Exception e) {
			System.out.println("aucun fichier trouvé");
			save = new Save();
		}
		GameData gD = new GameData(true, save);
		Scanner reader = gD.getReader();
		Board board = gD.getBoard();
		Player p1 = gD.getP1();
		IA iA = (IA) gD.getP2();
		Map<String, Consumer<EffectParameters>> allEffects = gD.getAlleffects();

		Player currentPlayer = p1;
		Player otherPlayer = iA;

		System.out.println("[ * ] La partie commence !\n");

		// Boucle principale
		while (true) {

			// On vï¿½rifie si quelqun a gagnï¿½
			if (p1.isDead() || iA.isDead()) { // Fin de partie
				if (p1.isDead()) {
					GameController.consoleOutputWin(iA);
				}
				if (iA.isDead()) {
					GameController.consoleOutputWin(p1);
				}
				break;
			}

			System.out.println("[ * ] " + p1.recapLife());
			System.out.println("[ * ] " + iA.recapLife());

			if (currentPlayer instanceof IA) {
				board.iATurn(iA, otherPlayer, allEffects);
			} else {
				board.playerTurn(currentPlayer, otherPlayer, reader, allEffects);
			}

			currentPlayer = (currentPlayer == p1) ? iA : p1; // Change the player each turn
			otherPlayer = (currentPlayer == p1) ? iA : p1;
		}

		reader.close();
	}

	// Graphs

	public static void wallimleo(ApplicationContext context) {
		Save save;
		try {
			FileInputStream fis = new FileInputStream("save.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			save = (Save) ois.readObject(); // 4
			ois.close();
		} catch (Exception e) {

			save = new Save();
		}
		ScreenInfo screenInfo = context.getScreenInfo();
		float width = screenInfo.getWidth();
		float height = screenInfo.getHeight();
		Home home = new Home();
		GameView view = GameView.initGameGraphics((int) (width / 6), (int) (height / 20),
				(int) (((width / 2.5) + (height / 2.5)) / 2), context);

		while (true) {
			GameView.drawHome(context, view);
			GameView.drawCloseButton(context, view);
			int choice = home.menuHome(context, view); // lance le menu du jeu avec un choix possible

			if (choice == 2) { // si on choisis player vs player

				// init du jeu
				GameData data = new GameData(false, save);
				Board board = data.getBoard();
				Player p1 = data.getP1();
				Player p2 = data.getP2();
				Map<String, Consumer<EffectParameters>> allEffects = data.getAlleffects();

				Player currentPlayer = p1;
				Player otherPlayer = p2;

				GameView.drawBackground(context, view);
				GameView.drawBackButton(context, view, "Abandonner(retour)");
				GameView.drawCloseButton(context, view);
				GameView.drawEndChange(context, view);
				while (true) {
					// On vï¿½rifie si on revenir en arriere
					if (board.wantGoBack()) {
						break;
					}
					// On vï¿½rifie si quelqun a gagnï¿½
					if (p1.isDead() || p2.isDead()) { // Fin de partie
						if (p1.isDead()) {
							view.setIndication(p2.getName() + " a gagner!!");
							GameView.draw(context, data, view);
							GameView.drawIndications(context, view);
							Board.pause(5000);
						}
						if (p2.isDead()) {
							view.setIndication(p1.getName() + " a gagner!!");
							GameView.draw(context, data, view);
							GameView.drawIndications(context, view);
							Board.pause(5000);
						}
						break;
					}

					board.playerTurnGraph(currentPlayer, otherPlayer, allEffects, context, view, data);

					currentPlayer = (currentPlayer == p1) ? p2 : p1; // Change the player each turn
					otherPlayer = (currentPlayer == p1) ? p2 : p1;
				}

			} else if (choice == 1) { // si on chosis player vs IA

				// init du jeu
				GameData gD = new GameData(true, save);
				Board board = gD.getBoard();
				Player p1 = gD.getP1();
				IA iA = (IA) gD.getP2();
				Map<String, Consumer<EffectParameters>> allEffects = gD.getAlleffects();

				Player currentPlayer = p1;
				Player otherPlayer = iA;

				GameView.drawBackground(context, view);
				GameView.drawBackButton(context, view, "Abandonner(retour)");
				GameView.drawCloseButton(context, view);
				GameView.drawEndChange(context, view);

				// Boucle principale
				while (true) {

					// On vï¿½rifie si on revenir en arriere
					if (board.wantGoBack()) {
						break;
					}
					// On vï¿½rifie si quelqun a gagnï¿½
					if (p1.isDead() || iA.isDead()) { // Fin de partie
						if (p1.isDead()) {
							view.setIndication("Vous avez perdu...");
							GameView.draw(context, gD, view);
							GameView.drawIndications(context, view);
							Board.pause(5000);
						}
						if (iA.isDead()) {
							view.setIndication(p1.getName() + " a gagner!!");
							GameView.draw(context, gD, view);
							GameView.drawIndications(context, view);
							Board.pause(2000);
							GameView.clearAll(context, view);
							Map<String, Card> allCards = Creator.createCards();
							String card = p1.winCard(allCards);
							save.addToChest(card);
							view.setIndication(card + " a ete ajouter au coffre !");
							GameView.drawCard(context, view, card, allCards);
							GameView.drawIndications(context, view);
							Board.pause(4000);

						}
						break;
					}

					if (currentPlayer instanceof IA) {
						board.iATurnGraph(iA, otherPlayer, allEffects, context, view, gD);
					} else {
						board.playerTurnGraph(currentPlayer, otherPlayer, allEffects, context, view, gD);
					}

					currentPlayer = (currentPlayer == p1) ? iA : p1; // Change the player each turn
					otherPlayer = (currentPlayer == p1) ? iA : p1;
				}
			} else if (choice == 3) {// si on choisis deck
				Edit edition = new Edit(save);
				edition.menuDeck(context, view);
				save = edition.getSave();
				try {
					FileOutputStream fos = new FileOutputStream("save.ser");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(save); // 4
					oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (choice == 4) {// si on chosis option
				Edit edition = new Edit(save);
				edition.Option(context, view);
				save = edition.getSave();
				try {
					FileOutputStream fos = new FileOutputStream("save.ser");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(save); // 4
					oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			continue;
		}

	}

	public static void graphiqueMain(String[] args) {
		Application.run(Color.BLACK, GameController::wallimleo);
	}
}

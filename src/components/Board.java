/**
 * 
 */
package components;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.function.*;

import java.awt.geom.Point2D;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

/**
 * @author wammouia
 *
 */
public class Board {
	/* Player 1 is on the bottom. */
	private static final int defaultFrontline1 = 4; // Top of the case.
	private static final int defaultFrontline2 = 0; // Bottom of the case.
	private static final int maxFrontline1 = 0;
	private static final int maxFrontline2 = 4;
	private Coordinates selected;
	private boolean goback = false;;

	private Unit[][] matrice;

	private int currentFrontline1;
	private int currentFrontline2;

	public Board() {
		matrice = new Unit[Constants.height][Constants.width];
		Unit defaultUnit = null;

		/* Initialize all matrice's cases to null */
		for (int i = 0; i < Constants.height; i++) {
			for (int j = 0; j < Constants.width; j++) {
				matrice[i][j] = defaultUnit;
			}
		}

		currentFrontline1 = defaultFrontline1;
		currentFrontline2 = defaultFrontline2;
	}

	// Methodes utilis�es par l'IA :

	public Board(Unit[][] matrice, int cFl1, int cFl2) {
		this.matrice = matrice;
		this.currentFrontline1 = cFl1;
		this.currentFrontline2 = cFl2;
	}

	private Unit[][] copyMatrice(Unit[][] matrice) {
		Unit[][] newMat = new Unit[Constants.height][Constants.width];

		for (int i = 0; i < Constants.height; i++) {
			for (int j = 0; j < Constants.width; j++) {
				if (matrice[i][j] != null) {
					newMat[i][j] = (Unit) matrice[i][j].clone();
				}
			}
		}
		return newMat;
	}

	@Override
	public Board clone() {
		// TODO Auto-generated method stub
		return new Board(copyMatrice(matrice), currentFrontline1, currentFrontline2);
	}

	public int getPlayerBoardPower(int currentPlayerNumber) {
		int total = 0;
		for (int i = 0; i < Constants.height; i++) {
			for (int j = 0; j < Constants.width; j++) {
				if (matrice[i][j] != null) {
					if (matrice[i][j].getPlayer() != currentPlayerNumber) {
						total += matrice[i][j].getPower();
					}
				}
			}
		}
		return total;
	}

	// M�thodes g�n�rales :

	/**
	 * Find the current frontline for a given player.
	 * 
	 * @param player
	 *            The frontline owner (1 or 2).
	 * @return The number corresponding to the frontline.
	 */
	public int verifFrontline(int player) {
		int result = 0;

		if (player != 1 && player != 2) {
			return result;
		}

		if (player == 2) {
			result = defaultFrontline2;

			for (int i = defaultFrontline2; i <= maxFrontline2; i++) {
				for (int j = 0; j < Constants.width; j++) {

					if (matrice[i][j] != null) {
						if (matrice[i][j].getPlayer() == 2) {
							result = i;
							result = (result == maxFrontline2) ? result - 1 : result;
						}
					}

				}
			}
		}

		else {
			result = defaultFrontline1;

			for (int i = defaultFrontline1; i >= maxFrontline1; i--) {
				for (int j = 0; j < Constants.width; j++) {

					if (matrice[i][j] != null) {
						if (matrice[i][j].getPlayer() == 1) {
							result = i;
							result = (result == maxFrontline1) ? result + 1 : result;
						}
					}

				}
			}
		}

		return result;
	}

	public void actuFrontline(int playerNumber) {
		if (playerNumber != 1 && playerNumber != 2) {
			throw new InvalidParameterException("playerNumber must be 1 or 2.");
		}

		if (playerNumber == 2) {
			currentFrontline2 = verifFrontline(2);
		} else {
			currentFrontline1 = verifFrontline(1);
		}
	}

	@Override
	public String toString() {
		String gameString = "";

		for (Unit[] line : matrice) {
			for (Unit caseM : line) {
				if (caseM != null) {
					gameString += "|" + caseM + "|";
				} else {
					gameString += "|[-/-](0, 0)|";
				}
			}
			gameString += "\n";
		}

		return gameString;
	}

	/* Verifie les units qui ont 0 ou moins de power et les remplace par null */
	public void cleanDead() {
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[0].length; j++) {
				if (matrice[i][j] != null) {
					if (matrice[i][j].isDead()) {
						matrice[i][j] = null;
					}
				}

			}
		}
	}

	/*
	 * Verifie si les coords donnees sont bonnes pour le board et renvoie un entier
	 * correspondant
	 */
	public int coordValidation(int i, int j) {
		/* Return 1 if coords are inside the matrice */
		/* Return 2 if coords are in player 2 base */
		/* Return 3 if coords are in player 1 base */
		/* Return 0 if coords are not correct */

		if ((i >= 0 && i < Constants.height) && (j >= 0 && j < Constants.width)) {
			return 1;
		} else if ((j >= 0 && j < Constants.width) && (i == -1)) {
			return 2;
		} else if ((j >= 0 && j < Constants.width) && (i == 5)) {
			return 3;
		} else {
			return 0;
		}
	}

	/*
	 * Insere une carte unit dans le board en verifiant les cotes pour se battre si
	 * ennemi
	 */
	public Unit insert(int i, int j, Unit unit, EffectParameters params,
			Map<String, Consumer<EffectParameters>> allEffects) {
		if (coordValidation(i, j) != 1) {
			throw new IllegalStateException();
		}

		Unit newU = (Unit) unit.clone();
		matrice[i][j] = newU; // Clone retourne une card

		if (!(newU.getType().equals(Constants.TYPE_STRUCTURE))) {
			// Unit�s ennemies sur les c�t�s
			if (j > 0) { // Pas sur la colonne toute a gauche
				if (matrice[i][j - 1] != null) {
					if (matrice[i][j - 1].getPlayer() != unit.getPlayer()) {
						move(i, j, i, j - 1, params, allEffects);
					}
				}
			}

			if (j < Constants.width - 1) { // Pas sur la colonne toute � droite
				if (matrice[i][j + 1] != null) {
					if (matrice[i][j + 1].getPlayer() != unit.getPlayer()) {
						move(i, j, i, j + 1, params, allEffects);
					}
				}
			}
		}
		return newU;
	}

	/* Vide la case correspondant a i et j */
	public boolean empty(int i, int j) {
		if (coordValidation(i, j) == 0) {
			return false;
		}
		matrice[i][j] = null;
		return true;
	}

	/* Mouvement de fin de tour pour les Units du joueur courant */
	public void beginTurnMove(Player currentPlayer, Player otherPlayer,
			Map<String, Consumer<EffectParameters>> allEffects) {
		int resultMove;
		Unit u = null;

		if (currentPlayer.getNumber() == 1) {
			for (int j = 0; j < Constants.width; j++) {
				for (int i = 0; i < Constants.height; i++) {
					if (matrice[i][j] != null) {
						u = matrice[i][j];
						if (u.getPlayer() == 1 && u.getType().equals(Constants.TYPE_UNIT)) {
							resultMove = move(i, j, i - 1, j,
									new EffectParameters(matrice, currentPlayer, otherPlayer, new int[] { i, j }),
									allEffects);

							if (resultMove == 2) {
								otherPlayer.lessHp(u.getPower());
							}

							if (resultMove == 1) {
								currentPlayer.lessHp(u.getPower());
							}
						}
					}
				}
			}
		}

		if (currentPlayer.getNumber() == 2) {
			for (int j = 0; j < Constants.width; j++) {
				for (int i = Constants.height - 1; i >= 0; i--) {
					if (matrice[i][j] != null) {
						u = matrice[i][j];
						if (u.getPlayer() == 2 && u.getType().equals(Constants.TYPE_UNIT)) {
							resultMove = move(i, j, i + 1, j,
									new EffectParameters(matrice, currentPlayer, otherPlayer, new int[] { i, j }),
									allEffects);

							if (resultMove == 2) {
								currentPlayer.lessHp(u.getPower());
							}

							if (resultMove == 1) {
								otherPlayer.lessHp(u.getPower());
							}

						}
					}
				}
			}
		}
	}

	/* Mouvement (comprenant l'attaque et les effets d'attaque) */
	public int move(int oldI, int oldJ, int newI, int newJ, EffectParameters params,
			Map<String, Consumer<EffectParameters>> allEffects) {
		if (coordValidation(newI, newJ) == 0 || coordValidation(oldI, oldJ) == 0) {
			return 0;
		}

		if (coordValidation(oldI, oldJ) == 1 && coordValidation(newI, newJ) == 2) {
			/* Enlever PVs a player2 */
			matrice[oldI][oldJ] = null;
			return 2;
		}

		if (coordValidation(oldI, oldJ) == 1 && coordValidation(newI, newJ) == 3) {
			/* Enlever PVs a player1 */
			matrice[oldI][oldJ] = null;
			return 1;
		}

		if (coordValidation(oldI, oldJ) == 1 && coordValidation(newI, newJ) == 1) {

			if (!(isEmpty(newI, newJ)) && !(isEmpty(oldI, oldJ))) {

				if (matrice[newI][newJ].getPlayer() != matrice[oldI][oldJ].getPlayer()) {

					// On verifie si effet d'avant attaque
					if (matrice[oldI][oldJ].hasEffect()) {
						if (matrice[oldI][oldJ].getEffect().equals(Constants.EF_ON_ATTACK)) {

							try {
								allEffects.get(matrice[oldI][oldJ].getName()).accept(params); // Appel de l'effet
							} catch (NoTargetEffectException nE) {
								System.out.println("\n[ * ] L'effet de la carte n'a pas �t� activ�");
							} catch (WrongEffectParametersException wE) {
								System.out.println("\n[ * ] L'effet de la carte n'a pas �t� activ�");
							}
						}
					}

					matrice[newI][newJ] = matrice[oldI][oldJ].fight(matrice[newI][newJ]);
					matrice[oldI][oldJ] = null;
				} else {
					return 0;
				}

			} else {
				matrice[newI][newJ] = matrice[oldI][oldJ];
				matrice[oldI][oldJ] = null;
			}
			return 5;
		}
		return 5;
	}

	/* Verifie si une case est vide */
	public boolean isEmpty(int i, int j) {
		if (coordValidation(i, j) == 0) {
			throw new InvalidParameterException("Coords mauvaises dans isEmpty\n");
		}
		return matrice[i][j] == null;
	}

	/*
	 * Renvoie l'ArrayList des positions valides pour jouer une carte pour un des
	 * joueurs
	 */
	public ArrayList<int[]> validPosition(int playerNumber) {
		if (playerNumber != 1 && playerNumber != 2) {
			throw new InvalidParameterException("playerNumber must be 1 or 2.");
		}

		ArrayList<int[]> valids = new ArrayList<int[]>();

		if (playerNumber == 2) {
			for (int i = defaultFrontline2; i <= currentFrontline2; i++) {
				for (int j = 0; j < Constants.width; j++) {
					if (isEmpty(i, j)) {
						valids.add(new int[] { i, j });
					}
				}
			}
		} else {
			for (int i = defaultFrontline1; i >= currentFrontline1; i--) {
				for (int j = 0; j < Constants.width; j++) {
					if (isEmpty(i, j)) {
						valids.add(new int[] { i, j });
					}
				}
			}
		}
		return valids;
	}

	public int[] chooseFromValid(Player player, Scanner reader) {

		/* Verification de la position ou jouer la carte */
		ArrayList<int[]> validPosition = validPosition(player.getNumber());
		boolean isValid = false;
		int coord[] = null; // Juste pour eviter qu'Eclipse detecte une erreur de non initialisation

		while (!(isValid)) {
			/* On print les coords o� il est possible de jouer. */
			String positions = "";
			for (int[] coords : validPosition) {
				positions += "(" + coords[0] + ", " + coords[1] + "), ";
			}
			System.out.println("Positions o� jouer disponibles : ");
			System.out.println(positions);

			/* On r�cup�re les coords. */
			coord = GameController.consoleInputCoord(reader);
			for (int[] position : validPosition) { // Remplace le contains car contient des arrays de int
				if (Arrays.equals(coord, position)) {
					isValid = true; // Si la boucle est quitt�e coord est donc bonne.
				}
			}
			if (!(isValid)) {
				System.out.println("[ ! ] Votre position n'est pas valide. R�essayez.");
			}
		}
		return coord;
	}

	/* Fonction principale du tour d'un joueur */
	public void playerTurn(Player player, Player otherPlayer, Scanner reader,
			Map<String, Consumer<EffectParameters>> allEffects) {
		int playerNumber = player.getNumber();
		player.initTurnMana(); // player.turnMana = mana
		boolean endTurn = false;
		boolean changeMade = false; // Car on ne peut faire qu'un echange par tour.

		// beginTurnMove and actuFrontline
		beginTurnMove(player, otherPlayer, allEffects);
		actuFrontline(player.getNumber());

		// Effets de d�but de tour
		for (int a = 0; a < Constants.height; a++) {
			for (int b = 0; b < Constants.width; b++) {
				if (matrice[a][b] != null) {
					if (matrice[a][b].getPlayer() == player.getNumber()) {
						if (matrice[a][b].hasEffect()) {
							if (matrice[a][b].getEffect().equals(Constants.EF_ON_BEGIN_TURN)) {

								try {
									launchEffect(matrice[a][b], player, otherPlayer, allEffects, new int[] { a, b });
								} catch (WrongEffectParametersException wE) {
									; // If effect can't be lauched we do nothing
								}

							}
						}
					}
				}
			}
		}

		System.out.println("\nAu tour du joueur " + playerNumber + " :");

		while (!(endTurn)) {
			System.out.println("\n" + this);
			System.out.println("\nVous avez " + player.getTurnMana() + " cristaux de mana.");
			GameController.consoleOutputHand(player.hand);
			int[] choose = GameController.consoleInputHand(reader);

			cleanDead();
			if (choose[0] == 0 || choose[1] == 0) {
				String tirets = new String(new char[60]).replace("\0", "-");
				System.out.println("\n[ * ] Vous avez pass� votre tour.\n");
				System.out.println(tirets);
				endTurn = true;

			} else { // choose[0] repr�sente le num�ro de la carte et choose[1] l'action �
						// effectuer.

				if (player.hand[choose[0] - 1] == null) { // Au cas ou il y a moins de 4 cartes dans la main et le
															// joueur selectionne null.
					System.out.println("\n[ ! ] Vous avez s�lectionn� un emplacement vide de la main !");
					continue;
				}

				if (choose[1] == 2) { // Change card
					if (!(changeMade)) {
						player.hand[choose[0] - 1] = player.deck.changeCard(player.hand[choose[0] - 1]);
						changeMade = true;
						System.out.println("\n[ * ] La carte a bien �t� �chang�e.");
					} else {
						System.out.println("\n[ ! ] Vous avez d�j� fait un �change ce tour !");
						continue;
					}

				} else if (choose[1] == 3) { // Lire description
					Card c = player.hand[choose[0] - 1];
					String infos = "\n[ * ] La carte '" + c.getName() + "' est un(e) " + c.getType() + " co�tant "
							+ c.getMana() + " cristaux de mana.";
					if (c.getType().equals(Constants.TYPE_SPELL)) {
						infos += "\n[ * ] L'effet de ce sort est : " + c.getDescription(); // Ajouter effet descri ici

					} else if (c.getType().equals(Constants.TYPE_UNIT)) {
						infos += "\n[ * ] La carte a " + c.getMoveSpeed() + " points de d�placement et "
								+ c.getPower() + " points d'attaque." + "\n[ * ] La carte est " + c.getRarity() + ".";

						Unit u = (Unit) c;
						if (u.getUnitType() != "") {
							infos += " Elle est de la famille " + u.getUnitType() + ".";
						}

						if (c.hasEffect()) {
							infos += "\n[ * ] Son effet est : " + c.getDescription();
						}

					}
					System.out.println(infos);

				} else if (choose[1] == 1) {
					/* Verification du mana */
					Card c = player.hand[choose[0] - 1];

					if (!(c.enoughMana(player.getTurnMana()))) { // Si pas assez de mana pour la carte
						System.out.println("\n[ ! ] Vous n'avez pas assez de mana pour jouer cette carte !");
						continue;
					}

					if (c.getType().equals(Constants.TYPE_SPELL)) {

						boolean successPlay = false;
						boolean spellLaunched = false;
						int[] coord = null;

						while (!(spellLaunched)) {

							try {
								successPlay = playCard(c, player, otherPlayer, allEffects, coord);
								spellLaunched = true;
							} catch (WrongEffectParametersException wE) {
								System.out.println("\n[ ! ] Vous devez choisir un endroit o� jouer votre sort !");
								coord = GameController.consoleInputCoord(reader);
							}
						}

						if (!(successPlay)) {
							System.out.println("\n[ ! ] Cet effet ne peut �tre jou� nul part.");
							continue;
						}

					} else { // If Unit

						boolean effectLaunched = false;
						int[] coord = null;

						while (!(effectLaunched)) {
							coord = chooseFromValid(player, reader);

							try {
								playCard(c, player, otherPlayer, allEffects, coord);
								effectLaunched = true;
							} catch (WrongEffectParametersException wE) {
								System.out.println("\n[ ! ] L'effet de la carte ne peut pas s'activer ici.");
								// Jamais ca n'arrivera normalement
							}
						}
					}

					/* On retire la carte de la main. */
					player.lessTurnMana(c.getMana());
					player.hand[choose[0] - 1] = null;
					cleanDead();
					actuFrontline(player.getNumber());
				}
			}
		}
		// Actu frontline
		cleanDead();
		actuFrontline(player.getNumber());
		player.addManaEndTurn();
		player.fillHand();
	}

	public boolean launchEffect(Card c, Player player, Player otherPlayer,
			Map<String, Consumer<EffectParameters>> allEffects, int[] coord) {
		String effectName = c.getName();

		EffectParameters ef = null;
		if (coord == null) {
			ef = new EffectParameters(matrice, player, otherPlayer);
		} else {
			ef = new EffectParameters(matrice, player, otherPlayer, coord);
		}

		try {
			allEffects.get(effectName).accept(ef); // Appel de l'effet
			return true;
		} catch (NoTargetEffectException nE) {
			return false; // Can't launch this spell
		} catch (WrongEffectParametersException wE) {
			throw new WrongEffectParametersException(); // La fonction qui l'appelle devra changer coord
		}
	}

	// Pas oublier de retirer le mana apres ca si true
	public boolean playCard(Card c, Player player, Player otherPlayer,
			Map<String, Consumer<EffectParameters>> allEffects, int[] coord) {

		if (c.getType().equals(Constants.TYPE_SPELL)) {

			try {
				boolean result = launchEffect(c, player, otherPlayer, allEffects, coord);
				return result;
			} catch (WrongEffectParametersException wE) {
				throw new WrongEffectParametersException(); // In case of bad coord
			}

		} else { // If Unit

			int resultMove;
			// On lance l'effet si il y en a un on_play
			if (c.hasEffect()) {
				if (c.getEffect().equals(Constants.EF_ON_PLAY)) {

					try {
						launchEffect(c, player, otherPlayer, allEffects, coord);
					} catch (WrongEffectParametersException wE) {
						throw new WrongEffectParametersException(); // In case of bad coord
					}
				}
			}

			Unit u = (Unit) c;
			int i = coord[0], j = coord[1];

			u = insert(i, j, u, new EffectParameters(matrice, player, otherPlayer, coord), allEffects);

			/* Maintenant on bouge l'unit� en fonction de ses points de mouvement */
			for (int k = 0; k < u.getMoveSpeed(); k++) {
				if (!(u.isDead())) {
					int newI = 0; // Eviter bug Eclipse
					if (player.getNumber() == 1) {
						newI = i - 1;
					}
					if (player.getNumber() == 2) {
						newI = i + 1;
					}

					resultMove = move(i, j, newI, j,
							new EffectParameters(matrice, player, otherPlayer, new int[] { i, j }), allEffects);

					if (resultMove == 5) {
						if (player.getNumber() == 1) {
							i--;
						}
						if (player.getNumber() == 2) {
							i++;
						}

					} else if (resultMove == 2) {
						if (player.getNumber() == 1) {
							otherPlayer.lessHp(u.getPower());
						} else {
							player.lessHp(u.getPower());
						}

					} else if (resultMove == 1) {
						if (player.getNumber() == 2) {
							otherPlayer.lessHp(u.getPower());
						} else {
							player.lessHp(u.getPower());
						}

					} else {
						break;
					}

				} else {
					break;
				}

			}
			return true;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*--------------methode de verification graphique (emplacement o� l'on clique ainsi que resultat
	 *	pour eviter de trop surcharger playerturn --------------------*/

	public Coordinates getSelected() {
		return selected;
	}

	public boolean hasASelectedCell() {
		return selected != null;
	}

	public void selectCell(int i, int j) {
		if (selected != null) {
			throw new IllegalStateException("First cell already selected");
		}
		selected = new Coordinates(i, j);
	}

	public void unselect() {
		selected = null;
	}

	public Unit getCellValue(int i, int j) {
		return matrice[i][j];
	}

	public int getFrontline1() {
		return currentFrontline1;
	}

	public int getFrontline2() {
		return currentFrontline2;
	}

	public boolean endOfTurn(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > v.getxOrigin() + 5 * v.getSquareSize()
				&& c.getI() < v.getxOrigin() + 5 * v.getSquareSize() + v.getSquareSize()
				&& c.getJ() > v.getyOrigin() + 2 * v.getSquareSize()
				&& c.getJ() < v.getyOrigin() + 2 * v.getSquareSize() + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	public int takeCard(Player player, GameView v) {
		int count = 0;
		int indi = 0;
		for (Card card : player.hand) {
			if (card == null) {
				indi++;
				count++;
				continue;
			}
			Coordinates c = getSelected();
			if (c != null) {
				if (c.getI() >= (v.getxOrigin() + v.getSquareSize() / 2) + (int) (v.getSquareSize() * 2.5) * count
						&& c.getI() < (v.getxOrigin() + v.getSquareSize() / 2) + (int) (v.getSquareSize() * 2.5) * count
								+ v.getSquareSize() + v.getSquareSize() / 2
						&& c.getJ() >= v.getyOrigin() + ((Constants.height + 1) * v.getSquareSize())
						&& c.getJ() < v.getyOrigin() + ((Constants.height + 1) * v.getSquareSize())
								+ v.getSquareSize() * 2) {
					return indi;
				}
			}
			count++;
			indi++;
		}
		return 5;
	}

	public boolean goodCase(Player player, GameView view) {
		Coordinates c = getSelected();
		int[] coord = { view.lineFromY(c.getJ()), view.columnFromX(c.getI()) };
		ArrayList<int[]> validPosition = validPosition(player.getNumber());
		for (int[] position : validPosition) { // Remplace le contains car contient des arrays de int
			if (Arrays.equals(coord, position)) {
				return true; // Si la boucle est quitt�e coord est donc bonne.
			}
		}
		return false;
	}

	private boolean wantChangeCard(Player player, GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > v.getxOrigin() + 5 * v.getSquareSize()
				&& c.getI() < v.getxOrigin() + 5 * v.getSquareSize() + v.getSquareSize()
				&& c.getJ() > v.getyOrigin() + 4 * v.getSquareSize()
				&& c.getJ() < v.getyOrigin() + 4 * v.getSquareSize() + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	private boolean clicOnBoard(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() >= v.getxOrigin() && c.getI() < v.getxOrigin() + Constants.width * v.getSquareSize()
				&& c.getJ() >= v.getyOrigin() && c.getJ() < v.getyOrigin() + Constants.height * v.getSquareSize()) {
			return true;
		}

		return false;
	}

	public boolean wantGoBack() {
		return goback;
	}

	private boolean clicOnBack(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() >= 0 && c.getI() < v.getSquareSize() && c.getJ() >= 0 && c.getJ() < v.getSquareSize()) {
			return true;
		}

		return false;
	}

	private boolean clicOnClose(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() >= v.getscrWidth() - v.getSquareSize() / 2 && c.getI() < v.getscrWidth() && c.getJ() >= 0
				&& c.getJ() < v.getSquareSize() / 2) {
			return true;
		}

		return false;
	}

	/* Fonction principale du tour d'un joueur graphique */
	public void playerTurnGraph(Player player, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects,
			ApplicationContext context, GameView view, GameData data) {
		Point2D.Float location;
		int playerNumber = player.getNumber();
		player.initTurnMana(); // player.turnMana = mana
		boolean endTurn = false;
		boolean changeMade = false; // Car on ne peut faire qu'un echange par tour.

		// beginTurnMove and actuFrontline
		beginTurnMove(player, otherPlayer, allEffects);
		actuFrontline(player.getNumber());

		// Effets de d�but de tour
		for (int a = 0; a < Constants.height; a++) {
			for (int b = 0; b < Constants.width; b++) {
				if (matrice[a][b] != null) {
					if (matrice[a][b].getPlayer() == player.getNumber()) {
						if (matrice[a][b].hasEffect()) {
							if (matrice[a][b].getEffect().equals(Constants.EF_ON_BEGIN_TURN)) {

								try {
									launchEffect(matrice[a][b], player, otherPlayer, allEffects, new int[] { a, b });
								} catch (WrongEffectParametersException wE) {
									; // If effect can't be lauched we do nothing
								}

							}
						}
					}
				}
			}
		}

		GameView.draw(context, data, view);
		GameView.drawMain(context, data, view, player);
		view.setIndication("au tour de " + player.getName());
		GameView.drawIndications(context, view);

		int indice = 5;
		while (!(endTurn)) {

			cleanDead();
			// GameView.draw(context, data, view); //si on veut un rafraichissement
			// permanenet
			// GameView.drawMain(context, data, view, player);

			Event event = context.pollOrWaitEvent(10);
			if (event == null) { // no event
				continue;
			}

			Action action = event.getAction();

			if (action != Action.POINTER_DOWN) {
				continue;
			}

			if (event.getLocation() != null) {

				unselect();
				location = event.getLocation();
				selectCell((int) location.x, (int) location.y);
				// si on a cliqer sur fin de tour alors on termine son tour

				if (clicOnBack(view)) {
					goback = true;
					break;
				}

				else if (clicOnClose(view)) {
					context.exit(0);
				}

				else if (endOfTurn(view) == true) {
					unselect();
					endTurn = true;
					break;
				}

				// memorise l'indice si une carte a �t� prise ( pour l'evenement suivant )
				else if (takeCard(player, view) != 5) {
					indice = takeCard(player, view);
					if (player.hand[indice].getType().equals(Constants.TYPE_SPELL))
						view.setIndication("Une carte magique a ete selectionnee");
					else if (player.hand[indice].getType().equals(Constants.TYPE_UNIT))
						view.setIndication("Une unite a ete selectionnee");
					else if (player.hand[indice].getType().equals(Constants.TYPE_STRUCTURE))
						view.setIndication("Une structure a ete selectionnee");
				}

				else if (wantChangeCard(player, view) == true && indice != 5) { // Change card

					if (!(changeMade)) {
						player.hand[indice] = player.deck.changeCard(player.hand[indice]);
						changeMade = true;
						view.setIndication("La carte a bien ete changee.");
						unselect();
						indice = 5;
					} else {
						view.setIndication("Vous avez deja fait un echange ce tour !");
						unselect();
						indice = 5;
					}
				}

				/*
				 * regarde si il y a une carte actuellement prise et si les coord du clic sont
				 * sur le plateau ( matrice )
				 */

				else if (indice != 5 && clicOnBoard(view) == true) {

					// si assez de mana
					if (player.hand[indice].enoughMana(player.getTurnMana())) {
						Coordinates c = getSelected();
						int[] coord = { view.lineFromY(c.getJ()), view.columnFromX(c.getI()) };

						// si on a une unit� ou structure selectionn�
						if (!(player.hand[indice].getType().equals(Constants.TYPE_SPELL))) {

							if (goodCase(player, view) == true) {
								Card card = player.hand[indice];

								try {
									playCard(card, player, otherPlayer, allEffects, coord);
								} catch (WrongEffectParametersException wE) {
									// System.out.println("\n[ ! ] L'effet de la carte ne peut pas s'activer ici.");
									// Jamais ca n'arrivera normalement
									;
								}
								view.setIndication("La carte a bien ete posee");
								/* On retire la carte de la main. */
								player.lessTurnMana(player.hand[indice].getMana());
								player.hand[indice] = null;
								indice = 5;
								cleanDead();
								actuFrontline(playerNumber);

							} else { // si on a une unit� mais que l'endroit o� l'on clic est invalide
								view.setIndication("La carte ne peut pas etre jou�e � cette endroit!");
								indice = 5;
							}
						}

						// si on a une carte magique
						else if (player.hand[indice].getType().equals(Constants.TYPE_SPELL)) {

							Card card = player.hand[indice];
							boolean successPlay = false;

							try {
								successPlay = playCard(card, player, otherPlayer, allEffects, coord);
							} catch (WrongEffectParametersException wE) {
								view.setIndication("La carte ne peut pas etre jou�e � cette endroit!");
								indice = 5;
							}

							if (!(successPlay)) {
								view.setIndication("La carte ne peut pas etre jou�e � cette endroit!");
								indice = 5;
							} else {
								view.setIndication("Le sort a bien ete utilise !");
								/* On retire la carte de la main. */
								player.lessTurnMana(player.hand[indice].getMana());
								player.hand[indice] = null;
								indice = 5;
								cleanDead();
								actuFrontline(playerNumber);
							}

						}

					} else {// autrement on a pas assez de mana
						view.setIndication("Pas assez de Mana");
						indice = 5;
					}

				} else {
					// si l'on clic sans provoquer d'evenement ou pas sur une autre carte l'indice
					// memoriser s'en vas
					indice = 5;
					view.setIndication("au tour de " + player.getName());
				}
			}
			GameView.draw(context, data, view);
			GameView.drawMain(context, data, view, player);
			GameView.drawIndications(context, view);
		}
		cleanDead();
		actuFrontline(player.getNumber());
		player.addManaEndTurn();
		player.fillHand();
	}

	public void iATurn(IA iA, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects) {
		int iANumber = iA.getNumber();
		iA.initTurnMana();
		boolean endTurn = false;
		boolean changeMade = false; // Car on ne peut faire qu'un echange par tour.

		// beginTurnMove and actuFrontline
		beginTurnMove(iA, otherPlayer, allEffects);
		actuFrontline(iANumber);

		// Effets de d�but de tour
		for (int a = 0; a < Constants.height; a++) {
			for (int b = 0; b < Constants.width; b++) {
				if (matrice[a][b] != null) {
					if (matrice[a][b].getPlayer() == iANumber) {
						if (matrice[a][b].hasEffect()) {
							if (matrice[a][b].getEffect().equals(Constants.EF_ON_BEGIN_TURN)) {

								try {
									launchEffect(matrice[a][b], iA, otherPlayer, allEffects, new int[] { a, b });
								} catch (WrongEffectParametersException wE) {
									; // If effect can't be lauched we do nothing
								}

							}
						}
					}
				}
			}
		}

		System.out.println("\nA l'IA de jouer.");

		while (!(endTurn)) {
			System.out.println("\n" + this);
			System.out.println("\nL'IA a " + iA.getTurnMana() + " cristaux de mana.");

			// Choix de l'IA.
			int[] choose = iA.chooseAction(this, otherPlayer, allEffects, changeMade);

			cleanDead();
			if (choose[1] == 0) {
				String tirets = new String(new char[60]).replace("\0", "-");
				System.out.println("\n[ * ] L'IA a pass� son tour.\n");
				System.out.println(tirets);
				endTurn = true;

			} else { // choose[0] repr�sente le num�ro de la carte et choose[1] l'action �
						// effectuer.

				if (choose[1] == 2) { // Change card

					iA.hand[choose[0]] = iA.deck.changeCard(iA.hand[choose[0]]);
					changeMade = true;
					System.out.println("\n[ * ] L'IA a echang� une carte.");

				} else if (choose[1] == 1) {

					Card c = iA.hand[choose[0]];

					if (c.getType().equals(Constants.TYPE_SPELL)) {

						int[] coord = new int[] { choose[2], choose[3] };

						playCard(c, iA, otherPlayer, allEffects, coord);

					} else { // If Unit

						int[] coord = new int[] { choose[2], choose[3] };

						playCard(c, iA, otherPlayer, allEffects, coord);
					}

					/* On retire la carte de la main. */
					System.out.println("\n[ * ] L'IA a jou� la carte " + c.getName() + " � la ligne " + choose[2]
							+ " et la colonne " + choose[3] + ";");
					iA.lessTurnMana(c.getMana());
					iA.hand[choose[0]] = null;
					cleanDead();
					actuFrontline(iANumber);
				}
			}
		}
		// Actu frontline
		cleanDead();
		actuFrontline(iANumber);
		iA.addManaEndTurn();
		iA.fillHand();
	}

	// tour de l'IA pour l'interface graphique
	public void iATurnGraph(IA iA, Player otherPlayer, Map<String, Consumer<EffectParameters>> allEffects,
			ApplicationContext context, GameView view, GameData data) {
		int iANumber = iA.getNumber();
		iA.initTurnMana();
		boolean endTurn = false;
		boolean changeMade = false; // Car on ne peut faire qu'un echange par tour.

		// beginTurnMove and actuFrontline
		beginTurnMove(iA, otherPlayer, allEffects);
		actuFrontline(iANumber);

		// Effets de d�but de tour
		for (int a = 0; a < Constants.height; a++) {
			for (int b = 0; b < Constants.width; b++) {
				if (matrice[a][b] != null) {
					if (matrice[a][b].getPlayer() == iANumber) {
						if (matrice[a][b].hasEffect()) {
							if (matrice[a][b].getEffect().equals(Constants.EF_ON_BEGIN_TURN)) {

								try {
									launchEffect(matrice[a][b], iA, otherPlayer, allEffects, new int[] { a, b });
								} catch (WrongEffectParametersException wE) {
									; // If effect can't be lauched we do nothing
								}

							}
						}
					}
				}
			}
		}

		GameView.draw(context, data, view);
		view.setIndication("au tour de l'IA ");
		GameView.drawIndications(context, view);

		while (!(endTurn)) {
			GameView.draw(context, data, view);
			GameView.drawIndications(context, view);
			pause(1000);
			// Choix de l'IA.
			int[] choose = iA.chooseAction(this, otherPlayer, allEffects, changeMade);

			cleanDead();
			if (choose[1] == 0) {
				view.setIndication("L'IA a pass� son tour.");
				GameView.draw(context, data, view);
				GameView.drawIndications(context, view);
				pause(500);
				endTurn = true;

			} else { // choose[0] repr�sente le num�ro de la carte et choose[1] l'action �
						// effectuer.

				if (choose[1] == 2) { // Change card

					iA.hand[choose[0]] = iA.deck.changeCard(iA.hand[choose[0]]);
					changeMade = true;
					view.setIndication("L'IA a echang� une carte.");

				} else if (choose[1] == 1) {

					Card c = iA.hand[choose[0]];

					if (c.getType().equals(Constants.TYPE_SPELL)) {

						int[] coord = new int[] { choose[2], choose[3] };

						playCard(c, iA, otherPlayer, allEffects, coord);

					} else { // If Unit

						int[] coord = new int[] { choose[2], choose[3] };

						playCard(c, iA, otherPlayer, allEffects, coord);
					}

					/* On retire la carte de la main. */
					view.setIndication("L'IA a jou� la carte " + c.getName());
					iA.lessTurnMana(c.getMana());
					iA.hand[choose[0]] = null;
					cleanDead();
					actuFrontline(iANumber);
				}
			}
		}
		// Actu frontline
		cleanDead();
		actuFrontline(iANumber);
		iA.addManaEndTurn();
		iA.fillHand();
	}

	public static void pause(int x) {
		try {
			Thread.sleep(x);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

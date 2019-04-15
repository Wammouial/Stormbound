package components;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class Edit {
	private Coordinates selected;
	private boolean goback = false;
	private Save save;
	private String[] currentdeck;
	private String currentdeckname;
	private String currentplayername;
	private String currentfaction;
	private ArrayList<ArrayList<Card>> currentchest;
	private static final Map<String, Card> allCards = Creator.createCards();

	public Edit(Save save) {
		this.save = save;
	}

	public Save getSave() {
		return save;
	}

	private int countCard(String[] deck) {
		int count = 0;
		for (String cardname : deck) {
			if (cardname != null) {
				count++;
			}
		}
		return count;
	}

	public Coordinates getSelected() {
		return selected;
	}

	private void giveAllCard() {
		HashSet<String> chest = new HashSet<>();
		for (String card : allCards.keySet()) {
			chest.add(card);
		}
		save.setChest(chest);

	}

	private int recapFaction() {
		for (String name : currentdeck) {
			if (name != null) {
				Card card = allCards.get(name);
				if (card.getFaction() != "Neutral") {
					currentfaction = card.getFaction();
					return 1;
				}
			}
		}
		currentfaction = "Neutral";
		return 0;
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

	public boolean wantGoBack() {
		return goback;
	}

	private boolean deckComplet() {
		for (String card : currentdeck) {
			if (card == null) {
				return false;
			}
		}
		return true;
	}

	private boolean clicOnBack(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() >= 0 && c.getI() < v.getSquareSize() && c.getJ() >= 0 && c.getJ() < v.getSquareSize()) {
			return true;
		}

		return false;
	}

	private boolean clicOnName(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() >= (int) (v.getSquareSize() * 1.5) && c.getI() < (int) (v.getSquareSize() * 3.5) && c.getJ() >= 0
				&& c.getJ() < v.getSquareSize()) {
			return true;
		}

		return false;
	}

	private boolean nextPage(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > (int) (v.getSquareSize() * 12.0833)
				&& c.getI() < (int) (v.getSquareSize() * 12.0833) + v.getSquareSize()
				&& c.getJ() > (int) (v.getSquareSize() * 7.5)
				&& c.getJ() < (int) (v.getSquareSize() * 7.5) + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	private boolean backPage(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > (int) (v.getSquareSize() * 10.833)
				&& c.getI() < (int) (v.getSquareSize() * 10.833) + v.getSquareSize()
				&& c.getJ() > (int) (v.getSquareSize() * 7.5)
				&& c.getJ() < (int) (v.getSquareSize() * 7.5) + v.getSquareSize()) {
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

	private int clicOnDeck(GameView v) {
		Coordinates c = getSelected();
		int e = (int) (v.getSquareSize() * 0.208);
		for (int i = 0; i < 6; i++) {

			if (c.getI() >= v.getscrWidth() / 4 && c.getI() < v.getscrWidth() / 4 + v.getSquareSize() && c.getJ() >= e
					&& c.getJ() < e + v.getSquareSize()) {
				return i;
			}
			e += (int) (v.getSquareSize() * 1.458);

		}
		return -1;
	}

	private int selectCurrentDeck(GameView v) {
		Coordinates c = getSelected();
		int e = (int) (v.getSquareSize() * 0.208);
		for (int i = 0; i < 6; i++) {

			if (c.getI() >= v.getscrWidth() / 6 && c.getI() < v.getscrWidth() / 6 + v.getSquareSize() && c.getJ() >= e
					&& c.getJ() < e + v.getSquareSize()) {
				return i;
			}
			e += (int) (v.getSquareSize() * 1.458);

		}
		return -1;
	}

	private int takeCardDeck(GameView v) {
		int y = 0;
		for (String cardname : currentdeck) {
			if (cardname == null) {
				y += 1;
				continue;

			}
			Coordinates c = getSelected();
			if (c.getI() > 0 && c.getI() < 0 + v.getSquareSize() + (int) (v.getSquareSize() * 1.5)
					&& c.getJ() > (int) (v.getSquareSize() * 1.083 + (y * 2 * v.getSquareSize() / 3))
					&& c.getJ() < (int) (v.getSquareSize() * 1.083 + (y * 2 * v.getSquareSize() / 3))
							+ (int) (v.getSquareSize() / 2)) {
				return y;
			}
			y += 1;
		}
		return -1;
	}

	private int takeCardChest(GameView v, int currentindice) {
		currentchest = new ArrayList<>();
		int i = 0;
		int indice = 0;
		ArrayList<Card> page = new ArrayList<>();
		currentchest.add(indice, page);
		for (String name : save.getChest()) {
			Card card = allCards.get(name);
			boolean deckalreadhave = false;
			for (String cardname : currentdeck) {
				if (card.getName().equals(cardname)) {
					deckalreadhave = true;
					break;
				}
			}
			if (deckalreadhave) {
				continue;
			}

			if (currentchest.contains(page)) {
				currentchest.get(indice).add(card);
			} else {
				page.add(card);
				currentchest.add(indice, page);
			}
			if (i > 7) {
				page = new ArrayList<>();
				indice++;
				i = -1;
			}
			i++;
		}
		if (currentchest.size() > currentindice) {
			if (currentchest.get(currentindice).size() > 0) {
				int count = 0;
				int y = 0;
				int indiceofcard = 0;
				for (Card card : currentchest.get(currentindice)) {
					if (count > 2) {
						count = 0;
						y += 3;
					}
					Coordinates c = getSelected();
					if (c.getI() > (v.getxOrigin() + v.getSquareSize() / 2) + (int) (v.getSquareSize() * 2.5) * count
							&& c.getI() < (v.getxOrigin() + v.getSquareSize() / 2)
									+ (int) (v.getSquareSize() * 2.5) * count + v.getSquareSize()
									+ v.getSquareSize() / 2
							&& c.getJ() > (int) (v.getyOrigin() + (y * v.getSquareSize()))
							&& c.getJ() < (int) (v.getyOrigin() + (y * v.getSquareSize())) + v.getSquareSize() * 2) {
						return (indiceofcard);
					}
					indiceofcard++;
					count++;
				}
			}
			return -1;
		}
		return -1;
	}

	private boolean transverseCard(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > (int) (v.getSquareSize() * 10.833)
				&& c.getI() < (int) (v.getSquareSize() * 10.833) + v.getSquareSize() * 2
				&& c.getJ() > (int) (v.getSquareSize() * 5.833)
				&& c.getJ() < (int) (v.getSquareSize() * 5.833) + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	private boolean clicOnJ1Name(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > (int) (v.getscrWidth() / 6) && c.getI() < (int) (v.getscrWidth() / 6) + v.getSquareSize()
				&& c.getJ() > (int) (v.getSquareSize() / 4.8)
				&& c.getJ() < (int) (v.getSquareSize() / 4.8) + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	private boolean clicOnJ2Name(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > (int) (v.getscrWidth() / 6) && c.getI() < (int) (v.getscrWidth() / 6) + v.getSquareSize()
				&& c.getJ() > (int) (v.getSquareSize() * 1.66)
				&& c.getJ() < (int) (v.getSquareSize() * 1.66) + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	private boolean clicOnCheat(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > (int) (v.getscrWidth() / 6) && c.getI() < (int) (v.getscrWidth() / 6) + v.getSquareSize()
				&& c.getJ() > (int) (v.getSquareSize() * 3.125)
				&& c.getJ() < (int) (v.getSquareSize() * 3.125) + v.getSquareSize()) {
			return true;
		}
		return false;
	}

	public void menuDeck(ApplicationContext context, GameView view) {
		Point2D.Float location;
		view.setIndication("Personnaliser vos decks ici");
		GameView.drawBackground(context, view);
		GameView.drawBackButton(context, view, "Quitter et sauvegarder");
		GameView.drawCloseButton(context, view);
		GameView.drawDeck(context, view, this.save);
		GameView.drawIndications(context, view);
		while (true) {

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

				if (clicOnClose(view)) {
					context.exit(0);
				} else if (clicOnBack(view)) {
					int number1 = countCard(save.getCurrentDeck1());
					int number2 = countCard(save.getCurrentDeck2());
					if (number1 == 12 && number2 == 12) {
						break;
					} else if (number1 < 12) {
						view.setIndication("Votre deck actuel contient moins de 12 cartes ! (" + number1 + ")");
					} else if (number2 < 12) {
						view.setIndication("Le deck actuel du J2 contient moins de 12 cartes ! (" + number2 + ")");
					}
				} else if (clicOnDeck(view) != -1) {
					switch (clicOnDeck(view)) {
					case 0:
						currentdeck = save.getDeck1();
						currentdeckname = save.getDeckname1();
						break;
					case 1:
						currentdeck = save.getDeck2();
						currentdeckname = save.getDeckname2();
						break;
					case 2:
						currentdeck = save.getDeck3();
						currentdeckname = save.getDeckname3();
						break;
					case 3:
						currentdeck = save.getDeck4();
						currentdeckname = save.getDeckname4();
						break;
					case 4:
						currentdeck = save.getDeck5();
						currentdeckname = save.getDeckname5();
						break;
					case 5:
						currentdeck = save.getDeck6();
						currentdeckname = save.getDeckname6();
						break;
					default:
						break;
					}
					recapFaction();
					menuDeckContent(context, view);

				} else if (selectCurrentDeck(view) != -1) {
					view.setIndication("Personnaliser vos decks ici");
					switch (selectCurrentDeck(view)) {
					case 0:
						save.setCurrentDeck1(save.getDeck1());
						break;
					case 1:
						save.setCurrentDeck1(save.getDeck2());
						break;
					case 2:
						save.setCurrentDeck1(save.getDeck3());
						break;
					case 3:
						save.setCurrentDeck2(save.getDeck4());
						break;
					case 4:
						save.setCurrentDeck2(save.getDeck5());
						break;
					case 5:
						save.setCurrentDeck2(save.getDeck6());
						break;
					default:
						break;
					}
				} else {
					unselect();
					view.setIndication("Personnaliser vos decks ici");
				}

			}
			GameView.drawBackground(context, view);
			GameView.drawBackButton(context, view, "Quitter et sauvegarder");
			GameView.drawCloseButton(context, view);
			GameView.drawDeck(context, view, this.save);
			GameView.drawIndications(context, view);
		}

	}

	private void menuDeckContent(ApplicationContext context, GameView view) {
		Point2D.Float location;
		unselect();
		view.setIndication("");
		int page = 0;
		int indice = -1;
		int cardship = 0;
		GameView.drawBackground(context, view);
		GameView.drawBackButton(context, view, "revenir aux decks");
		GameView.drawCloseButton(context, view);
		GameView.drawButtonTransvers(context, view);
		GameView.drawChest(context, view, this.save, currentdeck, allCards, page, this);
		GameView.drawCurrentDeck(context, view, save, allCards, currentdeck, this, currentfaction);
		view.setIndication("Cliquez sur une carte du deck ou du coffre !");
		GameView.drawIndications(context, view);
		while ((true)) {

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

				if (clicOnBack(view)) {
					view.setIndication("Personnaliser vos decks ici");
					break;
				}

				else if (clicOnClose(view)) {
					context.exit(0);
				}

				else if (clicOnName(view)) {
					nameDeckMenu(context, view);
				}

				else if (nextPage(view)) {
					takeCardChest(view, page);
					if (page < currentchest.size() - 1) {
						page++;
					}
					unselect();
					view.setIndication("");

				}

				else if (backPage(view)) {
					takeCardChest(view, page);
					if (page != 0) {
						page--;
					}
					unselect();
					view.setIndication("");

				}

				else if (takeCardDeck(view) != -1) {
					indice = (takeCardDeck(view));
					cardship = 1;
					Card card = allCards.get(currentdeck[indice]);
					if (card.getType().equals(Constants.TYPE_SPELL))
						view.setIndication("Un sort du deck a ete selectionnee");
					else if (card.getType().equals(Constants.TYPE_UNIT))
						view.setIndication("Une unite du deck a ete selectionnee");
					else if (card.getType().equals(Constants.TYPE_STRUCTURE))
						view.setIndication("Une structure du deck a ete selectionnee");
				}

				else if (takeCardChest(view, page) != -1) {
					indice = takeCardChest(view, page);
					cardship = 2;
					Card card = currentchest.get(page).get(indice);
					if (card.getType().equals(Constants.TYPE_SPELL))
						view.setIndication("Un sort du coffre a ete selectionnee");
					else if (card.getType().equals(Constants.TYPE_UNIT))
						view.setIndication("Une unite du coffre a ete selectionnee");
					else if (card.getType().equals(Constants.TYPE_STRUCTURE))
						view.setIndication("Une structure du coffre a ete selectionnee");
				}

				else if (indice != -1 && transverseCard(view) && cardship == 1) {
					view.setIndication("La carte a bien ete posee dans le coffre");
					currentdeck[indice] = null;
					indice = -1;
					recapFaction();
					unselect();
				}

				else if (indice != -1 && transverseCard(view) && cardship == 2) {
					if (deckComplet()) {
						view.setIndication("Votre deck est complet !");
						unselect();
					} else {
						if (currentchest.get(page).get(indice).getFaction() == currentfaction
								|| currentchest.get(page).get(indice).getFaction() == "Neutral"
								|| currentfaction == "Neutral") {
							for (int i = 0; i < currentdeck.length; i++) {

								if (currentdeck[i] == null) {
									currentdeck[i] = currentchest.get(page).get(indice).getName();
									recapFaction();
									break;
								}
							}
							indice = -1;
							view.setIndication("La carte a bien ajouter au deck");
							takeCardChest(view, page);
							if (currentchest.size() <= page) {
								page = currentchest.size() - 1;
							}
							unselect();
						} else {
							view.setIndication("Votre deck a deja une faction!");
							unselect();
						}
					}
				}

				else {
					// si l'on clic sans provoquer d'evenement ou pas sur une autre carte l'indice
					// memoriser s'en vas
					indice = -1;
					unselect();
					view.setIndication("Cliquez sur une carte du deck ou du coffre !");
				}
			}
			GameView.drawBackground(context, view);
			GameView.drawBackButton(context, view, "revenir aux decks");
			GameView.drawCloseButton(context, view);
			GameView.drawButtonTransvers(context, view);
			GameView.drawChest(context, view, this.save, currentdeck, allCards, page, this);
			GameView.drawCurrentDeck(context, view, save, allCards, currentdeck, this, currentfaction);
			GameView.drawIndications(context, view);
		}
	}

	public void Option(ApplicationContext context, GameView view) {
		Point2D.Float location;
		view.setIndication("Option du jeu");
		GameView.clearAll(context, view);
		GameView.drawBackButton(context, view, "Quitter et sauvegarder");
		GameView.drawCloseButton(context, view);
		GameView.drawIndications(context, view);
		GameView.drawOption(context, view);
		while (true) {

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

				if (clicOnBack(view)) {

					break;
				}

				else if (clicOnClose(view)) {
					context.exit(0);
				}

				else if (clicOnJ1Name(view)) {
					currentplayername = save.getPlayer1();
					changeNamePlayerMenu(context, view);
				} else if (clicOnJ2Name(view)) {
					currentplayername = save.getPlayer2();
					changeNamePlayerMenu(context, view);
				} else if (clicOnCheat(view)) {
					cheatMenu(context, view);
				}
			}
			GameView.clearAll(context, view);
			GameView.drawBackButton(context, view, "Quitter et sauvegarder");
			GameView.drawCloseButton(context, view);
			GameView.drawIndications(context, view);
			GameView.drawOption(context, view);

		}
	}

	private void changeNamePlayerMenu(ApplicationContext context, GameView view) {
		String name = currentplayername;
		Point2D.Float location;
		view.setIndication("Vous pouvez changer le nom  (longueur max = 12)");
		GameView.clearAll(context, view);
		GameView.drawBackButton(context, view, "Revenir aux options");
		GameView.drawCloseButton(context, view);
		GameView.drawIndications(context, view);
		GameView.drawCurrentName(context, view, currentplayername);
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) { // no event
				continue;
			}

			if (event.getAction() == Action.KEY_PRESSED) {
				view.setIndication("Vous pouvez changer le nom  (longueur max = 12)");
				KeyboardKey key = event.getKey();
				if (currentplayername.length() < 12) {
					if (key == KeyboardKey.A) {
						currentplayername += "a";
					}

					if (key == KeyboardKey.B) {
						currentplayername += "b";
					}

					if (key == KeyboardKey.C) {
						currentplayername += "c";
					}

					if (key == KeyboardKey.D) {
						currentplayername += "d";
					}

					if (key == KeyboardKey.E) {
						currentplayername += "e";
					}

					if (key == KeyboardKey.F) {
						currentplayername += "f";
					}

					if (key == KeyboardKey.G) {
						currentplayername += "g";
					}

					if (key == KeyboardKey.H) {
						currentplayername += "h";
					}

					if (key == KeyboardKey.I) {
						currentplayername += "i";
					}

					if (key == KeyboardKey.J) {
						currentplayername += "j";
					}

					if (key == KeyboardKey.K) {
						currentplayername += "k";
					}

					if (key == KeyboardKey.L) {
						currentplayername += "l";
					}

					if (key == KeyboardKey.M) {
						currentplayername += "m";
					}

					if (key == KeyboardKey.N) {
						currentplayername += "n";
					}

					if (key == KeyboardKey.O) {
						currentplayername += "o";
					}

					if (key == KeyboardKey.P) {
						currentplayername += "p";
					}

					if (key == KeyboardKey.Q) {
						currentplayername += "q";
					}

					if (key == KeyboardKey.R) {
						currentplayername += "r";
					}

					if (key == KeyboardKey.S) {
						currentplayername += "s";
					}

					if (key == KeyboardKey.T) {
						currentplayername += "t";
					}

					if (key == KeyboardKey.U) {
						currentplayername += "u";
					}

					if (key == KeyboardKey.V) {
						currentplayername += "v";
					}

					if (key == KeyboardKey.W) {
						currentplayername += "w";
					}

					if (key == KeyboardKey.X) {
						currentplayername += "x";
					}

					if (key == KeyboardKey.Y) {
						currentplayername += "y";
					}

					if (key == KeyboardKey.Z) {
						currentplayername += "z";
					}

					if (key == KeyboardKey.SPACE) {
						currentplayername += " ";
					}
				}
				if (key == KeyboardKey.LEFT) {
					try {
						currentplayername = currentplayername.substring(0, currentplayername.length() - 1);
					} catch (Exception e) {
						view.setIndication("nom deja vide !");
					}
				}

				if (key == KeyboardKey.SHIFT) {
					try {
						char c = currentplayername.charAt(currentplayername.length() - 1);
						c = Character.toUpperCase(c);
						currentplayername = currentplayername.substring(0, currentplayername.length() - 1);
						currentplayername += c;
					} catch (Exception e) {
						view.setIndication("nom vide, action impossible !");
					}
				}

			}

			if (event.getLocation() != null) {
				unselect();
				location = event.getLocation();
				selectCell((int) location.x, (int) location.y);

				if (clicOnBack(view)) {
					if (currentplayername.length() == 0) {
						view.setIndication("nom actuellement vide !");
					} else {
						if (name == save.getPlayer1()) {
							save.setPlayer1(currentplayername);
						} else if (name == save.getPlayer2()) {
							save.setPlayer2(currentplayername);
						}
						break;
					}
				}

				else if (clicOnClose(view)) {
					context.exit(0);
				}

				else {
					unselect();
					view.setIndication("Vous pouvez changer le nom  (longueur max = 12)");
				}
			}
			GameView.clearAll(context, view);
			GameView.drawBackButton(context, view, "Revenir aux options");
			GameView.drawCloseButton(context, view);
			GameView.drawIndications(context, view);
			GameView.drawCurrentName(context, view, currentplayername);
		}
	}

	private void CreateAllImageCardFromStormBoundWiki() throws IOException {
		for (String card : allCards.keySet()) {
			if (card.equals("Leo") || card.equals("William")) {
				continue;
			}
			String cardname = card.replace(' ', '_');
			String image = null;

			URL url = new URL("https://stormboundkingdomwars.gamepedia.com/File:" + cardname + ".png");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				if (line.length() > 28) {
					System.out.println(line.substring(12, 21));
					if (line.substring(12, 21).equals("fullMedia")) {
						System.out.println("ok");
						image = line.substring(32);
						for (int i = 0; i < image.length(); i++) {
							if (image.charAt(i) == ' ') {
								image = image.substring(0, i - 1);
								break;
							}
						}
						System.out.println(image);
						break;
					}

				}
			}
			br.close();
			try (InputStream in = new URL(image).openStream()) {
				Files.copy(in, Paths.get(card + ".png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void GenerateImageMapForCard(ApplicationContext context, GameView view) throws IOException {
		Path file = Paths.get("Imagemap.txt");
		Charset charset = StandardCharsets.UTF_8;
		try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
			for (String cardname : allCards.keySet()) {
				String s = "imagesMap.put(\"" + cardname + "\", new ImageIcon(\"src/pictures/" + cardname
						+ ".png\").getImage());\n";
				writer.write(s); // ou writer.write(s, 0, s.length());
			}
		}

	}

	private void cheatMenu(ApplicationContext context, GameView view) {
		String cheat = "";
		Point2D.Float location;
		view.setIndication("Trouvez un code secret...");
		GameView.clearAll(context, view);
		GameView.drawBackButton(context, view, "Revenir aux options");
		GameView.drawCloseButton(context, view);
		GameView.drawIndications(context, view);
		GameView.drawCurrentName(context, view, cheat);
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) { // no event
				continue;
			}

			if (event.getAction() == Action.KEY_PRESSED) {
				view.setIndication("Trouvez un code secret...");
				KeyboardKey key = event.getKey();
				if (cheat.length() < 12) {
					if (key == KeyboardKey.A) {
						cheat += "a";
					}

					if (key == KeyboardKey.B) {
						cheat += "b";
					}

					if (key == KeyboardKey.C) {
						cheat += "c";
					}

					if (key == KeyboardKey.D) {
						cheat += "d";
					}

					if (key == KeyboardKey.E) {
						cheat += "e";
					}

					if (key == KeyboardKey.F) {
						cheat += "f";
					}

					if (key == KeyboardKey.G) {
						cheat += "g";
					}

					if (key == KeyboardKey.H) {
						cheat += "h";
					}

					if (key == KeyboardKey.I) {
						cheat += "i";
					}

					if (key == KeyboardKey.J) {
						cheat += "j";
					}

					if (key == KeyboardKey.K) {
						cheat += "k";
					}

					if (key == KeyboardKey.L) {
						cheat += "l";
					}

					if (key == KeyboardKey.M) {
						cheat += "m";
					}

					if (key == KeyboardKey.N) {
						cheat += "n";
					}

					if (key == KeyboardKey.O) {
						cheat += "o";
					}

					if (key == KeyboardKey.P) {
						cheat += "p";
					}

					if (key == KeyboardKey.Q) {
						cheat += "q";
					}

					if (key == KeyboardKey.R) {
						cheat += "r";
					}

					if (key == KeyboardKey.S) {
						cheat += "s";
					}

					if (key == KeyboardKey.T) {
						cheat += "t";
					}

					if (key == KeyboardKey.U) {
						cheat += "u";
					}

					if (key == KeyboardKey.V) {
						cheat += "v";
					}

					if (key == KeyboardKey.W) {
						cheat += "w";
					}

					if (key == KeyboardKey.X) {
						cheat += "x";
					}

					if (key == KeyboardKey.Y) {
						cheat += "y";
					}

					if (key == KeyboardKey.Z) {
						cheat += "z";
					}

					if (key == KeyboardKey.SPACE) {
						cheat += " ";
					}
				}
				if (key == KeyboardKey.LEFT) {
					try {
						cheat = cheat.substring(0, cheat.length() - 1);
					} catch (Exception e) {
						view.setIndication("code deja vide !");
					}
				}

				if (key == KeyboardKey.SHIFT) {
					try {
						char c = cheat.charAt(cheat.length() - 1);
						c = Character.toUpperCase(c);
						cheat = cheat.substring(0, cheat.length() - 1);
						cheat += c;
					} catch (Exception e) {
						view.setIndication("code vide, action impossible !");
					}
				}
				if (cheat.equals("gIvEmEeverYY")) {
					view.setIndication("Bravo, toute les cartes ont ete ajouter au coffre!");
					giveAllCard();
				}
			}

			if (event.getLocation() != null) {
				unselect();
				location = event.getLocation();
				selectCell((int) location.x, (int) location.y);

				if (clicOnBack(view)) {
					break;
				}

				else if (clicOnClose(view)) {
					context.exit(0);
				}

				else {
					unselect();
					view.setIndication("Trouvez un code secret...");
				}
			}
			GameView.clearAll(context, view);
			GameView.drawBackButton(context, view, "Revenir aux options");
			GameView.drawCloseButton(context, view);
			GameView.drawIndications(context, view);
			GameView.drawCurrentName(context, view, cheat);
		}
	}

	private void nameDeckMenu(ApplicationContext context, GameView view) {
		String name = currentdeckname;
		Point2D.Float location;
		view.setIndication("Vous pouvez changer le nom  (longueur max = 12)");
		GameView.clearAll(context, view);
		GameView.drawBackButton(context, view, "Revenir au deck");
		GameView.drawCloseButton(context, view);
		GameView.drawIndications(context, view);
		GameView.drawCurrentName(context, view, currentdeckname);
		while (true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) { // no event
				continue;
			}

			if (event.getAction() == Action.KEY_PRESSED) {
				view.setIndication("Vous pouvez changer le nom  (longueur max = 12)");
				KeyboardKey key = event.getKey();
				if (currentdeckname.length() < 12) {
					if (key == KeyboardKey.A) {
						currentdeckname += "a";
					}

					if (key == KeyboardKey.B) {
						currentdeckname += "b";
					}

					if (key == KeyboardKey.C) {
						currentdeckname += "c";
					}

					if (key == KeyboardKey.D) {
						currentdeckname += "d";
					}

					if (key == KeyboardKey.E) {
						currentdeckname += "e";
					}

					if (key == KeyboardKey.F) {
						currentdeckname += "f";
					}

					if (key == KeyboardKey.G) {
						currentdeckname += "g";
					}

					if (key == KeyboardKey.H) {
						currentdeckname += "h";
					}

					if (key == KeyboardKey.I) {
						currentdeckname += "i";
					}

					if (key == KeyboardKey.J) {
						currentdeckname += "j";
					}

					if (key == KeyboardKey.K) {
						currentdeckname += "k";
					}

					if (key == KeyboardKey.L) {
						currentdeckname += "l";
					}

					if (key == KeyboardKey.M) {
						currentdeckname += "m";
					}

					if (key == KeyboardKey.N) {
						currentdeckname += "n";
					}

					if (key == KeyboardKey.O) {
						currentdeckname += "o";
					}

					if (key == KeyboardKey.P) {
						currentdeckname += "p";
					}

					if (key == KeyboardKey.Q) {
						currentdeckname += "q";
					}

					if (key == KeyboardKey.R) {
						currentdeckname += "r";
					}

					if (key == KeyboardKey.S) {
						currentdeckname += "s";
					}

					if (key == KeyboardKey.T) {
						currentdeckname += "t";
					}

					if (key == KeyboardKey.U) {
						currentdeckname += "u";
					}

					if (key == KeyboardKey.V) {
						currentdeckname += "v";
					}

					if (key == KeyboardKey.W) {
						currentdeckname += "w";
					}

					if (key == KeyboardKey.X) {
						currentdeckname += "x";
					}

					if (key == KeyboardKey.Y) {
						currentdeckname += "y";
					}

					if (key == KeyboardKey.Z) {
						currentdeckname += "z";
					}

					if (key == KeyboardKey.SPACE) {
						currentdeckname += " ";
					}
				}
				if (key == KeyboardKey.LEFT) {
					try {
						currentdeckname = currentdeckname.substring(0, currentdeckname.length() - 1);
					} catch (Exception e) {
						view.setIndication("nom deja vide !");
					}
				}

				if (key == KeyboardKey.SHIFT) {
					try {
						char c = currentdeckname.charAt(currentdeckname.length() - 1);
						c = Character.toUpperCase(c);
						currentdeckname = currentdeckname.substring(0, currentdeckname.length() - 1);
						currentdeckname += c;
					} catch (Exception e) {
						view.setIndication("nom vide, action impossible !");
					}
				}

			}

			if (event.getLocation() != null) {
				unselect();
				location = event.getLocation();
				selectCell((int) location.x, (int) location.y);

				if (clicOnBack(view)) {
					if (currentdeckname.length() == 0) {
						view.setIndication("nom actuellement vide !");
					} else {
						if (name == save.getDeckname1()) {
							save.setDeckname1(currentdeckname);
							;
						} else if (name == save.getDeckname2()) {
							save.setDeckname2(currentdeckname);
							;
						} else if (name == save.getDeckname3()) {
							save.setDeckname3(currentdeckname);
							;
						} else if (name == save.getDeckname4()) {
							save.setDeckname4(currentdeckname);
							;
						} else if (name == save.getDeckname5()) {
							save.setDeckname5(currentdeckname);
							;
						} else if (name == save.getDeckname6()) {
							save.setDeckname6(currentdeckname);
							;
						}
						view.setIndication("Cliquez sur une carte du deck ou du coffre !");
						break;
					}
				}

				else if (clicOnClose(view)) {
					context.exit(0);
				}

				else {
					unselect();
					view.setIndication("Vous pouvez changer le nom  (longueur max = 12)");
				}
			}
			GameView.clearAll(context, view);
			GameView.drawBackButton(context, view, "Revenir au deck");
			GameView.drawCloseButton(context, view);
			GameView.drawIndications(context, view);
			GameView.drawCurrentName(context, view, currentdeckname);
		}
	}
}

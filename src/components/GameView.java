package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

public class GameView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int xOrigin;
	private final int yOrigin;
	private final int width;
	private final int length;
	private final int squareSize;
	private final int screenwidth;
	private final int screenheight;
	private String indications;
	private static final Map<String, Image> allImages = Creator.createImage();

	// touts les calcul des draws sont faits pour �tre responsives
	private GameView(int xOrigin, int yOrigin, int length, int width, int squareSize, int screenwidth,
			int screenheight) {

		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.length = length;
		this.width = width;
		this.squareSize = squareSize;
		this.indications = "";
		this.screenwidth = screenwidth;
		this.screenheight = screenheight;
	}

	public int getxOrigin() { // renvoie le x du premier carre de la matrice
		return xOrigin;
	}

	public int getyOrigin() { // renvoie le y du premier carre de la matrice
		return yOrigin;
	}

	public int getSquareSize() { // renvoie la taille d'un carr�, principale unite de mesure pour le calcul
		return squareSize;
	}

	public int getscrWidth() { // renvoie la largeur de l'ecran
		return screenwidth;
	}

	public int getscrHeight() { // renvoie la hauteur de l'ecran
		return screenheight;
	}

	public static GameView initGameGraphics(int xOrigin, int yOrigin, int length, ApplicationContext context) {
		int squareSize = (int) (length * 1 / Constants.height); // taille des carr�s
		ScreenInfo screenInfo = context.getScreenInfo();
		int width = (int) screenInfo.getWidth();
		int height = (int) screenInfo.getHeight();
		return new GameView(xOrigin, yOrigin, length, Constants.width * squareSize, squareSize, width, height);
	}

	private int indexFromReaCoord(float coord, int origin) {
		return (int) ((coord - origin) / squareSize);
	}

	/**
	 * Transforms a real y-coordinate into the index of the corresponding line.
	 * 
	 * @param y
	 *            a float y-coordinate
	 * @return the index of the corresponding line.
	 * @throws IllegalArgumentException
	 *             if the float coordinate doesn't fit in the game board.
	 */
	public int lineFromY(float y) {
		return indexFromReaCoord(y, yOrigin);
	}

	/**
	 * Transforms a real x-coordinate into the index of the corresponding column.
	 * 
	 * @param x
	 *            a float x-coordinate
	 * @return the index of the corresponding column.
	 * @throws IllegalArgumentException
	 *             if the float coordinate doesn't fit in the game board.
	 */
	public int columnFromX(float x) {
		return indexFromReaCoord(x, xOrigin);
	}

	private float realCoordFromIndex(int index, int origin) {
		return origin + index * squareSize;
	}

	private float xFromI(int i) {
		return realCoordFromIndex(i, xOrigin);
	}

	private float yFromJ(int j) {
		return realCoordFromIndex(j, yOrigin);
	}

	private RectangularShape drawCell(int i, int j) {
		return new Rectangle2D.Float(xFromI(j) + 2, yFromJ(i) + 2, squareSize - 4, squareSize - 4);
	}

	private RectangularShape drawSelectedCell(int i, int j) {
		return new Rectangle2D.Float(xFromI(j), yFromJ(i), squareSize, squareSize);
	}

	// dessine l'image de fond en jeu
	private void drawBackground(Graphics2D graphics) {
		graphics.drawImage(allImages.get("Background"), 0, 0, screenwidth, screenheight, null);
	}

	public static void drawBackground(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawBackground(graphics)); // do not modify
	}

	// dessine la croix pour fermer
	private void drawCloseButton(Graphics2D graphics) {
		graphics.drawImage(allImages.get("Close"), screenwidth - squareSize / 2, 0, squareSize / 2, squareSize / 2,
				null);
	}

	public static void drawCloseButton(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawCloseButton(graphics)); // do not modify
	}

	// dessine la fleche de retour en arriere
	private void drawBackButton(Graphics2D graphics, String indication) {
		graphics.drawImage(allImages.get("Back"), 0, 0, squareSize, squareSize, null);
		graphics.setColor(Color.WHITE);
		graphics.setFont(celticfont((int) (squareSize / 8)));
		graphics.drawString(indication, squareSize / 4, squareSize);
	}

	public static void drawBackButton(ApplicationContext context, GameView view, String indication) {
		context.renderFrame(graphics -> view.drawBackButton(graphics, indication)); // do not modify
	}

	// dessine les deux bontons : fin de tour et changer la carte
	private void drawEndChange(Graphics2D graphics) {
		// ------ BOUTON-end--------INIT
		graphics.drawImage(allImages.get("End"), xOrigin + 5 * squareSize, yOrigin + 2 * squareSize, squareSize,
				squareSize, null);
		// ------ BOUTON-change--------INIT
		graphics.drawImage(allImages.get("Change"), xOrigin + 5 * squareSize, yOrigin + 4 * squareSize, squareSize,
				squareSize, null);

	}

	public static void drawEndChange(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawEndChange(graphics)); // do not modify
	}

	// dessine la zone d'indication ainsi que le plateau
	// recupere aussi les coordonn�es o� on clic pour voir si il n'y a pas une
	// case a afficher dans la zone
	// de detail
	private void draw(Graphics2D graphics, GameData data) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width, length));
		graphics.setColor(Color.WHITE);
		for (int i = 0; i <= Constants.height; i++) {
			graphics.draw(
					new Line2D.Float(xOrigin, yOrigin + i * squareSize, xOrigin + width, yOrigin + i * squareSize));
		}

		for (int i = 0; i <= Constants.width; i++) {
			graphics.draw(
					new Line2D.Float(xOrigin + i * squareSize, yOrigin, xOrigin + i * squareSize, yOrigin + length));
		}

		if (data.getBoard().getFrontline1() - 1 == data.getBoard().getFrontline2()) {
			graphics.setColor(Color.BLACK);
			graphics.draw(new Line2D.Float(xOrigin, yOrigin + squareSize * data.getBoard().getFrontline1(),
					xOrigin + Constants.width * squareSize, yOrigin + squareSize * data.getBoard().getFrontline1()));
		} else {
			graphics.setColor(Color.RED);
			graphics.draw(new Line2D.Float(xOrigin, yOrigin + squareSize * data.getBoard().getFrontline1(),
					xOrigin + Constants.width * squareSize, yOrigin + squareSize * data.getBoard().getFrontline1()));

			graphics.setColor(Color.BLUE);
			graphics.draw(new Line2D.Float(xOrigin, yOrigin + squareSize * (data.getBoard().getFrontline2() + 1),
					xOrigin + Constants.width * squareSize,
					yOrigin + squareSize * (data.getBoard().getFrontline2() + 1)));
		}
		// ------ J2--------INIT
		graphics.setColor(Color.BLUE);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin - (int) (squareSize * 0.416),
				squareSize * Constants.width + 1, (int) (squareSize * 0.416)));
		graphics.setFont(celticfont((int) (squareSize / 3)));
		graphics.setColor(Color.WHITE);
		graphics.drawString("" + data.getP2().getTurnMana(), xOrigin + squareSize * (Constants.width / 6),
				yOrigin - (int) (squareSize * 0.122));
		graphics.drawImage(allImages.get("Mana"),
				xOrigin + squareSize * (Constants.width / 6) + (int) (squareSize / 7.5),
				yOrigin - (int) (squareSize * 0.366), squareSize / 3, squareSize / 3, null);
		graphics.drawString(data.getP2().getName(), xOrigin + squareSize * (Constants.width / 3) + squareSize / 2,
				yOrigin - (int) (squareSize * 0.122));
		if (data.getP2().getHp() >= 0) {
			graphics.drawString("" + data.getP2().getHp(),
					xOrigin + squareSize * (Constants.width) - (int) (squareSize * 0.290),
					yOrigin - (int) (squareSize * 0.122));
		} else {
			graphics.drawString("0", xOrigin + squareSize * (Constants.width) - (int) (squareSize * 0.290),
					yOrigin - (int) (squareSize * 0.122));
		}

		graphics.drawImage(allImages.get("Heart"),
				xOrigin + squareSize * (Constants.width) - (int) (squareSize * 0.590),
				yOrigin - (int) (squareSize * 0.366), squareSize / 3, squareSize / 3, null);

		// ------ J1--------INIT
		graphics.setColor(Color.RED);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin + squareSize * Constants.height,
				squareSize * Constants.width + 1, (int) (squareSize * 0.416)));

		graphics.setColor(Color.BLACK);
		graphics.drawString("" + data.getP1().getTurnMana(), xOrigin + squareSize * (Constants.width / 5),
				yOrigin + squareSize * Constants.height + (int) (squareSize * 0.3));
		graphics.drawImage(allImages.get("Mana"),
				xOrigin + squareSize * (Constants.width / 6) + (int) (squareSize / 7.5),
				yOrigin + squareSize * Constants.height + (int) (squareSize * 0.0), squareSize / 3, squareSize / 3,
				null);
		graphics.drawString(data.getP1().getName(), xOrigin + squareSize * (Constants.width / 3) + squareSize / 2,
				yOrigin + squareSize * Constants.height + (int) (squareSize * 0.3));
		if (data.getP1().getHp() >= 0) {
			graphics.drawString("" + data.getP1().getHp(),
					xOrigin + squareSize * (Constants.width) - (int) (squareSize * 0.290),
					yOrigin + squareSize * Constants.height + (int) (squareSize * 0.3));
		} else {
			graphics.drawString("0", xOrigin + squareSize * (Constants.width) - (int) (squareSize * 0.290),
					yOrigin + squareSize * Constants.height + (int) (squareSize * 0.3));
		}
		graphics.drawImage(allImages.get("Heart"),
				xOrigin + squareSize * (Constants.width) - (int) (squareSize * 0.590),
				yOrigin + squareSize * Constants.height + (int) (squareSize * 0.0), squareSize / 3, squareSize / 3,
				null);

		// ---------Indication_and_detail_zone------INIT
		graphics.drawImage(allImages.get("Rune"), xOrigin + (int) (squareSize * 7.5), yOrigin, (int) (squareSize * 5.1),
				(int) (squareSize * 5.1), null);
		graphics.setColor(Color.WHITE);
		graphics.draw(new Line2D.Float(xOrigin + (int) (squareSize * 7.5), yOrigin,
				xOrigin + (int) (squareSize * 7.5) + (int) (squareSize * 5.1), yOrigin));
		graphics.draw(new Line2D.Float(xOrigin + (int) (squareSize * 7.5), yOrigin + squareSize,
				xOrigin + (int) (squareSize * 7.5) + (int) (squareSize * 5.1), yOrigin + squareSize));

		// recuperation des coordonn�es pour les details d'une carte
		Coordinates c = data.getBoard().getSelected();
		if (c != null) {
			if (c.getI() >= xOrigin && c.getI() < xOrigin + Constants.width * squareSize && c.getJ() >= yOrigin
					&& c.getJ() < yOrigin + Constants.height * squareSize) {
				graphics.setColor(Color.GREEN);

				graphics.fill(drawSelectedCell(lineFromY(c.getJ()), columnFromX(c.getI())));

				if (data.getBoard().getCellValue(lineFromY(c.getJ()), columnFromX(c.getI())) != null) {
					Card card = data.getBoard().getCellValue(lineFromY(c.getJ()), columnFromX(c.getI()));
					graphics.setColor(Color.WHITE);
					graphics.setFont(celticfont((int) (squareSize / 4.444)));
					graphics.drawString(card.getName(), xOrigin + (int) (squareSize * 9.2),
							yOrigin + (int) (squareSize * 1.66));
					graphics.fillOval(xOrigin + (int) (squareSize * 9.2), yOrigin + (int) (squareSize * 2),
							squareSize + squareSize / 3, squareSize + squareSize / 3);
					graphics.drawImage(allImages.get(card.getName()), xOrigin + (int) (squareSize * 9.2),
							yOrigin + (int) (squareSize * 2), squareSize + squareSize / 3, squareSize + squareSize / 3,
							null);
					graphics.setFont(celticfont((int) (squareSize / 7.5)));
					if (card.getDescription() != "") {
						if (card.getDescription().length() > 60) {
							graphics.setFont(celticfont((int) (squareSize / 9)));
						}
						graphics.drawString(card.getDescription(), xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.9));
					}
					graphics.setFont(celticfont((int) (squareSize / 7.5)));
					if (card.getType().equals(Constants.TYPE_UNIT)) {
						graphics.drawString("unite", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					} else if (card.getType().equals(Constants.TYPE_STRUCTURE)) {
						graphics.drawString("structure", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					}
					if (card.getPlayer() == 1) {
						graphics.drawString("Carte � " + data.getP1().getName(), xOrigin + (int) (squareSize * 9.2),
								yOrigin + (int) (squareSize * 3.6));
					} else if (card.getPlayer() == 2) {
						graphics.drawString("Carte � " + data.getP2().getName(), xOrigin + (int) (squareSize * 9.2),
								yOrigin + (int) (squareSize * 3.6));
					}
					graphics.setFont(celticfont((int) (squareSize / 6)));
					graphics.drawString("Mana :   " + card.getMana(), xOrigin + (int) (squareSize * 7.583),
							yOrigin + (int) (squareSize * 4.16));
					if (card.getPower() > 0) {
						graphics.drawString("Power :   " + card.getPower(), xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 4.5));
					}
					if (card.getMoveSpeed() > 0) {
						graphics.drawString("Speed deplacement :   " + card.getMoveSpeed(),
								xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 4.833));
					}
				}

			}

		}

		for (int i = 0; i < Constants.height; i++) {
			for (int j = 0; j < Constants.width; j++) {
				graphics.setColor(Color.WHITE);
				graphics.fill(drawCell(i, j));
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Courier New", 1, (int) (squareSize * 0.166)));
				if (data.getBoard().getCellValue(i, j) != null) {
					graphics.drawImage(allImages.get(data.getBoard().getCellValue(i, j).getName()), (int) xFromI(j),
							(int) yFromJ(i), squareSize, squareSize, null);
					if (data.getBoard().getCellValue(i, j).getPlayer() == 1) {
						graphics.setColor(Color.RED);
					} else if (data.getBoard().getCellValue(i, j).getPlayer() == 2) {
						graphics.setColor(Color.BLUE);
					}
					graphics.fill(new Rectangle2D.Float(xFromI(j) + (int) (squareSize / 1.3),
							yFromJ(i) + squareSize / 34, squareSize / 5, squareSize / 5));
					graphics.setColor(Color.WHITE);
					graphics.drawString("" + data.getBoard().getCellValue(i, j).getPower(),
							xFromI(j) + (int) (squareSize / 1.2), yFromJ(i) + squareSize / 5);
				}
			}
		}

	}

	/**
	 * Draws the game board from its data, using an existing
	 * {@code ApplicationContext}.
	 * 
	 * @param context
	 *            the {@code ApplicationContext} of the game
	 * @param data
	 *            the GameData containing the game data.
	 * @param view
	 *            the GameView on which to draw.
	 */
	public static void draw(ApplicationContext context, GameData data, GameView view) {
		context.renderFrame(graphics -> view.draw(graphics, data)); // do not modify
	}

	// determine quelle indication il doit y avoir
	public void setIndication(String indications) {
		this.indications = indications;
	}

	// dessine les indications dans la zone d'indication
	private void drawIndications(Graphics2D graphics) {
		graphics.setFont(celticfont((int) (squareSize / 4)));
		graphics.setColor(Color.WHITE);
		graphics.drawString(indications, xOrigin + (int) (squareSize * 7.7), yOrigin + (int) (squareSize * 0.5));
	}

	public static void drawIndications(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawIndications(graphics)); // do not modify
	}

	// dessine la main d'un joueur, les details de la carte selectionner et aussi
	// colore les cases
	// en grises en dessous de la frontline lorsque vous selectionnez une case
	public static void drawMain(ApplicationContext context, GameData data, GameView view, Player current) {
		context.renderFrame(graphics -> view.drawMain(graphics, data, current)); // do not modify
	}

	// change la police pour une police celtic, fantasy
	private Font celticfont(int size) {
		try {
			Font celticfont1 = Font.createFont(Font.TRUETYPE_FONT,
					new FileInputStream(new File("src/fonts/celticfont.ttf")));
			Font celticfont = celticfont1.deriveFont(Font.PLAIN, size);
			return celticfont;
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// change la police pour une police barbare, avec une �p�e dans la phrase
	private Font barbarianFont(int size) {
		try {
			Font barbarianfont1 = Font.createFont(Font.TRUETYPE_FONT,
					new FileInputStream(new File("src/fonts/Barbarian.ttf")));
			Font barbarianfont = barbarianfont1.deriveFont(Font.PLAIN, size);
			return barbarianfont;
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void drawMain(Graphics2D graphics, GameData data, Player current) {
		graphics.drawImage(allImages.get("Rune"), (int) (squareSize * 3), (int) (squareSize * 6.25),
				(int) (squareSize * 9.34), (int) (squareSize * 2.39), null);
		int count = 0;
		for (Card card : current.hand) {
			if (card == null) {
				count++;
				continue;
			}
			graphics.setColor(Color.WHITE);
			graphics.fill(new Rectangle2D.Float((xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count,
					yOrigin + ((Constants.height + 1) * squareSize), squareSize + squareSize / 2, squareSize * 2));
			graphics.setFont(celticfont((int) (squareSize / 4.444)));
			graphics.setColor(Color.BLACK);
			graphics.drawImage(allImages.get("Mana"),
					(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 4,
					yOrigin + ((Constants.height + 1) * squareSize), squareSize / 4, squareSize / 4, null);
			graphics.drawString("" + card.getMana(),
					(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 4
							- (int) (squareSize / 12),
					yOrigin + ((Constants.height + 1) * squareSize) + (int) (squareSize / 6));
			if (card.getPower() > 0) {
				graphics.drawImage(allImages.get("Heart"),
						(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + (int) (squareSize / 12),
						yOrigin + ((Constants.height + 1) * squareSize) + squareSize * 2 - (int) (squareSize / 3.42),
						squareSize / 4, squareSize / 4, null);
				graphics.drawString("" + card.getPower(), (xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count,
						yOrigin + ((Constants.height + 1) * squareSize) + squareSize * 2 - (int) (squareSize / 12));
			}
			if (card.getType() == Constants.TYPE_UNIT) {
				graphics.drawImage(allImages.get("Shoes"),
						(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 2
								- (int) (squareSize / 2.66),
						yOrigin + ((Constants.height + 1) * squareSize) + squareSize * 2 - (int) (squareSize / 3.42),
						squareSize / 4, squareSize / 4, null);
				graphics.drawString("" + card.getMoveSpeed(),
						(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 2
								- (int) (squareSize / 8),
						yOrigin + ((Constants.height + 1) * squareSize) + squareSize * 2 - (int) (squareSize / 12));
			}
			graphics.drawImage(allImages.get(card.getName()),
					(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + (int) (squareSize / 3.7),
					yOrigin + ((Constants.height + 1) * squareSize) + (int) (squareSize / 2), squareSize, squareSize,
					null);
			Coordinates c = data.getBoard().getSelected();
			if (c != null) {
				if (c.getI() >= (xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count
						&& c.getI() < (xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + squareSize
								+ squareSize / 2
						&& c.getJ() >= yOrigin + ((Constants.height + 1) * squareSize)
						&& c.getJ() < yOrigin + ((Constants.height + 1) * squareSize) + squareSize * 2) {
					graphics.setColor(Color.GREEN);
					graphics.drawRect((xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count - 2,
							yOrigin + ((Constants.height + 1) * squareSize) - 2, squareSize + squareSize / 2 + 2,
							squareSize * 2 + 2);
					graphics.drawImage(allImages.get("Cursor"),
							(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + (int) (squareSize / 1.6),
							yOrigin + ((Constants.height + 1) * squareSize) + (int) (squareSize * 1.83), squareSize / 4,
							squareSize / 4, null);
					graphics.setColor(Color.WHITE);
					graphics.drawString(card.getName(), xOrigin + (int) (squareSize * 9.2),
							yOrigin + (int) (squareSize * 1.66));
					graphics.fillOval(xOrigin + (int) (squareSize * 9.2), yOrigin + (int) (squareSize * 2),
							squareSize + squareSize / 3, squareSize + squareSize / 3);
					graphics.drawImage(allImages.get(card.getName()), xOrigin + (int) (squareSize * 9.2),
							yOrigin + (int) (squareSize * 2), squareSize + squareSize / 3, squareSize + squareSize / 3,
							null);
					graphics.setFont(celticfont((int) (squareSize / 7.5)));
					if (card.getType().equals(Constants.TYPE_UNIT)) {
						graphics.drawString("unite", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					} else if (card.getType().equals(Constants.TYPE_STRUCTURE)) {
						graphics.drawString("structure", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					} else if (card.getType().equals(Constants.TYPE_SPELL)) {
						graphics.drawString("carte magique", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					}
					if (card.getDescription() != "") {
						if (card.getDescription().length() > 60) {
							graphics.setFont(celticfont((int) (squareSize / 9)));
						}
						graphics.drawString(card.getDescription(), xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.9));
					}
					graphics.setFont(celticfont((int) (squareSize / 6)));
					graphics.drawString("Mana :   " + card.getMana(), xOrigin + (int) (squareSize * 7.583),
							yOrigin + (int) (squareSize * 4.16));
					if (card.getPower() > 0) {
						graphics.drawString("Power :   " + card.getPower(), xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 4.5));
					}
					if (card.getMoveSpeed() > 0) {
						graphics.drawString("Speed deplacement :   " + card.getMoveSpeed(),
								xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 4.833));
					}

					if (!(card.getType() == Constants.TYPE_SPELL)) {
						int frontline;
						if (current.getNumber() == 1) {
							frontline = data.getBoard().getFrontline1();
							for (int i = 0; i < Constants.height; i++) {
								for (int j = 0; j < Constants.width; j++) {
									if (i >= frontline && data.getBoard().getCellValue(i, j) == null) {
										graphics.setColor(Color.LIGHT_GRAY);
										graphics.fill(drawCell(i, j));
									}
								}
							}
						}
						if (current.getNumber() == 2) {
							frontline = data.getBoard().getFrontline2();
							for (int i = 0; i < Constants.height; i++) {
								for (int j = 0; j < Constants.width; j++) {
									if (i <= frontline && data.getBoard().getCellValue(i, j) == null) {
										graphics.setColor(Color.LIGHT_GRAY);
										graphics.fill(drawCell(i, j));
									}
								}
							}
						}
					}
				}

			}
			count++;

		}

	}

	public static void clearAll(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.clearAll(graphics, view)); // do not modify
	}

	private void clearAll(Graphics2D graphics, GameView view) {
		graphics.clearRect(0, 0, screenwidth, screenheight);
	}

	// dessine le menu home
	public static void drawHome(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawHome(graphics, view)); // do not modify
	}

	private void drawHome(Graphics2D graphics, GameView view) {

		clearAll(graphics, view);
		graphics.setColor(Color.white);
		graphics.setFont(barbarianFont((int) (squareSize * 1.66)));
		graphics.drawString("[wallimleo)", screenwidth / 8, (int) (screenheight / 6));

		graphics.setFont(celticfont((int) (squareSize * 0.50)));
		graphics.drawOval(screenwidth / 8, (int) (screenheight / 4), squareSize * 4, (int) (squareSize * 0.90));
		graphics.drawString("P1 Vs P2", screenwidth / 5, (int) (screenheight / 3.1));

		graphics.drawOval(screenwidth / 8, (int) (screenheight / 2.3), squareSize * 4, (int) (squareSize * 0.90));
		graphics.drawString("P1 Vs IA", screenwidth / 5, (int) (screenheight / 2));

		graphics.drawOval(screenwidth / 8, (int) (screenheight / 1.7), squareSize * 4, (int) (squareSize * 0.90));
		graphics.drawString("Decks", screenwidth / 5, (int) (screenheight / 1.5));

		graphics.drawOval(screenwidth / 8, (int) (screenheight / 1.3), squareSize * 4, (int) (squareSize * 0.90));
		graphics.drawString("Option", screenwidth / 5, (int) (screenheight / 1.2));
	}

	// dessine les six decks
	public static void drawDeck(ApplicationContext context, GameView view, Save save) {
		context.renderFrame(graphics -> view.drawDeck(graphics, view, save)); // do not modify
	}

	private void drawDeck(Graphics2D graphics, GameView view, Save save) {
		graphics.setColor(Color.white);
		graphics.setFont(celticfont((int) (squareSize * 0.98)));
		graphics.drawString(save.getDeckname1(), screenwidth / 3, (int) (squareSize / 1.2));
		graphics.drawString(save.getDeckname2(), screenwidth / 3, (int) (squareSize * 2.29));
		graphics.drawString(save.getDeckname3(), screenwidth / 3, (int) (squareSize * 3.75));
		graphics.drawString(save.getDeckname4(), screenwidth / 3, (int) (squareSize * 5.2));
		graphics.drawString(save.getDeckname5(), screenwidth / 3, (int) (squareSize * 6.66));
		graphics.drawString(save.getDeckname6(), screenwidth / 3, (int) (squareSize * 8.125));
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 4, (int) (squareSize / 4.8), squareSize,
				squareSize, null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 4, (int) (squareSize * 1.66), squareSize,
				squareSize, null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 4, (int) (squareSize * 3.125), squareSize,
				squareSize, null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 4, (int) (squareSize * 4.583), squareSize,
				squareSize, null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 4, (int) (squareSize * 6), squareSize, squareSize,
				null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 4, (int) (squareSize * 7.5), squareSize,
				squareSize, null);
		if (save.getCurrentDeck1().equals(save.getDeck1())) {
			graphics.drawImage(allImages.get("DeckJ1"), screenwidth / 6, (int) (squareSize / 4.8), squareSize,
					squareSize, null);
		} else {
			graphics.drawOval(screenwidth / 6, (int) (squareSize / 4.8), squareSize, squareSize);
		}
		if (save.getCurrentDeck1().equals(save.getDeck2())) {
			graphics.drawImage(allImages.get("DeckJ1"), screenwidth / 6, (int) (squareSize * 1.66), squareSize,
					squareSize, null);
		} else {
			graphics.drawOval(screenwidth / 6, (int) (squareSize * 1.66), squareSize, squareSize);
		}
		if (save.getCurrentDeck1().equals(save.getDeck3())) {
			graphics.drawImage(allImages.get("DeckJ1"), screenwidth / 6, (int) (squareSize * 3.125), squareSize,
					squareSize, null);
		} else {
			graphics.drawOval(screenwidth / 6, (int) (squareSize * 3.125), squareSize, squareSize);
		}
		if (save.getCurrentDeck2().equals(save.getDeck4())) {
			graphics.drawImage(allImages.get("DeckJ2"), screenwidth / 6, (int) (squareSize * 4.583), squareSize,
					squareSize, null);
		} else {
			graphics.drawOval(screenwidth / 6, (int) (squareSize * 4.583), squareSize, squareSize);
		}
		if (save.getCurrentDeck2().equals(save.getDeck5())) {
			graphics.drawImage(allImages.get("DeckJ2"), screenwidth / 6, (int) (squareSize * 6), squareSize, squareSize,
					null);
		} else {
			graphics.drawOval(screenwidth / 6, (int) (squareSize * 6), squareSize, squareSize);
		}
		if (save.getCurrentDeck2().equals(save.getDeck6())) {
			graphics.drawImage(allImages.get("DeckJ2"), screenwidth / 6, (int) (squareSize * 7.5), squareSize,
					squareSize, null);
		} else {
			graphics.drawOval(screenwidth / 6, (int) (squareSize * 7.5), squareSize, squareSize);
		}
		graphics.drawOval(screenwidth / 6, (int) (squareSize * 1.66), squareSize, squareSize);
		graphics.drawOval(screenwidth / 6, (int) (squareSize * 3.125), squareSize, squareSize);
		graphics.drawOval(screenwidth / 6, (int) (squareSize * 4.583), squareSize, squareSize);
		graphics.drawOval(screenwidth / 6, (int) (squareSize * 6), squareSize, squareSize);
		graphics.drawOval(screenwidth / 6, (int) (squareSize * 7.5), squareSize, squareSize);
	}

	// dessine le coffre
	public static void drawChest(ApplicationContext context, GameView view, Save save, String[] currentdeck,
			Map<String, Card> allCards, int currentindice, Edit edit) {
		context.renderFrame(
				graphics -> view.drawChest(graphics, view, save, currentdeck, allCards, currentindice, edit)); // do not
																												// modify
	}

	private void drawChest(Graphics2D graphics, GameView view, Save save, String[] currentdeck,
			Map<String, Card> allCards, int currentindice, Edit edit) {
		ArrayList<ArrayList<Card>> coffre = new ArrayList<>();
		int i = 0;
		int indice = 0;
		ArrayList<Card> page = new ArrayList<>();
		coffre.add(indice, page);
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

			if (coffre.contains(page)) {
				coffre.get(indice).add(card);
			} else {
				page.add(card);
				coffre.add(indice, page);
			}
			if (i > 7) {
				page = new ArrayList<>();
				indice++;
				i = -1;
			}
			i++;
		}
		if (coffre.size() != 1) {
			if (currentindice == 0) {
				graphics.drawImage(allImages.get("Next"), (int) (squareSize * 12.0833), (int) (squareSize * 7.5),
						squareSize, squareSize, null);
			} else if (currentindice == coffre.size() - 1) {
				graphics.drawImage(allImages.get("Back"), (int) (squareSize * 10.833), (int) (squareSize * 7.5),
						squareSize, squareSize, null);
			} else {
				graphics.drawImage(allImages.get("Back"), (int) (squareSize * 10.833), (int) (squareSize * 7.5),
						squareSize, squareSize, null);
				graphics.drawImage(allImages.get("Next"), (int) (squareSize * 12.0833), (int) (squareSize * 7.5),
						squareSize, squareSize, null);
			}
			graphics.setFont(celticfont((int) (squareSize / 4.444)));
			graphics.setColor(Color.white);
			graphics.drawString((currentindice + 1) + "/" + (coffre.size()), (int) (squareSize * 13.33),
					(int) (squareSize * 7.916));
		}
		// graphics.drawImage(allImages.get("Back"), 1500, 900, 1500+squareSize,
		// 900+squareSize, -squareSize, -squareSize, -squareSize, -squareSize, null);

		// ---------Indication_and_detail_zone------INIT
		graphics.drawImage(allImages.get("Rune"), xOrigin + (int) (squareSize * 7.5), yOrigin, (int) (squareSize * 5.1),
				(int) (squareSize * 5.1), null);
		graphics.setColor(Color.WHITE);
		graphics.draw(new Line2D.Float(xOrigin + (int) (squareSize * 7.5), yOrigin,
				xOrigin + (int) (squareSize * 7.5) + (int) (squareSize * 5.1), yOrigin));
		graphics.draw(new Line2D.Float(xOrigin + (int) (squareSize * 7.5), yOrigin + squareSize,
				xOrigin + (int) (squareSize * 7.5) + (int) (squareSize * 5.1), yOrigin + squareSize));

		// ------------separation-------------INIT
		graphics.draw(new Line2D.Float(xOrigin, yOrigin + screenheight, xOrigin, yOrigin - squareSize));

		if (coffre.size() > currentindice) {
			if (coffre.get(currentindice).size() > 0) {
				int count = 0;
				int y = 0;
				for (Card card : coffre.get(currentindice)) {
					if (count > 2) {
						count = 0;
						y += 3;
					}
					graphics.setColor(Color.WHITE);
					graphics.fill(new Rectangle2D.Float((xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count,
							(int) (yOrigin + (y * squareSize)), squareSize + squareSize / 2, squareSize * 2));
					graphics.setFont(celticfont((int) (squareSize / 4.444)));
					graphics.setColor(Color.BLACK);
					graphics.drawImage(allImages.get("Mana"),
							(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 4,
							(int) (yOrigin + (y * squareSize)), squareSize / 4, squareSize / 4, null);
					graphics.drawString("" + card.getMana(),
							(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 4
									- (int) (squareSize / 12),
							(int) (yOrigin + (y * squareSize)) + (int) (squareSize / 6));
					if (card.getPower() > 0) {
						graphics.drawImage(allImages.get("Heart"),
								(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + (int) (squareSize / 12),
								(int) (yOrigin + (y * squareSize)) + squareSize * 2 - (int) (squareSize / 3.42),
								squareSize / 4, squareSize / 4, null);
						graphics.drawString("" + card.getPower(),
								(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count,
								(int) (yOrigin + (y * squareSize)) + squareSize * 2 - (int) (squareSize / 12));
					}
					if (card.getType() == Constants.TYPE_UNIT) {
						graphics.drawImage(allImages.get("Shoes"),
								(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 2
										- (int) (squareSize / 2.66),
								(int) (yOrigin + (y * squareSize)) + squareSize * 2 - (int) (squareSize / 3.42),
								squareSize / 4, squareSize / 4, null);
						graphics.drawString("" + card.getMoveSpeed(),
								(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + 3 * squareSize / 2
										- (int) (squareSize / 8),
								(int) (yOrigin + (y * squareSize)) + squareSize * 2 - (int) (squareSize / 12));
					}
					graphics.drawImage(allImages.get(card.getName()),
							(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + (int) (squareSize / 3.7),
							(int) (yOrigin + (y * squareSize)) + (int) (squareSize / 2), squareSize, squareSize, null);
					if (card.getFaction() == "Winter Pact") {
						graphics.setColor(Color.blue);
					} else if (card.getFaction() == "Ironclad Union") {
						graphics.setColor(Color.red);
					} else if (card.getFaction() == "Swarm of the East") {
						graphics.setColor(Color.orange);
					} else if (card.getFaction() == "Tribes of Shadowfen") {
						graphics.setColor(Color.green);
					} else if (card.getFaction() == "Neutral") {
						graphics.setColor(Color.GRAY);
					}
					graphics.fill(new Rectangle2D.Float(
							(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + (int) (squareSize / 12),
							(int) (yOrigin + (y * squareSize)), (int) (squareSize / 7), (int) (squareSize / 7)));

					Coordinates c = edit.getSelected();
					if (c != null) {
						if (c.getI() > (xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count
								&& c.getI() < (xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count + squareSize
										+ squareSize / 2
								&& c.getJ() > (int) (yOrigin + (y * squareSize))
								&& c.getJ() < (int) (yOrigin + (y * squareSize)) + squareSize * 2) {
							graphics.setColor(Color.GREEN);
							graphics.drawRect((xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count - 2,
									(int) (yOrigin + (y * squareSize)) - 2, squareSize + squareSize / 2 + 2,
									squareSize * 2 + 2);
							graphics.drawImage(allImages.get("Cursor"),
									(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) * count
											+ (int) (squareSize / 1.6),
									(int) (yOrigin + (y * squareSize)) + (int) (squareSize * 1.83), squareSize / 4,
									squareSize / 4, null);
							graphics.setColor(Color.WHITE);
							graphics.drawString(card.getName(), xOrigin + (int) (squareSize * 9.2),
									yOrigin + (int) (squareSize * 1.66));
							graphics.fillOval(xOrigin + (int) (squareSize * 9.2), yOrigin + (int) (squareSize * 2),
									squareSize + squareSize / 3, squareSize + squareSize / 3);
							graphics.drawImage(allImages.get(card.getName()), xOrigin + (int) (squareSize * 9.2),
									yOrigin + (int) (squareSize * 2), squareSize + squareSize / 3,
									squareSize + squareSize / 3, null);
							graphics.setFont(celticfont((int) (squareSize / 7.5)));
							if (card.getType().equals(Constants.TYPE_UNIT)) {
								graphics.drawString("unite", xOrigin + (int) (squareSize * 7.583),
										yOrigin + (int) (squareSize * 3.6));
							} else if (card.getType().equals(Constants.TYPE_STRUCTURE)) {
								graphics.drawString("structure", xOrigin + (int) (squareSize * 7.583),
										yOrigin + (int) (squareSize * 3.6));
							} else if (card.getType().equals(Constants.TYPE_SPELL)) {
								graphics.drawString("carte magique", xOrigin + (int) (squareSize * 7.583),
										yOrigin + (int) (squareSize * 3.6));
							}
							if (card.getDescription() != "") {
								if (card.getDescription().length() > 60) {
									graphics.setFont(celticfont((int) (squareSize / 9)));
								}
								graphics.drawString(card.getDescription(), xOrigin + (int) (squareSize * 7.583),
										yOrigin + (int) (squareSize * 3.9));
							}
							graphics.setFont(celticfont((int) (squareSize / 6)));
							graphics.drawString("Faction:    " + card.getFaction(),
									xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 1.22));
							graphics.drawString("Mana :   " + card.getMana(), xOrigin + (int) (squareSize * 7.583),
									yOrigin + (int) (squareSize * 4.16));
							if (card.getPower() > 0) {
								graphics.drawString("Power :   " + card.getPower(),
										xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 4.5));
							}
							if (card.getMoveSpeed() > 0) {
								graphics.drawString("Speed deplacement :   " + card.getMoveSpeed(),
										xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 4.833));
							}
						}
					}
					count++;
				}
			}
		}

	}

	public static void drawCurrentDeck(ApplicationContext context, GameView view, Save save, Map<String, Card> allCards,
			String[] currentdeck, Edit edit, String faction) {
		context.renderFrame(
				graphics -> view.drawCurrentDeck(graphics, view, save, allCards, currentdeck, edit, faction)); // do not
																												// modify
	}

	private void drawCurrentDeck(Graphics2D graphics, GameView view, Save save, Map<String, Card> allCards,
			String[] currentdeck, Edit edit, String faction) {
		graphics.drawImage(allImages.get("EditButton"), (int) (squareSize * 1.5), 0, squareSize, squareSize, null);
		if (faction == "Winter Pact") {
			graphics.setColor(Color.blue);
		} else if (faction == "Ironclad Union") {
			graphics.setColor(Color.red);
		} else if (faction == "Swarm of the East") {
			graphics.setColor(Color.orange);
		} else if (faction == "Tribes of Shadowfen") {
			graphics.setColor(Color.green);
		} else if (faction == "Neutral") {
			graphics.setColor(Color.GRAY);
		}
		graphics.fill(new Rectangle2D.Float(xOrigin, 0, (int) (squareSize / 3), screenheight));

		int y = 0;
		for (String cardname : currentdeck) {
			if (cardname == null) {
				y += 1;
				continue;

			}
			graphics.setFont(celticfont((int) (squareSize / 4.444)));
			Card card = allCards.get(cardname);
			graphics.setColor(Color.WHITE);
			graphics.fill(new Rectangle2D.Float(0, (int) (squareSize * 1.083 + (y * 2 * squareSize / 3)),
					squareSize + (int) (squareSize * 1.5), (int) (squareSize / 2)));
			graphics.setColor(Color.BLACK);
			graphics.drawString(card.getName(), 0, (int) (squareSize * 1.444 + (y * squareSize / 1.5)));
			if (card.getFaction() == "Winter Pact") {
				graphics.setColor(Color.blue);
			} else if (card.getFaction() == "Ironclad Union") {
				graphics.setColor(Color.red);
			} else if (card.getFaction() == "Swarm of the East") {
				graphics.setColor(Color.orange);
			} else if (card.getFaction() == "Tribes of Shadowfen") {
				graphics.setColor(Color.green);
			} else if (card.getFaction() == "Neutral") {
				graphics.setColor(Color.GRAY);
			}
			graphics.fill(
					new Rectangle2D.Float((int) (squareSize * 2), (int) (squareSize * 1.083 + (y * 2 * squareSize / 3)),
							(int) (squareSize / 7), (int) (squareSize / 7)));
			Coordinates c = edit.getSelected();
			if (c != null) {
				if (c.getI() > 0 && c.getI() < 0 + squareSize + (int) (squareSize * 1.5)
						&& c.getJ() > (int) (squareSize * 1.083 + (y * 2 * squareSize / 3))
						&& c.getJ() < (int) (squareSize * 1.083 + (y * 2 * squareSize / 3)) + (int) (squareSize / 2)) {
					graphics.setColor(Color.GREEN);
					graphics.drawRect(0 - 2, (int) (squareSize * 1.083 + (y * 2 * squareSize / 3)) - 2,
							squareSize + (int) (squareSize * 1.5) + 2, (int) (squareSize / 2) + 2);
					graphics.drawImage(allImages.get("Cursor"), squareSize * 2,
							(int) (squareSize * 1.444 + (y * squareSize / 1.5)), squareSize / 4, squareSize / 4, null);
					graphics.setColor(Color.WHITE);
					graphics.drawString(card.getName(), xOrigin + (int) (squareSize * 9.2),
							yOrigin + (int) (squareSize * 1.66));
					graphics.fillOval(xOrigin + (int) (squareSize * 9.2), yOrigin + (int) (squareSize * 2),
							squareSize + squareSize / 3, squareSize + squareSize / 3);
					graphics.drawImage(allImages.get(card.getName()), xOrigin + (int) (squareSize * 9.2),
							yOrigin + (int) (squareSize * 2), squareSize + squareSize / 3, squareSize + squareSize / 3,
							null);
					graphics.setFont(celticfont((int) (squareSize / 7.5)));
					if (card.getType().equals(Constants.TYPE_UNIT)) {
						graphics.drawString("unite", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					} else if (card.getType().equals(Constants.TYPE_STRUCTURE)) {
						graphics.drawString("structure", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					} else if (card.getType().equals(Constants.TYPE_SPELL)) {
						graphics.drawString("carte magique", xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.6));
					}
					if (card.getDescription() != "") {
						if (card.getDescription().length() > 60) {
							graphics.setFont(celticfont((int) (squareSize / 9)));
						}
						graphics.drawString(card.getDescription(), xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 3.9));
					}
					graphics.setFont(celticfont((int) (squareSize / 6)));
					graphics.drawString("Faction:    " + card.getFaction(), xOrigin + (int) (squareSize * 7.583),
							yOrigin + (int) (squareSize * 1.22));
					graphics.drawString("Mana :   " + card.getMana(), xOrigin + (int) (squareSize * 7.583),
							yOrigin + (int) (squareSize * 4.16));
					if (card.getPower() > 0) {
						graphics.drawString("Power :   " + card.getPower(), xOrigin + (int) (squareSize * 7.583),
								yOrigin + (int) (squareSize * 4.5));
					}
					if (card.getMoveSpeed() > 0) {
						graphics.drawString("Speed deplacement :   " + card.getMoveSpeed(),
								xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 4.833));
					}
				}
			}
			y += 1;
		}
	}

	public static void drawButtonTransvers(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawButtonTransvers(graphics, view)); // do not modify
	}

	private void drawButtonTransvers(Graphics2D graphics, GameView view) {
		graphics.drawImage(allImages.get("Transvaser"), (int) (squareSize * 10.833), (int) (squareSize * 5.833),
				squareSize + squareSize, squareSize, null);
	}

	public static void drawOption(ApplicationContext context, GameView view) {
		context.renderFrame(graphics -> view.drawOption(graphics, view)); // do not modify
	}

	private void drawOption(Graphics2D graphics, GameView view) {
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 6, (int) (squareSize / 4.8), squareSize,
				squareSize, null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 6, (int) (squareSize * 1.66), squareSize,
				squareSize, null);
		graphics.drawImage(allImages.get("EditButton"), screenwidth / 6, (int) (squareSize * 3.125), squareSize,
				squareSize, null);
		graphics.setColor(Color.white);
		graphics.setFont(celticfont((int) (squareSize * 0.98)));
		graphics.drawString("Nom du J1", screenwidth / 4, (int) (squareSize / 1.2));
		graphics.drawString("Nom du J2", screenwidth / 4, (int) (squareSize * 2.29));
		graphics.drawString("Code triche", screenwidth / 4, (int) (squareSize * 3.75));
	}

	public static void drawCurrentName(ApplicationContext context, GameView view, String name) {
		context.renderFrame(graphics -> view.drawCurrentName(graphics, view, name));
	}

	private void drawCurrentName(Graphics2D graphics, GameView view, String name) {
		graphics.setColor(Color.white);
		graphics.drawString("a-z : ajoute la lettre au nom", 0, (int) (squareSize * 5.833));
		graphics.drawString("SHIFT: met la derniere lettre en MAJUSCULE", 0, (int) (squareSize * 6.25));
		graphics.drawString("LEFT (fleche gauche): supprime la derniere lettre", 0, (int) (squareSize * 6.66));
		graphics.setFont(celticfont((int) (squareSize * 0.98)));
		String tiret = "";
		for (int i = name.length(); i < 12; i++) {
			tiret += "-";
		}
		graphics.drawString(name + tiret, (int) (squareSize * 5.833), (int) (squareSize * 4.5));
	}

	public static void drawCard(ApplicationContext context, GameView view, String card, Map<String, Card> allCards) {
		context.renderFrame(graphics -> view.drawCard(graphics, view, card, allCards)); // do not modify
	}

	private void drawCard(Graphics2D graphics, GameView view, String cardname, Map<String, Card> allCards) {
		Card card = allCards.get(cardname);
		// ---------Indication_and_detail_zone------INIT
		graphics.drawImage(allImages.get("Rune"), xOrigin + (int) (squareSize * 7.5), yOrigin, (int) (squareSize * 5.1),
				(int) (squareSize * 5.1), null);
		graphics.setColor(Color.WHITE);
		graphics.draw(new Line2D.Float(xOrigin + (int) (squareSize * 7.5), yOrigin,
				xOrigin + (int) (squareSize * 7.5) + (int) (squareSize * 5.1), yOrigin));
		graphics.draw(new Line2D.Float(xOrigin + (int) (squareSize * 7.5), yOrigin + squareSize,
				xOrigin + (int) (squareSize * 7.5) + (int) (squareSize * 5.1), yOrigin + squareSize));
		graphics.setColor(Color.WHITE);
		graphics.drawString(card.getName(), xOrigin + (int) (squareSize * 9.2), yOrigin + (int) (squareSize * 1.66));
		graphics.fillOval(xOrigin + (int) (squareSize * 9.2), yOrigin + (int) (squareSize * 2),
				squareSize + squareSize / 3, squareSize + squareSize / 3);
		graphics.drawImage(allImages.get(card.getName()), xOrigin + (int) (squareSize * 9.2),
				yOrigin + (int) (squareSize * 2), squareSize + squareSize / 3, squareSize + squareSize / 3, null);
		graphics.setFont(celticfont((int) (squareSize / 7.5)));
		if (card.getType().equals(Constants.TYPE_UNIT)) {
			graphics.drawString("unite", xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 3.6));
		} else if (card.getType().equals(Constants.TYPE_STRUCTURE)) {
			graphics.drawString("structure", xOrigin + (int) (squareSize * 7.583), yOrigin + (int) (squareSize * 3.6));
		} else if (card.getType().equals(Constants.TYPE_SPELL)) {
			graphics.drawString("carte magique", xOrigin + (int) (squareSize * 7.583),
					yOrigin + (int) (squareSize * 3.6));
		}
		if (card.getDescription() != "") {
			if (card.getDescription().length() > 60) {
				graphics.setFont(celticfont((int) (squareSize / 9)));
			}
			graphics.drawString(card.getDescription(), xOrigin + (int) (squareSize * 7.583),
					yOrigin + (int) (squareSize * 3.9));
		}
		graphics.setFont(celticfont((int) (squareSize / 6)));
		graphics.drawString("Mana :   " + card.getMana(), xOrigin + (int) (squareSize * 7.583),
				yOrigin + (int) (squareSize * 4.16));
		if (card.getPower() > 0) {
			graphics.drawString("Power :   " + card.getPower(), xOrigin + (int) (squareSize * 7.583),
					yOrigin + (int) (squareSize * 4.5));
		}
		if (card.getMoveSpeed() > 0) {
			graphics.drawString("Speed deplacement :   " + card.getMoveSpeed(), xOrigin + (int) (squareSize * 7.583),
					yOrigin + (int) (squareSize * 4.833));
		}
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float((xOrigin + squareSize / 2) + (int) (squareSize * 2.5),
				(int) (yOrigin + (3 * squareSize)), squareSize + squareSize / 2, squareSize * 2));
		graphics.setFont(celticfont((int) (squareSize / 4.444)));
		graphics.setColor(Color.BLACK);
		graphics.drawImage(allImages.get("Mana"),
				(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + 3 * squareSize / 4,
				(int) (yOrigin + (3 * squareSize)), squareSize / 4, squareSize / 4, null);
		graphics.drawString("" + card.getMana(),
				(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + 3 * squareSize / 4 - (int) (squareSize / 12),
				(int) (yOrigin + (3 * squareSize)) + (int) (squareSize / 6));
		if (card.getPower() > 0) {
			graphics.drawImage(allImages.get("Heart"),
					(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + (int) (squareSize / 12),
					(int) (yOrigin + (3 * squareSize)) + squareSize * 2 - (int) (squareSize / 3.42), squareSize / 4,
					squareSize / 4, null);
			graphics.drawString("" + card.getPower(), (xOrigin + squareSize / 2) + (int) (squareSize * 2.5),
					(int) (yOrigin + (3 * squareSize)) + squareSize * 2 - (int) (squareSize / 12));
		}
		if (card.getType() == Constants.TYPE_UNIT) {
			graphics.drawImage(allImages.get("Shoes"),
					(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + 3 * squareSize / 2
							- (int) (squareSize / 2.66),
					(int) (yOrigin + (3 * squareSize)) + squareSize * 2 - (int) (squareSize / 3.42), squareSize / 4,
					squareSize / 4, null);
			graphics.drawString("" + card.getMoveSpeed(),
					(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + 3 * squareSize / 2 - (int) (squareSize / 8),
					(int) (yOrigin + (3 * squareSize)) + squareSize * 2 - (int) (squareSize / 12));
		}
		graphics.drawImage(allImages.get(card.getName()),
				(xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + (int) (squareSize / 3.7),
				(int) (yOrigin + (3 * squareSize)) + (int) (squareSize / 2), squareSize, squareSize, null);
		if (card.getFaction() == "Winter Pact") {
			graphics.setColor(Color.blue);
		} else if (card.getFaction() == "Ironclad Union") {
			graphics.setColor(Color.red);
		} else if (card.getFaction() == "Swarm of the East") {
			graphics.setColor(Color.orange);
		} else if (card.getFaction() == "Tribes of Shadowfen") {
			graphics.setColor(Color.green);
		} else if (card.getFaction() == "Neutral") {
			graphics.setColor(Color.GRAY);
		}
		graphics.fill(
				new Rectangle2D.Float((xOrigin + squareSize / 2) + (int) (squareSize * 2.5) + (int) (squareSize / 12),
						(int) (yOrigin + (3 * squareSize)), (int) (squareSize / 7), (int) (squareSize / 7)));
	}

}

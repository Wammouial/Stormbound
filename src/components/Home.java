package components;

import java.awt.geom.Point2D;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class Home {
	private Coordinates selected;

	public Home() {
		// TODO Auto-generated constructor stub
	}

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

	private boolean chooseVS(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > v.getscrWidth() / 8 && c.getI() < v.getscrWidth() / 8 + v.getSquareSize() * 4
				&& c.getJ() > v.getscrHeight() / 4 && c.getJ() < v.getscrHeight() / 4 + v.getSquareSize() * 0.90) {
			return true;
		}
		return false;
	}

	private boolean chooseIA(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > v.getscrWidth() / 8 && c.getI() < v.getscrWidth() / 8 + v.getSquareSize() * 4
				&& c.getJ() > v.getscrHeight() / 2.3 && c.getJ() < v.getscrHeight() / 2.3 + v.getSquareSize() * 0.90) {
			return true;
		}
		return false;
	}

	private boolean choosePerson(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > v.getscrWidth() / 8 && c.getI() < v.getscrWidth() / 8 + v.getSquareSize() * 4
				&& c.getJ() > v.getscrHeight() / 1.7 && c.getJ() < v.getscrHeight() / 1.7 + v.getSquareSize() * 0.90) {
			return true;
		}
		return false;
	}
	
	private boolean chooseOption(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() > v.getscrWidth() / 8 && c.getI() < v.getscrWidth() / 8 + v.getSquareSize() * 4
				&& c.getJ() > v.getscrHeight() / 1.3 && c.getJ() < v.getscrHeight() / 1.3 + v.getSquareSize() * 0.90) {
			return true;
		}
		return false;
	}

	private boolean clicOnClose(GameView v) {
		Coordinates c = getSelected();
		if (c.getI() >= v.getscrWidth() - v.getSquareSize()/2 && c.getI() < v.getscrWidth() && c.getJ() >= 0
				&& c.getJ() < v.getSquareSize()/2) {
			return true;
		}

		return false;
	}

	public int menuHome(ApplicationContext context, GameView view) {
		Point2D.Float location;
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
				} else if (chooseVS(view) == true) {
					return 2;
				} else if (chooseIA(view) == true) {
					return 1;
				}else if (chooseOption(view) == true) {
					return 4;
				}else if (choosePerson(view) == true) {
					return 3;
				}
			}
		}
	}
}

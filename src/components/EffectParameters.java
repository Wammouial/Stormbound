/**
 * Cette classe sert à transmettre les parametres utiles aux methodes de Effects.
 */
package components;

/**
 * @author W
 *
 */
public class EffectParameters {
	private Unit[][] matrice;
	private Player currentPlayer;
	private Player otherPlayer;
	private int[] unitPos;
	
	/**
	 * 
	 */
	public EffectParameters(Unit[][] matrice, Player currentPlayer, Player otherPlayer, int[] unitPos) {
		// TODO Auto-generated constructor stub
		this.matrice = matrice;
		this.currentPlayer = currentPlayer;
		this.otherPlayer = otherPlayer;
		this.unitPos = unitPos;
	}
	
	public EffectParameters(Unit[][] matrice, Player currentPlayer, Player otherPlayer) {
		// TODO Auto-generated constructor stub
		this.matrice = matrice;
		this.currentPlayer = currentPlayer;
		this.otherPlayer = otherPlayer;
		this.unitPos = null;
	}
	
	public Unit[][] getMatrice() {
		return matrice;
	}
	
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Player getOtherPlayer() {
		return otherPlayer;
	}
		
	public int[] getUnitPos() {
		return unitPos;
	}

}

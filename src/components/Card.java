/**
 * 
 */
package components;

/**
 * @author W
 *
 */
public interface Card extends Cloneable {
	public String getName();
	public String getType();
	public String getRarity();
	public String getEffect();
	public String getDescription();
	public String getFaction();
	
	public int getPlayer();
	public int getMoveSpeed();
	public int getPower();
	public int getMana();
	
	public boolean hasEffect();
	
	public boolean setPlayer(int player);
	public boolean enoughMana(int manaPlayer);
	public boolean play(int i, int j);
	
	public Card clone();


	
	

}

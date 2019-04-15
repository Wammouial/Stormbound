/**
 * 
 */
package components;

/**
 * @author William
 *
 */
public class Structure extends Unit {
	/**
	 * @param name
	 * @param mana
	 * @param moveSpeed
	 * @param player
	 * @param power
	 * @param unitType
	 */
	public Structure(String name, int mana, int player, int power, String rarity, String description, String effectType, String faction) {
		super(name, mana, 0, player, power, rarity, description, "", effectType, faction);
		// TODO Auto-generated constructor stub
	}
	
	public Structure(String name, int mana, int player, int power, String rarity, String description, String faction) {
		super(name, mana, 0, player, power, rarity, description, "", faction);
		// TODO Auto-generated constructor stub
	}
	
	public Structure(String name, int mana, int player, int power, String rarity, String description) {
		super(name, mana, 0, player, power, rarity, description, "");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		return Constants.TYPE_STRUCTURE;
	}
	
	@Override
	public String getUnitType() {
		// TODO Auto-generated method stub
		return "";
	}
	
	@Override
	public String getRepresent() {
		return "[B/" + super.getPlayer() + "]";
	}
	
	@Override
	public Card clone() {
		// TODO Auto-generated method stub
		return new Structure(super.getName(), super.getMana(), super.getPlayer(), super.getPower(), super.getRarity(), super.getDescription(), super.getEffect(), super.getFaction());
	}

}

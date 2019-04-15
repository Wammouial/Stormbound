/**
 * 
 */
package components;

/**
 * @author William
 *
 */
public class Unit implements Card {
	private String name;
	private int mana;
	private int moveSpeed;
	private int player;
	private int power;
	private String rarity;
	private String unitType;
	private String description;
	private EffectType effectType;
	private String faction;
	
	
	/**
	 * @param name
	 * @param mana
	 * @param moveSpeed
	 * @param player
	 * @param power
	 * @param unitType
	 */
	
	public Unit(String name, int mana, int moveSpeed, int player, int power, String rarity, String description, String unitType, String effectType, String faction) {
		this.name = name;
		this.mana = mana;
		this.moveSpeed = moveSpeed;
		this.player = player;
		this.power = power;
		this.rarity = rarity;
		this.unitType = unitType;
		this.description = description;
		this.effectType = new EffectType(effectType);
		this.faction = faction;
	}
		
		
	public Unit(String name, int mana, int moveSpeed, int player, int power, String rarity, String description, String unitType, String faction) {
		this.name = name;
		this.mana = mana;
		this.moveSpeed = moveSpeed;
		this.player = player;
		this.power = power;
		this.rarity = rarity;
		this.unitType = unitType;
		this.description = description;
		this.effectType = new EffectType("");
		this.faction = faction;
	}
	
	public Unit(String name, int mana, int moveSpeed, int player, int power, String rarity, String description, String unitType) {
		this.name = name;
		this.mana = mana;
		this.moveSpeed = moveSpeed;
		this.player = player;
		this.power = power;
		this.rarity = rarity;
		this.unitType = unitType;
		this.description = description;
		this.effectType = new EffectType("");
		this.faction = "Neutral";
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public int getMana() {
		// TODO Auto-generated method stub
		return mana;
	}
	
	public String getUnitType() {
		return unitType;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public int getMoveSpeed() {
		// TODO Auto-generated method stub
		return moveSpeed;
	}
	
	@Override
	public String getType() {
		return Constants.TYPE_UNIT;
	}
	
	@Override
	public String getRarity() {
		// TODO Auto-generated method stub
		return rarity;
	}
	
	@Override
	public int getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}
	
	@Override
	public int getPower() {
		// TODO Auto-generated method stub
		return power;
	}
	
	public String getRepresent() {
		return "[U/" + player + "]";
	}
	
	@Override
	public String getEffect() {
		return effectType.getEffect();
	}
	
	@Override
	public String getFaction() {
		return faction;
	}
	
	public boolean isDead() {
		return power <= 0;
	}
	
	@Override
	public boolean hasEffect() {
		// TODO Auto-generated method stub
		return effectType.hasEffect();
	}
	
	@Override
	public boolean setPlayer(int player) {
		// TODO Auto-generated method stub
		if (player == 0 || player == 1 || player == 2){
			this.player = player;
			return true;
		}
		return false;
	}
	
	/* Renvoie le gagnant du combat avec une autre unite */
	public Unit fight(Unit otherUnit) {
		if (this.power > otherUnit.power) {
			this.power -= otherUnit.power;
			return this;
		} else if (this.power < otherUnit.power) {
			otherUnit.power -= this.power;
			return otherUnit;
		} else {
			return null;
		}
	}
	
	@Override
	public boolean enoughMana(int manaPlayer) {
		// TODO Auto-generated method stub
		return (manaPlayer >= mana);
	}
	
	@Override
	public boolean play(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void lessPower(int i) {
		power -= i;
	}
	
	public void addPower(int i) {
		power += i;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getRepresent() +  "(" + mana + ", " + power + ")";
	}
	
	@Override
	public Card clone() {
		return new Unit(name, mana, moveSpeed, player, power, rarity, description, unitType, effectType.getEffect(), faction);
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof Unit) {
			Unit u = (Unit) o;
			return u.name == name && u.mana == mana && u.moveSpeed == moveSpeed && u.player == player && u.rarity == rarity && u.unitType == unitType && u.description.equals(description); 
		} else {
			return false;
		}
	}

}

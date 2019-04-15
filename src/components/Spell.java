/**
 * 
 */
package components;

/**
 * @author William
 *
 */
public class Spell implements Card {
	private String name;
	private int mana;
	private int player;
	private String rarity;
	private String description;
	private EffectType effectType;
	private String faction;
	

	/**
	 * 
	 */
	public Spell(String name, int mana, int player, String rarity, String description, String faction) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.mana = mana;
		this.player = player;
		this.rarity = rarity;
		this.description = description;
		this.effectType = new EffectType(Constants.EF_ON_PLAY);
		this.faction = faction;
	}
	
	public Spell(String name, int mana, int player, String rarity, String description) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.mana = mana;
		this.player = player;
		this.rarity = rarity;
		this.description = description;
		this.effectType = new EffectType(Constants.EF_ON_PLAY);
		this.faction = "Neutral";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public int getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}
	
	@Override
	public String getRarity() {
		// TODO Auto-generated method stub
		return rarity;
	}
	
	@Override
	public int getMana() {
		// TODO Auto-generated method stub
		return mana;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getType() {
		return Constants.TYPE_SPELL;
	}
	
	@Override
	public int getMoveSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getPower() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getFaction() {
		return faction;
	}
	
	@Override
	public boolean hasEffect() {
		// TODO Auto-generated method stub
		return effectType.hasEffect();
	}
	
	@Override
	public String getEffect() {
		// TODO Auto-generated method stub
		return effectType.getEffect();
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

	@Override
	public boolean enoughMana(int manaPlayer) {
		// TODO Auto-generated method stub
		return manaPlayer >= mana;
	}

	@Override
	public boolean play(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Card clone() {
		return new Spell(name, mana, player, rarity, description, faction);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[S/" + player + "](" + mana + ")";
	}

}

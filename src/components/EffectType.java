/**
 * Informations sur l'effet que possede chaque carte a effet.
 */

package components;

import java.util.ArrayList;

public class EffectType {
	private ArrayList<String> types;
	private String current;
	private String description;
	
	public EffectType(String s, String desc) {
		// TODO Auto-generated constructor stub
		types = new ArrayList<String>();
		types.add(Constants.EF_ON_PLAY);
		types.add(Constants.EF_ON_DEATH);
		types.add(Constants.EF_ON_ATTACK);
		types.add(Constants.EF_ON_DEFENSE);
		types.add(Constants.EF_ON_FIGHT);
		types.add(Constants.EF_ON_BEGIN_TURN);
		
		
		if (types.contains(s)) {
			current = s;
		} else {
			current = Constants.EF_NULL;
		}
		
		description = desc;
	}
	
	public EffectType(String s) {
		this(s, "");
	}
	
	public EffectType() {
		// TODO Auto-generated constructor stub
		this(Constants.EF_NULL);
	}
	
	public boolean hasEffect() {
		return !(current.equals(Constants.EF_NULL));
	}
	
	
	public String getEffect() {
		return current;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof EffectType) {
			EffectType et = (EffectType) o;
			return current.equals(et.current);
		}
		return false;
	}
	
}

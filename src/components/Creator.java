/**
 * 
 */
package components;

import java.awt.Image;
import java.util.*;
import java.util.function.Consumer;

import javax.swing.ImageIcon;



/**
 * @author William
 *
 */
public class Creator {
	
	/* Liste des cartes du deck de base */
	public static final String[] base = { "Bladestorm", "Felflares", "Heroic Soldiers", "Emerald Towers",
										 "Execution", "Fort of Ebonrock", "Gifted Recruits", "Joust Champions",
										 "Personal Servers", "Siegebreakers", "Veterans of War", "Warfront Runners" };
	
	
	public static Map<String, Card> createCards(){
		Map<String, Card> cardsMap = new HashMap<String, Card>();
		
		int player = 0; /* Base value for cards which are not in deck */ 
		
		/* 12 Base Cards */
		cardsMap.put("Bladestorm", new Spell("Bladestorm", 5, player, "Common", EffectDescription.Bladestorm));
		cardsMap.put("Felflares", new Unit("Felflares", 3, 0, player, 2, "Common", EffectDescription.Felflares, "Frostling", Constants.EF_ON_PLAY, "Neutral"));
		cardsMap.put("Heroic Soldiers", new Unit("Heroic Soldiers", 5, 1, player, 5, "Common", EffectDescription.None, "Knight", "Neutral"));
		cardsMap.put("Emerald Towers", new Structure("Emerald Towers", 4, player, 3, "Common", EffectDescription.Emerald_Towers, Constants.EF_ON_BEGIN_TURN, "Neutral"));
		cardsMap.put("Execution", new Spell("Execution", 4, player, "Common", EffectDescription.Execution));
		cardsMap.put("Fort of Ebonrock", new Structure("Fort of Ebonrock", 3, player, 4, "Common", EffectDescription.None));
		cardsMap.put("Gifted Recruits", new Unit("Gifted Recruits", 2, 1, player, 2, "Common", EffectDescription.None, "Knight", "Neutral"));
		cardsMap.put("Joust Champions", new Unit("Joust Champions", 8, 2, player, 3, "Rare", EffectDescription.Joust_Champions, "Knight", Constants.EF_ON_ATTACK, "Neutral"));
		cardsMap.put("Personal Servers", new Unit("Personal Servers", 4, 1, player, 2, "Common", EffectDescription.Personnal_Servers, "Construct", Constants.EF_ON_PLAY, "Neutral"));
		cardsMap.put("Siegebreakers", new Unit("Siegebreakers", 4, 0, player, 4, "Rare", EffectDescription.Siegebreakers, "Satyr", Constants.EF_ON_PLAY, "Neutral"));
		cardsMap.put("Veterans of War", new Unit("Veterans of War", 7, 1, player, 8, "Common", EffectDescription.None, "Knight", "Neutral"));
		cardsMap.put("Warfront Runners", new Unit("Warfront Runners", 4, 2, player, 2, "Common", EffectDescription.None, "Knight", "Neutral"));
		
		// other Neutrals cards
		
		cardsMap.put("Archdruid Earyn", new Unit("Archdruid Earyn", 7, 0, player, 7, "Legendary", EffectDescription.None, "")); //On play, randomly play up to 1 spell card from your hand.
		cardsMap.put("Avian Stalkers", new Unit("Avian Stalkers", 7, 0, player, 6, "Epic", EffectDescription.None, "Raven")); //On death, spawn 3 strength Ravens on all tiles behind.
		cardsMap.put("Beasts of Terror", new Unit("Beasts of Terror", 4, 1, player, 3, "Epic", EffectDescription.None, "Dragon")); //When attacking a non-Dragon unit, deal 2 damage to all other enemies with the same unit type.
		cardsMap.put("Bluesail Raiders", new Unit("Bluesail Raiders", 5, 2, player, 3, "Common", EffectDescription.None, "Pirate"));
		cardsMap.put("Boomstick Officers", new Unit("Boomstick Officers", 6, 0, player, 6, "Rare", EffectDescription.None, "Rodent")); //On play, deal 3 damage to a random enemy unit behind.
		cardsMap.put("Brothers in Arms", new Unit("Brothers in Arms", 3, 0, player, 3, "Common", EffectDescription.None, "Knight")); //On play, spawn a 1 strength Knight on the tile behind.
		cardsMap.put("Cabin Girls", new Unit("Cabin Girls", 4, 0, player, 5, "Common", EffectDescription.None, "Pirate"));
		cardsMap.put("Call for Aid", new Spell("Call for Aid", 7, player, "Rare", EffectDescription.Call_For_Aid)); //Spawn 3 strength units on all tiles bordering a target friendly unit.
		cardsMap.put("Collector Mirz", new Unit("Collector Mirz", 3, 1, player, 2, "Legendary", EffectDescription.None, "")); //On play, create a 0 cost unit with 5 strength and add it to your deck.
		cardsMap.put("Confinement", new Spell("Confinement", 4, player, "Common", EffectDescription.Confinement)); //Reduce the strength of a target enemy unit to 5.
		cardsMap.put("Conflicted Drakes", new Unit("Conflicted Drakes", 3, 0, player, 3, "Rare", EffectDescription.None, "Dragon")); //On play, deal 1 damage to all non-Dragon units in front.
		cardsMap.put("Crazy Bombers", new Unit("Crazy Bombers", 9, 1, player, 7, "Epic", EffectDescription.None, "Rodent")); //On play, deal 7 damage spread randomly among all bordering enemies.
		cardsMap.put("Dangerous Suitors", new Unit("Dangerous Suitors", 5, 1, player, 3, "Rare", EffectDescription.None, "Dragon")); //On play, gain 2 strength for every other Dragon on the board.
		cardsMap.put("First Mutineer", new Unit("First Mutineer", 2, 2, player, 1, "Common", EffectDescription.None, "Pirate")); //On play, discard 1 random card from your hand.
		cardsMap.put("Flooding the Gates", new Spell("Flooding the Gates", 6, player, "Rare", EffectDescription.Flooding_the_Gates)); //Deal 3 damage to all units bordering your base.
		cardsMap.put("Freebooters", new Unit("Freebooters", 3, 0, player, 2, "Common", EffectDescription.None, "Pirate")); //On play, draw 1 card.
		cardsMap.put("Goldgrubbers", new Unit("Goldgrubbers", 4, 1, player, 4, "Epic", EffectDescription.None, "Pirate")); //On play, replace 1 random non-Pirate card from your hand.
		cardsMap.put("Green Prototypes", new Unit("Green Prototypes", 1, 1, player, 1, "Rare", EffectDescription.None, "Construct")); //On death, give 1 strength to a random bordering enemy unit.
		cardsMap.put("Hearthguards", new Unit("Hearthguards", 6, 2, player, 3, "Epic", EffectDescription.None, "Dwarf")); //If played bordering a friendly structure or base, gain 3 strength.
		cardsMap.put("Kindred's Grace", new Spell("Kindred's Grace", 6, player, "Epic", EffectDescription.Kindreds_Grace)); //Give 5 strength to a target friendly unit and 2 strength to all others with the same unit type.
		cardsMap.put("Lawless Herd", new Unit("Lawless Herd", 1, 0, player, 1, "Common", EffectDescription.None, "Satyr"));
		cardsMap.put("Lich Summoners", new Unit("Lich Summoners", 6, 1, player, 6, "Epic", EffectDescription.None, "Undead")); //After attacking, spawn a 3 strength Undead on the tile behind.
		cardsMap.put("Lucky Charmers", new Unit("Lucky Charmers", 6, 1, player, 4, "Rare", EffectDescription.None, "Pirate")); //On play, gain 2 strength for each Pirate in your hand.
		cardsMap.put("Ludic Matriarchs", new Unit("Ludic Matriarchs", 6, 0, player, 5, "Epic", EffectDescription.None, "Dragon")); //If played bordering exactly 1 friendly Dragon, spawn 1 strength Dragons on all tiles bordering either.
		cardsMap.put("Needle Blast", new Spell("Needle Blast", 5, player, "Rare", EffectDescription.Needle_Blast)); //Deal 2 damage to 2 random enemies.
		cardsMap.put("Northsea Dog", new Unit("Northsea Dog", 1, 0, player, 1, "Rare", EffectDescription.None, "Pirate")); //If played as the last card in your hand, gain 4 strength.
		cardsMap.put("Potion of Growth", new Spell("Potion of Growth", 3, player, "Common", EffectDescription.Potion_of_Growth)); //Give a target friendly unit 3 strength.
		cardsMap.put("Salty Outcasts", new Unit("Salty Outcasts", 7, 2, player, 5, "Common", EffectDescription.None, "Toad"));
		cardsMap.put("Sharpfist Exiles", new Unit("Sharpfist Exiles", 7, 1, player, 1, "Rare", EffectDescription.None, "Toad")); //On play, gain 2 strength for every enemy unit.
		cardsMap.put("Siren of the Seas", new Unit("Siren of the Seas", 10, 3, player, 6, "Legendary", EffectDescription.None, "")); //Before attacking a unit, reduce its strength to 3.
		cardsMap.put("Snowmasons", new Unit("Snowmasons", 4, 1, player, 2, "Rare", EffectDescription.None, "Dwarf")); //On death, give 4 strength spread randomly among all surrounding friendly units.
		cardsMap.put("Spare Dragonlings", new Unit("Spare Dragonlings", 2, 1, player, 1, "Rare", EffectDescription.None, "Dragon")); //On death, give a random friendly Dragon 4 strength.
		cardsMap.put("Summon Militia", new Spell("Summon Militia", 2, player, "Common", EffectDescription.Summon_Militia)); //Randomly spawn a Knight with 4 strength.
		cardsMap.put("Tegor the Vengeful", new Unit("Tegor the Vengeful", 6, 2, player, 4, "Legendary", EffectDescription.None, "Dragon")); //On death, do a random effect: give 6 strength, deal 5 damage or spawn a 4 strength unit.
		cardsMap.put("Temple Guardians", new Unit("Temple Guardians", 8, 0, player, 6, "Epic", EffectDescription.None, "Knight")); //If played bordering your base, spawn 4 strength Knights on all tiles in the same row.
		cardsMap.put("Terrific Slayers", new Unit("Terrific Slayers", 5, 1, player, 4, "Rare", EffectDescription.None, "Knight")); //Before attacking a Dragon, deal 4 additional damage.
		cardsMap.put("Trueshot Post", new Structure("Trueshot Post", 5, player, 4, EffectDescription.None, "")); //At the start of your turn, deal 3 damage to a random enemy unit.
		cardsMap.put("Ubass the Hunter", new Unit("Ubass the Hunter", 5, 1, player, 5, "Legendary", EffectDescription.None, "")); //On play, for each unit type on the board, deal 1 damage to a random enemy.
		cardsMap.put("Victors of the Melee", new Unit("Victors of the Melee", 6, 1, player, 4, "Rare", EffectDescription.None, "Knight")); //When attacking, deal 2 damage to all surrounding enemy units and structures.
		cardsMap.put("Voidsurgers", new Unit("Voidsurgers", 5, 0, player, 4, "Rare", EffectDescription.None, "Frostling")); //If played bordering at least 2 enemies, deal 2 damage to each.
		cardsMap.put("Westwind Sailors", new Unit("Westwind Sailors", 3, 1, player, 3, "Common", EffectDescription.None, "Pirate"));
		
		// Tribes of Shadowfen
		
		cardsMap.put("Amberhides", new Unit("Amberhides", 5, 1, player, 4, "Common", EffectDescription.None, "Toad", "Tribes of Shadowfen")); //On play, drain 4 strength from a random surrounding poisoned unit.
		cardsMap.put("Azure Hatchers", new Unit("Azure Hatchers", 3, 2, player, 1, "Rare", EffectDescription.None, "Toad", "Tribes of Shadowfen")); //On death, spawn 2 Toads with 1 strength on random surrounding tiles.
		cardsMap.put("Blood Ministers", new Unit("Blood Ministers", 6, 0, player, 4, "Epic", EffectDescription.None, "Raven", "Tribes of Shadowfen")); //On play, convert all bordering enemy units with 3 or lower strength to fight for you.
		cardsMap.put("Brood Sage", new Unit("Brood Sage", 3, 1, player, 2, "Epic", EffectDescription.None, "Toad", "Tribes of Shadowfen")); //On play, spawn a 2 strength Toad for each surrounding poisoned unit.
		cardsMap.put("Broodmother Qordia", new Unit("Broodmother Qordia", 7, 1, player, 5, "Legendary", EffectDescription.None, "Dragon", "Tribes of Shadowfen")); //On play, spawn 3 Nests on random surrounding tiles which will hatch 3 strength Dragons.
		cardsMap.put("Copperskin Ranger", new Unit("Copperskin Ranger", 2, 1, player, 1, "Common", EffectDescription.None, "Toad", "Tribes of Shadowfen")); //On play, poison 1 random enemy unit.
		cardsMap.put("Crimson Sentry", new Unit("Crimson Sentry", 4, 2, player, 1, "Common", EffectDescription.None, "Toad", "Tribes of Shadowfen")); //On death, deal 2 damage to all bordering units and poison them.
		cardsMap.put("Curse of Strings", new Spell("Curse of Strings", 8, player, "Rare", EffectDescription.None, "Tribes of Shadowfen")); //Convert a target enemy unit with 7 or lower strength to fight for you.
		cardsMap.put("Dubious Hags", new Unit("Dubious Hags", 2, 1, player, 3, "Common", EffectDescription.None, "Raven", "Tribes of Shadowfen")); //On death, spawn a 1 strength Raven on a random bordering tile to fight for the enemy.
		cardsMap.put("Feral Shamans", new Unit("Feral Shamans", 5, 1, player, 3, "Rare", EffectDescription.None, "Raven", "Tribes of Shadowfen")); //Before attacking a stronger unit, drain 2 strength from it.
		cardsMap.put("Harpies of the Hunt", new Unit("Harpies of the Hunt", 3, 0, player, 4, "Common", EffectDescription.None, "Raven", "Tribes of Shadowfen"));
		cardsMap.put("High Priestess Klaxi", new Unit("High Priestess Klaxi", 8, 0, player, 6, "Legendary", EffectDescription.None, "Raven", "Tribes of Shadowfen")); //On play, randomly destroy 3 weaker friendly units and spawn 5 strength Ravens there.
		cardsMap.put("Marked as Prey", new Spell("Marked as Prey", 4, player, "Rare", EffectDescription.None, "Tribes of Shadowfen")); //Deal 4 damage to a target poisoned unit. If that kills it, spawn a 4 strength Raven there.
		cardsMap.put("Rain of Frogs", new Spell("Rain of Frogs", 3, player, "Epic", EffectDescription.None, "Tribes of Shadowfen")); //Randomly spawn 4 Toads with 1 strength.
		cardsMap.put("Soulcrushers", new Unit("Soulcrushers", 5, 1, player, 5, "Epic", EffectDescription.None, "Raven", "Tribes of Shadowfen")); //Before attacking a weaker enemy unit, destroy it instantly.
		cardsMap.put("Tode the Elevated", new Unit("Tode the Elevated", 6, 1, player, 5, "Legendary", EffectDescription.None, "Toad", "Tribes of Shadowfen")); //After attacking, jump in front of a random enemy unit or structure and gain 3 strength.
		cardsMap.put("Toxic Sacrifice", new Spell("Toxic Sacrifice", 3, player, "Common", EffectDescription.None, "Tribes of Shadowfen")); //Destroy a target friendly unit. Deal 2 damage to all surrounding units and poison them.
		cardsMap.put("Venomfall Spire", new Structure("Venomfall Spire", 4, player, 3, EffectDescription.None, "", "Tribes of Shadowfen")); //At the start of your turn, poison 1 random enemy unit.
		cardsMap.put("Wandering Wyrms", new Unit("Wandering Wyrms", 3, 0, player, 3, "Rare", EffectDescription.None, "Dragon", "Tribes of Shadowfen")); //If played bordering a friendly Dragon, deal 3 damage to the strongest enemy unit.
		cardsMap.put("Witches of the Wild", new Unit("Witches of the Wild", 4, 1, player, 2, "Common", EffectDescription.None, "Raven", "Tribes of Shadowfen")); //On play, drain 1 strength from bordering units.

		// Swarm of the East
		
		cardsMap.put("Broken Truce", new Spell("Broken Truce", 4, player, "Rare", EffectDescription.None, "Swarm of the East")); //Destroy a target unit or structure. Deal 5 damage to your base.
		cardsMap.put("Counselor Ahmi", new Unit("Counselor Ahmi", 3, 1, player, 3, "Legendary", EffectDescription.None, "Satyr", "Swarm of the East")); //If played with no bordering units, return its card back to your hand.
		cardsMap.put("Dark Harvest", new Spell("Dark Harvest", 5, player, "Rare", EffectDescription.None, "Swarm of the East")); //Deal 2 damage to each enemy bordering a friendly unit.
		cardsMap.put("Devastators", new Unit("Devastators", 4, 1, player, 3, "Epic", EffectDescription.None, "Undead", "Swarm of the East")); //When attacking the enemy base, deal 2 additional damage to it.
		cardsMap.put("Doppelbocks", new Unit("Doppelbocks", 3, 0, player, 2, "Common", EffectDescription.None, "Satyr", "Swarm of the East")); //On play, spawn a 1 strength Satyr on the tile in front.
		cardsMap.put("Draconic Roamers", new Unit("Draconic Roamers", 4, 2, player, 2, "Rare", EffectDescription.None, "Dragon", "Swarm of the East")); //When attacking a unit, spawn a 2 strength Dragon on a random tile bordering it.
		cardsMap.put("Dreadfauns", new Unit("Dreadfauns", 5, 0, player, 2, "Common", EffectDescription.None, "Satyr", "Swarm of the East")); //On play, spawn 2 Satyrs with 2 strength on random bordering tiles.
		cardsMap.put("Forgotten Souls", new Unit("Forgotten Souls", 3, 1, player, 1, "Rare", EffectDescription.None, "Undead", "Swarm of the East")); //On play, command a random bordering friendly unit forward.
		cardsMap.put("Grim Couriers", new Unit("Grim Couriers", 5, 3, player, 2, "Common", EffectDescription.None, "Undead", "Swarm of the East"));
		cardsMap.put("Herald's Hymn", new Spell("Herald's Hymn", 6, player, "Common", EffectDescription.None, "Swarm of the East")); //Give 1 strength to a friendly target and command all friendly units in its row forward.
		cardsMap.put("Mischiefs", new Unit("Mischiefs", 4, 1, player, 3, "Common", EffectDescription.None, "Undead", "Swarm of the East")); //On play, deal 1 damage to the enemy base.
		cardsMap.put("Moonlit Aerie", new Structure("Moonlit Aerie", 3, player, 3, EffectDescription.None, "", "Swarm of the East")); //At the start of your turn, give 1 strength to all friendly Satyrs.
		cardsMap.put("Pan Heralds", new Unit("Pan Heralds", 4, 0, player, 1, "Common", EffectDescription.None, "Satyr", "Swarm of the East")); //On play, give 1 strength to all surrounding friendly units.
		cardsMap.put("Pillars of Doom", new Structure("Pillars of Doom", 5, player, 4, EffectDescription.None, "", "Swarm of the East")); //At the start of your turn, deal 2 damage to the enemy base.
		cardsMap.put("Queen of Herds", new Unit("Queen of Herds", 6, 0, player, 5, "Legendary", EffectDescription.None, "Undead", "Swarm of the East")); //On play, randomly plays 1 Satyr from your deck to the board.
		cardsMap.put("Restless Goats", new Unit("Restless Goats", 1, 2, player, 1, "Epic", EffectDescription.None, "Satyr", "Swarm of the East")); //On death, deal 1 damage to your base.
		cardsMap.put("Shady Ghoul", new Unit("Shady Ghoul", 2, 2, player, 1, "Rare", EffectDescription.None, "Undead", "Swarm of the East")); //On death, spawn a 1 strength Satyr on a random bordering tile.
		cardsMap.put("Swarmcallers", new Unit("Swarmcallers", 3, 1, player, 1, "Rare", EffectDescription.None, "Satyr", "Swarm of the East")); //On play, gain 2 strength for each bordering friendly Satyr.
		cardsMap.put("Vindicators", new Unit("Vindicators", 6, 3, player, 3, "Epic", EffectDescription.None, "Undead", "Swarm of the East")); //Before attacking, deal 1 damage to the enemy base.
		cardsMap.put("Xuri, Lord of Life", new Unit("Xuri, Lord of Life", 5, 0, player, 5, "Legendary", EffectDescription.None, "Dragon", "Swarm of the East")); //On play, fly to the first empty tile in front. Give 2 strength to all friendly units passed.

		// Winter Pact
		
		cardsMap.put("Broken Earth Drakes", new Unit("Broken Earth Drakes", 6, 1, player, 3, "Rare", EffectDescription.None, "Dragon", "Winter Pact")); //If not bordering any base when it dies, deal 3 damage to all non-Dragon units.
		cardsMap.put("Calming Spirits", new Unit("Calming Spirits", 8, 0, player, 10, "Common", EffectDescription.None, "Frostling", "Winter Pact"));
		cardsMap.put("Chillbeards", new Unit("Chillbeards", 10, 2, player, 3, "Common", EffectDescription.None, "Dwarf", "Winter Pact")); //If played with an enemy on the tile in front, gain 6 strength.
		cardsMap.put("Dawnsparks", new Unit("Dawnsparks", 6, 0, player, 7, "Epic", EffectDescription.None, "Frostling", "Winter Pact")); //When this unit attacks, gain 3 mana.
		cardsMap.put("Fleshmenders", new Unit("Fleshmenders", 8, 2, player, 4, "Epic", EffectDescription.None, "Dwarf", "Winter Pact")); //On play, give 6 strength to a random bordering friendly unit or structure.
		cardsMap.put("Frosthexers", new Unit("Frosthexers", 2, 0, player, 1, "Common", EffectDescription.None, "Frostling", "Winter Pact")); //On play, freeze a random bordering enemy unit.
		cardsMap.put("Frozen Core", new Structure("Frozen Core", 5, player, 4, EffectDescription.None, "", "Winter Pact")); //At the start of your turn, gain 2 mana.
		cardsMap.put("Gift of the Wise", new Spell("Gift of the Wise", 7, player, "Epic", EffectDescription.None, "Winter Pact")); //Gain 9 mana.
		cardsMap.put("Icicle Burst", new Spell("Icicle Burst", 1, player, "Common", EffectDescription.None, "Winter Pact")); //Destroy a target frozen enemy with 7 or less strength.
		cardsMap.put("Lady Rime", new Unit("Lady Rime", 5, 0, player, 5, "Legendary", EffectDescription.None, "Frostling", "Winter Pact")); //On play, spend your remaining mana and gain 2 strength for each.
		cardsMap.put("Midwinter Chaos", new Spell("Midwinter Chaos", 3, player, "Rare", EffectDescription.None, "Winter Pact")); //Deal 4 damage to all frozen enemies, freeze the others.
		cardsMap.put("Moment's Peace", new Spell("Moment's Peace", 6, player, "Rare", EffectDescription.None, "Winter Pact")); //Give 6 strength to a target friendly unit or structure and freeze all surrounding enemies.
		cardsMap.put("Mystwives", new Unit("Mystwives", 4, 1, player, 3, "Epic", EffectDescription.None, "Dwarf", "Winter Pact")); //If played bordering a stronger friendly unit, gain 3 strength.
		cardsMap.put("Olf the Hammer", new Unit("Olf the Hammer", 9, 2, player, 6, "Legendary", EffectDescription.None, "Dwarf", "Winter Pact")); //If played bordering your base, give the base 3 strength.
		cardsMap.put("Rimelings", new Unit("Rimelings", 5, 1, player, 3, "Common", EffectDescription.None, "Frostling", "Winter Pact")); //On play, gain 3 mana.
		cardsMap.put("Rockworkers", new Unit("Rockworkers", 5, 1, player, 4, "Common", EffectDescription.None, "Dwarf", "Winter Pact")); //On play, spawn a 4 strength Fort on a random bordering tile.
		cardsMap.put("Spellbinder Zhevana", new Unit("Spellbinder Zhevana", 4, 0, player, 4, "Legendary", EffectDescription.None, "Dragon", "Winter Pact")); //On play, destroy all frozen enemies in front with 4 or less strength. Gain 4 mana for each unit destroyed.
		cardsMap.put("The Hearth", new Structure("The Hearth", 4, player, 3, EffectDescription.None, "", "Winter Pact")); //At the start of your turn, give 2 strength to another friendly unit or structure.
		cardsMap.put("Wisp Cloud", new Unit("Wisp Cloud", 4, 1, player, 3, "Rare", EffectDescription.None, "Frostling", "Winter Pact")); //On play, deal 4 damage to all surrounding frozen enemy units.
		cardsMap.put("Wolfcloaks", new Unit("Wolfcloaks", 7, 1, player, 10, "Rare", EffectDescription.None, "Dwarf", "Winter Pact")); //On play, lose 1 strength for every other friendly unit on the board.

		// Ironclad Union
		
		cardsMap.put("Armed Schemers", new Unit("Armed Schemers", 7, 0, player, 7, "Epic", EffectDescription.None, "Rodent", "Ironclad Union")); //On play, pull enemy units to you, then deal 3 damage to all bordering enemy units.
		cardsMap.put("Boosting Elixir", new Spell("Boosting Elixir", 4, player, "Rare", EffectDescription.None, "Ironclad Union")); //Give 3 strength to a target friendly unit and a random friendly unit bordering it.
		cardsMap.put("Chaotic Pupil", new Unit("Chaotic Pupil", 4, 2, player, 1, "Common", EffectDescription.None, "Rodent", "Ironclad Union")); //On death, deal 2 damage spread randomly among all surrounding enemies.
		cardsMap.put("Debug Loggers", new Unit("Debug Loggers", 5, 1, player, 5, "Epic", EffectDescription.None, "Construct", "Ironclad Union")); //After attacking, gain 2 strength.
		cardsMap.put("Delegators", new Unit("Delegators", 6, 1, player, 7, "Common", EffectDescription.None, "Construct", "Ironclad Union"));
		cardsMap.put("Destructobots", new Unit("Destructobots", 2, 1, player, 3, "Rare", EffectDescription.None, "Construct", "Ironclad Union")); //On play, deal 1 damage to another friendly unit or structure.
		cardsMap.put("Doctor Mia", new Unit("Doctor Mia", 2, 0, player, 2, "Legendary", EffectDescription.None, "Rodent", "Ironclad Union")); //On play, trigger the ability of all bordering friendly structures.
		cardsMap.put("Eloth the Ignited", new Unit("Eloth the Ignited", 5, 0, player, 5, "Legendary", EffectDescription.None, "Dragon", "Ironclad Union")); //On play, fly to the first enemy unit in front. Deal 3 damage to this enemy and push it away.
		cardsMap.put("Finite Loopers", new Unit("Finite Loopers", 3, 1, player, 2, "Common", EffectDescription.None, "Construct", "Ironclad Union")); //On death, spawn a 2 strength Construct on a random bordering tile.
		cardsMap.put("Flaming Stream", new Spell("Flaming Stream", 5, player, "Common", EffectDescription.None, "Ironclad Union")); //Deal 1-3 damage to all enemy units in a target unit's column and push them back.
		cardsMap.put("Fortification Tonic", new Spell("Fortification Tonic", 3, player, "Rare", EffectDescription.None, "Ironclad Union")); //Give 2 strength to each friendly unit bordering a friendly structure.
		cardsMap.put("Greengale Serpents", new Unit("Greengale Serpents", 3, 2, player, 1, "Rare", EffectDescription.None, "Dragon", "Ironclad Union")); //Before attacking a unit, gives itself and a random friendly Dragon 1 strength.
		cardsMap.put("Linked Golems", new Unit("Linked Golems", 4, 1, player, 2, "Epic", EffectDescription.None, "Construct", "Ironclad Union")); //If played bordering a friendly Construct, both gain 2 strength.
		cardsMap.put("Mech Workshop", new Structure("Mech Workshop", 4, player, 3, EffectDescription.None, "", "Ironclad Union")); //At the start of your turn, spawn a 2 strength Construct on the tile in front.
		cardsMap.put("Overchargers", new Unit("Overchargers", 4, 1, player, 3, "Common", EffectDescription.None, "Rodent", "Ironclad Union")); //On play, deal 1 damage to the first enemy in front.
		cardsMap.put("Ozone Purifiers", new Unit("Ozone Purifiers", 2, 0, player, 1, "Common", EffectDescription.None, "Rodent", "Ironclad Union")); //On play, push a random bordering enemy unit away.
		cardsMap.put("Project PH03-NIX", new Unit("Project PH03-NIX", 6, 1, player, 4, "Legendary", EffectDescription.None, "Construct", "Ironclad Union")); //If not bordering any base when it dies, randomly respawn at your base with 4 strength.
		cardsMap.put("Siege Assembly", new Structure("Siege Assembly", 5, player, 3, EffectDescription.None, "", "Ironclad Union")); //At the start of your turn, deal 3 damage to the first enemy in front.
		cardsMap.put("Upgrade Point", new Structure("Upgrade Point", 3, player, 3, EffectDescription.None, "", "Ironclad Union")); //At the start of your turn, give 2 strength to all surrounding friendly Constructs.
		cardsMap.put("Windmakers", new Unit("Windmakers", 6, 2, player, 3, "Rare", EffectDescription.None, "Rodent", "Ironclad Union")); //Before attacking a stronger unit, deal 3 damage to it and push it away.

		
		// DUT Info
		
		cardsMap.put("Wallim", new Unit("Wallim", 10, 1, player, 7, "Legendary", EffectDescription.Wallim, "God", Constants.EF_ON_BEGIN_TURN, "DUT Info")); //At the start of your turn, deal 5 damage to all enemy units.
		cardsMap.put("Leo", new Unit("Leo", 10, 1, player, 7, "Legendary", EffectDescription.Leo, "God", Constants.EF_ON_BEGIN_TURN, "DUT Info")); //At the start of your turn, add 5 to power of all your units.
		cardsMap.put("Carine", new Unit("Carine", 10, 1, player, 7, "Legendary", EffectDescription.Carine, "God", Constants.EF_ON_BEGIN_TURN, "DUT Info"));  //On play put your HP to 20.
		
		
		return cardsMap;
	}
	
	public static Map<String, Consumer<EffectParameters>> createEffects() {
		Map<String, Consumer<EffectParameters>> effectsMap = new HashMap<String, Consumer<EffectParameters>>();
		
		effectsMap.put("Bladestorm", params -> Effects.bladestorm(params));
		effectsMap.put("Execution", params -> Effects.execution(params));
		effectsMap.put("Emerald Towers", params -> Effects.emerald_towers(params));
		effectsMap.put("Felflares", params -> Effects.felflares(params));
		effectsMap.put("Joust Champions", params -> Effects.joust_champions(params));
		effectsMap.put("Personal Servers", params -> Effects.personal_servers(params));
		effectsMap.put("Siegebreakers", params -> Effects.siegebreakers(params));
		
		effectsMap.put("Call for Aid", params -> Effects.call_for_aid(params));
		effectsMap.put("Confinement", params -> Effects.confinement(params));
		effectsMap.put("Flooding the Gates", params -> Effects.flooding_the_gates(params));
		effectsMap.put("Kindred's Grace", params -> Effects.kindreds_grace(params));
		effectsMap.put("Needle Blast", params -> Effects.needle_blast(params));
		effectsMap.put("Potion of Growth", params -> Effects.potion_of_growth(params));
		effectsMap.put("Summon Militia", params -> Effects.summon_militia(params));
		
		effectsMap.put("Wallim", params -> Effects.wallim(params));
		effectsMap.put("Leo", params -> Effects.leo(params));
		effectsMap.put("Carine", params -> Effects.carine(params));
		
		return effectsMap;
	}
	
	// creer toute les images pour le jeu 
	public static Map<String, Image> createImage(){
		 Map<String, Image> imagesMap = new HashMap<>();
		 imagesMap.put("Heart",  new ImageIcon("src/pictures/heart.png").getImage());
		 imagesMap.put("Mana",  new ImageIcon("src/pictures/mana.png").getImage());
		 imagesMap.put("Shoes",  new ImageIcon("src/pictures/shoes.png").getImage());
		 imagesMap.put("Cursor", new ImageIcon("src/pictures/Cursor.png").getImage());
		 imagesMap.put("Background", new ImageIcon("src/pictures/background.png").getImage());
		 imagesMap.put("Rune", new ImageIcon("src/pictures/rune.jpg").getImage());
		 imagesMap.put("Border", new ImageIcon("src/pictures/border.png").getImage());
		 imagesMap.put("Back", new ImageIcon("src/pictures/back.png").getImage());
		 imagesMap.put("Next", new ImageIcon("src/pictures/next.png").getImage());
		 imagesMap.put("Close", new ImageIcon("src/pictures/close.png").getImage());
		 imagesMap.put("Change", new ImageIcon("src/pictures/changecard.png").getImage());
		 imagesMap.put("End", new ImageIcon("src/pictures/endturn.png").getImage());
		 imagesMap.put("DeckJ1", new ImageIcon("src/pictures/DeckJ1.png").getImage());
		 imagesMap.put("DeckJ2", new ImageIcon("src/pictures/DeckJ2.png").getImage());
		 imagesMap.put("EditButton", new ImageIcon("src/pictures/EditButton.png").getImage());
		 imagesMap.put("Transvaser", new ImageIcon("src/pictures/transvaser.png").getImage());
		 //card img
		 imagesMap.put("Counselor Ahmi", new ImageIcon("src/pictures/Counselor Ahmi.png").getImage());
		 imagesMap.put("First Mutineer", new ImageIcon("src/pictures/First Mutineer.png").getImage());
		 imagesMap.put("Ozone Purifiers", new ImageIcon("src/pictures/Ozone Purifiers.png").getImage());
		 imagesMap.put("Bladestorm", new ImageIcon("src/pictures/Bladestorm.png").getImage());
		 imagesMap.put("Call for Aid", new ImageIcon("src/pictures/Call for Aid.png").getImage());
		 imagesMap.put("Broken Truce", new ImageIcon("src/pictures/Broken Truce.png").getImage());
		 imagesMap.put("Draconic Roamers", new ImageIcon("src/pictures/Draconic Roamers.png").getImage());
		 imagesMap.put("Rockworkers", new ImageIcon("src/pictures/Rockworkers.png").getImage());
		 imagesMap.put("Temple Guardians", new ImageIcon("src/pictures/Temple Guardians.png").getImage());
		 imagesMap.put("Destructobots", new ImageIcon("src/pictures/Destructobots.png").getImage());
		 imagesMap.put("Siren of the Seas", new ImageIcon("src/pictures/Siren of the Seas.png").getImage());
		 imagesMap.put("Flaming Stream", new ImageIcon("src/pictures/Flaming Stream.png").getImage());
		 imagesMap.put("Devastators", new ImageIcon("src/pictures/Devastators.png").getImage());
		 imagesMap.put("Heroic Soldiers", new ImageIcon("src/pictures/Heroic Soldiers.png").getImage());
		 imagesMap.put("Copperskin Ranger", new ImageIcon("src/pictures/Copperskin Ranger.png").getImage());
		 imagesMap.put("Lucky Charmers", new ImageIcon("src/pictures/Lucky Charmers.png").getImage());
		 imagesMap.put("Siege Assembly", new ImageIcon("src/pictures/Siege Assembly.png").getImage());
		 imagesMap.put("Mech Workshop", new ImageIcon("src/pictures/Mech Workshop.png").getImage());
		 imagesMap.put("Ludic Matriarchs", new ImageIcon("src/pictures/Ludic Matriarchs.png").getImage());
		 imagesMap.put("Potion of Growth", new ImageIcon("src/pictures/Potion of Growth.png").getImage());
		 imagesMap.put("Tegor the Vengeful", new ImageIcon("src/pictures/Tegor the Vengeful.png").getImage());
		 imagesMap.put("Soulcrushers", new ImageIcon("src/pictures/Soulcrushers.png").getImage());
		 imagesMap.put("Wandering Wyrms", new ImageIcon("src/pictures/Wandering Wyrms.png").getImage());
		 imagesMap.put("Vindicators", new ImageIcon("src/pictures/Vindicators.png").getImage());
		 imagesMap.put("Lich Summoners", new ImageIcon("src/pictures/Lich Summoners.png").getImage());
		 imagesMap.put("Veterans of War", new ImageIcon("src/pictures/Veterans of War.png").getImage());
		 imagesMap.put("Dangerous Suitors", new ImageIcon("src/pictures/Dangerous Suitors.png").getImage());
		 imagesMap.put("Restless Goats", new ImageIcon("src/pictures/Restless Goats.png").getImage());
		 imagesMap.put("Warfront Runners", new ImageIcon("src/pictures/Warfront Runners.png").getImage());
		 imagesMap.put("Brood Sage", new ImageIcon("src/pictures/Brood Sage.png").getImage());
		 imagesMap.put("Swarmcallers", new ImageIcon("src/pictures/Swarmcallers.png").getImage());
		 imagesMap.put("Lawless Herd", new ImageIcon("src/pictures/Lawless Herd.png").getImage());
		 imagesMap.put("Linked Golems", new ImageIcon("src/pictures/Linked Golems.png").getImage());
		 imagesMap.put("Gifted Recruits", new ImageIcon("src/pictures/Gifted Recruits.png").getImage());
		 imagesMap.put("Flooding the Gates", new ImageIcon("src/pictures/Flooding the Gates.png").getImage());
		 imagesMap.put("Sharpfist Exiles", new ImageIcon("src/pictures/Sharpfist Exiles.png").getImage());
		 imagesMap.put("Calming Spirits", new ImageIcon("src/pictures/Calming Spirits.png").getImage());
		 imagesMap.put("Beasts of Terror", new ImageIcon("src/pictures/Beasts of Terror.png").getImage());
		 imagesMap.put("Toxic Sacrifice", new ImageIcon("src/pictures/Toxic Sacrifice.png").getImage());
		 imagesMap.put("Lady Rime", new ImageIcon("src/pictures/Lady Rime.png").getImage());
		 imagesMap.put("Broodmother Qordia", new ImageIcon("src/pictures/Broodmother Qordia.png").getImage());
		 imagesMap.put("Summon Militia", new ImageIcon("src/pictures/Summon Militia.png").getImage());
		 imagesMap.put("Brothers in Arms", new ImageIcon("src/pictures/Brothers in Arms.png").getImage());
		 imagesMap.put("Spare Dragonlings", new ImageIcon("src/pictures/Spare Dragonlings.png").getImage());
		 imagesMap.put("Gift of the Wise", new ImageIcon("src/pictures/Gift of the Wise.png").getImage());
		 imagesMap.put("Armed Schemers", new ImageIcon("src/pictures/Armed Schemers.png").getImage());
		 imagesMap.put("Fortification Tonic", new ImageIcon("src/pictures/Fortification Tonic.png").getImage());
		 imagesMap.put("Boosting Elixir", new ImageIcon("src/pictures/Boosting Elixir.png").getImage());
		 imagesMap.put("Grim Couriers", new ImageIcon("src/pictures/Grim Couriers.png").getImage());
		 imagesMap.put("Harpies of the Hunt", new ImageIcon("src/pictures/Harpies of the Hunt.png").getImage());
		 imagesMap.put("Westwind Sailors", new ImageIcon("src/pictures/Westwind Sailors.png").getImage());
		 imagesMap.put("Siegebreakers", new ImageIcon("src/pictures/Siegebreakers.png").getImage());
		 imagesMap.put("Needle Blast", new ImageIcon("src/pictures/Needle Blast.png").getImage());
		 imagesMap.put("Snowmasons", new ImageIcon("src/pictures/Snowmasons.png").getImage());
		 imagesMap.put("Archdruid Earyn", new ImageIcon("src/pictures/Archdruid Earyn.png").getImage());
		 imagesMap.put("High Priestess Klaxi", new ImageIcon("src/pictures/High Priestess Klaxi.png").getImage());
		 imagesMap.put("Pillars of Doom", new ImageIcon("src/pictures/Pillars of Doom.png").getImage());
		 imagesMap.put("Hearthguards", new ImageIcon("src/pictures/Hearthguards.png").getImage());
		 imagesMap.put("Shady Ghoul", new ImageIcon("src/pictures/Shady Ghoul.png").getImage());
		 imagesMap.put("Goldgrubbers", new ImageIcon("src/pictures/Goldgrubbers.png").getImage());
		 imagesMap.put("Conflicted Drakes", new ImageIcon("src/pictures/Conflicted Drakes.png").getImage());
		 imagesMap.put("Voidsurgers", new ImageIcon("src/pictures/Voidsurgers.png").getImage());
		 imagesMap.put("Tode the Elevated", new ImageIcon("src/pictures/Tode the Elevated.png").getImage());
		 imagesMap.put("Queen of Herds", new ImageIcon("src/pictures/Queen of Herds.png").getImage());
		 imagesMap.put("Greengale Serpents", new ImageIcon("src/pictures/Greengale Serpents.png").getImage());
		 imagesMap.put("Eloth the Ignited", new ImageIcon("src/pictures/Eloth the Ignited.png").getImage());
		 imagesMap.put("Forgotten Souls", new ImageIcon("src/pictures/Forgotten Souls.png").getImage());
		 imagesMap.put("Project PH03-NIX", new ImageIcon("src/pictures/Project PH03-NIX.png").getImage());
		 imagesMap.put("Doctor Mia", new ImageIcon("src/pictures/Doctor Mia.png").getImage());
		 imagesMap.put("Dubious Hags", new ImageIcon("src/pictures/Dubious Hags.png").getImage());
		 imagesMap.put("Execution", new ImageIcon("src/pictures/Execution.png").getImage());
		 imagesMap.put("Moonlit Aerie", new ImageIcon("src/pictures/Moonlit Aerie.png").getImage());
		 imagesMap.put("Curse of Strings", new ImageIcon("src/pictures/Curse of Strings.png").getImage());
		 imagesMap.put("Delegators", new ImageIcon("src/pictures/Delegators.png").getImage());
		 imagesMap.put("Windmakers", new ImageIcon("src/pictures/Windmakers.png").getImage());
		 imagesMap.put("Debug Loggers", new ImageIcon("src/pictures/Debug Loggers.png").getImage());
		 imagesMap.put("Blood Ministers", new ImageIcon("src/pictures/Blood Ministers.png").getImage());
		 imagesMap.put("Icicle Burst", new ImageIcon("src/pictures/Icicle Burst.png").getImage());
		 imagesMap.put("Moment's Peace", new ImageIcon("src/pictures/Moment's Peace.png").getImage());
		 imagesMap.put("Chillbeards", new ImageIcon("src/pictures/Chillbeards.png").getImage());
		 imagesMap.put("Midwinter Chaos", new ImageIcon("src/pictures/Midwinter Chaos.png").getImage());
		 imagesMap.put("Green Prototypes", new ImageIcon("src/pictures/Green Prototypes.png").getImage());
		 imagesMap.put("Overchargers", new ImageIcon("src/pictures/Overchargers.png").getImage());
		 imagesMap.put("Rimelings", new ImageIcon("src/pictures/Rimelings.png").getImage());
		 imagesMap.put("Mystwives", new ImageIcon("src/pictures/Mystwives.png").getImage());
		 imagesMap.put("Herald's Hymn", new ImageIcon("src/pictures/Herald's Hymn.png").getImage());
		 imagesMap.put("Rain of Frogs", new ImageIcon("src/pictures/Rain of Frogs.png").getImage());
		 imagesMap.put("Joust Champions", new ImageIcon("src/pictures/Joust Champions.png").getImage());
		 imagesMap.put("Venomfall Spire", new ImageIcon("src/pictures/Venomfall Spire.png").getImage());
		 imagesMap.put("Pan Heralds", new ImageIcon("src/pictures/Pan Heralds.png").getImage());
		 imagesMap.put("William", new ImageIcon("src/pictures/William.png").getImage());
		 imagesMap.put("Frosthexers", new ImageIcon("src/pictures/Frosthexers.png").getImage());
		 imagesMap.put("Upgrade Point", new ImageIcon("src/pictures/Upgrade Point.png").getImage());
		 imagesMap.put("Azure Hatchers", new ImageIcon("src/pictures/Azure Hatchers.png").getImage());
		 imagesMap.put("Dark Harvest", new ImageIcon("src/pictures/Dark Harvest.png").getImage());
		 imagesMap.put("The Hearth", new ImageIcon("src/pictures/The Hearth.png").getImage());
		 imagesMap.put("Avian Stalkers", new ImageIcon("src/pictures/Avian Stalkers.png").getImage());
		 imagesMap.put("Trueshot Post", new ImageIcon("src/pictures/Trueshot Post.png").getImage());
		 imagesMap.put("Crimson Sentry", new ImageIcon("src/pictures/Crimson Sentry.png").getImage());
		 imagesMap.put("Frozen Core", new ImageIcon("src/pictures/Frozen Core.png").getImage());
		 imagesMap.put("Dreadfauns", new ImageIcon("src/pictures/Dreadfauns.png").getImage());
		 imagesMap.put("Chaotic Pupil", new ImageIcon("src/pictures/Chaotic Pupil.png").getImage());
		 imagesMap.put("Spellbinder Zhevana", new ImageIcon("src/pictures/Spellbinder Zhevana.png").getImage());
		 imagesMap.put("Boomstick Officers", new ImageIcon("src/pictures/Boomstick Officers.png").getImage());
		 imagesMap.put("Confinement", new ImageIcon("src/pictures/Confinement.png").getImage());
		 imagesMap.put("Xuri, Lord of Life", new ImageIcon("src/pictures/Xuri, Lord of Life.png").getImage());
		 imagesMap.put("Olf the Hammer", new ImageIcon("src/pictures/Olf the Hammer.png").getImage());
		 imagesMap.put("Broken Earth Drakes", new ImageIcon("src/pictures/Broken Earth Drakes.png").getImage());
		 imagesMap.put("Bluesail Raiders", new ImageIcon("src/pictures/Bluesail Raiders.png").getImage());
		 imagesMap.put("Doppelbocks", new ImageIcon("src/pictures/Doppelbocks.png").getImage());
		 imagesMap.put("Crazy Bombers", new ImageIcon("src/pictures/Crazy Bombers.png").getImage());
		 imagesMap.put("Amberhides", new ImageIcon("src/pictures/Amberhides.png").getImage());
		 imagesMap.put("Felflares", new ImageIcon("src/pictures/Felflares.png").getImage());
		 imagesMap.put("Salty Outcasts", new ImageIcon("src/pictures/Salty Outcasts.png").getImage());
		 imagesMap.put("Collector Mirz", new ImageIcon("src/pictures/Collector Mirz.png").getImage());
		 imagesMap.put("Finite Loopers", new ImageIcon("src/pictures/Finite Loopers.png").getImage());
		 imagesMap.put("Personal Servers", new ImageIcon("src/pictures/Personal Servers.png").getImage());
		 imagesMap.put("Victors of the Melee", new ImageIcon("src/pictures/Victors of the Melee.png").getImage());
		 imagesMap.put("Freebooters", new ImageIcon("src/pictures/Freebooters.png").getImage());
		 imagesMap.put("Fleshmenders", new ImageIcon("src/pictures/Fleshmenders.png").getImage());
		 imagesMap.put("Emerald Towers", new ImageIcon("src/pictures/Emerald Towers.png").getImage());
		 imagesMap.put("Northsea Dog", new ImageIcon("src/pictures/Northsea Dog.png").getImage());
		 imagesMap.put("Feral Shamans", new ImageIcon("src/pictures/Feral Shamans.png").getImage());
		 imagesMap.put("Wisp Cloud", new ImageIcon("src/pictures/Wisp Cloud.png").getImage());
		 imagesMap.put("Wolfcloaks", new ImageIcon("src/pictures/Wolfcloaks.png").getImage());
		 imagesMap.put("Fort of Ebonrock", new ImageIcon("src/pictures/Fort of Ebonrock.png").getImage());
		 imagesMap.put("Kindred's Grace", new ImageIcon("src/pictures/Kindred's Grace.png").getImage());
		 imagesMap.put("Ubass the Hunter", new ImageIcon("src/pictures/Ubass the Hunter.png").getImage());
		 imagesMap.put("Mischiefs", new ImageIcon("src/pictures/Mischiefs.png").getImage());
		 imagesMap.put("Witches of the Wild", new ImageIcon("src/pictures/Witches of the Wild.png").getImage());
		 imagesMap.put("Dawnsparks", new ImageIcon("src/pictures/Dawnsparks.png").getImage());
		 imagesMap.put("Leo", new ImageIcon("src/pictures/Leo.png").getImage());
		 imagesMap.put("Marked as Prey", new ImageIcon("src/pictures/Marked as Prey.png").getImage());
		 imagesMap.put("Cabin Girls", new ImageIcon("src/pictures/Cabin Girls.png").getImage());
		 imagesMap.put("Terrific Slayers", new ImageIcon("src/pictures/Terrific Slayers.png").getImage());
		 
		 return imagesMap;
	}
	
}

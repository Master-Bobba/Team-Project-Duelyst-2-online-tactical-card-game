package utils;

/**
 * This is a utility class that just has short-cuts to the location of various
 * config files. 
 * 
 * IMPORTANT: Note the start letter for unit types is u_... while the start letter
 * for card types is c_... 
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class StaticConfFiles {

	// Board Pieces
	public final static String tileConf = "conf/gameconfs/tile.json";
	public final static String gridConf = "conf/gameconfs/grid.json";
	
	// Avatars
	public final static String humanAvatar = "conf/gameconfs/avatars/avatar1.json";
	public final static String aiAvatar = "conf/gameconfs/avatars/avatar2.json";
	
	// Deck 1 Cards
	public final static String c_truestrike = "conf/gameconfs/cards/1_c_s_truestrike.json";
	public final static String c_sundrop_elixir = "conf/gameconfs/cards/1_c_s_sundrop_elixir.json";
	public final static String c_comodo_charger = "conf/gameconfs/cards/1_c_u_comodo_charger.json";
	public final static String c_azure_herald = "conf/gameconfs/cards/1_c_u_azure_herald.json";
	public final static String c_azurite_lion = "conf/gameconfs/cards/1_c_u_azurite_lion.json";
	public final static String c_fire_spitter = "conf/gameconfs/cards/1_c_u_fire_spitter.json";
	public final static String c_hailstone_golem = "conf/gameconfs/cards/1_c_u_hailstone_golem.json";
	public final static String c_ironcliff_guardian = "conf/gameconfs/cards/1_c_u_ironcliff_guardian.json";
	public final static String c_pureblade_enforcer = "conf/gameconfs/cards/1_c_u_pureblade_enforcer.json";
	public final static String c_silverguard_knight = "conf/gameconfs/cards/1_c_u_silverguard_knight.json";

	// Deck 1 Units
	public final static String u_comodo_charger = "conf/gameconfs/units/comodo_charger.json";
	public final static String u_azure_herald = "conf/gameconfs/units/azure_herald.json";
	public final static String u_azurite_lion = "conf/gameconfs/units/azurite_lion.json";
	public final static String u_fire_spitter = "conf/gameconfs/units/fire_spitter.json";
	public final static String u_hailstone_golem = "conf/gameconfs/units/hailstone_golem.json";
	public final static String u_ironcliff_guardian = "conf/gameconfs/units/ironcliff_guardian.json";
	public final static String u_pureblade_enforcer = "conf/gameconfs/units/pureblade_enforcer.json";
	public final static String u_silverguard_knight = "conf/gameconfs/units/silverguard_knight.json";
	
	// Deck 2 Cards
	public final static String c_staff_of_ykir = "conf/gameconfs/cards/2_c_s_staff_of_ykir.json";
	public final static String c_entropic_decay = "conf/gameconfs/cards/2_c_s_entropic_decay.json";
	public final static String c_blaze_hound = "conf/gameconfs/cards/2_c_u_blaze_hound.json";
	public final static String c_bloodshard_golem = "conf/gameconfs/cards/2_c_u_bloodshard_golem.json";
	public final static String c_planar_scout = "conf/gameconfs/cards/2_c_u_planar_scout.json";
	public final static String c_pyromancer = "conf/gameconfs/cards/2_c_u_pyromancer.json";
	public final static String c_rock_pulveriser = "conf/gameconfs/cards/2_c_u_rock_pulveriser.json";
	public final static String c_serpenti = "conf/gameconfs/cards/2_c_u_serpenti.json";
	public final static String c_windshrike = "conf/gameconfs/cards/2_c_u_windshrike.json";
	
	// Deck 2 Units
	public final static String u_blaze_hound = "conf/gameconfs/units/blaze_hound.json";
	public final static String u_bloodshard_golem = "conf/gameconfs/units/bloodshard_golem.json";
	public final static String u_hailstone_golemR = "conf/gameconfs/units/hailstone_golem2.json";
	public final static String u_planar_scout = "conf/gameconfs/units/planar_scout.json";
	public final static String u_pyromancer = "conf/gameconfs/units/pyromancer.json";
	public final static String u_rock_pulveriser = "conf/gameconfs/units/rock_pulveriser.json";
	public final static String u_serpenti = "conf/gameconfs/units/serpenti.json";
	public final static String u_windshrike = "conf/gameconfs/units/windshrike.json";
	
	// Effects
	public final static String f1_inmolation = "conf/gameconfs/effects/f1_inmolation.json";
	public final static String f1_buff = "conf/gameconfs/effects/f1_buff.json";
	public final static String f1_martyrdom = "conf/gameconfs/effects/f1_martyrdom.json";
	public final static String f1_projectiles = "conf/gameconfs/effects/f1_projectiles.json";
	public final static String f1_summon = "conf/gameconfs/effects/f1_summon.json";

	public static String getUnitConf(String name){
		switch(name){
			case "Comodo Charger":
				return u_comodo_charger;
			case "Azure Herald":
				return u_azure_herald;
			case "Azurite Lion":
				return u_azurite_lion;
			case "Fire Spitter":
				return u_fire_spitter;
			case "Hailstone Golem":
				return u_hailstone_golem;
			case "Ironcliff Guardian":
				return u_ironcliff_guardian;
			case "Pureblade Enforcer":
				return u_pureblade_enforcer;
			case "Silverguard Knight":
				return u_silverguard_knight;
			case "Blaze Hound":
				return u_blaze_hound;
			case "Bloodshard Golem":
				return u_bloodshard_golem;
			case "Planar Scout":
				return u_planar_scout;
			case "Pyromancer":
				return u_pyromancer;
			case "Rock Pulveriser":
				return u_rock_pulveriser;
			case "Serpenti":
				return u_serpenti;
			case "WindShrike":
				return u_windshrike;
		}
		return null;
	}

	public static String getCardConf(String name){		
		switch(name){
			case "Comodo Charger":
				return c_comodo_charger;
			case "Azure Herald":
				return c_azure_herald;
			case "Azurite Lion":
				return c_azurite_lion;
			case "Fire Spitter":
				return c_fire_spitter;
			case "Hailstone Golem":
				return c_hailstone_golem;
			case "Ironcliff Guardian":
				return c_ironcliff_guardian;
			case "Pureblade Enforcer":
				return c_pureblade_enforcer;
			case "Silverguard Knight":
				return c_silverguard_knight;
			case "Blaze Hound":
				return c_blaze_hound;
			case "Bloodshard Golem":
				return c_bloodshard_golem;
			case "Planar Scout":
				return c_planar_scout;
			case "Pyromancer":
				return c_pyromancer;
			case "Rock Pulveriser":
				return c_rock_pulveriser;
			case "Serpenti":
				return c_serpenti;
			case "WindShrike":
				return c_windshrike;
		}
		return null;
	}
}

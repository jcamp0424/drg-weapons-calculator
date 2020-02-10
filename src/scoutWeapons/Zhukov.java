package scoutWeapons;

import java.util.Arrays;
import java.util.List;

import drillerWeapons.Subata;
import modelPieces.EnemyInformation;
import modelPieces.Mod;
import modelPieces.Overclock;
import modelPieces.StatsRow;
import modelPieces.Weapon;

public class Zhukov extends Weapon {
	
	/****************************************************************************************
	* Class Variables
	****************************************************************************************/
	
	private int directDamage;
	private int carriedAmmo;
	private int magazineSize;
	private double rateOfFire;
	private double reloadTime;
	private double baseSpread;
	private int maxPenetrations;
	private double weakpointBonus;
	private double movespeedWhileFiring;
	
	/****************************************************************************************
	* Constructors
	****************************************************************************************/
	
	// Shortcut constructor to get baseline data
	public Zhukov() {
		this(-1, -1, -1, -1, -1, -1);
	}
	
	// Shortcut constructor to quickly get statistics about a specific build
	public Zhukov(String combination) {
		this(-1, -1, -1, -1, -1, -1);
		buildFromCombination(combination);
	}
	
	public Zhukov(int mod1, int mod2, int mod3, int mod4, int mod5, int overclock) {
		fullName = "Zhukov Nuk17";
		
		// Base stats, before mods or overclocks alter them:
		directDamage = 11;
		carriedAmmo = 600;
		magazineSize = 50;  // Really 25
		rateOfFire = 30.0;  // Really 15
		reloadTime = 1.8;
		baseSpread = 1.0;
		maxPenetrations = 0;
		weakpointBonus = 0.0;
		movespeedWhileFiring = 1.0;
		
		initializeModsAndOverclocks();
		// Grab initial values before customizing mods and overclocks
		setBaselineStats();
		
		// Selected Mods
		selectedTier1 = mod1;
		selectedTier2 = mod2;
		selectedTier3 = mod3;
		selectedTier4 = mod4;
		selectedTier5 = mod5;
		
		// Overclock slot
		selectedOverclock = overclock;
	}
	
	@Override
	protected void initializeModsAndOverclocks() {
		tier1 = new Mod[2];
		tier1[0] = new Mod("Expanded Ammo Bags", "You had to give up some sandwich-storage, but your total ammo capacity is increased", 1, 0);
		tier1[1] = new Mod("High Velocity Rounds", "The good folk in R&D have been busy. The overall damage of your weapon is increased.", 1, 1);
		
		tier2 = new Mod[3];
		tier2[0] = new Mod("High Capacity Magazine", "The good thing about clips, magazines, ammo drums, fuel tanks...you can always get bigger variants.", 2, 0);
		tier2[1] = new Mod("Supercharged Feed Mechanism", "We overclocked your gun. It fires faster. Don't ask. Just enjoy. Also probably don't tell Management, please.", 2, 1);
		tier2[2] = new Mod("Quickfire Ejector", "Experience, training, and a couple of under-the-table design \"adjustments\" means your gun can be reloaded significantly faster.", 2, 2);
		
		tier3 = new Mod[2];
		tier3[0] = new Mod("Increased Caliber Rounds", "The good folk in R&D have been busy. The overall damage of your weapon is increased.", 3, 0);
		tier3[1] = new Mod("Better Weight Balance", "Base accuracy increase", 3, 1);
		
		tier4 = new Mod[3];
		tier4[0] = new Mod("Blowthrough Rounds", "Shaped projectiles designed to over-penetrate targets with a minimal loss of energy. In other words: Fire straight through an enemy!", 4, 0);
		tier4[1] = new Mod("Hollow-Point Bullets", "Hit em' where it hurts! Literally! We've upped the damage you'll be able to do to any creature's fleshy bits. You're welcome.", 4, 1);
		tier4[2] = new Mod("Expanded Ammo Bags", "You had to give up on some sandwich-storage, but your total ammo capacity is increased!", 4, 2);
		
		tier5 = new Mod[2];
		tier5[0] = new Mod("Conductive Bullets", "More damage to targets that are in an electric field", 5, 0, false);
		tier5[1] = new Mod("Get In, Get Out", "Temporary movement speed bonus after emptying clip", 5, 1, false);
		
		overclocks = new Overclock[5];
		overclocks[0] = new Overclock(Overclock.classification.clean, "Minimal Magazines", "By filling away unnecessary material from the magazines you've made them lighter, and that means they pop out faster when reloading. Also the rounds can move more freely increasing the max rate of fire slightly.", 0);
		overclocks[1] = new Overclock(Overclock.classification.balanced, "Custom Casings", "Fit more of these custom rounds in each magazine but at small loss in raw damage.", 1);
		overclocks[2] = new Overclock(Overclock.classification.unstable, "Cryo Minelets", "After impacting terrain, these high-tech bullets convert into cryo-minelets that will super-cool anything that comes close. However they don't last forever and the rounds themselves take more space in the clip and deal less direct damage.", 2, false);
		overclocks[3] = new Overclock(Overclock.classification.unstable, "Embedded Detonators", "Special bullets contain micro-explosives that detonate when you reload the weapon at the cost of total ammo and direct damage.", 3, false);
		overclocks[4] = new Overclock(Overclock.classification.unstable, "Gas Recycling", "Special hardened bullets combined with rerouting escaping gasses back into the chamber greatly increases the raw damage of the weapon but makes it more difficult to control and removes any bonus to weakpoint hits.", 4);
	}
	
	@Override
	public void buildFromCombination(String combination) {
		boolean combinationIsValid = true;
		char[] symbols = combination.toCharArray();
		if (combination.length() != 6) {
			System.out.println(combination + " does not have 6 characters, which makes it invalid");
			combinationIsValid = false;
		}
		else {
			List<Character> validModSymbols = Arrays.asList(new Character[] {'A', 'B', 'C', '-'});
			for (int i = 0; i < 5; i ++) {
				if (!validModSymbols.contains(symbols[i])) {
					System.out.println("Symbol #" + (i+1) + ", " + symbols[i] + ", is not a capital letter between A-C or a hyphen");
					combinationIsValid = false;
				}
			}
			if (symbols[0] == 'C') {
				System.out.println("Zhukov's first tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			if (symbols[2] == 'C') {
				System.out.println("Zhukov's third tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			if (symbols[4] == 'C') {
				System.out.println("Zhukov's fifth tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			List<Character> validOverclockSymbols = Arrays.asList(new Character[] {'1', '2', '3', '4', '5', '-'});
			if (!validOverclockSymbols.contains(symbols[5])) {
				System.out.println("The sixth symbol, " + symbols[5] + ", is not a number between 1-5 or a hyphen");
				combinationIsValid = false;
			}
		}
		
		if (combinationIsValid) {
			switch (symbols[0]) {
				case '-': {
					selectedTier1 = -1;
					break;
				}
				case 'A': {
					selectedTier1 = 0;
					break;
				}
				case 'B': {
					selectedTier1 = 1;
					break;
				}
			}
			
			switch (symbols[1]) {
				case '-': {
					selectedTier2 = -1;
					break;
				}
				case 'A': {
					selectedTier2 = 0;
					break;
				}
				case 'B': {
					selectedTier2 = 1;
					break;
				}
				case 'C': {
					selectedTier2 = 2;
					break;
				}
			}
			
			switch (symbols[2]) {
				case '-': {
					selectedTier3 = -1;
					break;
				}
				case 'A': {
					selectedTier3 = 0;
					break;
				}
				case 'B': {
					selectedTier3 = 1;
					break;
				}
			}
			
			switch (symbols[3]) {
				case '-': {
					selectedTier4 = -1;
					break;
				}
				case 'A': {
					selectedTier4 = 0;
					break;
				}
				case 'B': {
					selectedTier4 = 1;
					break;
				}
				case 'C': {
					selectedTier4 = 2;
					break;
				}
			}
			
			switch (symbols[4]) {
				case '-': {
					selectedTier5 = -1;
					break;
				}
				case 'A': {
					selectedTier5 = 0;
					break;
				}
				case 'B': {
					selectedTier5 = 1;
					break;
				}
			}
			
			switch (symbols[5]) {
				case '-': {
					selectedOverclock = -1;
					break;
				}
				case '1': {
					selectedOverclock = 0;
					break;
				}
				case '2': {
					selectedOverclock = 1;
					break;
				}
				case '3': {
					selectedOverclock = 2;
					break;
				}
				case '4': {
					selectedOverclock = 3;
					break;
				}
				case '5': {
					selectedOverclock = 4;
					break;
				}
			}
			
			if (countObservers() > 0) {
				setChanged();
				notifyObservers();
			}
		}
	}
	
	@Override
	public Zhukov clone() {
		return new Zhukov(selectedTier1, selectedTier2, selectedTier3, selectedTier4, selectedTier5, selectedOverclock);
	}
	
	/****************************************************************************************
	* Setters and Getters
	****************************************************************************************/
	
	private int getDirectDamage() {
		int toReturn = directDamage;
		
		if (selectedTier1 == 1) {
			toReturn += 1;
		}
		if (selectedTier3 == 0) {
			toReturn += 2;
		}
		
		if (selectedOverclock == 1 || selectedOverclock == 2) {
			toReturn -= 1;
		}
		else if (selectedOverclock == 3) {
			toReturn -= 3;
		}
		else if (selectedOverclock == 4) {
			toReturn += 5;
		}
		
		return toReturn;
	}
	private int getCarriedAmmo() {
		int toReturn = carriedAmmo;
		
		if (selectedTier1 == 0) {
			toReturn += 75;
		}
		if (selectedTier4 == 2) {
			toReturn += 150;
		}
		
		if (selectedOverclock == 3) {
			toReturn -= 75;
		}
		
		return toReturn;
	}
	private int getMagazineSize() {
		int toReturn = magazineSize;
		
		if (selectedTier2 == 0) {
			toReturn += 10;
		}
		
		if (selectedOverclock == 1) {
			toReturn += 30;
		}
		else if (selectedOverclock == 2) {
			toReturn -= 10;
		}
		
		return toReturn;
	}
	private double getRateOfFire() {
		double toReturn = rateOfFire;
		
		if (selectedTier2 == 1) {
			toReturn += 8.0;
		}
		
		if (selectedOverclock == 0) {
			toReturn += 2.0;
		}
		
		return toReturn;
	}
	private double getReloadTime() {
		double toReturn = reloadTime;
		
		if (selectedTier2 == 2) {
			toReturn -= 0.6;
		}
		
		if (selectedOverclock == 0) {
			toReturn -= 0.4;
		}
		
		return toReturn;
	}
	private double getBaseSpread() {
		double toReturn = baseSpread;
		
		if (selectedTier3 == 1) {
			toReturn *= 0.5;
		}
		
		if (selectedOverclock == 4) {
			toReturn *= 1.5;
		}
		
		return toReturn;
	}
	private int getMaxPenetrations() {
		int toReturn = maxPenetrations;
		
		if (selectedTier4 == 0) {
			toReturn += 1;
		}
		
		return toReturn;
	}
	private double getWeakpointBonus() {
		double toReturn = weakpointBonus;
		
		if (selectedTier4 == 1) {
			toReturn += 0.3;
		}
		
		if (selectedOverclock == 4) {
			// Since this removes the Zhukov's ability to get weakpoint bonus damage, return a -100% to symbolize it.
			return -1.0;
		}
		
		return toReturn;
	}
	private double getMovespeedWhileFiring() {
		double toReturn = movespeedWhileFiring;
		
		if (selectedOverclock == 4) {
			toReturn -= 0.5;
		}
		
		return toReturn;
	}
	
	@Override
	public StatsRow[] getStats() {
		StatsRow[] toReturn = new StatsRow[9];
		
		boolean directDamageModified = selectedTier1 == 1 || selectedTier3 == 0 || (selectedOverclock > 0 && selectedOverclock < 5);
		toReturn[0] = new StatsRow("Direct Damage:", "" + getDirectDamage(), directDamageModified);
		
		boolean magSizeModified = selectedTier2 == 0 || selectedOverclock == 1 || selectedOverclock == 2;
		toReturn[1] = new StatsRow("Magazine Size:", "" + getMagazineSize(), magSizeModified);
		
		boolean carriedAmmoModified = selectedTier1 == 0 || selectedTier4 == 2 || selectedOverclock == 3;
		toReturn[2] = new StatsRow("Max Ammo:", "" + getCarriedAmmo(), carriedAmmoModified);
		
		toReturn[3] = new StatsRow("Rate of Fire:", "" + getRateOfFire(), selectedTier2 == 1 || selectedOverclock == 0);
		
		toReturn[4] = new StatsRow("Reload Time:", "" + getReloadTime(), selectedTier2 == 2 || selectedOverclock == 0);
		
		String sign = "";
		if (selectedOverclock != 4) {
			sign = "+";
		}
		
		toReturn[5] = new StatsRow("Weakpoint Bonus:", sign + convertDoubleToPercentage(getWeakpointBonus()), selectedTier4 == 1 || selectedOverclock == 4);
		
		toReturn[6] = new StatsRow("Base Spread:", convertDoubleToPercentage(getBaseSpread()), selectedTier3 == 1 || selectedOverclock == 4);
		
		toReturn[7] = new StatsRow("Max Penetrations:", "" + getMaxPenetrations(), selectedTier4 == 0);
		
		toReturn[8] = new StatsRow("Movespeed While Firing:", convertDoubleToPercentage(getMovespeedWhileFiring()), selectedOverclock == 4);
		
		return toReturn;
	}
	
	/****************************************************************************************
	* Other Methods
	****************************************************************************************/

	@Override
	public boolean currentlyDealsSplashDamage() {
		// This weapon can never deal splash damage
		return false;
	}
	
	// Single-target calculations
	private double calculateDamagePerMagazine(boolean weakpointBonus) {
		// Somehow "Embedded Detonators" will have to be modeled in here.
		int effectiveMagazineSize = getMagazineSize() / 2;
		if (weakpointBonus) {
			return (double) increaseBulletDamageForWeakpoints(getDirectDamage(), getWeakpointBonus()) * effectiveMagazineSize;
		}
		else {
			return (double) getDirectDamage() * effectiveMagazineSize;
		}
	}

	@Override
	public double calculateIdealBurstDPS() {
		double effectiveMagazineSize = getMagazineSize() / 2.0;
		double effectiveRoF = getRateOfFire() / 2.0;
		double timeToFireMagazine = effectiveMagazineSize / effectiveRoF;
		return calculateDamagePerMagazine(false) / timeToFireMagazine;
	}

	@Override
	public double calculateIdealSustainedDPS() {
		double effectiveMagazineSize = getMagazineSize() / 2.0;
		double effectiveRoF = getRateOfFire() / 2.0;
		double timeToFireMagazineAndReload = (effectiveMagazineSize / effectiveRoF) + getReloadTime();
		return calculateDamagePerMagazine(false) / timeToFireMagazineAndReload;
	}
	
	@Override
	public double sustainedWeakpointDPS() {
		double effectiveMagazineSize = getMagazineSize() / 2.0;
		double effectiveRoF = getRateOfFire() / 2.0;
		double timeToFireMagazineAndReload = (effectiveMagazineSize / effectiveRoF) + getReloadTime();
		
		// Because the Overclock "Gas Recycling" removes the ability to get any weakpoint bonus damage, that has to be modeled here.
		boolean canGetWeakpointBonus = selectedOverclock != 4;
		
		return calculateDamagePerMagazine(canGetWeakpointBonus) / timeToFireMagazineAndReload;
	}

	@Override
	public double sustainedWeakpointAccuracyDPS() {
		// TODO Auto-generated method stub
		return 0;
	}

	// Multi-target calculations
	@Override
	public double calculateAdditionalTargetDPS() {
		if (selectedTier4 == 0) {
			return calculateIdealSustainedDPS();
		}
		else {
			return 0;
		}
	}

	@Override
	public double calculateMaxMultiTargetDamage() {
		double effectiveMagazineSize = getMagazineSize() / 2.0;
		// If there's an odd number carried ammo, round up since you can fire the last "odd" ammo as a full-damage shot
		double effectiveCarriedAmmo = Math.ceil(getCarriedAmmo() / 2.0);
		return (effectiveMagazineSize + effectiveCarriedAmmo) * getDirectDamage() * calculateMaxNumTargets();
	}

	@Override
	public int calculateMaxNumTargets() {
		return 1 + getMaxPenetrations();
	}

	@Override
	public double calculateFiringDuration() {
		// Because of how this weapon works, all these numbers need to be halved to be accurate.
		double effectiveMagazineSize = getMagazineSize() / 2.0;
		// If there's an odd number carried ammo, round up since you can fire the last "odd" ammo as a full-damage shot
		double effectiveCarriedAmmo = Math.ceil(getCarriedAmmo() / 2.0);
		double effectiveRoF = getRateOfFire() / 2.0;
		
		// Don't forget to add the magazine that you start out with, in addition to the carried ammo
		double numberOfMagazines = (effectiveCarriedAmmo / effectiveMagazineSize) + 1.0;
		double timeToFireMagazine = effectiveMagazineSize / effectiveRoF;
		// There are one fewer reloads than there are magazines to fire
		return numberOfMagazines * timeToFireMagazine + (numberOfMagazines - 1.0) * getReloadTime();
	}

	@Override
	public double averageTimeToKill() {
		return EnemyInformation.averageHealthPool() / sustainedWeakpointDPS();
	}

	@Override
	public double averageOverkill() {
		// Because the Overclock "Gas Recycling" removes the ability to get any weakpoint bonus damage, that has to be modeled here.
		double dmgPerShot;
		if (selectedOverclock == 4) {
			dmgPerShot = getDirectDamage();
		}
		else {
			dmgPerShot = increaseBulletDamageForWeakpoints(getDirectDamage(), getWeakpointBonus());
		}
		
		double overkill = EnemyInformation.averageHealthPool() % dmgPerShot;
		return overkill / dmgPerShot * 100.0;
	}

	@Override
	public double estimatedAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double utilityScore() {
		// TODO Auto-generated method stub
		return 0;
	}
}
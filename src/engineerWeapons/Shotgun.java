package engineerWeapons;

import java.util.Arrays;
import java.util.List;

import modelPieces.AccuracyEstimator;
import modelPieces.EnemyInformation;
import modelPieces.Mod;
import modelPieces.Overclock;
import modelPieces.StatsRow;
import modelPieces.Weapon;

public class Shotgun extends Weapon {
	
	/****************************************************************************************
	* Class Variables
	****************************************************************************************/
	
	private int damagePerPellet;
	private int numberOfPellets;
	private int carriedAmmo;
	private int magazineSize;
	private double rateOfFire;
	private double reloadTime;
	private double weakpointStunChance;
	private int stunDuration;
	private double armorBreakChance;
	private double baseSpread;
	private double recoil;
	
	/****************************************************************************************
	* Constructors
	****************************************************************************************/
	
	// Shortcut constructor to get baseline data
	public Shotgun() {
		this(-1, -1, -1, -1, -1, -1);
	}
	
	// Shortcut constructor to quickly get statistics about a specific build
	public Shotgun(String combination) {
		this(-1, -1, -1, -1, -1, -1);
		buildFromCombination(combination);
	}
	
	public Shotgun(int mod1, int mod2, int mod3, int mod4, int mod5, int overclock) {
		fullName = "\"Warthog\" Auto 210";
		
		// Base stats, before mods or overclocks alter them:
		damagePerPellet = 6;
		numberOfPellets = 8;
		carriedAmmo = 90;
		magazineSize = 6;
		rateOfFire = 1.6;
		reloadTime = 2.0;
		weakpointStunChance = 0.1;
		stunDuration = 2;
		armorBreakChance = 1.0;
		baseSpread = 1.0;
		recoil = 1.0;
		
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
		tier1[0] = new Mod("Supercharged Feed Mechanism", "We overclocked your gun. It fires faster. Don't ask, just enjoy. Also probably don't tell Management, please.", 1, 0);
		tier1[1] = new Mod("Overstuffed Magazine", "The good thing about clips, magazines, ammo drums, fuel tanks... You can always get bigger variants.", 1, 1);
		
		tier2 = new Mod[3];
		tier2[0] = new Mod("Expanded Ammo Bags", "You had to give up some sandwich-storage, but your total ammo capacity is increased!", 2, 0);
		tier2[1] = new Mod("Loaded Shells", "More pellets in each shell", 2, 1);
		tier2[2] = new Mod("Choke", "Decreased shot spread", 2, 2);
		
		tier3 = new Mod[3];
		tier3[0] = new Mod("Recoil Dampener", "Quality engineering, the best lasercut parts, the tender loving care of a dedicated R&D Department, The recoil of your gun is drastically reduced.", 3, 0);
		tier3[1] = new Mod("Quickfire Ejector", "Experience, training, and a couple of under-the-table \"adjustments\" means your gun can be reloaded significantly faster.", 3, 1);
		tier3[2] = new Mod("High Capacity Magazine", "The good thing about clips, magazines, ammo drums, fuel tanks... You can always get bigger variants.", 3, 2);
		
		tier4 = new Mod[3];
		tier4[0] = new Mod("Tungsten Coated Buckshot", "We're proud of this one. Armor shredding. Tear through that high-impact plating of those big buggers like butter. What could be finer?", 4, 0);
		tier4[1] = new Mod("Stun Duration", "Stunned enemies are incapacitated for a longer period of time.", 4, 1);
		tier4[2] = new Mod("Bigger Pellets", "The good folk in R&D have been busy. The overall damage of your weapon is increased.", 4, 2);
		
		tier5 = new Mod[2];
		tier5[0] = new Mod("Turret Whip", "Shoot your turrets to make them create an overcharged shot", 5, 0, false);
		tier5[1] = new Mod("Miner Adjustments", "Fully automatic with an increased rate of fire", 5, 1);
		
		overclocks = new Overclock[5];
		overclocks[0] = new Overclock(Overclock.classification.clean, "Compact Shells", "Using these shells expands magazine capacity slightly and the weapon can fire them faster!", 0);
		overclocks[1] = new Overclock(Overclock.classification.clean, "Light-Weight Magazines", "It's amazing how much material can be removed without affecting anything and lighter magazines means more magazines and faster reloading.", 1);
		overclocks[2] = new Overclock(Overclock.classification.balanced, "Magnetic Pellet Alignment", "Electromagnets in the chamber help reduce shot spread at the cost of a reduced rate of fire and magazine capacity.", 2);
		overclocks[3] = new Overclock(Overclock.classification.unstable, "Cycle Overload", "Heavy modification to the chamber greatly increases the maximum rate of fire but reduces the weapon's accuracy and reload speed as a consequence.", 3);
		overclocks[4] = new Overclock(Overclock.classification.unstable, "Mini Shells", "Smaller shells designed around a new charge type reduce recoil and increase overall ammo and magazine capacity at the cost of raw damage.", 4);
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
				System.out.println("Shotgun's first tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			if (symbols[4] == 'C') {
				System.out.println("Shotgun's fifth tier of mods only has two choices, so 'C' is an invalid choice.");
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
				case 'C': {
					selectedTier3 = 2;
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
	public Shotgun clone() {
		return new Shotgun(selectedTier1, selectedTier2, selectedTier3, selectedTier4, selectedTier5, selectedOverclock);
	}
	
	/****************************************************************************************
	* Setters and Getters
	****************************************************************************************/
	
	private int getDamagePerPellet() {
		int toReturn = damagePerPellet;
		
		if (selectedTier4 == 2) {
			toReturn += 2;
		}
		
		if (selectedOverclock == 3) {
			toReturn += 1;
		}
		else if (selectedOverclock == 4) {
			toReturn -= 2;
		}
		
		return toReturn;
	}
	private int getNumberOfPellets() {
		int toReturn = numberOfPellets;
		
		if (selectedTier2 == 1) {
			toReturn += 2;
		}
		
		return toReturn;
	}
	private int getCarriedAmmo() {
		int toReturn = carriedAmmo;
		
		if (selectedTier2 == 0) {
			toReturn += 40;
		}
		
		if (selectedOverclock == 1) {
			toReturn += 20;
		}
		else if (selectedOverclock == 4) {
			toReturn += 90;
		}
		
		return toReturn;
	}
	private int getMagazineSize() {
		int toReturn = magazineSize;
		
		if (selectedTier1 == 1) {
			toReturn += 2;
		}
		
		if (selectedTier3 == 2) {
			toReturn += 3;
		}
		
		if (selectedOverclock == 0) {
			toReturn += 1;
		}
		else if (selectedOverclock == 2) {
			toReturn -= 2; 
		}
		else if (selectedOverclock == 4) {
			toReturn += 6;
		}
		
		return toReturn;
	}
	private double getRateOfFire() {
		double toReturn = rateOfFire;
		
		if (selectedTier1 == 0) {
			toReturn += 0.8;
		}
		
		if (selectedTier5 == 1) {
			toReturn += 1.2;
		}
		
		if (selectedOverclock == 0) {
			toReturn += 0.4;
		}
		else if (selectedOverclock == 2) {
			toReturn -= 0.4;
		}
		else if (selectedOverclock == 3) {
			toReturn += 2.0;
		}
		
		return toReturn;
	}
	private double getReloadTime() {
		double toReturn = reloadTime;
		
		if (selectedTier3 == 1) {
			toReturn -= 0.5;
		}
		
		if (selectedOverclock == 1) {
			toReturn -= 0.2;
		}
		else if (selectedOverclock == 3) {
			toReturn += 0.5;
		}
		
		return toReturn;
	}
	private double getWeakpointStunChance() {
		double toReturn = weakpointStunChance;
		
		if (selectedTier4 == 1) {
			toReturn += 0.2;
		}
		
		return toReturn;
	}
	private int getStunDuration() {
		int toReturn = stunDuration;
		
		if (selectedTier4 == 1) {
			toReturn += 1;
		}
		
		return toReturn;
	}
	private double getArmorBreakChance() {
		double toReturn = armorBreakChance;
		
		if (selectedTier4 == 0) {
			toReturn += 4.0;
		}
		
		return toReturn;
	}
	private double getBaseSpread() {
		double toReturn = baseSpread;
		
		if (selectedTier2 == 2) {
			toReturn *= 0.5;
		}
		
		if (selectedOverclock == 2) {
			toReturn *= 0.5;
		}
		else if (selectedOverclock == 3) {
			toReturn *= 2.0;
		}
		
		return toReturn;
	}
	private double getRecoil() {
		double toReturn = recoil;
		
		if (selectedTier3 == 0) {
			toReturn *= 0.4;
		}
		
		if (selectedOverclock == 4) {
			toReturn *= 0.5;
		}
		
		return toReturn;
	}
	
	@Override
	public StatsRow[] getStats() {
		StatsRow[] toReturn = new StatsRow[12];
		
		boolean damageModified = selectedTier4 == 2 || selectedOverclock == 3 || selectedOverclock == 4;
		toReturn[0] = new StatsRow("Damage Per Pellet:", "" + getDamagePerPellet(), damageModified);
		
		toReturn[1] = new StatsRow("Number of Pellets/Shot:", "" + getNumberOfPellets(), selectedTier2 == 1);
		
		boolean magSizeModified = selectedTier1 == 1 || selectedTier3 == 2 || selectedOverclock % 2 == 0; // OCs 0, 2, & 4 all modify mag size, but -1, 1, & 3 do not.
		toReturn[2] = new StatsRow("Magazine Size:", "" + getMagazineSize(), magSizeModified);
		
		boolean carriedAmmoModified = selectedTier2 == 0 || selectedOverclock == 1 || selectedOverclock == 4;
		toReturn[3] = new StatsRow("Max Ammo:", "" + getCarriedAmmo(), carriedAmmoModified);
		
		boolean RoFModified = selectedTier1 == 0 || selectedTier5 == 1 || selectedOverclock == 0 || selectedOverclock == 2 || selectedOverclock == 3;
		toReturn[4] = new StatsRow("Rate of Fire:", "" + getRateOfFire(), RoFModified);
		
		boolean reloadModified = selectedTier3 == 1 || selectedOverclock == 1 || selectedOverclock == 3;
		toReturn[5] = new StatsRow("Reload Time:", "" + getReloadTime(), reloadModified);
		
		toReturn[6] = new StatsRow("Weakpoint Stun Chance:", convertDoubleToPercentage(getWeakpointStunChance()), selectedTier4 == 1);
		
		toReturn[7] = new StatsRow("Stun Duration:", "" + getStunDuration(), selectedTier4 == 1);
		
		toReturn[8] = new StatsRow("Armor Break Chance:", convertDoubleToPercentage(getArmorBreakChance()), selectedTier4 == 0);
		
		boolean baseSpreadModified = selectedTier2 == 2 || selectedOverclock == 2 || selectedOverclock == 3;
		toReturn[9] = new StatsRow("Base Spread:", convertDoubleToPercentage(getBaseSpread()), baseSpreadModified);
		
		toReturn[10] = new StatsRow("Recoil:", convertDoubleToPercentage(getRecoil()), selectedTier3 == 0 || selectedOverclock == 4);
		
		toReturn[11] = new StatsRow("Accuracy:", convertDoubleToPercentage(new AccuracyEstimator(getRateOfFire(), getMagazineSize(), getBaseSpread(), 1.0, 1.0, 1.0, getRecoil(), 1.0, getRecoil()).calculateAccuracy()), false);
		
		return toReturn;
	}
	
	/****************************************************************************************
	* Other Methods
	****************************************************************************************/
	
	private double calculateDamagePerMagazine(boolean weakpointBonus) {
		if (weakpointBonus) {
			return (double) (increaseBulletDamageForWeakpoints(getDamagePerPellet()) * getNumberOfPellets() * getMagazineSize());
		}
		else {
			return (double) (getDamagePerPellet() * getNumberOfPellets() * getMagazineSize());
		}
	}
	
	@Override
	public boolean currentlyDealsSplashDamage() {
		return false;
	}

	@Override
	public double calculateIdealBurstDPS() {
		double timeToFireMagazine = ((double) getMagazineSize()) / getRateOfFire();
		return calculateDamagePerMagazine(false) / timeToFireMagazine;
	}

	@Override
	public double calculateIdealSustainedDPS() {
		double timeToFireMagazineAndReload = (((double) getMagazineSize()) / getRateOfFire()) + getReloadTime();
		return calculateDamagePerMagazine(false) / timeToFireMagazineAndReload;
	}
	
	@Override
	public double sustainedWeakpointDPS() {
		double timeToFireMagazineAndReload = (((double) getMagazineSize()) / getRateOfFire()) + getReloadTime();
		return calculateDamagePerMagazine(true) / timeToFireMagazineAndReload;
	}

	@Override
	public double sustainedWeakpointAccuracyDPS() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateAdditionalTargetDPS() {
		// Shotgun cannot naturally hit more than one target, unless pellet spread is large enough to hit multiple swarmers. In that case, the DPS is equivalent to the single-target DPS but spread between targets.
		return 0.0;
	}

	@Override
	public double calculateMaxMultiTargetDamage() {
		return (double) (getDamagePerPellet() * getNumberOfPellets() * (getMagazineSize() + getCarriedAmmo()));
	}

	@Override
	public int calculateMaxNumTargets() {
		return 1;
	}

	@Override
	public double calculateFiringDuration() {
		double magSize = (double) getMagazineSize();
		// Don't forget to add the magazine that you start out with, in addition to the carried ammo
		double numberOfMagazines = (((double) getCarriedAmmo()) / magSize) + 1.0;
		double timeToFireMagazine = magSize / getRateOfFire();
		// There are one fewer reloads than there are magazines to fire
		return numberOfMagazines * timeToFireMagazine + (numberOfMagazines - 1.0) * getReloadTime();
	}

	@Override
	public double averageTimeToKill() {
		return EnemyInformation.averageHealthPool() / sustainedWeakpointDPS();
	}

	@Override
	public double averageOverkill() {
		double dmgPerShot = increaseBulletDamageForWeakpoints(increaseBulletDamageForWeakpoints(getDamagePerPellet()) * getNumberOfPellets());
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
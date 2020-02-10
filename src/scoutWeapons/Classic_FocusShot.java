package scoutWeapons;

import java.util.Arrays;
import java.util.List;

import modelPieces.DoTInformation;
import modelPieces.EnemyInformation;
import modelPieces.Mod;
import modelPieces.Overclock;
import modelPieces.StatsRow;
import modelPieces.Weapon;

public class Classic_FocusShot extends Weapon {
	
	/****************************************************************************************
	* Class Variables
	****************************************************************************************/
	
	private double directDamage;
	private double focusedShotMultiplier;
	private double carriedAmmo;
	private int magazineSize;
	// private double rateOfFire;
	private double reloadTime;
	private double delayBeforeFocusing;
	private double focusDuration;
	private double movespeedWhileFocusing;
	private int maxPenetrations;
	private double weakpointBonus;
	private double armorBreakChance;
	private int stunDuration;
	private double recoil;
	
	/****************************************************************************************
	* Constructors
	****************************************************************************************/
	
	// Shortcut constructor to get baseline data
	public Classic_FocusShot() {
		this(-1, -1, -1, -1, -1, -1);
	}
	
	// Shortcut constructor to quickly get statistics about a specific build
	public Classic_FocusShot(String combination) {
		this(-1, -1, -1, -1, -1, -1);
		buildFromCombination(combination);
	}
	
	public Classic_FocusShot(int mod1, int mod2, int mod3, int mod4, int mod5, int overclock) {
		fullName = "M1000 Classic (Focused Shots)";
		
		// Base stats, before mods or overclocks alter them:
		directDamage = 50;
		focusedShotMultiplier = 2.0;
		carriedAmmo = 96;
		magazineSize = 8;
		// rateOfFire = 4.0;
		reloadTime = 2.5;
		delayBeforeFocusing = 0.4;  // seconds
		focusDuration = 0.6;  // seconds.
		movespeedWhileFocusing = 0.3;
		maxPenetrations = 0;
		weakpointBonus = 0.1;
		armorBreakChance = 0.3;
		stunDuration = 3;
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
		tier1[0] = new Mod("Expanded Ammo Bags", "You had to give up some sandwich-storage, but your total ammo capacity is increased!", 1, 0);
		tier1[1] = new Mod("Increased Caliber Rounds", "The good folk in R&D have been busy. The overall damage of your weapon is increased.", 1, 1);
		
		tier2 = new Mod[2];
		tier2[0] = new Mod("Fast-Charging Coils", "Faster focus when holding down the trigger.", 2, 0);
		tier2[1] = new Mod("Better Weight Balance", "Improved hip-shot accuracy", 2, 1);
		
		tier3 = new Mod[2];
		tier3[0] = new Mod("Killer Focus", "Greater focus damage bonus", 3, 0);
		tier3[1] = new Mod("Extended Clip", "The good thing about clips, magazines, ammo drums, fuel tanks... You can always get bigger variants.", 3, 1);
		
		tier4 = new Mod[3];
		tier4[0] = new Mod("Super Blowthrough Rounds", "Shaped projectiles designed to over-penetrate targets with a minimal loss of energy. In other words: Fire straight through several enemies at once!", 4, 0);
		tier4[1] = new Mod("Hollow-Point Bullets", "Hit 'em where it hurts! Literally! We've upped the damage you'll be able to do to any creatures fleshy bits. You're welcome.", 4, 1);
		tier4[2] = new Mod("Hardened Rounds", "We're proud of this one. Armor shredding. Tear through that high-impact plating of those big buggers like butter. What could be finer?", 4, 2);
		
		tier5 = new Mod[3];
		tier5[0] = new Mod("Hitting Where it Hurts", "Focused shots stagger the target", 5, 0);
		tier5[1] = new Mod("Precision Terror", "Killing your target with a focused shot to the weakspot will send nearby creatures fleeing with terror!", 5, 1, false);
		tier5[2] = new Mod("Killing Machine", "You can perform a lightning-fast reload right after killing an enemy.", 5, 2, false);  // Supposedly reduces manual reload time by 0.75 sec after a kill?
		
		overclocks = new Overclock[6];
		overclocks[0] = new Overclock(Overclock.classification.clean, "Hoverclock", "Your movement slows down for a few seconds while using focus mode in the air.", 0, false);
		overclocks[1] = new Overclock(Overclock.classification.clean, "Minimal Clips", "Make space for more ammo and speed up reloads by getting rid of dead weight on the clips.", 1);
		overclocks[2] = new Overclock(Overclock.classification.balanced, "Active Stability System", "Focus without slowing down but the power drain from the coils lowers the power of the focused shots.", 2);
		overclocks[3] = new Overclock(Overclock.classification.balanced, "Hipster", "A rebalancing of weight distribution, enlarged vents and a reshaped grip result in a rifle that is more controllable when hip-firing in quick succession but at the cost of pure damage output.", 3);
		overclocks[4] = new Overclock(Overclock.classification.unstable, "Electrocuting Focus Shots", "Embedded capacitors in a copper core carry the electric charge from the EM coils used for focus shots and will electrocute the target at the cost of a reduced focus shot damage bonus.", 4);
		overclocks[5] = new Overclock(Overclock.classification.unstable, "Supercooling Chamber", "Take the M1000'S focus mode to the extreme by supercooling the rounds before firing to improve their acceleration through the coils, but the extra coolant in the clips limits how much ammo you can bring.", 5);
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
				System.out.println("Classic's first tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			if (symbols[1] == 'C') {
				System.out.println("Classic's second tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			if (symbols[2] == 'C') {
				System.out.println("Classic's third tier of mods only has two choices, so 'C' is an invalid choice.");
				combinationIsValid = false;
			}
			List<Character> validOverclockSymbols = Arrays.asList(new Character[] {'1', '2', '3', '4', '5', '6', '-'});
			if (!validOverclockSymbols.contains(symbols[5])) {
				System.out.println("The sixth symbol, " + symbols[5] + ", is not a number between 1-6 or a hyphen");
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
				case 'C': {
					selectedTier5 = 2;
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
				case '6': {
					selectedOverclock = 5;
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
	public Classic_FocusShot clone() {
		return new Classic_FocusShot(selectedTier1, selectedTier2, selectedTier3, selectedTier4, selectedTier5, selectedOverclock);
	}
	
	/****************************************************************************************
	* Setters and Getters
	****************************************************************************************/
	
	private double getDirectDamage() {
		double toReturn = directDamage;
		
		// Additive bonuses first
		if (selectedOverclock == 3) {
			toReturn -= 20;
		}
		
		// Multiplicative bonuses last
		if (selectedTier1 == 1) {
			toReturn *= 1.2;
		}
		
		return toReturn;
	}
	private double getFocusedShotMultiplier() {
		double toReturn = focusedShotMultiplier;
		
		// Additive bonuses first
		if (selectedTier3 == 0) {
			toReturn += 0.25;
		}
		
		if (selectedOverclock == 2 || selectedOverclock == 4) {
			toReturn -= 0.25;
		}
		else if (selectedOverclock == 5) {
			toReturn += 1.25;
		}
		
		return toReturn;
	}
	private int getCarriedAmmo() {
		double toReturn = carriedAmmo;
		
		if (selectedTier1 == 0) {
			toReturn += 32;
		}
		
		if (selectedOverclock == 1) {
			toReturn += 16;
		}
		else if (selectedOverclock == 3) {
			toReturn += 72;
		}
		else if (selectedOverclock == 5) {
			toReturn *= 0.635;
		}
		
		// Divide by 2 to account for firing two ammo per focused shot
		// Use ceiling function because you can do a fully-charged Focus Shot with only 1 ammo if there's no ammo left in the clip
		return (int) Math.ceil(toReturn / 2.0);
	}
	private int getMagazineSize() {
		int toReturn = magazineSize;
		
		if (selectedTier3 == 1) {
			toReturn += 6;
		}
		
		// Divide by 2 to account for firing two ammo per focused shot
		return toReturn / 2;
	}
	private double getRateOfFire() {
		// Because the max RoF will never be achieved with Focus Shots, instead model the RoF as the inverse of the Focus Duration
		return 1.0 / (delayBeforeFocusing + getFocusDuration());
	}
	private double getReloadTime() {
		double toReturn = reloadTime;
		
		if (selectedOverclock == 1) {
			toReturn -= 0.2;
		}
		
		// TODO: implement T5 "Killing Machine" reload time reduction
		
		return toReturn;
	}
	private double getFocusDuration() {
		double focusSpeedCoefficient = 1.0;
		if (selectedTier2 == 0) {
			focusSpeedCoefficient *= 1.6;
		}
		
		if (selectedOverclock == 5) {
			focusSpeedCoefficient *= 0.5;
		}
		
		return focusDuration / focusSpeedCoefficient;
	}
	private double getMovespeedWhileFocusing() {
		double toReturn = movespeedWhileFocusing;
		
		if (selectedOverclock == 2) {
			toReturn += 0.7;
		}
		else if (selectedOverclock == 5) {
			toReturn *= 0;
		}
		
		return toReturn;
	}
	private int getMaxPenetrations() {
		int toReturn = maxPenetrations;
		
		if (selectedTier4 == 0) {
			toReturn += 3;
		}
		
		return toReturn;
	}
	private double getWeakpointBonus() {
		double toReturn = weakpointBonus;
		
		if (selectedTier4 == 1) {
			toReturn += 0.25;
		}
		
		return toReturn;
	}
	private double getArmorBreakChance() {
		double toReturn = armorBreakChance;
		
		if (selectedTier4 == 2) {
			toReturn += 2.2;
		}
		
		return toReturn;
	}
	private double getRecoil() {
		double toReturn = recoil;
		
		if (selectedTier2 == 1) {
			toReturn *= 0.5;
		}
		
		if (selectedOverclock == 3) {
			toReturn *= 0.5;
		}
		
		return toReturn;
	}
	private int getStunDuration() {
		if (selectedTier5 == 0) {
			return stunDuration;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public StatsRow[] getStats() {
		StatsRow[] toReturn = new StatsRow[14];
		
		toReturn[0] = new StatsRow("Direct Damage:", "" + getDirectDamage(), selectedOverclock == 3 || selectedTier1 == 1);
		
		boolean multiplierModified = selectedTier3 == 0 || selectedOverclock == 2 || selectedOverclock == 4 || selectedOverclock == 5;
		toReturn[1] = new StatsRow("Focused Shot Multiplier:", convertDoubleToPercentage(getFocusedShotMultiplier()), multiplierModified);
		
		toReturn[2] = new StatsRow("Delay Before Focusing:", "" + delayBeforeFocusing, false);
		
		toReturn[3] = new StatsRow("Focus Shot Charge-up Duration:", "" + getFocusDuration(), selectedTier2 == 0 || selectedOverclock == 5);
		
		toReturn[4] = new StatsRow("Movespeed While Focusing:", convertDoubleToPercentage(getMovespeedWhileFocusing()), selectedOverclock == 2 || selectedOverclock == 5);
		
		toReturn[5] = new StatsRow("Magazine Size:", "" + getMagazineSize(), selectedTier3 == 1);
		
		boolean carriedAmmoModified = selectedTier1 == 0 || selectedOverclock == 1 || selectedOverclock == 3 || selectedOverclock == 5;
		toReturn[6] = new StatsRow("Max Ammo:", "" + getCarriedAmmo(), carriedAmmoModified);
		
		toReturn[7] = new StatsRow("Rate of Fire:", "" + getRateOfFire(), selectedTier2 == 0 || selectedOverclock == 5);
		
		toReturn[8] = new StatsRow("Reload Time:", "" + getReloadTime(), selectedOverclock == 1);
		
		toReturn[9] = new StatsRow("Weakpoint Bonus:", "+" + convertDoubleToPercentage(getWeakpointBonus()), selectedTier4 == 1);
		
		toReturn[10] = new StatsRow("Armor Breaking:", convertDoubleToPercentage(getArmorBreakChance()), selectedTier4 == 2);
		
		toReturn[11] = new StatsRow("Max Penetrations:", "" + getMaxPenetrations(), selectedTier4 == 0);
		
		toReturn[12] = new StatsRow("Recoil:", convertDoubleToPercentage(getRecoil()), selectedTier2 == 1 || selectedOverclock == 3);
		
		toReturn[13] = new StatsRow("Stun Duration:", "" + getStunDuration(), selectedTier5 == 0);
		
		return toReturn;
	}
	
	/****************************************************************************************
	* Other Methods
	****************************************************************************************/

	@Override
	public boolean currentlyDealsSplashDamage() {
		return false;
	}

	// Single-target calculations
	private double calculateDamagePerMagazine(boolean weakpointBonus) {
		double bulletDamage = getDirectDamage() * getFocusedShotMultiplier();
		if (weakpointBonus) {
			return (double) increaseBulletDamageForWeakpoints(bulletDamage, getWeakpointBonus()) * getMagazineSize();
		}
		else {
			return (double) bulletDamage * getMagazineSize();
		}
	}

	@Override
	public double calculateIdealBurstDPS() {
		double timeToFireMagazine = ((double) getMagazineSize()) / getRateOfFire();
		double dps = calculateDamagePerMagazine(false) / timeToFireMagazine;
		
		if (selectedOverclock == 4) {
			return dps + DoTInformation.Electro_DPS;
		}
		else {
			return dps;
		}
	}

	@Override
	public double calculateIdealSustainedDPS() {
		double timeToFireMagazineAndReload = (((double) getMagazineSize()) / getRateOfFire()) + getReloadTime();
		double dps = calculateDamagePerMagazine(false) / timeToFireMagazineAndReload;
		
		if (selectedOverclock == 4) {
			return dps + DoTInformation.Electro_DPS;
		}
		else {
			return dps;
		}
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
		double directDamageDealt = calculateMaxNumTargets() * (getMagazineSize() + getCarriedAmmo()) * getDirectDamage() * getFocusedShotMultiplier();
		
		if (selectedOverclock == 4) {
			// Because Electrocute DoT will be refreshed instead of stacked, this block of code will estimate how many shots of the whole ammo pool apply a new DoT.
			// Functionally, this is basically asking how many enemies get DoTs on them, which is equivalent to how many enemies are expected to be killed with the carried ammo
			double avgShotsToKillEnemy = Math.ceil(EnemyInformation.averageHealthPool() / (getDirectDamage() * getFocusedShotMultiplier()));
			int avgNumEnemiesAfflictedWithDoT = (int) Math.ceil((getMagazineSize() + getCarriedAmmo()) / avgShotsToKillEnemy);
			double electrocuteDoTDamageDealt = calculateMaxNumTargets() * avgNumEnemiesAfflictedWithDoT * DoTInformation.Electro_DPS * DoTInformation.Electro_SecsDuration;
			return directDamageDealt + electrocuteDoTDamageDealt;
		}
		else {
			return directDamageDealt;
		}
	}

	@Override
	public int calculateMaxNumTargets() {
		return 1 + getMaxPenetrations();
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
		double effectiveDPS;
		
		if (selectedOverclock == 4) {
			effectiveDPS = sustainedWeakpointDPS() + DoTInformation.Electro_DPS;
		}
		else {
			effectiveDPS = sustainedWeakpointDPS();
		}
		
		return EnemyInformation.averageHealthPool() / effectiveDPS;
	}

	@Override
	public double averageOverkill() {
		double dmgPerShot = increaseBulletDamageForWeakpoints(getDirectDamage() * getFocusedShotMultiplier(), getWeakpointBonus());
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
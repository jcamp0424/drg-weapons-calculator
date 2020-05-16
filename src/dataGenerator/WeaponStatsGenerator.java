package dataGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import modelPieces.Weapon;

public class WeaponStatsGenerator {
	
	private Weapon weaponToTest;
	private String csvFolderPath;
	private String csvFilePath;
	private String[] headers;
	private ArrayList<String> csvLinesToWrite;
	
	public WeaponStatsGenerator(Weapon testingWeapon) {
		weaponToTest = testingWeapon;
		csvFolderPath = "";
		
		String weaponClass = weaponToTest.getDwarfClass();
		String weaponName = weaponToTest.getSimpleName();
		File csvOut = new File(csvFolderPath, weaponClass + "_" + weaponName + ".csv");
		csvFilePath = csvOut.getAbsolutePath();
		
		headers = new String[] {"Mods/OC", "Ideal Burst DPS", "Ideal Sustained DPS", "Sustained DPS (+Weakpoints)", 
								"Sustained DPS (+Weakpoints, +Accuracy)", "Ideal Additional Target DPS", "Max Num Targets", 
								"Max Multi-Target Dmg", "Ammo Efficiency", "General Accuracy", "Weakpoint Accuracy", 
								"Firing Duration", "Avg Overkill", "Avg TTK", "Breakpoints", "Utility"};
		csvLinesToWrite = new ArrayList<String>();
	}
	
	public String getCSVFolderPath() {
		return csvFolderPath;
	}
	public void setCSVFolderPath(String newPath) {
		csvFolderPath = newPath;
		String weaponClass = weaponToTest.getDwarfClass();
		String weaponName = weaponToTest.getSimpleName();
		File csvOut = new File(csvFolderPath, weaponClass + "_" + weaponName + ".csv");
		csvFilePath = csvOut.getAbsolutePath();
	}
	
	public void changeWeapon(Weapon newWeaponToCalculate) {
		weaponToTest = newWeaponToCalculate;
		String weaponClass = weaponToTest.getDwarfClass();
		String weaponName = weaponToTest.getSimpleName();
		File csvOut = new File(csvFolderPath, weaponClass + "_" + weaponName + ".csv");
		csvFilePath = csvOut.getAbsolutePath();
		
		// Proactively clear out the old CSV lines, since they won't be applicable to the new Weapon
		csvLinesToWrite = new ArrayList<String>();
	}
	
	public void runTest(boolean printStatsToConsole, boolean exportStatsToCSV) {
		/*
			Questions I want to answer:
				1. What are the baseline stats for the weapon?
				2. How much does each individual mod increase or decrease those stats?
				3. How much does each individual overclock increase or decrease those stats?
				4. What is the best combination of mods and overclocks?
		*/
		
		if (!printStatsToConsole && !exportStatsToCSV) {
			System.out.println("The statistics need an output destination, otherwise this method is useless.");
			return;
		}
		
		// Start by resetting all mods and overclocks to be unselected
		String currentCombination = weaponToTest.getCombination();
		
		if (exportStatsToCSV) {
			// New run; clear out old data and write the header line.
			csvLinesToWrite = new ArrayList<String>();
			try {
				// Set append=False so that it clears existing lines
				FileWriter CSVwriter = new FileWriter(csvFilePath, false);
				String headerLine = String.join(",", headers) + ",\n";
				CSVwriter.append(headerLine);
				CSVwriter.flush();
				CSVwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (printStatsToConsole) {
			System.out.printf("******** %s ********\n", weaponToTest.getFullName());
			System.out.printf("%s\t\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t\t%s\t%s\t%s\n", headers[0], headers[1], headers[2], headers[3], headers[4], 
							  headers[5], headers[6], headers[7], headers[8], headers[9], headers[10], headers[11], headers[12]);
		
			// Section 1: baseline statistics
			weaponToTest.setSelectedModAtTier(1, -1, false);
			weaponToTest.setSelectedModAtTier(2, -1, false);
			weaponToTest.setSelectedModAtTier(3, -1, false);
			weaponToTest.setSelectedModAtTier(4, -1, false);
			weaponToTest.setSelectedModAtTier(5, -1, false);
			weaponToTest.setSelectedOverclock(-1, false);
			
			calculateStatsAndPrint(printStatsToConsole, false);
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
			// Section 2: stat changes of individual mods
			int i;
			for (i = 0; i < weaponToTest.getModsAtTier(1).length; i++) {
				weaponToTest.setSelectedModAtTier(1, i, false);
				calculateStatsAndPrint(printStatsToConsole, false);
			}
			// Unselect the mod at this tier so it doesn't affect the next tier
			weaponToTest.setSelectedModAtTier(1, -1, false);
			
			for (i = 0; i < weaponToTest.getModsAtTier(2).length; i++) {
				weaponToTest.setSelectedModAtTier(2, i, false);
				calculateStatsAndPrint(printStatsToConsole, false);	
			}
			// Unselect the mod at this tier so it doesn't affect the next tier
			weaponToTest.setSelectedModAtTier(2, -1, false);
			
			for (i = 0; i < weaponToTest.getModsAtTier(3).length; i++) {
				weaponToTest.setSelectedModAtTier(3, i, false);
				calculateStatsAndPrint(printStatsToConsole, false);
			}
			// Unselect the mod at this tier so it doesn't affect the next tier
			weaponToTest.setSelectedModAtTier(3, -1, false);
			
			for (i = 0; i < weaponToTest.getModsAtTier(4).length; i++) {
				weaponToTest.setSelectedModAtTier(4, i, false);
				calculateStatsAndPrint(printStatsToConsole, false);
			}
			// Unselect the mod at this tier so it doesn't affect the next tier
			weaponToTest.setSelectedModAtTier(4, -1, false);
			
			for (i = 0; i < weaponToTest.getModsAtTier(5).length; i++) {
				weaponToTest.setSelectedModAtTier(5, i, false);
				calculateStatsAndPrint(printStatsToConsole, false);
			}
			// Unselect the mod at this tier so it doesn't affect the next tier
			weaponToTest.setSelectedModAtTier(5, -1, false);
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
			// Section 3: stat changes of individual overclocks
			for (i = 0; i < weaponToTest.getOverclocks().length; i++) {
				weaponToTest.setSelectedOverclock(i, false);
				calculateStatsAndPrint(printStatsToConsole, false);
			}
			// Unselect the overclock so that weaponToTest will be at "baseline" before doing the 6 clone() calls
			weaponToTest.setSelectedOverclock(-1, false);
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
		
		// Section 4: COMBINATORICS
		// Start by cloning the weapon with no mods or overclocks selected to get baselines
		String bestIdealBurstDPSCombination = weaponToTest.getCombination();
		double bestIdealBurstDPS = weaponToTest.calculateIdealBurstDPS();
		
		String bestIdealSustainedDPSCombination = bestIdealBurstDPSCombination;
		double bestIdealSustainedDPS = weaponToTest.calculateIdealSustainedDPS();
		
		String bestWeakpointSustainedDPSCombination = bestIdealBurstDPSCombination;
		double bestWeakpointSustainedDPS = weaponToTest.sustainedWeakpointDPS();
		
		String bestWeakpointAccuracySustainedDPSCombination = bestIdealBurstDPSCombination;
		double bestWeakpointAccuracySustainedDPS = weaponToTest.sustainedWeakpointAccuracyDPS();
		
		String bestAdditionalTargetDPSCombination = bestIdealBurstDPSCombination;
		double bestAdditionalTargetDPS = weaponToTest.calculateAdditionalTargetDPS();
		
		String mostMultiTargetDamageCombination = bestIdealBurstDPSCombination;
		double mostMultiTargetDamage = weaponToTest.calculateMaxMultiTargetDamage();
		
		String mostNumTargetsCombination = bestIdealBurstDPSCombination;
		double mostNumTargets = weaponToTest.calculateMaxNumTargets();
		
		String longestFiringDurationCombination = bestIdealBurstDPSCombination;
		double longestFiringDuration = weaponToTest.calculateFiringDuration();
		
		String fastestTTKCombination = bestIdealBurstDPSCombination;
		double fastestTTK = weaponToTest.averageTimeToKill();
		
		String lowestOverkillCombination = bestIdealBurstDPSCombination;
		double lowestOverkill = weaponToTest.averageOverkill();
		
		String bestAccuracyCombination = bestIdealBurstDPSCombination;
		double bestAccuracy = weaponToTest.estimatedAccuracy(false);
		
		String bestUtilityCombination = bestIdealBurstDPSCombination;
		double bestUtility = weaponToTest.utilityScore();
		
		double currentBurst, currentSustained, currentSustainedWeakpoint, currentSustainedWeakpointAccuracy, currentAdditional, currentMaxDamage, 
				currentNumTargets, currentFiringDuration, currentTTK, currentOverkill, currentAccuracy, currentUtility;
		String forLoopsCombination;
		
		// The overclocks are the outermost loop because they should change last, and tier 1 is the innermost loop since it should change first.
		for (int oc = -1; oc < weaponToTest.getOverclocks().length; oc++) {
			weaponToTest.setSelectedOverclock(oc, false);
			
			for (int t5 = -1; t5 < weaponToTest.getModsAtTier(5).length; t5++) {
				weaponToTest.setSelectedModAtTier(5, t5, false);
				
				for (int t4 = -1; t4 < weaponToTest.getModsAtTier(4).length; t4++) {
					weaponToTest.setSelectedModAtTier(4, t4, false);
					
					for (int t3 = -1; t3 < weaponToTest.getModsAtTier(3).length; t3++) {
						weaponToTest.setSelectedModAtTier(3, t3, false);
						
						for (int t2 = -1; t2 < weaponToTest.getModsAtTier(2).length; t2++) {
							weaponToTest.setSelectedModAtTier(2, t2, false);
							
							for (int t1 = -1; t1 < weaponToTest.getModsAtTier(1).length; t1++) {
								weaponToTest.setSelectedModAtTier(1, t1, false);
								
								// Because this will generate thousands of lines of data, never print to console.
								calculateStatsAndPrint(false, exportStatsToCSV);
								
								forLoopsCombination = weaponToTest.getCombination();
								
								// Single-target calculations
								currentBurst = weaponToTest.calculateIdealBurstDPS();
								if (currentBurst > bestIdealBurstDPS) {
									bestIdealBurstDPSCombination = forLoopsCombination;
									bestIdealBurstDPS = currentBurst;
								}
								currentSustained = weaponToTest.calculateIdealSustainedDPS();
								if (currentSustained > bestIdealSustainedDPS) {
									bestIdealSustainedDPSCombination = forLoopsCombination;
									bestIdealSustainedDPS = currentSustained;
								}
								currentSustainedWeakpoint = weaponToTest.sustainedWeakpointDPS();
								if (currentSustainedWeakpoint > bestWeakpointSustainedDPS) {
									bestWeakpointSustainedDPSCombination = forLoopsCombination;
									bestWeakpointSustainedDPS = currentSustainedWeakpoint;
								}
								currentSustainedWeakpointAccuracy = weaponToTest.sustainedWeakpointAccuracyDPS();
								if (currentSustainedWeakpointAccuracy > bestWeakpointAccuracySustainedDPS) {
									bestWeakpointAccuracySustainedDPSCombination = forLoopsCombination;
									bestWeakpointAccuracySustainedDPS = currentSustainedWeakpointAccuracy;
								}
								
								// Multi-target calculations
								currentAdditional = weaponToTest.calculateAdditionalTargetDPS();
								if (currentAdditional > bestAdditionalTargetDPS) {
									bestAdditionalTargetDPSCombination = forLoopsCombination;
									bestAdditionalTargetDPS = currentAdditional;
								}
								currentMaxDamage = weaponToTest.calculateMaxMultiTargetDamage();
								if (currentMaxDamage > mostMultiTargetDamage) {
									mostMultiTargetDamageCombination = forLoopsCombination;
									mostMultiTargetDamage = currentMaxDamage;
								}
								
								// Non-damage calculations
								currentNumTargets = weaponToTest.calculateMaxNumTargets();
								if (currentNumTargets > mostNumTargets) {
									mostNumTargetsCombination = forLoopsCombination;
									mostNumTargets = currentNumTargets;
								}
								currentFiringDuration = weaponToTest.calculateFiringDuration();
								if (currentFiringDuration > longestFiringDuration) {
									longestFiringDurationCombination = forLoopsCombination;
									longestFiringDuration = currentFiringDuration;
								}
								currentTTK = weaponToTest.averageTimeToKill();
								if (currentTTK < fastestTTK) {
									fastestTTKCombination = forLoopsCombination;
									fastestTTK = currentTTK;
								}
								currentOverkill = weaponToTest.averageOverkill();
								if (currentOverkill < lowestOverkill) {
									lowestOverkillCombination = forLoopsCombination;
									lowestOverkill = currentOverkill;
								}
								currentAccuracy = weaponToTest.estimatedAccuracy(false);
								if (currentAccuracy > bestAccuracy) {
									bestAccuracyCombination = forLoopsCombination;
									bestAccuracy = currentAccuracy;
								}
								currentUtility = weaponToTest.utilityScore();
								if (currentUtility > bestUtility) {
									bestUtilityCombination = forLoopsCombination;
									bestUtility = currentUtility;
								}
							}
						}
					}
				}
			}
		}
		
		if (printStatsToConsole) {
			System.out.println("Best build combinations for each category:");
			System.out.println("	Best Ideal Burst DPS: " + bestIdealBurstDPSCombination + " at " + bestIdealBurstDPS  + " DPS");
			System.out.println("	Best Ideal Sustained DPS: " + bestIdealSustainedDPSCombination + " at " + bestIdealSustainedDPS + " DPS");
			System.out.println("	Best Sustained + Weakpoint DPS: " + bestWeakpointSustainedDPSCombination + " at " + bestWeakpointSustainedDPS + " DPS");
			System.out.println("	Best Sustained + Weakpoint + Accuracy DPS: " + bestWeakpointAccuracySustainedDPSCombination + " at " + bestWeakpointAccuracySustainedDPS + " DPS");
			System.out.println("	Best Additional Target DPS: " + bestAdditionalTargetDPSCombination + " at " + bestAdditionalTargetDPS + " extra DPS per additional target");
			System.out.println("	Most damage dealt to multiple targets: " + mostMultiTargetDamageCombination + " at " + mostMultiTargetDamage + " damage");
			System.out.println("	Most number of targets hit per projectile: " + mostNumTargetsCombination + " at " + mostNumTargets + " targets per projectile");
			System.out.println("	Longest time to fire all projectiles: " + longestFiringDurationCombination + " at " + longestFiringDuration + " sec");
			System.out.println("	Shortest average Time To Kill: " + fastestTTKCombination + " at " + fastestTTK + " sec");
			System.out.println("	Lowest average Overkill: " + lowestOverkillCombination + " at " + lowestOverkill + "%");
			System.out.println("	Most Accurate: " + bestAccuracyCombination + " at " + bestAccuracy + "%");
			System.out.println("	Most Utility: " + bestUtilityCombination + " at " + bestUtility);
		}
		if (exportStatsToCSV) {
			// Open the CSV file once, then dump the accumulated ArrayList of lines all at once to minimize I/O time
			try {
				// Set append=True so that it appends the lines after the header line
				FileWriter CSVwriter = new FileWriter(csvFilePath, true);
				for (String line: csvLinesToWrite) {
					CSVwriter.append(line);
				}
				CSVwriter.flush();
				CSVwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Finally, set the Weapon back to the mods/oc combination that it started at before the test.
		weaponToTest.buildFromCombination(currentCombination);
	}
	
	public ArrayList<String> dumpToMySQL() {
		ArrayList<String> toReturn = new ArrayList<String>();
		
		// The overclocks are the outermost loop because they should change last, and tier 1 is the innermost loop since it should change first.
		for (int oc = -1; oc < weaponToTest.getOverclocks().length; oc++) {
			weaponToTest.setSelectedOverclock(oc, false);
			
			for (int t5 = -1; t5 < weaponToTest.getModsAtTier(5).length; t5++) {
				weaponToTest.setSelectedModAtTier(5, t5, false);
				
				for (int t4 = -1; t4 < weaponToTest.getModsAtTier(4).length; t4++) {
					weaponToTest.setSelectedModAtTier(4, t4, false);
					
					for (int t3 = -1; t3 < weaponToTest.getModsAtTier(3).length; t3++) {
						weaponToTest.setSelectedModAtTier(3, t3, false);
						
						for (int t2 = -1; t2 < weaponToTest.getModsAtTier(2).length; t2++) {
							weaponToTest.setSelectedModAtTier(2, t2, false);
							
							for (int t1 = -1; t1 < weaponToTest.getModsAtTier(1).length; t1++) {
								weaponToTest.setSelectedModAtTier(1, t1, false);
								
								toReturn.add(String.format("INSERT INTO `%s` VALUES(NULL, %d, %d, '%s', '%s', %f, %f, %f, %f, %f, %d, %f, %f, %f, %f, %f, %f, %f, %d, %f);\n", 
										DatabaseConstants.tableName, weaponToTest.getDwarfClassID(), weaponToTest.getWeaponID(), weaponToTest.getSimpleName(), weaponToTest.getCombination(),
										weaponToTest.calculateIdealBurstDPS(), weaponToTest.calculateIdealSustainedDPS(), weaponToTest.sustainedWeakpointDPS(), weaponToTest.sustainedWeakpointAccuracyDPS(), weaponToTest.calculateAdditionalTargetDPS(), 
										weaponToTest.calculateMaxNumTargets(), weaponToTest.calculateMaxMultiTargetDamage(), weaponToTest.ammoEfficiency(), weaponToTest.estimatedAccuracy(false), weaponToTest.estimatedAccuracy(true),
										weaponToTest.calculateFiringDuration(), weaponToTest.averageOverkill(), weaponToTest.averageTimeToKill(), weaponToTest.breakpoints(), weaponToTest.utilityScore()
								));
							}
						}
					}
				}
			}
		}
		
		return toReturn;
	}
	
	private void calculateStatsAndPrint(boolean console, boolean csv) {
		// There is a niche where both console and csv can be false; in the 6 nested for-loops in runTest() console will always be false, 
		// and csv could be false because of the run parameters. If they're both false, don't do anything in this method to save some CPU time.
		if (!console && !csv) {
			return;
		}
		
		String combination = weaponToTest.getCombination();
		
		double[] metrics = weaponToTest.getMetrics();
		
		if (console) {
			printStatsToConsole(combination, metrics);
		}
		if (csv) {
			printStatsToCSV(combination, metrics);
		}
	}
	
	private void printStatsToConsole(String combination, double[] metrics) {
		String format = "%s,\t\t%f,\t%f,\t\t%f,\t\t\t%f,\t\t\t\t%f,\t\t\t%f,\t\t%f,\t%f,\t%f,\t%f,\t%f,\t%f\n";
		System.out.printf(format, combination, metrics[0], metrics[1], metrics[2], metrics[3], metrics[4], metrics[5], 
						  metrics[6], metrics[7], metrics[8], metrics[9], metrics[10], metrics[11]);
	}
	
	private void printStatsToCSV(String combination, double[] metrics) {
		String format = "%s, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f,\n";
		csvLinesToWrite.add((String.format(format, combination, metrics[0], metrics[1], metrics[2], metrics[3], metrics[4], metrics[5], 
				  			 metrics[6], metrics[7], metrics[8], metrics[9], metrics[10], metrics[11])));
	}
	
	public String getBestMetricCombination(int metricIndex) {
		if (metricIndex < 0 || metricIndex > headers.length - 2) {
			return "------";
		}
		
		// Lowest Overkill, Fastest TTK, and Breakpoints should all be lowest-possible values
		Integer[] indexesThatShouldUseLessThan = new Integer[] {11, 12, 13};
		boolean comparatorShouldBeLessThan = new HashSet<Integer>(Arrays.asList(indexesThatShouldUseLessThan)).contains(metricIndex);
		
		String bestCombination = "------";
		double bestValue, currentValue;
		// To the best of my knowledge, none of these values goes above 200k, so setting the starting "best" value at 1 million should automatically make the first combination tried the new best
		if (comparatorShouldBeLessThan) {
			bestValue = 1000000;
		}
		else {
			bestValue = -1000000;
		}
		
		// The overclocks are the outermost loop because they should change last, and tier 1 is the innermost loop since it should change first.
		for (int oc = -1; oc < weaponToTest.getOverclocks().length; oc++) {
			weaponToTest.setSelectedOverclock(oc, false);
			
			for (int t5 = -1; t5 < weaponToTest.getModsAtTier(5).length; t5++) {
				weaponToTest.setSelectedModAtTier(5, t5, false);
				
				for (int t4 = -1; t4 < weaponToTest.getModsAtTier(4).length; t4++) {
					weaponToTest.setSelectedModAtTier(4, t4, false);
					
					for (int t3 = -1; t3 < weaponToTest.getModsAtTier(3).length; t3++) {
						weaponToTest.setSelectedModAtTier(3, t3, false);
						
						for (int t2 = -1; t2 < weaponToTest.getModsAtTier(2).length; t2++) {
							weaponToTest.setSelectedModAtTier(2, t2, false);
							
							for (int t1 = -1; t1 < weaponToTest.getModsAtTier(1).length; t1++) {
								weaponToTest.setSelectedModAtTier(1, t1, false);
								
								switch (metricIndex) {
									case 0:{
										currentValue = weaponToTest.calculateIdealBurstDPS();
										break;
									}
									case 1:{
										currentValue = weaponToTest.calculateIdealSustainedDPS();
										break;
									}
									case 2:{
										currentValue = weaponToTest.sustainedWeakpointDPS();
										break;
									}
									case 3:{
										currentValue = weaponToTest.sustainedWeakpointAccuracyDPS();
										break;
									}
									case 4:{
										currentValue = weaponToTest.calculateAdditionalTargetDPS();
										break;
									}
									case 5:{
										currentValue = weaponToTest.calculateMaxNumTargets();
										break;
									}
									case 6:{
										currentValue = weaponToTest.calculateMaxMultiTargetDamage();
										break;
									}
									case 7:{
										currentValue = weaponToTest.ammoEfficiency();
										break;
									}
									case 8:{
										currentValue = weaponToTest.estimatedAccuracy(false);
										break;
									}
									case 9:{
										currentValue = weaponToTest.estimatedAccuracy(true);
										break;
									}
									case 10:{
										currentValue = weaponToTest.calculateFiringDuration();
										break;
									}
									case 11:{
										currentValue = weaponToTest.averageOverkill();
										break;
									}
									case 12:{
										currentValue = weaponToTest.averageTimeToKill();
										break;
									}
									case 13:{
										currentValue = weaponToTest.breakpoints();
										break;
									}
									case 14:{
										currentValue = weaponToTest.utilityScore();
										break;
									}
									default: {
										currentValue = 0;
										break;
									}
								}
								
								if (comparatorShouldBeLessThan) {
									if (currentValue < bestValue) {
										bestCombination = weaponToTest.getCombination();
										bestValue = currentValue;
									}
								}
								else {
									if (currentValue > bestValue) {
										bestCombination = weaponToTest.getCombination();
										bestValue = currentValue;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return bestCombination;
	}
	
	// This method is currently used only for finding best builds using a pre-selected partial build from the GUI, but it would have also been used for multi-threading if I hadn't fixed that "many times updating GUI" bug last patch.
	public String getBestMetricCombination(int metricIndex, int[] tier1Subset, int[] tier2Subset, int[] tier3Subset, int[] tier4Subset, int[] tier5Subset, int[] overclocksSubset) {
		if (metricIndex < 0 || metricIndex > headers.length - 2) {
			return "------";
		}
		
		if (tier1Subset.length != 2 || tier2Subset.length != 2 || tier3Subset.length != 2 || tier4Subset.length != 2 || tier5Subset.length != 2 || overclocksSubset.length != 2) {
			return "------";
		}
		
		if (tier1Subset[0] > tier1Subset[1] || tier1Subset[0] < -1 || tier1Subset[0] > weaponToTest.getModsAtTier(1).length - 1 || tier1Subset[1] < -1 || tier1Subset[1] > weaponToTest.getModsAtTier(1).length - 1) {
			return "------";
		}
		if (tier2Subset[0] > tier2Subset[1] || tier2Subset[0] < -1 || tier2Subset[0] > weaponToTest.getModsAtTier(2).length - 1 || tier2Subset[1] < -1 || tier2Subset[1] > weaponToTest.getModsAtTier(2).length - 1) {
			return "------";
		}
		if (tier3Subset[0] > tier3Subset[1] || tier3Subset[0] < -1 || tier3Subset[0] > weaponToTest.getModsAtTier(3).length - 1 || tier3Subset[1] < -1 || tier3Subset[1] > weaponToTest.getModsAtTier(3).length - 1) {
			return "------";
		}
		if (tier4Subset[0] > tier4Subset[1] || tier4Subset[0] < -1 || tier4Subset[0] > weaponToTest.getModsAtTier(4).length - 1 || tier4Subset[1] < -1 || tier4Subset[1] > weaponToTest.getModsAtTier(4).length - 1) {
			return "------";
		}
		if (tier5Subset[0] > tier5Subset[1] || tier5Subset[0] < -1 || tier5Subset[0] > weaponToTest.getModsAtTier(5).length - 1 || tier5Subset[1] < -1 || tier5Subset[1] > weaponToTest.getModsAtTier(5).length - 1) {
			return "------";
		}
		if (overclocksSubset[0] > overclocksSubset[1] || overclocksSubset[0] < -1 || overclocksSubset[0] > weaponToTest.getOverclocks().length - 1 || overclocksSubset[1] < -1 || overclocksSubset[1] > weaponToTest.getOverclocks().length - 1) {
			return "------";
		}
		
		// Set these boolean values once instead of evaluating tier 1 about 3000 times
		boolean onlyOneTier1 = tier1Subset[0] == tier1Subset[1];
		boolean onlyOneTier2 = tier2Subset[0] == tier2Subset[1];
		boolean onlyOneTier3 = tier3Subset[0] == tier3Subset[1];
		boolean onlyOneTier4 = tier4Subset[0] == tier4Subset[1];
		boolean onlyOneTier5 = tier5Subset[0] == tier5Subset[1];
		boolean onlyOneOC = overclocksSubset[0] == overclocksSubset[1];
		
		/*
			This is important: because the current Weapon ALREADY has the wanted partial combination pre-selected when the menu for "Best Metric" gets called,
			DO NOT, I repeat, DO NOT set the mod or overclock again, because that just un-sets it.
		*/
		
		// Lowest Overkill, Fastest TTK, and Breakpoints should all be lowest-possible values
		Integer[] indexesThatShouldUseLessThan = new Integer[] {11, 12, 13};
		boolean comparatorShouldBeLessThan = new HashSet<Integer>(Arrays.asList(indexesThatShouldUseLessThan)).contains(metricIndex);
		
		String bestCombination = "------";
		double bestValue, currentValue;
		// To the best of my knowledge, none of these values goes above 200k, so setting the starting "best" value at 1 million should automatically make the first combination tried the new best
		if (comparatorShouldBeLessThan) {
			bestValue = 1000000;
		}
		else {
			bestValue = -1000000;
		}
		
		// The overclocks are the outermost loop because they should change last, and tier 1 is the innermost loop since it should change first.
		for (int oc = overclocksSubset[0]; oc <= overclocksSubset[1]; oc++) {
			if (!onlyOneOC) {
				weaponToTest.setSelectedOverclock(oc, false);
			}
			
			for (int t5 = tier5Subset[0]; t5 <= tier5Subset[1]; t5++) {
				if (!onlyOneTier5) {
					weaponToTest.setSelectedModAtTier(5, t5, false);
				}
				
				for (int t4 = tier4Subset[0]; t4 <= tier4Subset[1]; t4++) {
					if (!onlyOneTier4) {
						weaponToTest.setSelectedModAtTier(4, t4, false);
					}
					
					for (int t3 = tier3Subset[0]; t3 <= tier3Subset[1]; t3++) {
						if (!onlyOneTier3) {
							weaponToTest.setSelectedModAtTier(3, t3, false);
						}
						
						for (int t2 = tier2Subset[0]; t2 <= tier2Subset[1]; t2++) {
							if (!onlyOneTier2) {
								weaponToTest.setSelectedModAtTier(2, t2, false);
							}
							
							for (int t1 = tier1Subset[0]; t1 <= tier1Subset[1]; t1++) {
								if (!onlyOneTier1) {
									weaponToTest.setSelectedModAtTier(1, t1, false);
								}
								
								switch (metricIndex) {
									case 0:{
										currentValue = weaponToTest.calculateIdealBurstDPS();
										break;
									}
									case 1:{
										currentValue = weaponToTest.calculateIdealSustainedDPS();
										break;
									}
									case 2:{
										currentValue = weaponToTest.sustainedWeakpointDPS();
										break;
									}
									case 3:{
										currentValue = weaponToTest.sustainedWeakpointAccuracyDPS();
										break;
									}
									case 4:{
										currentValue = weaponToTest.calculateAdditionalTargetDPS();
										break;
									}
									case 5:{
										currentValue = weaponToTest.calculateMaxNumTargets();
										break;
									}
									case 6:{
										currentValue = weaponToTest.calculateMaxMultiTargetDamage();
										break;
									}
									case 7:{
										currentValue = weaponToTest.ammoEfficiency();
										break;
									}
									case 8:{
										currentValue = weaponToTest.estimatedAccuracy(false);
										break;
									}
									case 9:{
										currentValue = weaponToTest.estimatedAccuracy(true);
										break;
									}
									case 10:{
										currentValue = weaponToTest.calculateFiringDuration();
										break;
									}
									case 11:{
										currentValue = weaponToTest.averageOverkill();
										break;
									}
									case 12:{
										currentValue = weaponToTest.averageTimeToKill();
										break;
									}
									case 13:{
										currentValue = weaponToTest.breakpoints();
										break;
									}
									case 14:{
										currentValue = weaponToTest.utilityScore();
										break;
									}
									default: {
										currentValue = 0;
										break;
									}
								}
								
								if (comparatorShouldBeLessThan) {
									if (currentValue < bestValue) {
										bestCombination = weaponToTest.getCombination();
										bestValue = currentValue;
									}
								}
								else {
									if (currentValue > bestValue) {
										bestCombination = weaponToTest.getCombination();
										bestValue = currentValue;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return bestCombination;
	}
}

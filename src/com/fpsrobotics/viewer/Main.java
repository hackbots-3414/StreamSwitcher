package com.fpsrobotics.viewer;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxDriver;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Main {
	public static void main(String[] args) {
		AutoRunFromConsole.runYourselfInConsole(true);
		new Main().run();
	}

	/*
	 * To update the stream: cameratable table update "target" with url update
	 * "newAddress" to true every time you make an update.
	 *  you also need to set webdriver.gecko.driver to wherever your geckodriver.exe is
	 */

	public void run() {
		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("cameratable");

		NetworkTableEntry target = table.getEntry("target");
		NetworkTableEntry newAddress = table.getEntry("newAddress");
		// inst.startClientTeam(3414); // where TEAM=190, 294, etc, or use
		// inst.startClient("hostname") or similar
		inst.startDSClient(); // recommended if running on DS computer; this gets the robot IP from the DS
		inst.startServer();
		//System.setProperty("webdriver.gecko.driver", // "~\\lib\\geckdriver.exe\\");
		//		"C:\\Users\\Jon\\Desktop\\Workspace\\CameraView2\\lib\\geckodriver.exe"); //Need to either set this manually or find a way to make it relative
		FirefoxDriver driver = new FirefoxDriver();
		driver.navigate().to("http://google.com");
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				System.out.println("interrupted");
				return;
			}
			if (newAddress.getBoolean(false)) {
				newAddress.setBoolean(true);
				System.out.println(target.getString("http://google.com"));
				driver.navigate().to(target.getString("http://google.com"));
			}
			if (!inst.isConnected()) {
				inst.startDSClient();
			}
			// driver.navigate().to(target.getString("reddit.com"));

		}
	}
}

/*
 * Old code System.setProperty("webdriver.gecko.driver",
 * "C:\\Users\\Jon\\Desktop\\Workspace\\CameraView2\\lib\\geckodriver.exe");
 * FirefoxDriver driver = new FirefoxDriver(); while (true) { try {
 * Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
 * if (System.currentTimeMillis() - start > 5000) { start =
 * System.currentTimeMillis(); if
 * (!driver.getCurrentUrl().equalsIgnoreCase("http://google.com")) {
 * driver.navigate().to("http://google.com"); } } else { if
 * (!driver.getCurrentUrl().equalsIgnoreCase("http://stackoverflow.com")) {
 * driver.navigate().to("http://stackoverflow.com"); } } }
 */

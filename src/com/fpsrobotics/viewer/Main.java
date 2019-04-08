package com.fpsrobotics.viewer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Scanner;

import org.openqa.selenium.chrome.ChromeDriver;

//import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTable; //2 of these exist
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Main {
	public static void main(String[] args) {
		
		AutoRunFromConsole.runYourselfInConsole(true);
		// new Main().run();
		config();
		run();
	}

	/*
	 * To update the stream: cameratable table update "target" with url update
	 * "newAddress" to true every time you make an update. you also need to set
	 * webdriver.gecko.driver to wherever your geckodriver.exe is
	 */
	static int tries = 0;
	static boolean escapeKey = false;
	static String driverPath = "";
	public static void config() {
		
		Properties saveProps = new Properties();
		try {
			saveProps.loadFromXML(new FileInputStream("settings.xml"));
		} catch (InvalidPropertiesFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			saveProps.setProperty("driver location", "default");
		    try {
				saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		    try {
				saveProps.loadFromXML(new FileInputStream("settings.xml"));
			} catch (InvalidPropertiesFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			driverPath = saveProps.getProperty("driver location");
		}
	
	
	public static void run() {
		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("cameratable");

		NetworkTableEntry target = table.getEntry("target");
		NetworkTableEntry newAddress = table.getEntry("newAddress");
		// inst.startClientTeam(3414); // where TEAM=190, 294, etc, or use
		// inst.startClient("hostname") or similar
		inst.startDSClient(); // recommended if running on DS computer; this gets the robot IP from the DS
		
		System.setProperty("webdriver.gecko.driver", // "~\\lib\\geckdriver.exe\\");
				"C:\\Users\\Jon\\Desktop\\Workspace\\CameraView2\\lib\\geckodriver.exe"); //Need to either set this manually or find a way to make it relative
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\FPSVaysmanJ00\\git\\StreamSwitcher\\lib\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", driverPath);
		//FirefoxDriver driver = new FirefoxDriver();
		ChromeDriver driver = new ChromeDriver();
		driver.navigate().to("http://fpsrobotics.com");
		while (!escapeKey) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				System.out.println("interrupted");
				return;
			}
			if (newAddress.getBoolean(false)) {
				newAddress.setBoolean(false);
				System.out.println("Navigating to "+target.getString("http://fpsrobotics.com")+".");
				driver.navigate().to(target.getString("http://fpsrobotics.com"));
			}
			if (!inst.isConnected()) {
				tries++;
				System.out.println("No DS found, this is attempt #"+tries+".");
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

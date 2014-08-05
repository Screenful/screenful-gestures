package screenful.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Settings class for GestureServer. Reads and writes a configuration file and
 * allows accessing the fields via a Properties object.
 */
public class Settings {

    String filename;
    Properties prop;

    /**
     * Default values
     */
    private final String defaultPort = "8887";
    private final String defaultAddress = "localhost";
    private final String defaultStartDelay = "800";
    private final String defaultTravelDistance = "10";
    private final String defaultTravelFrames = "10";
    private final String defaultCooldown = "500";

    /**
     * Create default settings.
     */
    public Settings() {
        this.filename = "default.conf";
        prop = new Properties();
        prop.setProperty("port", defaultPort);
        prop.setProperty("address", defaultAddress);
        prop.setProperty("startdelay", defaultStartDelay);
        prop.setProperty("traveldistance", defaultTravelDistance);
        prop.setProperty("travelframes", defaultTravelFrames);
        prop.setProperty("cooldown", defaultCooldown);
    }

    /**
     * If some settings are not defined, assign default values to them.
     */
    private void fillBlanks() {
        prop.setProperty("port", prop.getProperty("port", defaultPort));
        prop.setProperty("address", prop.getProperty("address", defaultAddress));
        prop.setProperty("startdelay", prop.getProperty("startdelay", defaultStartDelay));
        prop.setProperty("traveldistance", prop.getProperty("traveldistance", defaultTravelDistance));
        prop.setProperty("travelframes", prop.getProperty("travelframes", defaultTravelFrames));
        prop.setProperty("cooldown", prop.getProperty("cooldown", defaultCooldown));
    }

    /**
     * Load settings from a file.
     *
     * @param filename configuration filename
     */
    public Settings(String filename) {
        prop = new Properties();
        this.filename = filename;

        InputStream input = null;
        try {
            input = new FileInputStream(filename);
            prop.load(input);
            fillBlanks();
        } catch (FileNotFoundException ex) {
            System.out.println("Settings file " + filename + " not found.");
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error loading " + filename + ".");
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    System.out.println("Error closing " + filename + ".");
                    Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Save current settings (comments will be discarded)
     */
    public void save() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(filename);
            prop.store(output, null);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found while saving " + filename);
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("IO Exception while saving " + filename);
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

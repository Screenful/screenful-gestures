package screenful.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * Settings class for GestureServer. Reads and writes a configuration file and
 * allows accessing the fields via a Properties object.
 */
public class Settings {

    String filename;
    Properties prop;
    HashSet<CardinalDirection> enabledDirections;
    HashSet<CardinalDirection> exitDirections;

    /**
     * Default values
     */
    private final String defaultPort = "8887";
    private final String defaultAddress = "localhost";
    private final String defaultStartDelay = "800";
    private final String defaultTravelDistance = "10";
    private final String defaultTravelFrames = "5";
    private final String defaultCooldown = "500";
    private final String defaultExitDirections = "out";
    private final String defaultEnabledDirections = "left,right";

    /**
     * Create default settings.
     */
    public Settings() {
        enabledDirections = new HashSet<>();
        exitDirections = new HashSet<>();
        this.filename = "default.conf";
        prop = new Properties();
        fillBlanks();
    }

    /**
     * Load settings from a file.
     *
     * @param filename configuration filename
     */
    public Settings(String filename) {
        this();
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
     * If some settings are not defined, assign default values to them.
     */
    private void fillBlanks() {

        prop.setProperty("port", prop.getProperty("port", defaultPort));
        prop.setProperty("address", prop.getProperty("address", defaultAddress));
        prop.setProperty("startdelay", prop.getProperty("startdelay", defaultStartDelay));
        prop.setProperty("traveldistance", prop.getProperty("traveldistance", defaultTravelDistance));
        prop.setProperty("travelframes", prop.getProperty("travelframes", defaultTravelFrames));
        prop.setProperty("cooldown", prop.getProperty("cooldown", defaultCooldown));
        prop.setProperty("exitdirections", prop.getProperty("exitdirections", defaultExitDirections));
        prop.setProperty("enableddirections", prop.getProperty("enableddirections", defaultEnabledDirections));

        parseDirections(prop.getProperty("exitdirections"), prop.getProperty("enableddirections"));
    }

    /**
     * Parse comma limited strings to populate exit and enabled directions
     * lists.
     *
     * @param exitdirections comma limited string of exit directions, e.g. "out"
     * @param enableddirections comma limited string of enabled directions, e.g.
     * "left,right,up"
     */
    private void parseDirections(String exitdirections, String enableddirections) {
        String[] exittokens = exitdirections.split("[,]+");
        String[] directiontokens = enableddirections.split("[,]+");
        if (exittokens.length > 0) {
            for (String token : exittokens) {
                exitDirections.add(CardinalDirection.valueOf(token.toUpperCase()));
            }
        }
        if (directiontokens.length > 0) {
            for (String token : directiontokens) {
                enabledDirections.add(CardinalDirection.valueOf(token.toUpperCase()));
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

package screenful.server;

import com.primesense.nite.GestureType;
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
import screenful.basic.TrackerSettings;
import screenful.gestures.Utilities.CardinalDirection;

/**
 * ServerSettings class for GestureServer. Reads and writes a configuration file
 * and allows accessing the fields via a Properties object. Clunky and
 * convoluted unfortunately.
 */
public class ServerSettings {

    String filename;
    Properties prop;
    int port;
    String address;
    TrackerSettings trackersettings;
    GestureSettings gesturesettings;

    /**
     * Default values (as strings for writing a configuration file)
     */
    private final String defaultPort = "8887";
    private final String defaultAddress = "localhost";
    private final String defaultStartDelay = "800";
    private final String defaultTravelDistance = "10";
    private final String defaultTravelFrames = "5";
    private final String defaultCooldown = "500";
    private final String defaultExitDirections = "out";
    private final String defaultEnabledDirections = "left,right";
    private final String defaultStartGestures = "click,wave";

    /**
     * Create default settings.
     */
    public ServerSettings() {
        this.filename = "default.conf";
        prop = new Properties();
        parseSettingsAndFillBlanks();
    }

    /**
     * Load settings from a file.
     *
     * @param filename configuration filename
     */
    public ServerSettings(String filename) {
        this();
        this.filename = filename;

        InputStream input = null;
        try {
            System.out.println("Loading settings file " + filename);
            input = new FileInputStream(filename);
            prop.load(input);
            parseSettingsAndFillBlanks();

        } catch (FileNotFoundException ex) {
            System.out.println("Settings file " + filename + " not found.");
            Logger.getLogger(ServerSettings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error loading " + filename + ".");
            Logger.getLogger(ServerSettings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    System.out.println("Error closing " + filename + ".");
                    Logger.getLogger(ServerSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Set a default value for simple parameters.
     *
     * @param setting property name to initialize
     */
    private void setValueOrDefault(String setting) {
        String defaultValue = "";
        switch (setting.toLowerCase()) {
            case "port":
                defaultValue = defaultPort;
                break;
            case "address":
                defaultValue = defaultAddress;
                break;
            case "startdelay":
                defaultValue = defaultStartDelay;
                break;
            case "traveldistance":
                defaultValue = defaultTravelDistance;
                break;
            case "travelframes":
                defaultValue = defaultTravelFrames;
                break;
            case "cooldown":
                defaultValue = defaultCooldown;
                break;

        }
        // if property name was found, set its default value
        if (!defaultValue.isEmpty()) {
            prop.setProperty(setting, prop.getProperty(setting, defaultValue).toLowerCase());
        }
    }

    /**
     * If some settings are not defined, assign default values to them.
     */
    private void parseSettingsAndFillBlanks() {
        setValueOrDefault("port");
        setValueOrDefault("address");
        setValueOrDefault("startdelay");
        setValueOrDefault("traveldistance");
        setValueOrDefault("travelframes");
        setValueOrDefault("cooldown");

        // if exit directions or enabled directions is "none", don't assign the default values
        String exits = prop.getProperty("exitdirections");
        if (exits != null && exits.equals("none")) {
            prop.setProperty("exitdirections", "none");
        } else {
            prop.setProperty("exitdirections", prop.getProperty("exitdirections", defaultExitDirections));
        }
        String enabled = prop.getProperty("enableddirections");
        if (enabled != null && enabled.equals("none")) {
            prop.setProperty("enableddirections", "none");
        } else {
            prop.setProperty("enableddirections", prop.getProperty("enableddirections", defaultEnabledDirections));
        }
        // read gestures used for starting hand tracking ("click" and/or "wave" or "none")
        String gestures = prop.getProperty("startgestures");
        if (gestures != null && gestures.equals("none")) {
            prop.setProperty("startgestures", "none");
        } else {
            prop.setProperty("startgestures", prop.getProperty("startgestures", defaultStartGestures));
        }

        // parse settings into tracker and gesture settings objects
        this.port = Integer.parseInt(prop.getProperty("port"));
        this.address = prop.getProperty("address");
        this.gesturesettings = new GestureSettings();
        this.gesturesettings.startDelay = Integer.parseInt(prop.getProperty("startdelay"));
        this.gesturesettings.travelDistance = Integer.parseInt(prop.getProperty("traveldistance"));
        this.gesturesettings.travelFrames = Integer.parseInt(prop.getProperty("travelframes"));
        this.gesturesettings.cooldown = Integer.parseInt(prop.getProperty("cooldown"));
        this.gesturesettings.exitDirections = parseDirections(prop.getProperty("exitdirections"));
        this.gesturesettings.enabledDirections = parseDirections(prop.getProperty("enableddirections"));
        String[] starts = prop.getProperty("startgestures").split("[,]+");
        trackersettings = new TrackerSettings();
        for (String gesture : starts) {
            if (!gesture.equals("none")) {
                trackersettings.startGestures.add(GestureType.valueOf(gesture.toUpperCase()));
            }
        }

    }

    /**
     * Parse a comma-delimited list of directions and return a HashSet of them.
     *
     * @param directions string to parse
     * @return HashSet of CardinalDirections found
     */
    private HashSet<CardinalDirection> parseDirections(String directions) {
        String[] tokens = directions.split("[,]+");

        HashSet<CardinalDirection> dirs = new HashSet<>();
        if (tokens.length > 0) {
            for (String token : tokens) {
                if (!token.isEmpty() && !token.equals("none")) {
                    dirs.add(CardinalDirection.valueOf(token.toUpperCase()));
                }
            }
        }
        return dirs;
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
            Logger.getLogger(ServerSettings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("IO Exception while saving " + filename);
            Logger.getLogger(ServerSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

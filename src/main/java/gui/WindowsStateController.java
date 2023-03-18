package gui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WindowsStateController {
    static void saveStates(List<JInternalFrame> frames) {
        Map<String, Map<String, Object>> frameInfo = new HashMap<>();
        for (JInternalFrame frame : frames) {
            HashMap<String, Object> frameStateInfo = new HashMap<>();
            frameStateInfo.put("width", frame.getWidth());
            frameStateInfo.put("height", frame.getHeight());
            frameStateInfo.put("x", frame.getX());
            frameStateInfo.put("y", frame.getY());
            frameStateInfo.put("isMaximized", frame.isMaximum());
            frameStateInfo.put("isIcon", frame.isIcon());
            frameInfo.put(frame.getTitle(), frameStateInfo);
        }
        saveStateToFile(frameInfo);
    }

    private static void saveStateToFile(Map<String, Map<String, Object>> windowInfo) {
        JSONObject json = new JSONObject(windowInfo);
        try (FileWriter fileWriter = new FileWriter("save.json")) {
            json.writeJSONString(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Map<String, Object>> restoreStateFromFile(File json) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader fileReader = new FileReader(json)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);
            return new HashMap<String, Map<String, Object>>(jsonObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void resetState(List<JInternalFrame> windows) {
        File jsonStatesFile = new File("save.json");
        Map<String, Map<String, Object>> states = new HashMap<>();
        if (jsonStatesFile.exists() && !jsonStatesFile.isDirectory()) {
            states = restoreStateFromFile(jsonStatesFile);
        }

        if (!states.isEmpty()) {
            for (JInternalFrame frame : windows) {
                if (states.containsKey(frame.getTitle())) {
                    Map<String, Object> currentState = states.get(frame.getTitle());
                    frame.setLocation((int) (long) currentState.get("x"), (int) (long) currentState.get("y"));
                    frame.setSize((int) (long) currentState.get("width"), (int) (long) currentState.get("height"));
                    try {
                        frame.setMaximum((boolean) currentState.get("isMaximized"));
                        frame.setIcon((boolean) currentState.get("isIcon"));
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
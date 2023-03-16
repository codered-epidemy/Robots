package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Menu {
    private JMenu menu;

    public Menu(String s, String accessDescription, Integer menuKeyEvent){
        menu = new JMenu(s);
        menu.setMnemonic(menuKeyEvent);
        menu.getAccessibleContext().setAccessibleDescription(accessDescription);
    }

    public void createMenuItem(String menuItemText, ActionListener actionListener, Integer itemKeyEvent){
        JMenuItem menuItem = new JMenuItem(menuItemText, itemKeyEvent);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    public JMenu getMenu(){
        return menu;
    }
}

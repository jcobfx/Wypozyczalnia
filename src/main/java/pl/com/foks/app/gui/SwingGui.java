package pl.com.foks.app.gui;

import pl.com.foks.app.App;
import pl.com.foks.app.gui.panel.LoginPanel;

import javax.swing.*;
import java.awt.*;

public class SwingGui extends JFrame {
    private final JPanel mainPanel;
    private final LoginPanel loginPanel;
    private final App app;

    public SwingGui(App app) {
        this.app = app;

        int width = 400;
        int height = 400;
        String title = "Car Rental";

        setSize(width, height);
        setTitle(title);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardLayout layout = new CardLayout();
        mainPanel = new JPanel(layout);
        add(mainPanel);

        loginPanel = new LoginPanel(getSize(),
                (authData) -> app.login(authData.getLogin(), authData.getPassword()));
        mainPanel.add(loginPanel, "loginPanel");

        layout.show(mainPanel, "loginPanel");
        setVisible(true);
    }

    public void showMainPanel() {
        // TODO
    }

    public void showInfo(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}

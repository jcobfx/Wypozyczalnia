package pl.com.foks.app.gui.panel;

import pl.com.foks.domain.AuthService;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoginPanel extends AbstractPanel {
    private String login;
    private String password;

    public LoginPanel(Dimension dimension, Consumer<AuthService.AuthData> handleLogin) {
        super(dimension);
        setBackground(Color.darkGray);
        setLayout(new GridLayout(3, 1));

        JTextField loginField = new JTextField(10);
        loginField.setToolTipText("Login");
        add(loginField);

        JPasswordField passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');
        passwordField.setToolTipText("Password");
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            login = loginField.getText();
            password = new String(passwordField.getPassword());
            if (validateCredentials()) {
                handleLogin.accept(AuthService.AuthData.of(login, password));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(loginButton);
    }

    private boolean validateCredentials() {
        return login.matches("[a-zA-Z0-9]*") && password.matches("[a-zA-Z0-9]*");
    }
}

package pl.com.foks.app;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import pl.com.foks.app.gui.SwingGui;
import pl.com.foks.domain.*;

@AllArgsConstructor
@Log
public class App {
    private final SwingGui swingGui = new SwingGui(this);

    private final RentalService rentalService;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final AuthService authService;

    public void run() {
        log.info("App is running");
    }

    public void login(String login, String password) {
        if (authService.login(AuthService.AuthData.of(login, password))) {
            swingGui.showInfo("Logged in successfully", "Success");
            swingGui.showMainPanel();
        } else {
            swingGui.showError("Invalid login or password", "Error");
        }
    }

    public void register(String login, String password) {
        authService.register(AuthService.AuthData.of(login, password));
    }
}

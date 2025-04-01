package pl.com.foks.app.gui.panel;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractPanel extends JPanel {
    public AbstractPanel(Dimension dimension) {
        setSize(dimension);
    }
}

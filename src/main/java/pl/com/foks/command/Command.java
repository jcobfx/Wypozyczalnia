package pl.com.foks.command;

import java.util.function.Consumer;

public abstract class Command implements Consumer<String[]> {
    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

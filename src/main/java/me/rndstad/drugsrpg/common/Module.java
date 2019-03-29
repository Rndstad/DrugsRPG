package me.rndstad.drugsrpg.common;

public abstract class Module {
    private final String name;

    public Module(String name) {
        this.name = name;
        System.out.println("Loading " + name);
    }

    public String getName() {
        return this.name;
    }

    public void disable() {
    }
}


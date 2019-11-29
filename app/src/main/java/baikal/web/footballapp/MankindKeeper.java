package baikal.web.footballapp;

import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.model.Person;

public class MankindKeeper {
    private static final MankindKeeper ourInstance = new MankindKeeper();

    public static MankindKeeper getInstance() {
        return ourInstance;
    }

    public TreeMap<String, Person> allPlayers;

    private MankindKeeper() {
        allPlayers = new TreeMap<>();
    }
}

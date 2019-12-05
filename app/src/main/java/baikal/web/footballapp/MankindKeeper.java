package baikal.web.footballapp;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Tourney;

public class MankindKeeper {
    private static final MankindKeeper ourInstance = new MankindKeeper();

    public static MankindKeeper getInstance() {
        return ourInstance;
    }

    public TreeMap<String, Person> allPlayers = new TreeMap<>();

    public List<League> allLeagues = new ArrayList<>();
    public List<Club> allClubs = new ArrayList<>();
    public List<Tourney> allTourneys = new ArrayList<>();
    public List<Region> regions = new ArrayList<>();

    private MankindKeeper() { }

    public void updateClub (Club club) {
        for (int i=0; i<allClubs.size(); i++)
            if (allClubs.get(i).getId().equals(club.getId()))
                allClubs.set(i, club);
    }
}

package baikal.web.footballapp;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.PersonStatus;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;

public class MankindKeeper {
    private static final MankindKeeper ourInstance = new MankindKeeper();

    public static MankindKeeper getInstance() {
        return ourInstance;
    }

    public final TreeMap<String, Person> allPerson;
    private final TreeMap<String, Team> allTeamsTree;
    public final List<League>   allLeagues ;
    public final List<Club>     allClubs   ;
    public final List<Tourney>  allTourneys;
    public final List<Region>   regions    ;
    public final List<String>   allTeams   ;
    public final List<PersonStatus> allPersonStatus;

    private MankindKeeper() {
        allPersonStatus = new ArrayList<>();
        allPerson    = new TreeMap<>();
        allTeamsTree = new TreeMap<>();
        allLeagues   = new ArrayList<>();
        allClubs     = new ArrayList<>();
        allTourneys  = new ArrayList<>();
        regions      = new ArrayList<>();
        allTeams     = new ArrayList<>();
    }

    public void updateClub (Club club) {
        for (int i=0; i<allClubs.size(); i++)
            if (allClubs.get(i).getId().equals(club.getId()))
                allClubs.set(i, club);
    }

    public Team getTeamById (String id)
    {
        if (allTeamsTree.containsKey(id) && allTeamsTree.get(id) != null)
            return allTeamsTree.get(id);

        return null;
    }

    public void addTeam (Team team)
    {
        allTeamsTree.put(team.getId(), team);
        allTeams.add(team.getId());
    }

    public Person getPersonById(String id)
    {
        if (allPerson.containsKey(id) && allPerson.get(id) != null)
            return allPerson.get(id);

        return null;
    }

    public void addPerson (Person person) {
        allPerson.put(person.get_id(), person);
    }
}

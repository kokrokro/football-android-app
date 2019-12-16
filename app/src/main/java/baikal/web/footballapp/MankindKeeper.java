package baikal.web.footballapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import baikal.web.footballapp.model.Club;
import baikal.web.footballapp.model.League;
import baikal.web.footballapp.model.Person;
import baikal.web.footballapp.model.Region;
import baikal.web.footballapp.model.Team;
import baikal.web.footballapp.model.Tourney;

public class MankindKeeper {
    private static final MankindKeeper ourInstance = new MankindKeeper();

    public static MankindKeeper getInstance() {
        return ourInstance;
    }

    public TreeMap<String, Person> allPerson = new TreeMap<>();

    private TreeMap<String, Team> allTeamsTree = new TreeMap<>();

    public List<League> allLeagues = new ArrayList<>();
    public List<Club> allClubs = new ArrayList<>();
    public List<Tourney> allTourneys = new ArrayList<>();
    public List<Region> regions = new ArrayList<>();
    public List<String> allTeams = new ArrayList<>();

    private MankindKeeper() { }

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

    public ArrayList<Person> getNPersons (int n) {
        Set<String> keys = allPerson.keySet();
        ArrayList<Person> ans = new ArrayList<>();

        int k=0;
        for (String key: keys) {
            if (k==n)
                break;
            ans.add(allPerson.get(key));
            k++;
        }

        return ans;
    }
}

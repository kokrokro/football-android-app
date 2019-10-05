package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Team;

import java.util.Comparator;

public class GroupTeamPlaceComparator implements Comparator<Team>
{
    public int compare(Team o1, Team o2)
    {
            return o1.getGroupScore() >= o2.getGroupScore() ? o1.getGroupScore() : o2.getGroupScore();
    }
}
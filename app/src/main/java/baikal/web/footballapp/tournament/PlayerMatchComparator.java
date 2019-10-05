package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Player;

import java.util.Comparator;

public class PlayerMatchComparator implements Comparator<Player>
{
    public int compare(Player o1, Player o2)
    {
            return o2.getMatches().compareTo(o1.getMatches());
    }
}
package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Player;

import java.util.Comparator;

public class PlayerRCComparator implements Comparator<Player>
{
    public int compare(Player o1, Player o2)
    {
        return o2.getRedCards().compareTo(o1.getRedCards());
    }
}

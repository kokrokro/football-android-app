package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Player;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player>
{
    public int compare(Player o1, Player o2)
    {
        if (o1.getDisquals().equals(0) && o2.getDisquals().equals(0)){
            return o2.getActiveYellowCards().compareTo(o1.getActiveYellowCards());
        }
        else {
            return o2.getDisquals().compareTo(o1.getDisquals());
        }
    }
}
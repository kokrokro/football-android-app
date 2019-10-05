package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Player;

import java.util.Comparator;

public class PlayerGoalsComparator implements Comparator<Player>
    {
        public int compare(Player o1, Player o2)
        {
            return o2.getGoals().compareTo(o1.getGoals());
        }
    }

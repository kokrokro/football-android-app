package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.PersonStats;
import baikal.web.footballapp.model.Player;

import java.util.Comparator;

public class PlayerYCComparator implements Comparator<PersonStats>
{
    public int compare(PersonStats o1, PersonStats o2)
    {
        return o2.getYellowCards().compareTo(o1.getYellowCards());
    }
}

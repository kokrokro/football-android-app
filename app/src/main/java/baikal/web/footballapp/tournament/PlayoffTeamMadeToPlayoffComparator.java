package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Team;

import java.util.Comparator;

public class PlayoffTeamMadeToPlayoffComparator implements Comparator<Team> {
    public int compare(Team o1, Team o2)
    {
        Boolean count1;
        Boolean count2;
        try{
            count1 = o1.getMadeToPlayoff();
        }catch (NullPointerException e){
            count1 = true;
        }
        try{
            count2 = o2.getMadeToPlayoff();
        }catch (NullPointerException e){
            count2 = true;
        }

//        try{
//            count1 = o1.getPlace();
//            if (o1.getPlace()==null){
//                count1 = Integer.MAX_VALUE;
//            }
//        }catch (NullPointerException e){
//            count1 = 100;
//        }
//        try{
//            count2 = o2.getPlace();
//            if (o2.getPlace()==null){
//                count1 = Integer.MAX_VALUE;
//            }
//        }catch (NullPointerException e){
//            count2 = Integer.MAX_VALUE;
//        }

//        return o1.getPlace().compareTo(o2.getPlace());
//        if (count1 <= count2)
//            return o1;
//        else return o2;
        return count1.compareTo(count2);
//        return o1.getPlayoffPlace() <= o2.getPlayoffPlace() ? o1.getPlayoffPlace() : o2.getPlayoffPlace();
//        return o1.getPlace().compareTo(o2.getPlace());
    }
}

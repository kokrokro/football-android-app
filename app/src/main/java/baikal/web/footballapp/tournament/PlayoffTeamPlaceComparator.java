package baikal.web.footballapp.tournament;

import baikal.web.footballapp.model.Team;

import java.util.Comparator;

public class PlayoffTeamPlaceComparator implements Comparator<Team>
{
    public int compare(Team o1, Team o2)
    {
        int count1;
        int count2;
        try{
        count1 = o1.getPlayoffPlace();
        }catch (NullPointerException e){
            count1 = Integer.MAX_VALUE;
        }
        try{
        count2 = o2.getPlayoffPlace();
        }catch (NullPointerException e){
            count2 = Integer.MAX_VALUE;
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
        return count1 >= count2 ? count1 : count2;
//        return o1.getPlayoffPlace() <= o2.getPlayoffPlace() ? o1.getPlayoffPlace() : o2.getPlayoffPlace();
//        return o1.getPlace().compareTo(o2.getPlace());
    }
}
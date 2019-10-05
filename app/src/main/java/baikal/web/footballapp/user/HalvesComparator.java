package baikal.web.footballapp.user;

import java.util.Comparator;

class HalvesComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
}

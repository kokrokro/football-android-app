package baikal.web.footballapp;

public class CheckName {
    public String check(String surname, String name, String patronymic){
        String str = null;
        if (surname.equals("") &&  name.equals("") && patronymic.equals("")){
            str = "Не указано";
        }
        if (!surname.equals("") && name.equals("") && patronymic.equals("")){
            str = surname;
        }
        if (surname.equals("") && !name.equals("") && patronymic.equals("")){
            str = name;
        }
        if (surname.equals("") && name.equals("") && !patronymic.equals("")){
            str = patronymic;
        }
        if (!surname.equals("") && !name.equals("") && !patronymic.equals("")){
            str = surname;
            String name1 = name.substring(0, 1);
            str += " " + name1 + ".";
            name1 = patronymic.substring(0, 1);
            str += " " + name1 + ".";
        }
        if (surname.equals("") && !name.equals("") && !patronymic.equals("")){
            str = name;
        }
        if (!surname.equals("") && !name.equals("") && patronymic.equals("")){
            str = surname;
        }
        if (!surname.equals("") && name.equals("") && !patronymic.equals("")){
            str = surname;
        }
        return str;
    }
}

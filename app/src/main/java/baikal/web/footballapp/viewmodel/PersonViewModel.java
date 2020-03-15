package baikal.web.footballapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.TreeMap;

import baikal.web.footballapp.Controller;
import baikal.web.footballapp.model.Person;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonViewModel extends ViewModel {
    private TreeMap<String, Person> allPerson = new TreeMap<>();

    public interface OnPersonLoad {
        void PersonLoaded(Person person);
    }

    public Person getPersonById (String id, OnPersonLoad onPersonLoad) {
        if (id == null) return null;
        if (allPerson.get(id) == null) {
            loadOnePerson(id, onPersonLoad);
            return null;
        }

        return allPerson.get(id);
    }

    private void loadOnePerson (String id, OnPersonLoad onPersonLoad) {
        Callback<List<Person>> responseCallback = new Callback<List<Person>>() {
            @Override
            public void onResponse(@NonNull Call<List<Person>> call, @NonNull Response<List<Person>> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    Person p = response.body().get(0);
                    allPerson.put(p.getId(), p);
                    onPersonLoad.PersonLoaded(p);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Person>> call, @NonNull Throwable t) { }
        };
        Controller.getApi().getPerson(id).enqueue(responseCallback);
    }
}

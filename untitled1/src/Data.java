import java.util.ArrayList;
import java.util.List;

public class Data {
    public List<Person> getPersons (){
        List<Person> personList = new ArrayList<>();
        Person person = new Person("max",  "Fedor", 29);
        Person person2 = new Person("alex",  "Fedor", 31);
        Person person3 = new Person("vera",  "Fedor", 55);
        Person person4 = new Person("lino",  "Fedor", 33);
        Person person5 = new Person("mama",  "Fedor", 12);
        personList.add(person);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);
        personList.add(person5);
        return personList;
    }


}

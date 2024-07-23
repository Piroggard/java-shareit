import lombok.*;


public class Person implements Comparable<Person>{
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lastNAme='" + lastNAme + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public Person() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastNAme() {
        return lastNAme;
    }

    public void setLastNAme(String lastNAme) {
        this.lastNAme = lastNAme;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;

    public Person(String name, String lastNAme, int age) {
        this.name = name;
        this.lastNAme = lastNAme;
        this.age = age;
    }

    private String lastNAme;
    private int age;

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.getName());
    }
}

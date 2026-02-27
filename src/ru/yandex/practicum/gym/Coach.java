package ru.yandex.practicum.gym;

import java.util.Objects;

public class Coach {

    // фамилия
    private final String surname;
    // имя
    private final String name;
    // отчество
    private final String middleName;

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    // Нужно для корректной работы HashMap в getCountByCoaches()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(surname, coach.surname)
                && Objects.equals(name, coach.name)
                && Objects.equals(middleName, coach.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }
}
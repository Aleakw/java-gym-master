package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertSame(singleTrainingSession, mondaySessions.get(0));

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(
                groupAdult, coach, DayOfWeek.THURSDAY, new TimeOfDay(20, 0)
        );
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(
                groupChild, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );
        TrainingSession thursdayChildTrainingSession = new TrainingSession(
                groupChild, coach, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)
        );
        TrainingSession saturdayChildTrainingSession = new TrainingSession(
                groupChild, coach, DayOfWeek.SATURDAY, new TimeOfDay(10, 0)
        );

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertSame(mondayChildTrainingSession, mondaySessions.get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertSame(thursdayChildTrainingSession, thursdaySessions.get(0)); // 13:00
        assertSame(thursdayAdultTrainingSession, thursdaySessions.get(1)); // 20:00

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(
                group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> at13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)
        );
        assertEquals(1, at13.size());
        assertSame(singleTrainingSession, at13.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> at14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0)
        );
        assertTrue(at14.isEmpty());
    }

    // -------------------- 3+ теста для getCountByCoaches() --------------------

    @Test
    void testGetCountByCoaches_emptyTimetable_returnsEmptyList() {
        Timetable timetable = new Timetable();

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCountByCoaches_singleCoach_multipleTrainings_correctCount() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(1, result.size());
        assertEquals(coach, result.get(0).getCoach());
        assertEquals(3, result.get(0).getCount());
    }

    @Test
    void testGetCountByCoaches_twoCoaches_sortedByCountDesc() {
        Timetable timetable = new Timetable();

        Coach coachMore = new Coach("Петров", "Пётр", "Петрович");
        Coach coachLess = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Группа", Age.ADULT, 60);

        // coachMore = 3 занятия
        timetable.addNewTrainingSession(new TrainingSession(group, coachMore, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachMore, DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachMore, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        // coachLess = 1 занятие
        timetable.addNewTrainingSession(new TrainingSession(group, coachLess, DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));

        List<Timetable.CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());

        assertEquals(coachMore, result.get(0).getCoach());
        assertEquals(3, result.get(0).getCount());

        assertEquals(coachLess, result.get(1).getCoach());
        assertEquals(1, result.get(1).getCount());
    }
}
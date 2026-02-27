package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    // O(1) получить отсортированный список занятий за день
    private Map<DayOfWeek, List<TrainingSession>> dayCache;

    // O(1) получить занятия по дню и времени
    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> byDayAndTime;

    // сразу считаем тренировки тренеров при добавлении
    private Map<Coach, Integer> coachesCounter;

    public Timetable() {
        dayCache = new HashMap<>();
        byDayAndTime = new HashMap<>();
        coachesCounter = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            dayCache.put(day, new ArrayList<>());
            byDayAndTime.put(day, new HashMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (trainingSession == null) {
            return;
        }

        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        Coach coach = trainingSession.getCoach();

        if (day == null || time == null || coach == null) {
            return;
        }

        // 1) Добавление в список дня
        List<TrainingSession> dayList = dayCache.get(day);
        dayList.add(trainingSession);

        // сортируем по времени начала (разрешено делать при добавлении)
        Collections.sort(dayList, new Comparator<TrainingSession>() {
            @Override
            public int compare(TrainingSession a, TrainingSession b) {
                return a.getTimeOfDay().compareTo(b.getTimeOfDay());
            }
        });

        //  Добавление в map день->время->список
        Map<TimeOfDay, List<TrainingSession>> timeMap = byDayAndTime.get(day);

        List<TrainingSession> sessionsAtTime = timeMap.get(time);
        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            timeMap.put(time, sessionsAtTime);
        }
        sessionsAtTime.add(trainingSession);

        //  Сразу обновляем статистику по тренерам
        Integer count = coachesCounter.get(coach);
        if (count == null) {
            coachesCounter.put(coach, 1);
        } else {
            coachesCounter.put(coach, count + 1);
        }
    }


    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return new ArrayList<>();
        }
        return dayCache.get(dayOfWeek);
    }


    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (dayOfWeek == null || timeOfDay == null) {
            return new ArrayList<>();
        }

        Map<TimeOfDay, List<TrainingSession>> timeMap = byDayAndTime.get(dayOfWeek);
        List<TrainingSession> result = timeMap.get(timeOfDay);

        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    //  Теперь без перебора всех тренировок: берём готовую статистику
    public List<CounterOfTrainings> getCountByCoaches() {
        List<CounterOfTrainings> result = new ArrayList<>();

        for (Coach coach : coachesCounter.keySet()) {
            result.add(new CounterOfTrainings(coach, coachesCounter.get(coach)));
        }

        // сортировка по убыванию количества
        Collections.sort(result, new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings a, CounterOfTrainings b) {
                return b.getCount() - a.getCount();
            }
        });

        return result;
    }

    public static class CounterOfTrainings {
        private Coach coach;
        private int count;

        public CounterOfTrainings(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }
    }
}
package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {


    private Map<DayOfWeek, List<TrainingSession>> dayCache;


    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> byDayAndTime;

    public Timetable() {
        dayCache = new HashMap<>();
        byDayAndTime = new HashMap<>();

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

        if (day == null || time == null) {
            return;
        }


        List<TrainingSession> dayList = dayCache.get(day);
        dayList.add(trainingSession);


        Collections.sort(dayList, new Comparator<TrainingSession>() {
            @Override
            public int compare(TrainingSession a, TrainingSession b) {
                return a.getTimeOfDay().compareTo(b.getTimeOfDay());
            }
        });


        Map<TimeOfDay, List<TrainingSession>> timeMap = byDayAndTime.get(day);

        List<TrainingSession> sessionsAtTime = timeMap.get(time);
        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            timeMap.put(time, sessionsAtTime);
        }
        sessionsAtTime.add(trainingSession);
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
        List<TrainingSession> list = timeMap.get(timeOfDay);

        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counters = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> list = dayCache.get(day);

            for (TrainingSession session : list) {
                Coach coach = session.getCoach();

                Integer count = counters.get(coach);
                if (count == null) {
                    counters.put(coach, 1);
                } else {
                    counters.put(coach, count + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Coach coach : counters.keySet()) {
            result.add(new CounterOfTrainings(coach, counters.get(coach)));
        }

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
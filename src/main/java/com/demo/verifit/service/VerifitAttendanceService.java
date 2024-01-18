package com.demo.verifit.service;

import com.demo.verifit.entity.VerifitAttendanceRecord;
import com.demo.verifit.repository.VerifitAttendanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VerifitAttendanceService {
    @Autowired
    protected VerifitAttendanceRecordRepository verifitAttendanceRecordRepository;

    public void addAttendanceRecord(String userId) {
        VerifitAttendanceRecord record = new VerifitAttendanceRecord();
        record.setUserId(userId);
        record.setAttendanceDate(LocalDateTime.now());
        verifitAttendanceRecordRepository.save(record);
    }

    public boolean isDiscountEligible(String userId) {
        // Logic to check if the user is eligible for a discount
        // (e.g., attended at least once a week for more than 3 weeks)
        List<VerifitAttendanceRecord> verifitAttendanceRecords = verifitAttendanceRecordRepository.findByUserIdOrderByAttendanceDateDesc(userId);

        // If the user hasn't attended for at least three weeks, not eligible for discount
        if (verifitAttendanceRecords.size() < 3) {
            return false;
        }

        // Calculate the start date for the comparison (three weeks ago from the latest attendance)
        LocalDate startDate = verifitAttendanceRecords.get(0).getAttendanceDate().toLocalDate();

        // Calculate the expected number of attendances (once a week for three weeks)
        long expectedAttendances = 3;

        // Count the number of attendances in the last three weeks
        Map<Integer, VerifitAttendanceRecord> weekToAttendance = verifitAttendanceRecords.stream()
                .collect(Collectors.toMap(attendanceRecord -> getWeekNumber(startDate, attendanceRecord.getAttendanceDate().toLocalDate()),
                        attendanceRecord -> attendanceRecord,
                        (r1, r2) -> r1));
        weekToAttendance.remove(0);
        return weekToAttendance.size() >= expectedAttendances;
    }


    public static int getWeekNumber(LocalDate startDate, LocalDate currentDate) {
        if(currentDate.isAfter(startDate.minusWeeks(1))) {
            return 3;
        } else if(currentDate.isBefore(startDate.minusWeeks(1)) &&
                currentDate.isAfter(startDate.minusWeeks(2))) {
            return 2;
        } else if(currentDate.isBefore(startDate.minusWeeks(2)) &&
                currentDate.isAfter(startDate.minusWeeks(3))) {
            return 1;
        }
        return 0;
    }

    public int getUserCurrentStreak(String userId) {
        // Logic to calculate the user's current streak
        List<VerifitAttendanceRecord> verifitAttendanceRecords = verifitAttendanceRecordRepository.findByUserIdOrderByAttendanceDateDesc(userId);

        if (verifitAttendanceRecords.isEmpty()) {
            return 0;
        }
        int streak = 1;
        int weekNumber = 0;

        // Calculate the start date for the comparison
        for(int i = 0; i < verifitAttendanceRecords.size(); i++) {
            VerifitAttendanceRecord currentRecord = verifitAttendanceRecords.get(i);
            LocalDate currentDate = currentRecord.getAttendanceDate().toLocalDate();

            int currentDatesWeek = currentDate.get(WeekFields.ISO.weekOfWeekBasedYear());
            if(weekNumber == 0) {
                weekNumber = currentDatesWeek;
            } else if(weekNumber == currentDatesWeek){
                continue;
            } else if(weekNumber - 1 == currentDatesWeek) {
                weekNumber = currentDatesWeek;
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }
}

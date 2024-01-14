package com.demo.verifit.repository;

import com.demo.verifit.entity.VerifitAttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VerifitAttendanceRecordRepository extends JpaRepository<VerifitAttendanceRecord, Long> {
    List<VerifitAttendanceRecord> findByUserIdOrderByAttendanceDateAsc(String userId);

    List<VerifitAttendanceRecord> findByUserIdOrderByAttendanceDateDesc(String userId);

}

package com.demo.verifit;

import com.demo.verifit.controller.VerifitAttendanceController;
import com.demo.verifit.entity.VerifitAttendanceRecord;
import com.demo.verifit.repository.VerifitAttendanceRecordRepository;
import com.demo.verifit.service.VerifitAttendanceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VerifitAttendanceControllerTest {

	@Mock
	private VerifitAttendanceService verifitAttendanceService;

	@Autowired
	private VerifitAttendanceRecordRepository verifitAttendanceRecordRepository;

	@InjectMocks
	private VerifitAttendanceController verifitAttendanceController;

	@Autowired
	private MockMvc mockMvc;

    @Test
	void contextLoads() {

	}

	@Test
	public void testAddAttendance() throws Exception {
		mockMvc.perform(post("/api/addAttendance")
				.content("1")
				.contentType(MediaType.TEXT_PLAIN)
				)
				.andExpect(status().isOk())
				.andExpect(content().string("Attendance record added successfully"));

	}

	@Test
	public void testCheckDiscountEligibility() throws Exception {
		when(verifitAttendanceService.isDiscountEligible("1")).thenReturn(true);

		mockMvc.perform(get("/api/checkDiscountEligibility")
						.param("userId", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").value(true));

	}

	@Test
	public void testGetCurrentUserStreak() throws Exception {
		List<VerifitAttendanceRecord> verifitAttendanceRecordList = new ArrayList<>();
		LocalDateTime dateTime1 = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 30);
		LocalDateTime dateTime2 = LocalDateTime.of(2024, Month.JANUARY, 15, 12, 30);
		LocalDateTime dateTime3 = LocalDateTime.of(2024, Month.JANUARY, 22, 12, 30);
		VerifitAttendanceRecord verifitAttendanceRecord1 = new VerifitAttendanceRecord();
		VerifitAttendanceRecord verifitAttendanceRecord2 = new VerifitAttendanceRecord();
		VerifitAttendanceRecord verifitAttendanceRecord3 = new VerifitAttendanceRecord();
		verifitAttendanceRecord1.setUserId("1");
		verifitAttendanceRecord1.setAttendanceDate(dateTime1);
		verifitAttendanceRecordList.add(verifitAttendanceRecord1);
		verifitAttendanceRecord2.setUserId("1");
		verifitAttendanceRecord2.setAttendanceDate(dateTime2);
		verifitAttendanceRecordList.add(verifitAttendanceRecord2);
		verifitAttendanceRecord3.setUserId("1");
		verifitAttendanceRecord3.setAttendanceDate(dateTime3);
		verifitAttendanceRecordList.add(verifitAttendanceRecord3);
		verifitAttendanceRecordRepository.saveAll(verifitAttendanceRecordList);

		when(verifitAttendanceService.getUserCurrentStreak("1")).thenReturn(3);

		mockMvc.perform(get("/api/getUserCurrentStreak")
						.param("userId", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").value(3));

	}
}

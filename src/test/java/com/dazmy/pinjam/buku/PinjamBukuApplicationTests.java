package com.dazmy.pinjam.buku;

import com.dazmy.pinjam.buku.entity.Role;
import com.dazmy.pinjam.buku.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;
import java.util.Base64;

@SpringBootTest
class PinjamBukuApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(LocalDate.now());
	}

	@Test
	void testLocalDate() {
		LocalDate born = LocalDate.of(2003, Month.JANUARY, 1);
		LocalDate now = LocalDate.now();

		Period period = born.until(now);

		System.out.println(period.normalized().getYears());

		System.out.println("period is " + period.toTotalMonths());
		System.out.println("age : " + period.toTotalMonths() / 12);
		double doFloor = Math.floor((double) period.toTotalMonths() / 12);
		System.out.println(doFloor);

		short shFloor = (short) doFloor;
		System.out.println(shFloor);
	}

	@Test
	void testBuilderUser() {
		User dazmy = new User("dazmy", LocalDate.of(2003, Month.DECEMBER, 26), Role.ROLE_USER, false);
		System.out.println(dazmy.getAge());
	}

	@Test
	void testParse() {
		String date = "2003-12-26";
		LocalDate parse = LocalDate.parse(date);
		System.out.println(parse);
	}

	@Test
	void test64() {
		String encode = Base64.getEncoder().encodeToString("adam:zamzam".getBytes());
		System.out.println(encode);
	}

	@Test
	void testDecode() {
		String auth = "Basic YWRhbTp6YW16YW0=";
		String basicAuth = auth.substring(6);
		System.out.println(basicAuth);

		byte[] decode = Base64.getDecoder().decode(basicAuth);
		String decodeString = new String(decode);
		String username = null;
		for (String s : decodeString.split(":")) {
			username = s;
			break;
		}
		System.out.println(username);
	}

	@Test
	void testDays() {
		String date1 = "2024-01-01";
		String date2 = "2024-01-05";

		int days = LocalDate.parse(date1).until(LocalDate.parse(date2)).getDays();
		// 4
		System.out.println(days);
	}
}

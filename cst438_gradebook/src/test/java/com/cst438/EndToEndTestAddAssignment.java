package com.cst438;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@SpringBootTest
public class EndToEndTestAddAssignment {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/rubentafoya/Downloads/chromedriver_mac_arm64/chromedriver";

	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // 1 second.
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
	public static final String TEST_ASSIGNMENT_DATE = "04-18-2023";
	public static final String TEST_COURSE_TITLE = "Test Course";
	public static final String TEST_STUDENT_NAME = "Test";

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentRepository assignmentRepository;

	@Test
	public void addAssignmentTest() throws Exception {

//		Database setup:  create test course		
		Course c = new Course();
		c.setCourse_id(99999);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle(TEST_COURSE_TITLE);
		
		courseRepository.save(c);
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		// Open  URL
		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		try {			
			// locate and click new assignment button
			driver.findElement(By.xpath("//a[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			//Enter assignment name
			WebElement element = driver.findElement(By.xpath("//input[@id='name']"));
			element.sendKeys(TEST_ASSIGNMENT_NAME);
			Thread.sleep(SLEEP_DURATION);
			
			//Enter assignment date
			element = driver.findElement(By.xpath("//input[@id='dueDate']"));
			element.sendKeys(TEST_ASSIGNMENT_DATE);
			Thread.sleep(SLEEP_DURATION);
			
			// Locate submit button and click
			driver.findElement(By.xpath("//button[@id='Submit' and @name='submit_assignment']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Go back to previous page
			driver.navigate().back();
			Thread.sleep(SLEEP_DURATION);

			// Verify new assignment is added for the test course
			List<WebElement> elements = driver.findElements(By.xpath("//div[@data-field='name']/div"));
			boolean found = false;
			for (WebElement we : elements) {
				System.out.println(we.getText()); // for debug
				if (we.getText().equals(TEST_ASSIGNMENT_NAME)) {
					found=true;
					we.findElement(By.xpath("descendant::input")).click();
					break;
				}
			}
			assertTrue( found, "Unable to locate TEST ASSIGNMENT in list of assignments to be graded.");

		} catch (Exception ex) {
			throw ex;
		} finally {
			// clean up database so the test is repeatable.
			List<Assignment> assignments = assignmentRepository.findNeedGradingByEmail(TEST_INSTRUCTOR_EMAIL);
			for (Assignment a : assignments) {
				if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
					assignmentRepository.delete(a);
					break;
				}
			}
			courseRepository.delete(c);

			driver.quit();
		}

	}
}


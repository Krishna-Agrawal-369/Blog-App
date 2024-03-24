package com.springboot.bloggingApp;

import com.springboot.bloggingApp.controller.CommentController;
import com.springboot.bloggingApp.service.CategoryService;
import com.springboot.bloggingApp.service.PostService;
import com.springboot.bloggingApp.service.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class BloggingAppApplicationTests {

	@Autowired
	private CategoryService categoryService;

	private Logger logger = LoggerFactory.getLogger(BloggingAppApplicationTests.class);

	@BeforeAll
	public static void beforeTest() {
		System.out.println("------------------------Before Test------------------------------------------------------");
	}

	@Test
	@Timeout(9)
	public void test1() throws InterruptedException {
		Assertions.assertEquals("Krishna", "Krishna", "Test1 : ");
		Thread.sleep(2000);
		Assertions.assertEquals(2, this.categoryService.getAllCategories(10,0,"categoryId","DESC").size(),"Get all categories response is not ok");
		logger.info("Test 1 Executed successfully");
	}

	@Test
	@Timeout(9)
	public void test2() throws InterruptedException {
		Assertions.assertTrue(this.categoryService.getCategoryById(2).isPresent(),"Get category by ID : 2 response is not ok");
		logger.info("Test 2 Executed successfully");
	}

	@AfterAll
	public static void afterTest() {
		System.out.println("------------------------After Test------------------------------------------------------");
	}


}

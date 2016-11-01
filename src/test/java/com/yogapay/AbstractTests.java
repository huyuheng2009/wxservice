package com.yogapay;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = "classpath*:spring*.xml")
public abstract class AbstractTests extends AbstractTransactionalJUnit4SpringContextTests {

	public static void printTitle() {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2];
		String className = st.getClassName();
		String methodName = st.getMethodName();
		String title = className + " # " + methodName;
		System.out.println("");
		for (int i = 0; i < 3; i++) {
			if (i == 1) {
				System.out.println(" " + title + " ");
			} else {
				for (int j = 0; j < title.length() + 2; j++) {
					System.out.print("-");
				}
				System.out.println("");
			}
		}
	}

}

package com.smarthealth.io.smarthealth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.smarthealth.io.smarthealth.shared.core.QueryBuilder;


@SpringBootTest
class SmarthealthApplicationTests {

  public static void main(String[] args) throws IllegalArgumentException, Exception {
    final QueryBuilder builder = new QueryBuilder("users", "x")
      .select(new String[]{ "user_id", "tax_cpf", "nuked_at" });

    System.out.println(builder.where("WHERE x.user_id = $1"));
  }

	@Test
	void contextLoads() { }
}

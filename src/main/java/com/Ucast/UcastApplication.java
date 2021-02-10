package com.Ucast;

import com.Ucast.db.MongoAuthorModel;
import com.Ucast.db.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UcastApplication{
//	 implements CommandLineRunner

	@Autowired

	public static void main(String[] args) {
		SpringApplication.run(UcastApplication.class, args);
	}


//	@Override
//	public void run(String... args) throws Exception {

//		System.out.println("Authors found with findAll():");
//		System.out.println("-------------------------------");
//		for (AuthorModel author : authorRepository.findAll()) {
//			System.out.println(author);
//		}
//		System.out.println("-------------------------------");
//		System.out.println("Customer found with findByName('Simon'):");
//		System.out.println("--------------------------------");
//		System.out.println(authorRepository.findByName("Simon"));

//	}
}

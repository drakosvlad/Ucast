package com.Ucast;

import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.MongoPodcastModel;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.PodcastRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//public class UcastApplication implements CommandLineRunner {
public class UcastApplication{


	public static void main(String[] args) {
		SpringApplication.run(UcastApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		MongoAuthorModel author = new MongoAuthorModel("Test","data@mail.com");
//		ObjectId objectId = new ObjectId("12345566qwerty");
//		MongoPodcastModel podcast = new MongoPodcastModel("Name",objectId,"author","https://hosting.com/file.mp3");
//		//authorRepository.deleteAll();
//		authorRepository.save(author);
//		podcastRepository.save(podcast);
//	}


}

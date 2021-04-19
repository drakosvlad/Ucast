package com.Ucast.repositories;

import com.Ucast.models.MongoPodcastModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PodcastRepository extends MongoRepository<MongoPodcastModel, String> {

    public MongoPodcastModel findById(ObjectId id);
    public MongoPodcastModel findByAuthorNameAndName(String authorName, String name);
    public MongoPodcastModel findByAuthorIdAndName(ObjectId id, String name);
    public List<MongoPodcastModel> findByAuthorName(String authorName);
    public List<MongoPodcastModel> findByAuthorNameAndCheckedTrue(String authorName);   // ??

    public List<MongoPodcastModel> findAllByAuthorId(ObjectId authorId);
    public List<MongoPodcastModel> findAllByName(String name);
    public List<MongoPodcastModel> findAllByNameAndCheckedTrue(String name);    // ??
    public List<MongoPodcastModel> findAllByCheckedTrue();
    public MongoPodcastModel deleteById(ObjectId id);

}

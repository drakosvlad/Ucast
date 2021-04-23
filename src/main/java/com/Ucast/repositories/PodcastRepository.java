package com.Ucast.repositories;

import com.Ucast.models.MongoPodcastModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PodcastRepository extends MongoRepository<MongoPodcastModel, String> {

    public MongoPodcastModel findById(ObjectId id);

    @Query("{'name': {'$regex':'?0', '$options':'i'}, 'authorName': {'$regex':'?0', '$options':'i'}}")
    public MongoPodcastModel findByAuthorNameAndName(String authorName, String name);
    public MongoPodcastModel findByAuthorIdAndName(ObjectId id, String name);
    public List<MongoPodcastModel> findByAuthorName(String authorName);
    public List<MongoPodcastModel> findByAuthorNameAndCheckedTrue(String authorName);   // ??

    public List<MongoPodcastModel> findAllByAuthorId(ObjectId authorId);

    @Query("{'name': {'$regex':'?0', '$options':'i'}}")
    public List<MongoPodcastModel> findAllByName(String name);

    @Query("{'name': {'$regex':'?0', '$options':'i'}}")
    public List<MongoPodcastModel> findAllByNameAndCheckedTrue(String name);    // ??

    public List<MongoPodcastModel> findAllByCheckedTrue();
    public MongoPodcastModel deleteById(ObjectId id);

}

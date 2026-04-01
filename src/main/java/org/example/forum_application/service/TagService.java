package org.example.forum_application.service;


import org.example.forum_application.model.Tag;
import org.example.forum_application.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll(){
        List<Tag> tags = (List<Tag>) tagRepository.findAll();
        return tags;
    }

    public Tag findById(int id){
        Optional<Tag> tag = tagRepository.findById(Long.valueOf(id));
        if(tag.isPresent()){
            return tag.get();
        }
        return null;
    }

    public Tag createTag(Tag tag){
        return tagRepository.save(tag);
    }
    public Tag updateTag(int id, Tag updatedTag){
        Optional<Tag> tagOptional = tagRepository.findById(Long.valueOf(updatedTag.getId()));
        if(tagOptional.isPresent()){
            Tag tagToUpdate = tagOptional.get();
            tagToUpdate.setName(updatedTag.getName());

            return tagRepository.save(tagToUpdate);
        }
        return null;
    }
    public void deleteById(int id){
       tagRepository.deleteById(Long.valueOf(id));
    }



}

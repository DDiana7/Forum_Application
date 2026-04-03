package org.example.forum_application.controller;

import org.example.forum_application.model.Tag;
import org.example.forum_application.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/getAll")
    public List<Tag> getAllTags() {
        return this.tagService.findAll();
    }

    @GetMapping("/getById/{id}")
    public Tag getTagById(@PathVariable int id) {
        return tagService.findById(id);
    }

    @PostMapping("/add")
    public Tag addTag(@RequestBody Tag tag) {
        return this.tagService.createTag(tag);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTag(@PathVariable int id) {
        tagService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public Tag updateTag(@PathVariable int id, @RequestBody Tag tag) {
        return tagService.updateTag(id, tag);
    }
}



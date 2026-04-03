package org.example.forum_application.service;

import org.example.forum_application.model.Tag;
import org.example.forum_application.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tag = new Tag();
        tag.setName("java");
    }

    // create tag
    @Test
    void shouldCreateTag() {
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag saved = tagService.createTag(tag);

        assertNotNull(saved);
        assertEquals("java", saved.getName());

        verify(tagRepository, times(1)).save(tag);
    }

    // find all
    @Test
    void shouldReturnAllTags() {
        Tag tag2 = new Tag();
        tag2.setName("spring");

        when(tagRepository.findAll()).thenReturn(Arrays.asList(tag, tag2));

        List<Tag> result = tagService.findAll();

        assertEquals(2, result.size());
    }

    // empty list
    @Test
    void shouldReturnEmptyListWhenNoTags() {
        when(tagRepository.findAll()).thenReturn(List.of());

        List<Tag> result = tagService.findAll();

        assertTrue(result.isEmpty());
    }

    // find
    @Test
    void shouldFindTagById() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        Tag result = tagService.findById(1);

        assertNotNull(result);
        assertEquals("java", result.getName());
    }

    //
    @Test
    void shouldReturnNullWhenTagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        Tag result = tagService.findById(1);

        assertNull(result);
    }

    // update tag
    @Test
    void shouldUpdateTag() {
        Tag existing = new Tag();
        existing.setName("old");

        Tag updated = mock(Tag.class);
        when(updated.getId()).thenReturn(1L);
        when(updated.getName()).thenReturn("updated");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tagRepository.save(any(Tag.class))).thenReturn(existing);

        Tag result = tagService.updateTag(1, updated);

        assertNotNull(result);
        assertEquals("updated", result.getName());
    }

    //
    @Test
    void shouldReturnNullWhenUpdatingNonExistingTag() {
        Tag updated = mock(Tag.class);
        when(updated.getId()).thenReturn(1L);
        when(updated.getName()).thenReturn("updated");

        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        Tag result = tagService.updateTag(1, updated);

        assertNull(result);
    }

    // delete
    @Test
    void shouldDeleteTagById() {
        doNothing().when(tagRepository).deleteById(1L);

        tagService.deleteById(1);

        verify(tagRepository, times(1)).deleteById(1L);
    }
}
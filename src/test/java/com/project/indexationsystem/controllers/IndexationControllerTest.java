package com.project.indexationsystem.controllers;

import java.util.List;

import com.project.indexationsystem.entity.News;
import com.project.indexationsystem.model.IndexDir;
import com.project.indexationsystem.services.NewsService;

import java.util.ArrayList;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class IndexationControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private NewsService service;

    @Test
    public void emptyNewsWhenGetAllThenReturnNoContent() throws Exception {
        List<News> allNews = new ArrayList<News>();
        
        when(service.getAll()).thenReturn(allNews);

        mvc.perform(get("/api/content"))
            .andExpect(status().isNoContent());

    }

    @Test
    public void givenNewsWhenGetAllThenReturnJson() throws Exception {
        News news = new News("http://notasweb.com");
        List<News> allNews = new ArrayList<News>();
        allNews.add(news);
        
        when(service.getAll()).thenReturn(allNews);

        mvc.perform(get("/api/content"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].url", is(news.getUrl())));
        
    }

    @Test
    public void givenUrlWhenCheckThenInvalidUrl() throws Exception {

        String body = "{\"url\":\"something\", \"word\":\"some\"}";

        mvc.perform(post("/api/content/check")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state", is("rejected")));
    }

    @Test
    public void givenUrlWhenCheckThenCorrectUrl() throws Exception {

        String body = "{\"url\":\"https://stackoverflow.com\", \"word\":\"some\"}";
        IndexDir data = new IndexDir();
        data.setUrl("https://stackoverflow.com");
        data.setWord("someword");

        when(service.save(new News(data.getUrl()))).thenReturn(true);

        mvc.perform(post("/api/content/check")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUrlWhenDeleteThenReturnOk() throws Exception {

        String url = "{\"url\":\"http:something\"}";

        when(service.delete("http:something"))
            .thenReturn(true);

        mvc.perform(delete("/api/content")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(url))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUrlWhenDeleteThenReturnNoContent() throws Exception {

        String url = "{\"url\":\"http:something\"}";

        when(service.delete("http:something"))
            .thenReturn(false);

        mvc.perform(delete("/api/content")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(url))
                .andExpect(status().isNoContent());
    }

}
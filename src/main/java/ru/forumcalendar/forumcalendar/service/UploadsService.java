package ru.forumcalendar.forumcalendar.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface UploadsService {

    Optional<File> upload(MultipartFile file);

    Optional<File> upload(MultipartFile file, String name);

    Optional<File> upload(String url, String name);

    boolean delete(String name);
}

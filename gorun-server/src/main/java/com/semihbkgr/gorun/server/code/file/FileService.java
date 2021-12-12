package com.semihbkgr.gorun.server.code.file;

import reactor.core.publisher.Mono;

import java.nio.file.Path;

public interface FileService {

    Mono<String> createFile(String fileName, String content);

    Mono<String> deleteFile(String filepath);

    Path getRootDirPath();

}
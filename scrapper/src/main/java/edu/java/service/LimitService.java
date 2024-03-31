package edu.java.service;

import io.github.bucket4j.Bucket;

public interface LimitService {
    Bucket resolve(String ip);
}

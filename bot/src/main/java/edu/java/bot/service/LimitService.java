package edu.java.bot.service;

import io.github.bucket4j.Bucket;

public interface LimitService {
    Bucket resolve(String ip);
}

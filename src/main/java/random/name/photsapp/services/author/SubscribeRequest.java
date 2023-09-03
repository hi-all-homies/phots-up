package random.name.photsapp.services.author;

import random.name.photsapp.entities.Author;

public record SubscribeRequest(Author subscriber, String subscription) {}
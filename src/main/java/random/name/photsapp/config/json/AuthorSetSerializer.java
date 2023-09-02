package random.name.photsapp.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import random.name.photsapp.entities.Author;
import java.io.IOException;
import java.util.Set;

public class AuthorSetSerializer extends StdSerializer<Set<Author>> {

    public AuthorSetSerializer(){
        this(null);
    }

    public  AuthorSetSerializer(Class<Set<Author>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<Author> subscriptions, JsonGenerator gen, SerializerProvider provider) throws IOException {
        subscriptions.forEach(sub -> {
            sub.setSubscriptions(null);
            sub.setSubscribers(null);
        });
        gen.writeObject(subscriptions);
    }
}
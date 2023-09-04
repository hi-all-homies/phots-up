package random.name.photsapp.services.author;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import random.name.photsapp.entities.Author;
import java.util.Collection;
import java.util.List;


public class AuthorDetails implements UserDetails {

    private final long id;
    private final String username;
    private final String password;
    private final String avatarUrl;

    public AuthorDetails(Author author){
        this.id = author.getId();
        this.password = author.getPassword();
        this.username = author.getUsername();
        this.avatarUrl = author.getAvatarUrl();
    }

    public long getId(){
        return this.id;
    }

    public String getAvatarUrl(){
        return this.avatarUrl;
    }

    public Author getAuthor(){
        var author = new Author();
        author.setId(this.id);
        author.setUsername(this.username);
        author.setPassword(this.password);
        author.setAvatarUrl(this.avatarUrl);
        return author;
    }


    public final static String ROLE_USER = "ROLE_USER";
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_USER));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
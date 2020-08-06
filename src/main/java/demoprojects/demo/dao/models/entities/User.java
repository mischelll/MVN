package demoprojects.demo.dao.models.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    private String username;
    private String password;
    private String email;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String bio;
    private Image avatar;
    private LocalDateTime registeredOn = LocalDateTime.now();
    private Set<Role> authorities;
    private Set<Post> posts;
    private Set<Product> offers;
    private Set<Product> soldProducts;
    private Set<Product> boughtProducts;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    private Boolean isSubscribedToBlog;
    private Boolean isSubscribedToShop;

    public User() {
        this.soldProducts = new HashSet<>();
        this.boughtProducts = new HashSet<>();
        this.isSubscribedToBlog = false;
        this.isSubscribedToShop = false;
    }

    @Column(name = "is_subscribed_blog")
    public Boolean getSubscribedToBlog() {
        return isSubscribedToBlog;
    }

    public void setSubscribedToBlog(Boolean subscribedToBlog) {
        isSubscribedToBlog = subscribedToBlog;
    }
    @Column(name = "is_subscribed_shop")
    public Boolean getSubscribedToShop() {
        return isSubscribedToShop;
    }

    public void setSubscribedToShop(Boolean subscribedToShop) {
        isSubscribedToShop = subscribedToShop;
    }

    @OneToOne(targetEntity = Image.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    @OneToMany(mappedBy = "seller", targetEntity = Product.class, fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    public Set<Product> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<Product> soldProducts) {
        this.soldProducts = soldProducts;
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name = "registered_on")
    public LocalDateTime getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDateTime registeredOn) {
        this.registeredOn = registeredOn;
    }

    @OneToMany(mappedBy = "author", targetEntity = Post.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Email(message = "Please enter a valid email")
    @NotEmpty(message = "Field cannot be empty")
    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    public Set<Role> getAuthorities() {
        return authorities;
    }

    @OneToMany(mappedBy = "seller", targetEntity = Product.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    public Set<Product> getOffers() {
        return offers;
    }

    public void setOffers(Set<Product> soldProducts) {
        this.offers = soldProducts;
    }

    @OneToMany(mappedBy = "buyer", targetEntity = Product.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    public Set<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(Set<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Field cannot be empty")
    public String getPassword() {
        return this.password;
    }

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    @NotEmpty(message = "Field cannot be empty")
    @Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "bio")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.isAccountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.isCredentialsNonExpired = credentialsNonExpired;
    }


}

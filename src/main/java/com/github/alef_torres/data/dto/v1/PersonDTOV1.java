package com.github.alef_torres.data.dto.v1;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.alef_torres.model.Book;
import com.github.alef_torres.serializer.GenderSerializer;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Relation(collectionRelation = "people")
@JsonPropertyOrder({"id", "firstName", "lastName", "gender", "address", "birthDate", "enabled", "links"})
@JsonFilter("PersonFilter")
public class PersonDTOV1 extends RepresentationModel<PersonDTOV1> implements Serializable {

    @JsonProperty("person_id")
    private Long id;

    @JsonProperty("person_first_name")
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("person_last_name")
    private String lastName;

    @JsonProperty("person_birthDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;

    @JsonProperty("person_address")
    private String address;

    @JsonProperty("person_gender")
    @JsonSerialize(using = GenderSerializer.class)
    private String gender;

    @JsonProperty("person_phone")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String phone;

    @JsonProperty("person_status")
    private Boolean enabled;

    private String sensitiveData;

    private String profileUrl;

    private String photoUrl;

    @JsonIgnore
    private List<Book> books;

    public PersonDTOV1() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(String sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonIgnore
    public String getName() {
        return (firstName != null ? firstName : "") + (lastName != null ? " " + lastName : "");
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonDTOV1 that = (PersonDTOV1) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getBirthDate(), that.getBirthDate()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getGender(), that.getGender()) && Objects.equals(getPhone(), that.getPhone()) && Objects.equals(getEnabled(), that.getEnabled()) && Objects.equals(getSensitiveData(), that.getSensitiveData()) && Objects.equals(getProfileUrl(), that.getProfileUrl()) && Objects.equals(getPhotoUrl(), that.getPhotoUrl()) && Objects.equals(getBooks(), that.getBooks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getFirstName(), getLastName(), getBirthDate(), getAddress(), getGender(), getPhone(), getEnabled(), getSensitiveData(), getProfileUrl(), getPhotoUrl(), getBooks());
    }
}

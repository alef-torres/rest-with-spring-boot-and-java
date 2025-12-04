package com.github.alef_torres.data.dto.v1;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.alef_torres.serializer.GenderSerializer;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "gender", "lastName", "address", "firstName",})
@JsonFilter("PersonFilter")
public class PersonDTOV1 extends RepresentationModel<PersonDTOV1> implements Serializable {

    @JsonProperty("person_id")
    private Long id;

    @JsonProperty("person_first_name")
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("person_last_name")
    private String lastName;

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

    private String sensitiveData;

    public PersonDTOV1() {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTOV1 that = (PersonDTOV1) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getBirthDate(), that.getBirthDate()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getGender(), that.getGender()) && Objects.equals(getPhone(), that.getPhone()) && Objects.equals(getSensitiveData(), that.getSensitiveData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getBirthDate(), getAddress(), getGender(), getPhone(), getSensitiveData());
    }
}

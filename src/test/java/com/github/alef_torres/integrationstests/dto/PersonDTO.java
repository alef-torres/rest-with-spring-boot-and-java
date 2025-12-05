package com.github.alef_torres.integrationstests.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.alef_torres.serializer.GenderSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "gender", "lastName", "address", "firstName",})
@JsonFilter("PersonFilter")
public class PersonDTO implements Serializable {

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

    @JsonProperty("person_status")
    private Boolean enabled;

    public PersonDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(getId(), personDTO.getId()) && Objects.equals(getFirstName(), personDTO.getFirstName()) && Objects.equals(getLastName(), personDTO.getLastName()) && Objects.equals(getBirthDate(), personDTO.getBirthDate()) && Objects.equals(getAddress(), personDTO.getAddress()) && Objects.equals(getGender(), personDTO.getGender()) && Objects.equals(getPhone(), personDTO.getPhone()) && Objects.equals(getSensitiveData(), personDTO.getSensitiveData()) && Objects.equals(getEnabled(), personDTO.getEnabled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getBirthDate(), getAddress(), getGender(), getPhone(), getSensitiveData(), getEnabled());
    }
}

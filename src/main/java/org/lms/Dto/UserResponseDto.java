package org.lms.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    @JsonProperty("id")
    public UUID id;


    @JsonProperty("username")
    public String username;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("email")
    public String email;

    @JsonProperty("role")
    public String role;

    @JsonProperty("departmentId")
    public UUID departmentId;

    @JsonProperty("isActive")
    public boolean isActive;

    public UserResponseDto(){}
    public UserResponseDto(boolean isActive){
        this.isActive = isActive;
    }
}

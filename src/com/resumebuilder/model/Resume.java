package com.resumebuilder.model;

public class Resume {
    private String jobTitle;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String professionalSummary;
    private int id;

    // Getters
    public String getJobTitle() { return jobTitle; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public String getProfessionalSummary() { return professionalSummary; }

    // Setter
    public void setId(int id) { this.id = id; }
}
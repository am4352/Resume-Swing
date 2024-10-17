package com.resumebuilder.model;

public class Resume {
    private int id; // Assuming you have an ID field
    private String jobTitle;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String professionalSummary;

    // Constructor
    public Resume(String jobTitle, String firstName, String lastName, String email,
                  String phone, String country, String city, String professionalSummary) {
        this.jobTitle = jobTitle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.professionalSummary = professionalSummary;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    // Optionally, you can add a toString() method for easier debugging
    @Override
    public String toString() {
        return "Resume{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", professionalSummary='" + professionalSummary + '\'' +
                '}';
    }
}

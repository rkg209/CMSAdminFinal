package com.example.cms_admin;

public class ClubDetail {
    private String Email,Logo,UserName;

    public ClubDetail(){}

    public ClubDetail(String userName, String email, String logo ) {
        Email = email;
        Logo = logo;
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}

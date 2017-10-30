package com.picsdream.picsdreamsdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodResponse {

@SerializedName("success")
@Expose
private Integer success;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("email")
@Expose
private String email;
@SerializedName("enabled")
@Expose
private Integer enabled;

public Integer getSuccess() {
return success;
}

public void setSuccess(Integer success) {
this.success = success;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public Integer getEnabled() {
return enabled;
}

public void setEnabled(Integer enabled) {
this.enabled = enabled;
}

}
package ng.com.babbangona.kymapp.Data.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberApproved {
    @SerializedName("unique_member_id")
    @Expose
    private String unique_member_id;
    @SerializedName("mik")
    @Expose
    private String mik;

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getMik() {
        return mik;
    }

    public void setMik(String mik) {
        this.mik = mik;
    }

}

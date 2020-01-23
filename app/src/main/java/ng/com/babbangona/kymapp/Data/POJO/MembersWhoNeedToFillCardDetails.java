package ng.com.babbangona.kymapp.Data.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MembersWhoNeedToFillCardDetails {
    @SerializedName("unique_member_id")
    @Expose
    private String uniqueMemberId;
    @SerializedName("pass_flag")
    @Expose
    private String passFlag;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("need_approval")
    @Expose
    private String need_approval;

    public String getUniqueMemberId() {
        return uniqueMemberId;
    }

    public void setUniqueMemberId(String uniqueMemberId) {
        this.uniqueMemberId = uniqueMemberId;
    }

    public String getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(String passFlag) {
        this.passFlag = passFlag;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNeed_approval() {
        return need_approval;
    }

    public void setNeed_approval(String need_approval) {
        this.need_approval = need_approval;
    }
}


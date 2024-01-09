package ma.fstt.dto;

import ma.fstt.entity.TypeAid;

public class AidDTO {
    private Long AidId;

    private TypeAid type;

    private Double amount;

    private String details;

    private Integer userId;

    public AidDTO() {
    }

    public AidDTO(Long aidId, TypeAid type, Double amount, String details, Integer userId) {
        AidId = aidId;
        this.type = type;
        this.amount = amount;
        this.details = details;
        this.userId = userId;
    }

    public Long getAidId() {
        return AidId;
    }

    public void setAidId(Long aidId) {
        AidId = aidId;
    }

    public TypeAid getType() {
        return type;
    }

    public void setType(TypeAid type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

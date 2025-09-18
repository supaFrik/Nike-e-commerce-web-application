package vn.devpro.javaweb32.common.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date", nullable = true)
    private Date createDate;

    // Date: java.util.Date

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "update_date", nullable = true)
    private Date updateDate;

    @Column(name = "status", nullable = true)
    private String status;

    public BaseEntity() {
        super();
    }

    public BaseEntity(Long id, Date createDate, Date updateDate, String status) {
        super();
        this.id = id;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

package vn.devpro.javaweb32.common.base;

import java.util.Date;

import javax.persistence.*; // ...existing code...
import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date", updatable = false)
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "status")
    private String status;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        if (this.createDate == null) {
            this.createDate = now;
        }
        this.updateDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = new Date();
    }

    public BaseEntity() { }

    public BaseEntity(Long id, Date createDate, Date updateDate, String status) {
        this.id = id;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }
}

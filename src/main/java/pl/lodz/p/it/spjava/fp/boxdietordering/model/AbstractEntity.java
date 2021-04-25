package pl.lodz.p.it.spjava.fp.boxdietordering.model;
    import java.util.Date;
    import javax.persistence.*;


@MappedSuperclass
public abstract class AbstractEntity {
    
    protected static final long serialVersionUID = 1L;

    protected abstract Object getId();

    protected abstract Object getBusinessKey();

    @Version
    @Column(name="version")
    private int version;

    public int getVersion() {
        return version;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_timestamp")
    private Date creationTimestamp;

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public Date getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_timestamp")
    private Date modificationTimestamp;

    @PreUpdate
    private void updateTimestamp() {
        modificationTimestamp = new Date();
    }

    @PrePersist
    private void creationTimestamp() {
        creationTimestamp = new Date();
    }
    @Override
    public String toString() {
        return "AbstractEntity("+"version"+'}';
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        
        if(this.getClass().isAssignableFrom(obj.getClass())) {
            return this.getBusinessKey().equals(((AbstractEntity)obj).getBusinessKey());
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return getBusinessKey().hashCode(); 
    }
}

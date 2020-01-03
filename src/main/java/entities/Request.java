package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "requests")
@NamedQuery(name = "Request.deleteAllRows", query = "DELETE from Request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "requestTimestamp")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date requestTimestamp;
    
    @ManyToMany(mappedBy = "requestList")
    private List<Category> categoryList;
    

//    public Request() {
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request() {
        Date date = new Date();
        this.requestTimestamp = date;
    }

    public Date getTimestamp() {
        return requestTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.requestTimestamp = timestamp;
    }

    public Date getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(Date requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

}

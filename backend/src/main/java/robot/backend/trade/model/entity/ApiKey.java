package robot.backend.trade.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_key")
public class ApiKey {
    @Id
    public Long id;

    public String userId;

    public String key;

    public String secretKey;

}

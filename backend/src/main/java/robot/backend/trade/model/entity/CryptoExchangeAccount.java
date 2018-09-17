package robot.backend.trade.model.entity;

import robot.backend.trade.model.contant.CryptoExchangeName;

import javax.persistence.*;

@Entity
@Table(name = "crypto_exchange_account")
public class CryptoExchangeAccount {

    @Id
    public Long id;

    public String accountName;

    public CryptoExchangeName cryptoExchangeName;

    @ManyToOne
    public ApiKey apiKey = new ApiKey();

}

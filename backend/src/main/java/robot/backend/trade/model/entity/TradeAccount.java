package robot.backend.trade.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trade_account")
public class TradeAccount {

    @Id
    public Long id;

    @OneToMany(cascade = CascadeType.ALL)
    public List<CryptoExchangeAccount> cryptoExchangeAccountList = new ArrayList<>();

}

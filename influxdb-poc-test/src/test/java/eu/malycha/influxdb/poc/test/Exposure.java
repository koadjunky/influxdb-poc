package eu.malycha.influxdb.poc.test;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Measurement(name = "exposure")
public class Exposure {

    @Column(timestamp = true)
    Instant time;

    @Column(tag = true)
    String currency;

    @Column(tag = true)
    String portfolio;

    @Column
    BigDecimal value;

    private Exposure(Builder builder) {
        setTime(builder.time);
        setCurrency(builder.currency);
        setPortfolio(builder.portfolio);
        setValue(builder.value);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Exposure copy) {
        Builder builder = new Builder();
        builder.time = copy.getTime();
        builder.currency = copy.getCurrency();
        builder.portfolio = copy.getPortfolio();
        builder.value = copy.getValue();
        return builder;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Exposure exposure = (Exposure) o;

        return new EqualsBuilder()
            .append(time, exposure.time)
            .append(currency, exposure.currency)
            .append(portfolio, exposure.portfolio)
            .append(value, exposure.value)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(time)
            .append(currency)
            .append(portfolio)
            .append(value)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "Exposure{" +
            "time=" + time +
            ", currency='" + currency + '\'' +
            ", portfolio='" + portfolio + '\'' +
            ", value='" + value + '\'' +
            '}';
    }


    public static final class Builder {
        private Instant time;
        private String currency;
        private String portfolio;
        private BigDecimal value;

        private Builder() {
        }

        public Builder withTime(Instant val) {
            time = val;
            return this;
        }

        public Builder withCurrency(String val) {
            currency = val;
            return this;
        }

        public Builder withPortfolio(String val) {
            portfolio = val;
            return this;
        }

        public Builder withValue(BigDecimal val) {
            value = val;
            return this;
        }

        public Exposure build() {
            return new Exposure(this);
        }
    }
}

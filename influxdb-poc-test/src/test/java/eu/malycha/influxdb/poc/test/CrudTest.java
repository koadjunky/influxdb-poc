package eu.malycha.influxdb.poc.test;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;


class CrudTest {

    @Test
    void addRecord() {
        char [] token = "admin-secret-token".toCharArray();
        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, "dev", "db0");
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        Exposure exposure = Exposure.newBuilder()
            .withCurrency("USD")
            .withPortfolio("Pocket")
            .withTime(Instant.now())
            .withValue(new BigDecimal("1000"))
            .build();

        writeApi.writeMeasurement(WritePrecision.NS, exposure);
    }

    @Test
    void deleteRecord() {

    }

    @Test
    void queryRecord() {

    }

    @Test
    void queryRecordKeys() {

    }
}

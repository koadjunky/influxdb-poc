package eu.malycha.influxdb.poc.test;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;


class CrudTest {

    static InfluxDBClient influxDBClient;
    static String org = "dev";
    static String bucket = "db0";

    @BeforeAll
    static void setUp() {
        char [] token = "admin-secret-token".toCharArray();
        influxDBClient = InfluxDBClientFactory.create("http://localhost:8086", token, org, bucket);
    }

    @AfterAll
    static void tearDown() {
        influxDBClient.close();
    }

    @Test
    void addRecord_synchronousBlocking() {
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
        DeleteApi deleteApi = influxDBClient.getDeleteApi();

        OffsetDateTime start = OffsetDateTime.now().minus(1, ChronoUnit.YEARS);
        OffsetDateTime stop = OffsetDateTime.now();

        deleteApi.delete(start, stop, "currency = \"USD\"", bucket, org);
    }

    @Test
    void queryRecord() {

    }

    @Test
    void queryRecordKeys() {

    }
}

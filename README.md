# hazelcast-poc

## Docker Compose

Start docker compose:

```docker compose up```

Shut docker compose:

```docker compose down```

Shut docker compose and remove volumes:

```docker compose down -v```

## InfluxDB

InfluxDB console is available at:

```http://localhost:8086/``` (admin/adminuser)

Obtain token from running container:

```docker exec influxdb-poc-influxdb-1 influx auth list | awk '/admin/ {print $4 " "}'```

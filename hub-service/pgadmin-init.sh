#!/bin/sh -eu

cat >/pgadmin4/servers.json <<EOF
{
  "Servers": {
    "1": {
      "Name": "local-docker",
      "Group": "Servers",
      "Host": "db",
      "Port": 5435,
      "MaintenanceDB": "fastlinehub",
      "Username": "user",
      "SSLMode": "prefer",
      "PassFile": "/pgpass"
    }
  }
}
EOF

echo "db:5435:fastlinehub:user:1234" > /pgpass
chmod 600 /pgpass
chown 5050:5050 /pgpass /pgadmin4/servers.json   # ★ 중요

exec /entrypoint.sh

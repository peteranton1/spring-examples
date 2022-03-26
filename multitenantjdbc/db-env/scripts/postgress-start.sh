#
# Starts postgres container
# use sudo before docker command if necessary
#

NAME=${1:-default}-postgres
PORT=${2:-5432}
docker ps -aq -f "name=${NAME}" | \
  while read 1; do docker rm -f $1 ; echo "Stopping $1"; done
docker run --name $NAME \
  -p ${PORT}:5432 \
  -e POSTGRES_USER=user \
  -e PGUSER=user \
  -e POSTGRES_PASSWORD=pw \
  postgres:latest


docker build -t disk .
cd docker
docker-compose up -d
docker run -d -p 18089:18083 disk



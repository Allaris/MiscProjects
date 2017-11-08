### Setup

Download Docker for Ubuntu: https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/

Make sure Docker is working by running Hello World: https://hub.docker.com/_/hello-world/
```shell
sudo docker run hello-world
```
A block of text starting with "Hello from Docker!" should be printed to the terminal. If you got something else, Docker is not installed or running correctly. Docker requires sudo privelages because it uses Unix socket which is owned by root. It is possible to make the `docker` command sudo-less but it is a security concern and is not recommended.

Lets create a cool demo of a monitoring service. The core componets will be a custome built Docker image to send metrics, Graphite database for the metrics, and Grafana to make graphs from the metrics. 

### Building a Docker image
https://docs.docker.com/engine/reference/builder/

Docker images are like a USB drive that stores all the files and dependencies for the application to run. They are portable and self contained so they can't mess with the OS (more on this later). Making an image for sending our metrics requires creating a `Dockerfile`.

First create a `metricGenerator.sh` file
```shell
#!/bin/bash
while [ 1 == 1 ]
do
	echo "rootDir.subDir.myStat $((RANDOM % 100)) `date +%s`" | nc graphite 2003
	sleep 1
done
```
Create a `Dockerfile`
```dockerfile
# Linux distro
FROM alpine:latest 

# Add the script
COPY ./metricGenerator.sh /

# Run the add script
CMD chmod +x /metricGenerator.sh
CMD sh /metricGenerator.sh
```
Now build it with `sudo docker build -t myname/myapp ./`

### Time run
Because of the structure of Docker, a network will need to be created to allow the containers on the same host talk to each other. If you run the containers on different machines you will have no problems.
```
sudo docker network create -d bridge demoNetwork
sudo docker network ls
```
Now run the images
```shell
sudo docker run -d\
 --name graphite\
 --restart=always\
 -p 80:80\
 -p 2003-2004:2003-2004\
 -p 2023-2024:2023-2024\
 -p 8125:8125/udp\
 -p 8126:8126\
 --network=demoNetwork
 hopsoft/graphite-statsd
 
 sudo docker run -d --name=grafana -p 3000:3000 --network=demoNetwork grafana/grafana
 
 sudo docker run --name=myapp --network=demoNetwork myname/myapp
```



A database is needed to store the metrics of the computer resources. Graphite is a timerseries database and will be used 
for collecting and storing the metrics. https://hub.docker.com/r/hopsoft/graphite-statsd/
```
sudo docker run -d\
 --name graphite\
 --restart=always\
 -p 80:80\
 -p 2003-2004:2003-2004\
 -p 2023-2024:2023-2024\
 -p 8125:8125/udp\
 -p 8126:8126\
 --network=demoNetwork
 hopsoft/graphite-statsd
 

 
```





Some resource metrics will need to be collected in Graphite before they can be graphed. Use use the script below to 
generate random numbers and feed them to Graphite to store. Use the `sudo docker network inspect demoNetwork` command to 
find the ip address of the Graphite container to send the generated numbers to.
```bash
while [ 1 == 1 ]
do
	echo "rootDir.subDir.myStat $((RANDOM % 100)) `date +%s`" | nc 172.18.0.2 2003
	sleep 1
done
```

Graphite has its own graphing utility but Grafana is better. https://hub.docker.com/r/grafana/grafana/
```
sudo docker run -d --name=grafana -p 3000:3000 --network=demoNetwork grafana/grafana
```
In your host browser go to localhost:3000. User and password is `admin`. Add a data source, set URL to `http://graphite` 
and access to proxy. Create a dashboard, add a graph, edit the graph, and select metric `rootDir,subDir,myStat`.

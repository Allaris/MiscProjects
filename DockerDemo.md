### Docker Installation and Setup

Download Docker for Ubuntu: https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/

Make sure Docker is working by running Hello World: https://hub.docker.com/_/hello-world/
```shell
sudo docker run hello-world
```
A block of text starting with "Hello from Docker!" should be printed to the terminal. If you got something else, Docker is not installed or running correctly. Docker requires sudo privileges because it uses Unix socket which is owned by root. It is possible to make the `docker` command sudo-less but it is a security concern and is not recommended.

Lets create a cool demo of a monitoring service. The core components will be a custom built Docker image to send metrics, Graphite database for the metrics, and Grafana to make graphs from the stored metrics. 

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

# Run the added script
CMD chmod +x /metricGenerator.sh
CMD sh /metricGenerator.sh
```
Now build it with `sudo docker build -t myname/myapp ./`

### Running Docker images
Because of the structure of Docker, a network will need to be created to allow the containers on the same host talk to other containers. If you run the containers on different host machines, you will have no problems.
```
sudo docker network create -d bridge demoNetwork
sudo docker network ls
```
Now run the images (Graphite, Graphana, myapp)
```shell
sudo docker run -d\
 --name graphite\
 --restart=always\
 -p 80:80\
 -p 2003-2004:2003-2004\
 -p 2023-2024:2023-2024\
 -p 8125:8125/udp\
 -p 8126:8126\
 --network=demoNetwork\
 hopsoft/graphite-statsd
 
 sudo docker run -d --name=grafana -p 3000:3000 --network=demoNetwork grafana/grafana
 
 sudo docker run -d --name=myapp --network=demoNetwork myname/myapp
```
### Configuring it Together
In your host browser go to localhost:3000. User and password is `admin`. Click add a data source, set URL to `http://graphite` 
and access to `proxy`. Now create a dashboard, add a graph, edit the graph, and select metric `rootDir,subDir,myStat`. You now have installed Docker, made your own Docker image, run multiple images (your own and others), and configured them to communicate between them.

#### Topics to talk about
* Container hostname (no IPs yay!)
* docker run --arguements -it -d -p
* Super useful for container debugging `sudo docker exec -it myapp /bin/sh`
* Container Kernel and Host Kernel http://floydhilton.com/docker/2017/03/31/Docker-ContainerHost-vs-ContainerOS-Linux-Windows.html
* Docker Hub
* File permissions between container and host with -v arguement
* Alphine doesn't have bash

Download Docker for Ubuntu: https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/

Make sure Docker is working by running Hello World: https://hub.docker.com/_/hello-world/
```shell
sudo docker run hello-world
```
A block of text saying "hello" and some steps. If you got something else, Docker is not installed or running correctly.
Docker requires sudo privelages because it can mount volumes and other low level Linux functions. It is possible to make 
the `docker` command sudo less but would allow a regular use to gain access to sudo areas through Docker.

Lets create a cool demo of a monitoring service. Because of the structure of Docker a network will need to be created to 
allow the containers on the same host talk to each other. If you run the containers on different machines you will have 
no problems.
```
sudo docker network create -d bridge demoNetwork
sudo docker network ls
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

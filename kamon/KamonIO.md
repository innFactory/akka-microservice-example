# Kamon
Manual for configuring Kamon 2 for this Akka example

Requirements:
- Scala 2.11 or 2.12
- Java 8+

## Application Configuration
- Check the following files:
    - ```service1/build.sbt```
    - ```service1/src/main/resources/application.conf```
- In the file ```service1/src/main/scala/de/innfactory/svc1/Service1.scala``` add ```Kamon.init()``` at the beginning of the application
- In the file ```k8s/service1.yaml``` mention the annotation for prometheus
- Build and update the service
- To check if Kamon works run the following command ```kubectl port-forward YOURPODNAME 5266:5266```
- Launch your browser and navigate to ```http://localhost:5266```
    - There you should see the Kamon status page
    
## Prometheus setup
- Create namespace ```kubectl create namespace kamon```
- Install prometheus ```helm install --name prometheus --namespace kamon /PATH/TO/YOUR/PROJECT/akka-microservice-sample/kamon/prometheus/```
- Check if pods are running: ```kubectl get pods -n kamon```
- If something goes wrong you can delete prometheus with```kubectl delete --purge prometheus```
- Access prometheus on port 9090 via ```kubectl port-forward -n kamon YOUR-PROMETHEUS-POD-NAME 9090:9090```

## Grafana setup
- Create namespace ```kamon``` if it doesn't exist
- Install prometheus ```helm install --name grafana --namespace kamon /PATH/TO/YOUR/PROJECT/akka-microservice-sample/kamon/grafana/```
- Check if pods are running: ```kubectl get pods -n kamon```
- If something goes wrong you can delete prometheus with```helm delete --purge grafana```
- Access grafana on port 3000 via ```kubectl port-forward -n kamon YOUR-GRAFANA-POD-NAME 3000:3000```
- Add a datasource with the url ```http://prometheus-server.kamon.svc.cluster.local```
- Configure your dashboard

## Kamon metric hint
- time-buckets for metrics with a unit in the time dimension. Everything is scaled to seconds.
- information-buckets for all units in the information dimension. Everything is scaled to bytes.
- default-buckets are used when there is no measurement unit information in a metric.
 

## Sources
- https://kamon.io/docs/latest/guides/frameworks/elementary-akka-setup/
- https://kamon.io/docs/latest/reporters/prometheus/
- https://kamon.io/docs/latest/reporters/zipkin/
- https://blog.kubernauts.io/cloud-native-monitoring-with-prometheus-and-grafana-9c8003ab9c7
- https://github.com/kamon-io/kamon-prometheus
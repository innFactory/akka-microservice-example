# Kamon
Manual for configuring Kamon/Jaeger for this Akka example

Requirements:
- Scala 2.11 or 2.12
- Java 8+

# Helm setup
- ```TILLER_NAMESPACE=kube-system```
- ```kubectl create serviceaccount --namespace $TILLER_NAMESPACE tiller```
- ```kubectl create clusterrolebinding $TILLER_NAMESPACE:tiller --clusterrole=cluster-admin --serviceaccount=$TILLER_NAMESPACE:tiller```
- ```helm init --wait --service-account tiller --tiller-namespace=$TILLER_NAMESPACE```
- verify that helm client and server version match ```helm version```

## Application Configuration
- Check the following files:
    - ```service1/build.sbt```
    - ```service1/src/main/resources/application.conf```
    - ```service2/build.sbt```
    - ```service2/src/main/resources/application.conf```
- In the file ```service1/src/main/scala/de/innfactory/svc1/Service1.scala``` add ```Kamon.init()``` at the beginning of the application
- Make the above step for service 2 ```service2/src/main/scala/de/innfactory/svc2/Service2.scala``` add ```Kamon.init()``` at the beginning of the application
- In the file ```k8s/service1.yaml``` and ```k8s/service2.yaml``` mention the annotation for prometheus
- Build and update the service
- To check if Kamon works run the following command ```kubectl port-forward YOURPODNAME 5266:5266```
- Launch your browser and navigate to ```http://localhost:5266```
    - There you should see the Kamon status page
   
## InfluxDB
- Create namespace ```kubectl create monitoring_and_tracing```
- Install InfluxDB ```helm install stable/influxdb --name influxdb --namespace monitoring_and_tracing```
- Access InfluxDB API on port 8086 via ```kubectl port-forward --namespace monitoring_and_tracing $(kubectl get pods --namespace monitoring_and_tracing -l app=influxdb -o jsonpath='{ .items[0].metadata.name }') 8086:8086```
- Check if pods are running: ```kubectl get pods -n monitoring_and_tracing```
- Connect to the CLI: ```kubectl exec -i -t --namespace monitoring_and_tracing $(kubectl get pods --namespace monitoring_and_tracing -l app=influxdb -o jsonpath='{.items[0].metadata.name}') /bin/sh```
    - Start InfluxDB client: ```influx```
    - Create database: ```CREATE DATABASE "prometheus"```

## Prometheus setup
- Create namespace ```monitoring_and_tracing``` if it doesn't exist
- In the file ```/PATH/TO/YOUR/PROJECT/akka-microservice-sample/monitoring_and_tracing/prometheus/velues.yaml``` the remote_write and remote_read value was added for InfluxDB
- Install prometheus ```helm install --name prometheus --namespace monitoring_and_tracing /PATH/TO/YOUR/PROJECT/akka-microservice-sample/monitoring_and_tracing/prometheus/```
- Check if pods are running: ```kubectl get pods -n monitoring_and_tracing```
- If something goes wrong you can delete prometheus with```kubectl delete --purge prometheus```
- Access prometheus on port 9090 via ```kubectl port-forward -n monitoring_and_tracing YOUR-PROMETHEUS-POD-NAME 9090:9090```

## Validate if Prometheus can write to InfluxDB
- Connect to the InfluxDB CLI: ```kubectl exec -i -t --namespace monitoring_and_tracing $(kubectl get pods --namespace monitoring_and_tracing -l app=influxdb -o jsonpath='{.items[0].metadata.name}') /bin/sh```
    - Start InfluxDB client: ```influx```
    - Use prometheus database: ```use prometheus```
    - Check if there are prometheus entrys with ```SHOW MEASUREMENTS```

## Grafana setup
- Create namespace ```monitoring_and_tracing``` if it doesn't exist
- Install grafana ```helm install --name grafana --namespace monitoring_and_tracing /PATH/TO/YOUR/PROJECT/akka-microservice-sample/monitoring_and_tracing/grafana/```
- Check if pods are running: ```kubectl get pods -n monitoring_and_tracing```
- If something goes wrong you can delete prometheus with```helm delete --purge grafana```
- Access grafana on port 3000 via ```kubectl port-forward -n monitoring_and_tracing YOUR-GRAFANA-POD-NAME 3000:3000```
- Get the admin password with ```kubectl get secret --namespace kamon grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo``` 
- Add a datasource with the url ```http://prometheus-server.monitoring_and_tracing.svc.cluster.local```
- !! Describe add datasource !!
- Configure your dashboard

## Jaeger setup
- ```kubectl create -n monitoring_and_tracing -f /PATH/TO/YOUR/PROJECT/akka-microservice-sample/monitoring_and_tracing/jaeger/jaeger-all-in-one-template.yml```

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
- https://github.com/jaegertracing/jaeger-kubernetes/blob/master/README.md
- https://github.com/helm/charts/tree/master/stable/influxdb
# Lightbend Telemetry
Manual for configuring Lightbend Telemetry for this Akka example.

Requirements:
- installed Helm
- python 2.x available in your path

## Lightbend Console setup:
- ```TILLER_NAMESPACE=kube-system```
- ```kubectl create serviceaccount --namespace $TILLER_NAMESPACE tiller```
- ```kubectl create clusterrolebinding $TILLER_NAMESPACE:tiller --clusterrole=cluster-admin --serviceaccount=$TILLER_NAMESPACE:tiller```
- ```helm init --wait --service-account tiller --tiller-namespace=$TILLER_NAMESPACE```
- verify that helm client and server version match ```helm version```
- setup your Lightbend credentials: https://developer.lightbend.com/docs/console/current/installation/credentials.html
- ```kubectl create namespace lightbend```
- ```curl -O https://raw.githubusercontent.com/lightbend/console-charts/master/enterprise-suite/scripts/lbc.py```
- ```chmod u+x lbc.py```
- ```./lbc.py install --namespace=lightbend --version=1.1.1 --set exposeServices=NodePort```
    - if the above command fails, you can delete the console with ```./lbc.py uninstall``` and try the above command again
- it can take a while till the pods are available, you can check there status with ```./lbc.py verify --namespace=lightbend```
- verify if everything is working: ./lbc.py verify --namespace=lightbend
- open the console ```minikube service expose-es-console --namespace lightbend```

## Application Configuration
- the sbt version in ```project/build.properties``` has to be 0.13.7 or higher
- Check the files ```project/plugins.sbt``` and ```build.sbt```. In this files you can find the configuration in the ```// LIGHTBEND TELEMETRY``` sections

## ToDo
- Add all actors to config: https://developer.lightbend.com/docs/telemetry/current/instrumentations/akka/actors.html
    - See also for sharded actors
- Configure dispatchers if necessary: https://developer.lightbend.com/docs/telemetry/current/instrumentations/akka/dispatchers.html

## Problems
- Prometheus doesn't collect data -> Add scrape_config to prometheus

Sources:
- https://developer.lightbend.com/docs/console/current/installation/index.html
- https://developer.lightbend.com/docs/telemetry/current/home.html
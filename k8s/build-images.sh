#!/usr/bin/env bash

cd ..
#first start minikube (minikube start)
eval $(minikube docker-env)
echo "Service 1 and Service 2 will be build!"

sbt ";project service1; docker:publishLocal" &
sbt ";project service2; docker:publishLocal" &
wait

echo "finished!"


cd k8s
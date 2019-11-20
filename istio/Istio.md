# Istio
Manual for configuration of Istio for this Akka example

Requirements:
- minikube with 4 cpus and 16gb of ram

# Istio installation
- Download istio ```curl -L https://git.io/getLatestIstio | ISTIO_VERSION=1.2.4 sh -```
- Switch to istio folder ```cd istio-1.2.4/```
- Add the istioctl client to your PATH environment variable ```export PATH=$PWD/bin:$PATH```
- Install all istio crds ```for i in install/kubernetes/helm/istio-init/files/crd*yaml; do kubectl apply -f $i; done```
- Install demo profile with permissive mutual TLS mode ```kubectl apply -f install/kubernetes/istio-demo.yaml```
- Verify installation with ```kubectl get svc -n istio-system```
    - All services except the jaeger-agent need a cluster-ip
- Check if all pods except three(istio-security-post-install, istio-grafana-post-install, istio-cleanup-secrets) are in running state```kubectl get pods -n istio-system```

# Setup demo application
- ```kubectl create -f innfactory-demo-ns.yaml```

# Dashboards
- Show kiali dashboard ```kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=kiali -o jsonpath='{.items[0].metadata.name}') 20001:20001```
    - Navigate to ```http://localhost:20001```
    - Default credentials are: admin/admin
- Show grafana dashboard ```kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=grafana -o jsonpath='{.items[0].metadata.name}') 3000:3000```
    - Navigate to ```http://localhost:3000```

# Sources
- https://istio.io/docs/setup/kubernetes/platform-setup/minikube/
- https://istio.io/docs/setup/kubernetes/#downloading-the-release
- https://istio.io/docs/setup/kubernetes/install/kubernetes/
- https://istio.io/docs/setup/kubernetes/additional-setup/requirements/
- https://doc.akka.io/docs/akka-management/current/bootstrap/istio.html
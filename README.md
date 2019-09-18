Istio Sample (Work in Progress)
==================================================

This sample code helps get you started with Istio Service Mesh

Related technologies: 

* Kotlin: https://kotlinlang.org/docs/reference/
* Spring Boot: https://spring.io/projects/spring-boot#overview
* Docker: https://www.docker.com/resources/what-container
* Kubernetes: https://kubernetes.io/docs/concepts/overview/what-is-kubernetes/
* Minikube: https://kubernetes.io/docs/setup/learning-environment/minikube/
* Istio: https://istio.io/docs/concepts/what-is-istio/
* MetalLB: https://metallb.universe.tf/

What's Here
-----------

This sample includes:

* README.md - this file
* pom.xml - this file is the Maven Project Object Model for the web service
* demo1 - this directory contains your Kotlin Service source files

Getting Started
---------------

To work on the sample code, you'll need to clone project's repository to your
local computer. If you haven't, do that first.

1. Install Docker. See https://docs.docker.com/install/

2. Install Minikube. See https://kubernetes.io/docs/tasks/tools/install-minikube/

3. Install Istio. See https://istio.io/docs/setup/#downloading-the-release

4. Minikube comes with it's own Docker daemon, so you don't have to use Docker Desktop. You only need the Docker CLI and to point it to Minikube: 

        $ eval $(minikube docker-env)
        
5. Create the cluster:

        $ minikube config set cpus 4 ; minikube config set memory 8192 ; minikube config set disk-size 50g ; minikube addons enable ingress ; minikube start
        
6. Into your Istio source folder, run the following command to apply the Custom Resource Definitions (CRDs): 

        $ for i in install/kubernetes/helm/istio-init/files/crd*yaml; do kubectl apply -f $i; done

6.1. If you have the folling error: 
        
        error: SchemaError(io.k8s.api.networking.v1beta1.IngressList): invalid object doesn't have additional properties

6.2. Run the following command to relink kubectl:

        rm /usr/local/bin/kubectl ; brew link --overwrite kubernetes-cli

6.4. Then run again: 

        $ for i in install/kubernetes/helm/istio-init/files/crd*yaml; do kubectl apply -f $i; done
      
7. Into your Istio source folder, run the following command to apply Istio in your cluster:

        $ kubectl apply -f install/kubernetes/istio-demo.yaml

7.1. To follow the Istio health in your cluster, run the following command: 

        $ kubectl get pod -n istio-system

7.2. When **all** items from STATUS column are Completed or Running, run the following command to enable the [Sidecar](https://blog.davemdavis.net/2018/03/13/the-sidecar-pattern/) injection:

        $ kubectl label namespace default istio-injection=enabled

8. To install the Load Balancer MetalLB, run the following command: 

        $ kubectl apply -f https://raw.githubusercontent.com/google/metallb/v0.7.3/manifests/metallb.yaml
        
8.1. To check the MetalLB health check, run the following command:

        $ kubectl get service -n istio-system istio-ingressgateway

8.2. When colunm EXTERNAL-IP has a IP address, MetalLB is running.

9. Run the following command to get the Minikube ip: 

        $ minikube ip

9.1. Copy the result from the previous step and navigate to istio-sample (this project) folder: 

        $ cd istio-sample/demo1/kube

10. Paste the copied IP in last section (addresses) of deployment.yaml file, example:

``` 
    # External Load Balancer
    apiVersion: v1
    kind: ConfigMap
    metadata:
      namespace: metallb-system
      name: config
    data:
      config: |
        address-pools:
        - name: custom-ip-space
          protocol: layer2
          addresses:
          - MINIKUBE IP HERE/28
```

11. Still in istio-sample folder, run the following command to deploy your Spring Boot app into your cluster:

        $ kubectl apply -f deployment.yaml

12. Run the following command to check the Spring Boot health in your browser:

        $ minikube dashboard

13. When all the Workloads Statuses section in Kubernetes are green, get the result of EXTERNAL-IP (step 8.1) from MetalLB and run:

        $ curl -v YOUR-EXTERNAL-IP-HERE/welcome

13.1. Your Istio cluster is working \o/

14. To test the [Canary Release](https://martinfowler.com/bliki/CanaryRelease.html), apply the following command, from your istio-sample folder:

        $ kubectl apply -f canary.yaml

14.1. The traffic will be routed 80% to V1 and 20% to V2, you can test running the step 13 some times.

14.2 To rollout all the traffic to V2, run the following command: 

        $ kubectl apply -f rollout_v2.yaml

14.3. The traffic will be routed 100% to V2, you can test running the step 13 some times.



**For a while**, That's all Folks! :)

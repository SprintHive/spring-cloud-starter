# Spring Starter with hot-reloadable properties

Purpose of this project is to investigate the updating of Spring Boot app properties at runtime when running in
a xKubernetes container. 

## Running

Package and deploy the app to Minikube

    # Run mvn goals to package and deploy with kubernetes profile
    mvn clean package fabric8:deploy -Pkubernetes
      
    # Confirm that the pod is deployed
    kubectl get po --all-namespaces
      
    # Output should be something like:
    NAMESPACE     NAME                                   READY     STATUS    RESTARTS   AGE
    ...
    default       spring-cloud-reload-6b6879447f-g9npt   1/1       Running   0          6m
    ...

Test that you can connect to the pod by port forwarding to the pod.
    
    # Use kubectl to port forward to the pod
    kubectl port-forward spring-cloud-reload-6b6879447f-g9npt 8080:8080
      
    # To confirm that you can connect to the app browse to http://localhost:8080/ping 
    # You should see something like: 
    OK: message=default.message    
    
Create ConfigMap from config-map.yaml

    kubectl create -f config-map.yaml
      
    # Confirm that the property value changed to the new value in the ConfigMap by reloading http://localhost:8080/ping 
    # You should see something like: 
    OK: message=initial.configmap.message 

Edit ConfigMap and update property value

    kubectl edit cm cloud-reload
      
    # Confirm that the property value changed to the new value in the ConfigMap by reloading http://localhost:8080/ping 
    # You should see something like: 
    OK: message=<edited property value>
     
## Notes
    
src/main/resources/application.properties

    # You can provide default property value:
    property.message=default.message
      
    # Must let spring cloud kubernetes know to enable property reloading.
    # Default looks to be false as property isn't updated when not explicitly set. 
    spring.cloud.kubernetes.reload.enabled=true
    
Dependencies

    org.springframework.boot:spring-boot-actuator
    # It's role is unknown at the moment. If removed, property is not updated when ConfigMap is edited.
      
    org.springframework.cloud:spring-cloud-starter-kubernetes-config
    # Assumption is that this is what provides the mechanism for property reloading from ConfigMap.
    # Trigger?
      
    io.fabric8:fabric8-maven-plugin
    # Fabric8 plugin used to build and deploy to Minikube.
    # Is this plugin doing some setup magic for spring-cloud-starter-kubernetes-config?
    # Currently used in a zero-config way where goals are added to the kubernetes profile in the pom that creates
      a service and deployment. Can be setup to use provided descriptors and docker image. 

Gradle build env
    
    # Tried a gradle version of the build with existing springstarter repo, using same dependencies. Not working.
    # Again, what is the trigger?

## Todo    

    1. Determine what's happening behind the io.fabric8:fabric8-maven-plugin to enable functionality in order to
       decouple io.fabric8:fabric8-maven-plugin
    2. Explore plugin's xml-based setup
    3. Investigate   
    2. If F8 plugin cannot be decoupled, integrate this setup into our existing maven builds, providing own docker
       images and descriptors for services and deployments
    3. Try to get this working in a gradle build env 
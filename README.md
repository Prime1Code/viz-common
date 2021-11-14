The project provides all the necessary classes to visualize & monitor the request & response streams of a Spring microservice in the `viz-server-app` in a network diagram in real time. 

To visualize a microservice stream, the following things need to be done:
1. annotate the main class with `@VizClient("<service-name>")`. The service name will be displayed in the `viz-server-app` as a node.
2. add two properties to the `application.properties`:
  - `vzsUrl`: the url of the `viz-server-app`.
  - `vzsPort`: the port of the `viz-server-app`.

If the `viz-server-app` is running, then the web app can be called in the browser via the specified url and the request & response streams can be monitored.

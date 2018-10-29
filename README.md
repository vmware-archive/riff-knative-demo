# riff-knative-demo
This is a walkthrough of the Riff on Knative (PFS) demo at the SpringOne Platform 2018 Pivotal demo booth. You can view a recording of this demo here: https://goo.gl/7m97K1

## Setup
Here is the recommended window layout for running the demo: 2 terminal windows on the left, and a browser window on the right. The top terminal window is for running commands, and the current directory should be the root of a copy of this repo. The bottom is for viewing live container logs. The browser window should open to http://s1p-weave.corby.cc, where you will see a weavescope visualization of our Kubernetes cluster. Select the "Pods" view from the top-level menu of weave. Have a second tab open to this repo, so that you can navigate to code examples as we go along. Version 0.1.2 of the riff cli should be installed on your demo workstation.

![Desktop Layout](https://raw.githubusercontent.com/Pivotal-Field-Engineering/riff-knative-demo/master/images/layout.png)

Before jumping into the code, you may want to introduce the basic concepts of Knative (build, eventing, serving), and talk about how Riff is built on this foundation. See the demo recording for an example of this talk track.

>If you are working in your own cluster, you can follow these instructions to [install Weave Scope](https://www.weave.works/docs/scope/latest/installing/#k8s)

### Container Registry and Setup References

```bash
export DOCKER_ID=<your_docker_id>
```

> for GCR use gcr.io/<project_id>

## Node.js Example

Show <b>powerof2.js</b> in the repo root directory, a simple node function which returns the square of the integer input. Make sure that the weavescope window is showing the default namespace. Execute the following command:

    riff function create node powerof2 \
      --git-repo https://github.com/Pivotal-Field-Engineering/riff-knative-demo.git \
      --artifact powerof2.js --image $DOCKER_ID/node-fun-powerof2 --verbose
    
Alternatively, you can use the shortcut script to execute the same command:

    ./scripts/node-create.sh powerof2 Pivotal-Field-Engineering/riff-knative-demo node-fun-powerof2
    
This command will use package a Docker container with Riff's node invoker and your function, store the container image in a repo, and make it available for scheduling in Knative. Immediately, you will see in the Weavescope interface that a new pod has been deployed for the powerof2 function.

Execute the function with an input value of 9 with the following riff command:

    riff service invoke powerof2 -- -w '\n' \
      -H 'Content-Type: text/plain' -d 9

Or, use this script shortcut:

    ./scripts/invoke.sh powerof2 9
    
You can try different values and see how the function handler responds. Later on, you'll notice in weavescope that the deployment automatically scales down to zero instances when there has been no traffic for a while.

## Java Example

For a more interesting function, look at the code for TextDisplay.java in the root folder. It takes a numeric input, and converts it into a textual representation of the number. The code uses Spring's `<bean>` annotation to expose the function as a bean. We have packaged the code into a Spring Boot jar which is stored in the Github repo.

We can deploy the function with the following command:

    riff function create java textdisplay \
      --local-path textdisplay \
      --image $DOCKER_ID/textdisplay \
      --verbose

Or use the shortcut script:

    ./scripts/java-create.sh textdisplay textdisplay

Now, use the shortcut script to run the function with different numeric inputs:

    ./scripts/invoke.sh textdisplay 4298
    
## Chaining Functions

So far, we have demonstrated request-response on a single function. Now, we will use Riff channels to build a chain of polyglot functions. We will add a new function called Generator that creates a stream of randomly generated numbers (between 0 and 1000), and then runs them through our previously generated functions through named channels:

![Chaining](https://raw.githubusercontent.com/Pivotal-Field-Engineering/riff-knative-demo/master/images/channels.png)

The generator function outputs random numbers onto the numbers channel, where our powerof2 function picks it ups and puts its results into the squares channel, which our textdisplay function uses as inputs.

Create the channels with the following commands:

    riff channel create numbers --cluster-bus stub
    riff channel create squares --cluster-bus stub
    
Deploy the generator function to stream the random numbers:

    riff service create generator --image jldec/random:v0.0.2
    
Now, wire up the connections between your functions and channels:

    riff subscription create --subscriber powerof2 --channel numbers --reply-to squares
    riff subscription create --subscriber textdisplay --channel squares
    
It's time to start the stream. Use the following script to tell generator to emit a slow stream (1/s) of numbers onto the channel:

    ./scripts/slowstream.sh
    
In weave, you will see instances of generator, then powerof2, then textdisplay spin up as traffic progresses through the pipeline.
    
In the bottom terminal window for container logs, use the following script to see what the textdisplay function is doing at the end of the pipeline:

    ./scripts/logs.sh textdisplay
    
## Scale for Increased Load

Now, we'll increase the load from 1 message/sec to 200 messages/sec.

    ./scripts/faststream.sh
    
On weave you will see a lot of new pods spin up to increase concurrent processing of messages and maintain throughput through the channels. 

![Scaling](https://raw.githubusercontent.com/Pivotal-Field-Engineering/riff-knative-demo/master/images/scale.png)

PFS can scale up in seconds on-demand, and scale to zero when there are no outstanding workloads. This enables extremely efficient usage of cloud infrastructure resources.

<h3>Cleanup</h3>

At the end of the demo, use the following script to delete the services and channels we created, and reset for the next demo run:

    ./scripts/cleanup.sh
    

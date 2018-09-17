# riff-knative-demo
This is a walkthrough of the Riff on Knative (PFS) demo at the SpringOne Platform 2018 Pivotal demo booth. You can view a recording of this demo here: [insert link] 

<h3>Setup</h3>
Here is the recommended window layout for running the demo: 2 terminal windows on the left, and a browser window on the right. The top terminal window is for running commands, and the current directory should be the root of a copy of this repo. The bottom is for viewing live container logs. The browser window should open to http://s1p-weave.corby.cc, where you will see a weavescope visualization of our Kubernetes cluster. Select the "Pods" view from the top-level menu of weave. Have a second tab open to this repo, so that you can navigate to code examples as we go along. Version 0.1.2 of the riff cli should be installed on your demo workstation.

![Desktop Layout](https://raw.githubusercontent.com/cpage-pivotal/riff-knative-demo/master/images/layout.png)

Before jumping into the code, you may want to introduce the basic concepts of Knative (build, eventing, serving), and talk about how Riff is built on this foundation. See the demo recording for an example of this talk track.

<h3>Node.js Example</h3>

Show <b>powerof2.js</b> in the repo root directory, a simple node function which returns the square of the integer input. Make sure that the weavescope window is showing the default namespace. Execute the following command:

    riff function create node powerof2 \
      --git-repo https://github.com/Pivotal-Field-Engineering/riff-knative-demo.git \
      --artifact powerof2.js --image cepage/node-fun-powerof2
    
Alternatively, you can use the shortcut script to execute the same command:

    ./scripts/node-create.sh powerof2 Pivotal-Field-Engineering/riff-knative-demo node-fun-powerof2
    
This command will use package a Docker container with Riff's node invoker and your function, store the container image in a repo, and make it available for scheduling in Knative. Immediately, you will see in the Weavescope interface that a new pod has been deployed for the powerof2 function.

Execute the function with an input value of 9 with the following riff command:

    riff service invoke powerof2 -- -w '\n' \
      -H 'Content-Type: text/plain' -d 9

Or, use this script shortcut:

    ./scripts/invoke.sh powerof2 9
    
You can try different values and see how the function handler responds.

<h3>Java Example</h3>

# riff-knative-demo
This is a walkthrough of the Riff on Knative (PFS) demo at the SpringOne Platform 2018 Pivotal demo booth. You can view a recording of this demo here: [insert link] 

<h3>Setup</h3>
Here is the recommended window layout for running the demo: 2 terminal windows on the left, and a browser window on the right. The top terminal window is for running commands, and the current directory should be the root of a copy of this repo. The bottom is for viewing live container logs. The browser window should open to http://s1p-weave.corby.cc, where you will see a weavescope visualization of our Kubernetes cluster. Select the "Pods" view from the top-level menu of weave. Have a second tab open to this repo, so that you can navigate to code examples as we go along.

![Desktop Layout](https://raw.githubusercontent.com/cpage-pivotal/riff-knative-demo/master/images/layout.png)

Before jumping into the code, you may want to introduce the basic concepts of Knative (build, eventing, serving), and talk about how Riff is built on this foundation. See the demo recording for an example of this talk track.

<h3>Node.js Example</h3>

Show <b>powerof2.js</b> in the repo root directory, a simple node function which returns the square of the integer input.


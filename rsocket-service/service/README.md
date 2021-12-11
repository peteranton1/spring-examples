Getting Started
----

# Overview

A simple service using rsockets. 

# Running rsc

To run with [rsc](https://github.com/making/rsc)

```aidl
    rsc tcp://localhost:8888 \
    --stream --route greetings \
    --log --debug -d \
    "{ \"name\" : \"Josh\" }"
```


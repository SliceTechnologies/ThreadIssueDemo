# Slice interview test project (threadpool)

## Intro

Test application that needs to be extended

#### Example

* Start application in default mode (and receive error)
```
$ gradle executeDefault
```

* Start application in experimental mode: 
```

$ gradle executeExperiment
```

## Problem definition

Current implementation EventsProcessorDefault has a vulnerability - if a lot of events with the same lock key come at once 
all threads got blocked and further tasks are rejected.

## Task

Write own implementation EventsProcessorExperimental that solves issue with blocked threads so that application finishes without error.
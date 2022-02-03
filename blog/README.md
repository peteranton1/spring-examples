# Blog
------

## Overview

Creates a simple react-node app that demonstrates using the various front end technologies. 

## Setup

Perform the following steps. 

Go to folder where project will be and from command line issue:

    mkdir blog
    cd blog

### Create Client

    npx create-react-app client

(if get asked if you want to install create-react-app then say yes)

after process completes, issue:

    cd client
    npm start

This should start the client and fire up the browser, going to : 

    http://localhost:3000/

If you see this then this client is all working. We haven't implemented anything in it yet. 

http://localhost:3000/

To build the app use the following

    npm run build
    npm install -g serve
    serve -s build


### Create posts, comments

Go to folder where blog project is and from command line issue:

    cd ..
    mkdir posts
    cd posts

    npm init -y 
    npm install express cors axios nodemon

    cd ..
    mkdir posts
    cd posts

    npm init -y 
    npm install express cors axios nodemon


## React app

### Suppress export warning

You can suppress the warning by refactoring from this:

    import React from "react";
    
    export default () => {
    return <div>Blog app</div>;
    };

to this:

    import React from "react";
    
    const App = () => {
    return <div>Blog app</div>;
    };
    
    export default App;

### fix CORS error

When calls are made from one app to another which is on a different path or port, 
there is a CORS error. 

To fix this, the server that receives the request must do the following:

1. Stop the service. 
2. npm install cors
3. npm start


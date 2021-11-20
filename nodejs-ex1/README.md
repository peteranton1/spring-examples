Technical Notes
----

## Installation

Install nvm - Node Version Manager:

     curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

Ensure your .bashrc (or equivelent) has been updated to add NVM command

    nvm -v
    0.39.0

List versions of node available:

    nvm ls-remote

Install version of node you want, e.g.

    nvm install v16.13.0

Check node is installed:

    node -v
    v16.13.0

    npm -v
    8.1.0


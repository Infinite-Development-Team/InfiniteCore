# InfiniteCore

InfiniteCore, a fork of [Redisbungee](https://github.com/ProxioDev/RedisBungee).  

InfiniteCore is used to link proxies together via Redis. InfiniteCore also has some core commands for the Infinite Network.

Our Minecraft server: mc.playinfinite.net

## Notice: about older versions of redis than redis 6.0

As of now Version 0.6.4 is only supporting redis 6.0 and above!

## Compiling

InfiniteCore is distributed as a [maven](https://maven.apache.org) project. 

To compile and installing to in your local Maven repository:

    git clone https://github.com/SeanMurillo/InfiniteCore.git .
    mvn clean install
    mvn package

You will recieve a jar file under the folder target after compiling it.

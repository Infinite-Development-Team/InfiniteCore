# InfiniteCore

InfiniteCore, a fork of Redisbungee [Redisbungee](https://github.com/ProxioDev/RedisBungee).  

InfiniteCore is used to link proxies together via Redis. InfiniteCore also has some core commands for the server.

## Notice: about older versions of redis than redis 6.0

As of now Version 0.6.4 is only supporting redis 6.0 and above!

## Compiling

InfiniteCore is distributed as a [maven](https://maven.apache.org) project. 

To compile and installing to in your local Maven repository:

    git clone https://github.com/SeanMurillo/InfiniteCore.git .
    mvn clean install
    mvn package


And use it in your pom file.

    <dependency>
      <groupId>com.imaginarycode.minecraft</groupId>
      <artifactId>RedisBungee</artifactId>
      <version>0.6.5-SNAPSHOT</version>
      <scope>provided</scope>
    </dependency>

Or if you want to use the jitpack maven server

    <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
    
And use it in your pom file.
    
    <dependency>
	    <groupId>com.github.limework</groupId>
	    <artifactId>redisbungee</artifactId>
	    <version>0.6.5</version>
        <scope>provided</scope>
	</dependency>



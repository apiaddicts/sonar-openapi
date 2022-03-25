Running the integration tests
=============================

The integration tests downloads and launches a local Sonar 6.7 server configured with the plugin. **An Internet access 
is required to run them**.

Running from Maven
------------------

Run Maven in the `its` directory. This will launch all the tests. Make sure that you have built the plugin before so
that it can be installed

```
mvn install
```

*Important note*: if you are running the tests behind a proxy, you need to provide the following variables to Maven via
the command line:

* `http.proxyHost`: the FQDN of the proxy (without http://)
* `http.proxyPort`
* `http.proxyUser` (if your proxy is authenticating)
* `http.proxyPassword` (if your proxy is authenticating)
* `https.proxyHost`
* `https.proxyPort`
* `https.proxyUser` (if your proxy is authenticating)
* `https.proxyPassword` (if your proxy is authenticating)

```
mvn install -Dhttp.proxyHost=my.proxy.com -Dhttp.proxyPort=80 -Dhttp.proxyUser=mylogin http.proxyPassword=MyP@ssw0rd \
            -Dhttps.proxyHost=my.proxy.com -Dhttps.proxyPort=80 -Dhttps.proxyUser=mylogin https.proxyPassword=MyP@ssw0rd
```

You don't need to encode the proxy password. 

Running from your editor
------------------------

Launch the `Tests` class. Don't forget to provide the proxy configuration if you're running behind a proxy.

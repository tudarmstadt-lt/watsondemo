Watsondemo
======================

Sample application that shows the usage and capabilities of the Java wrapper  [JWatson](https://github.com/tudarmstadt-lt/jwatson) for the IBM Watson DQA service. The application is written in Java with the Play framework.

Usage
-----------

* Add user credentials to your Watson instance in `/conf/application.conf`
* Execute `sbt run`
* Visit `http://localhost:9000/`

The application is configured to use an in-memory database for storage. If you restart your application, your stored information will be lost. You can replace the database in the `application.conf` file. For more information refer to [Play Fraemwork Documentation](https://www.playframework.com/documentation/2.0.4/ScalaDatabase).

License
-------

```
Copyright 2016 Technische Universität Darmstadt.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

See also
--------
[JWatson](https://github.com/tudarmstadt-lt/jwatson)

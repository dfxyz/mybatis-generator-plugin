mybatis-generator-plugin
========================
The default behavior of MBG really sucks, so I made a plugin to improve it slightly. Currently only supports MySQL. Not working for constructor based models.

See my blog for more details:

[http://dfxyz.space/post/580b59fe1a6cfc30d601ab4e/](http://dfxyz.space/post/580b59fe1a6cfc30d601ab4e/)

## What does this plugin do
To put it simply, this plugin:

* for the generated models, make all fields public and removes evil getters and setters
* add limit/offset related fields and methods into the Example classes to implement pagination for `selectByExample()` method
* add new methods implementing MySQL's `insert ... on duplicate key update`
* add new select methods allowing you choose which columns to select
* add new update methods allowing you to update like `set field = field + 1` or `set str = upper(str)`

## Usage
Add my repository into your pom.xml and add it as MGB's dependency:

```xml
<pluginRepositories>
    <pluginRepository>
        <id>mybatis-generator-limit-plugin-mvn-repo</id>
        <url>https://raw.github.com/dfxyz/mybatis-generator-plugin/mvn-repo/</url>
    </pluginRepository>
</pluginRepositories>
...
<build>
    <plugins>
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.3.5</version>
            <dependencies>
                ...
                <dependency>
                    <groupId>space.dfxyz</groupId>
                    <artifactId>mybatis-generator-plugin</artifactId>
                    <version>1.0</version>
                </dependency>
            </dependencies>
            ...
        </plugin>
        ...
    </plugins>
</build>
```

Then add it into your generatorConfig.xml:

```xml
<generatorConfiguration>
    <context id="default" targetRuntime="MyBatis3">
        <plugin type="space.dfxyz.mybatis.generator.Plugin"></plugin>
        ...
    </context>
</generatorConfiguration>
```

## Using the generated object

To select with limit:

```java
// select ... limit 10, 20
XExample example = new XExample();
...
example.setLimit(20);
example.setOffset(10);
List<X> results = mapper.selectByExample(example);
```

To insert a model if it not exists or update it with raw clause:

```java
// insert ... on duplicate key update c = c + 1;
X model = new Model();
model.a = 1;
model.b = 2;
model.c = 3;
mapper.insertOrUpdateManually(model, "c = c + 1"); // or insertSelectiveOrUpdateManually()
```

If the model has a id property, it will be always set correctly after insertion or updating.

To select certain column(s) only:

```java
// select a, b ...
List<X> results = mapper.selectManuallyByExample("a, b", example);
// or
X result = mapper.selectManuallyByPrimaryKey("a, b", key);
assert result.c == null;
assert result.d == null;
```

To update with raw clause:

```java
mapper.updateManuallyByExample("int_value = int_value + 1, str_value = upper(str_value)", example);
// or
mapper.updateManuallyByPrimaryKey("int_value = int_value + 1, str_value = upper(str_value)", key);
```

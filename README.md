# MMPT: Manni's MyBatis PostgreSQL Types

## This is abandonware

In particular, I don't know how much longer the bintray download will work.

Feel free to fork this repo, though!

## Summary

Note: This README is for the release version of MMPT.
As development continues on master, follow README-master.md.
When releases are made, part of the process will be copying README-master.md
to README.md.

If you are using MyBatis with PostgreSQL, here are some
PostgreSQL-specific type mappers you can use.

As we all know, PostgreSQL has a rich, extensible type system.

MyBatis, much to its credit, can have new type handlers plugged
into it.

As this library grows, it will provide type handlers for
many PostgreSQL types, especially PostgreSQL's array types.

## Installation

For Gradle users, your `repositories` section will have
the Manni Wood repository added to it:

```Groovy
repositories {
    mavenCentral()
    maven {
        name = 'Manni Wood'
        url = 'http://dl.bintray.com/manniwood/maven'
    }
}
```
and then your dependencies section will have this entry for the MMPT library:
```Groovy
dependencies {
    compile 'com.manniwood:mmpt:2.1.0'
    // other dependencies here, obviously
}
```

## Use

### Configuration

In your main MyBatis configuration file, use type aliases to make your life easier.

Mybatis comes with a number of built-in type aliases that are documented 
[on their web site](http://mybatis.github.io/mybatis-3/configuration.html#typeAliases).

In addition to those type aliases, here are some other ones you might want to
put in your main configuration file:

```XML
<typeAliases>
    <typeAlias alias="UUID"            type="java.util.UUID" />
    <typeAlias alias="String[]"        type="[Ljava.lang.String;" />
    <!-- ... other type aliases ... -->
```
Also, the type handlers in MMPT have the usual long Java names, so giving those
type aliases will also be convenient when you refer to them later:

```XML
    <!-- ... other type aliases ... -->
    <typeAlias alias="UUIDTypeHandler"           type="com.manniwood.mmpt.typehandlers.UUIDTypeHandler" />
    <typeAlias alias="IntegerArrayTypeHandler"   type="com.manniwood.mmpt.typehandlers.IntegerArrayTypeHandler" />
    <typeAlias alias="BigIntArrayTypeHandler"    type="com.manniwood.mmpt.typehandlers.BigIntArrayTypeHandler" />
    <typeAlias alias="SmallIntArrayTypeHandler"  type="com.manniwood.mmpt.typehandlers.SmallIntArrayTypeHandler" />
    <typeAlias alias="TextArrayTypeHandler"      type="com.manniwood.mmpt.typehandlers.TextArrayTypeHandler" />
</typeAliases>
```
You also have to let MyBatis know about all of our new type hanlders.

In the typeHandlers section of your MyBatis configuration file,
put the following line:

```XML
<typeHandlers>
    <package name="com.manniwood.mmpt.typehandlers"/>
 </typeHandlers>
```

### UUID Example

If you are using PostgreSQL's 
[UUID type](http://www.postgresql.org/docs/9.3/static/datatype-uuid.html),
MMPT's UUID type handler will come in handy.

Let's say you have a table with this definition:

```SQL
create temporary table uuid_beans (
     test_id  uuid  not null,
     name     text  not null)
```

Let's also say that you have a Java bean with attributes (including a UUID attribute)
matching the columns of your `uuid_beans` table

```Java
package com.manniwood.mmpt.test.beans;

import java.util.UUID;

public class UUIDBean {

    private UUID testId;
    private String name;

    public UUIDBean() {
        // null constructor keeps mybatis happy
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

Here's how you would mark up your SQL mapper file to insert UUIDBeans
into the `uuid_beans` table:

```XML
  <insert id="insertUUID" parameterType="UUIDBean">
    insert into uuid_beans (
            test_id, 
            name) 
    values (#{testId,javaType=UUID,jdbcType=OTHER,typeHandler=UUIDTypeHandler}, 
            #{name})
  </insert>
```
Note how the `test_id` column, which is of type UUID, needs more detail than the `name` column,
which MyBatis already has a built-in mapper for. In particular, note an unfortunate feature
of the underlying JDBC standard that maps the UUID type (and all other esoteric types) to the JDBC 
type OTHER, rather than a specific JDBC type. 

The above MyBatis insert can be used like so:

```Java
UUIDBean t = new UUIDBean();
t.setTestId(UUID.fromString("37a82ee2-114c-4044-ba90-c073bf6d7830"));
t.setName("regular test");
session.insert("test.insertUUID", t);
```

Unlike the insert statement, which was able to use
inline type mapper information, the select needs an explicit result map to
tell MyBatis which type handler to use for the returned `test_id` column.

```XML
  <resultMap id="selectUUIDResultMap" type="UUIDBean">
    <id     property="testId"  column="test_id"  typeHandler="UUIDTypeHandler" />
    <result property="name"    column="name"     />
  </resultMap>

  <select id="selectUUID"
          parameterType="String"
          resultMap="selectUUIDResultMap">
    select test_id,
           name
      from uuid_test
     where name = #{string}
  </select>
```

The above MyBatis select can be used like so:

```Java
UUIDBean result = session.selectOne("test.selectUUID", "regular test");

```

### String Array Example

Let's say you have the following table definition:

```SQL
create temporary table string_array_beans (
    text_array  text[]      null,
    name        text        not null)
```

and the following Java bean:

```Java
package com.manniwood.mmpt.test.beans;

public class StringArrayBean {

    private String[] stringArray;
    private String name;

    public StringArrayBean() {
        // null constructor keeps mybatis happy
    }

    public String[] getStringArray() {
        return stringArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

Then to do an insert into this table, you would use the following mapping:

```XML
  <insert id="insertTextArray" parameterType="StringArrayBean">
    insert into string_array_beans (
        text_array, 
        name) 
    values (
        #{stringArray,javaType=String[],jdbcType=OTHER,typeHandler=TextArrayTypeHandler}, 
        #{name})
  </insert>
```

Note how we use the type alias `String[]` that we definied up above, instead of having to
use the uglier, longer `[Ljava.lang.String;`. (Note, too, how the `jdbcType` is `OTHER`. As usual,
for non-standard JDBC SQL types, the type is always `OTHER`.)

To do a select from this table, the mapping would look like this:

```XML
  <resultMap id="selectTextArrayResultMap" type="StringArrayBean">
    <id     property="stringArray"   column="text_array"  typeHandler="TextArrayTypeHandler" />
    <result property="name"          column="name"     />
  </resultMap>

  <select id="selectTextArray"
          parameterType="String"
          resultMap="selectTextArrayResultMap">
    select text_array,
           name
      from string_array_beans
     where name = #{string}
  </select>
```

The full list of mappings available in MMPT are:

PostgreSQL Type |Java Type            |MyBatis Built-In Alias|Suggested Alias|JDBC Type|MMPT Type Handler (in com.manniwood.mmpt.typehandlers.)
----------------|---------------------|----------------------|---------------|---------|-----------------
uuid            |java.util.UUID       |                      |UUID           |OTHER    |UUIDTypeHandler
json            |java.lang.String     |string                |               |OTHER    |JSONTypeHandler
smallint[]      |[Ljava.lang.Integer; |int[]                 |               |OTHER    |SmallIntArrayTypeHandler
integer[]       |[Ljava.lang.Integer; |int[]                 |               |OTHER    |IntegerArrayTypeHandler
bigint[]        |[Ljava.lang.Long;    |long[]                |               |OTHER    |BigIntArrayTypeHandler
text[]          |[Ljava.lang.String;  |                      |String[]       |OTHER    |TextArrayTypeHandler
boolean[]       |[Ljava.lang.Boolean; |boolean[]             |               |OTHER    |BooleanArrayTypeHandler
uuid[]          |[Ljava.lang.UUID;    |                      |UUID[]         |OTHER    |UUIDArrayTypeHandler

Note that it might have been more ideal (not to mention more expected!) to have the SmallIntArrayTypeHandler
take/receive an array of Short rather than an array of Integer. However, the underlying PostgreSQL JDBC
driver actually returns an array of Integer, not an array of Short! So, this concession had to be made
in the type handler.


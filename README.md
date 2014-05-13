# Manni's MyBatis PostgreSQL Types

If you are using MyBatis with PostgreSQL, here are some
PostgreSQL-specific type mappers you can use.

As we all know, PostgreSQL has a rich, extensible type system.

MyBatis, much to its credit, can have new type handlers plugged
into it.

As of this writing (1.0-SNAPSHOT version), MMPT has a UUID
type handler, and an IntegerArray type handler.

PostgreSQL allows storing any type as an array, so as I grow
this library, I will be adding type handlers for all of the
array types.

## Installation

For now, I do not have a Maven repo for MMPT, nor do I have
a downloadable jar file yet. When I release version 1.0,
those things will be in place.

## Use

Put the MMPT jar in your classpath. (I know, I haven't made
a jar available yet on github, but I will soon.)

In the typeAliases section of your MyBatis configuration file,
put the following type aliases (this part is optional, but
it will save you typing later:

```
<typeAliases>
    <typeAlias alias="UUIDTypeHandler" type="com.manniwood.mmpt.typehandlers.UUIDTypeHandler" />
    <typeAlias alias="IntegerArrayTypeHandler" type="com.manniwood.mmpt.typehandlers.IntegerArrayTypeHandler" />
</typeAliases>
```

In the typeHandlers section of your MyBatis configuration file,
put the following line:

```
<typeHandlers>
    <package name="com.manniwood.mmpt.typehandlers"/>
 </typeHandlers>
```

Let's say you have these beans that you would like mapped:

A bean with a UUID attribute:

```
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

A bean with an integer array attribute:

```
package com.manniwood.mmpt.test.beans;

public class IntegerArrayBean {

    private Integer[] integerArray;
    private String name;

    public IntegerArrayBean() {
        // null constructor keeps mybatis happy
    }

    public Integer[] getIntegerArray() {
        return integerArray;
    }

    public void setIntegerArray(Integer[] integerArray) {
        this.integerArray = integerArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

You can set up these aliases in your MyBatis config file:

```
  <typeAliases>
    <typeAlias alias="UUIDBean"        type="com.manniwood.mmpt.test.beans.UUIDBean" />
    <typeAlias alias="UUID"            type="java.util.UUID" />
    <typeAlias alias="UUIDTypeHandler" type="com.manniwood.mmpt.typehandlers.UUIDTypeHandler" />

    <typeAlias alias="IntegerArrayBean"        type="com.manniwood.mmpt.test.beans.IntegerArrayBean" />
    <typeAlias alias="IntegerArrayTypeHandler" type="com.manniwood.mmpt.typehandlers.IntegerArrayTypeHandler" />
  </typeAliases>
```

In your mappers file, you can do things like this:

```
  <insert id="createUUIDTestTable">
    create temporary table uuid_test (
         test_id  uuid      null,
         name     text  not null)
  </insert>

  <insert id="insertUUID" parameterType="UUIDBean">
    insert into uuid_test (test_id, name) values (#{testId,javaType=UUID,jdbcType=OTHER,typeHandler=UUIDTypeHandler}, #{name})
  </insert>

  <resultMap id="selectUUIDResultMap" type="UUIDBean">
    <id     property="testId"  column="test_id"  typeHandler="UUIDTypeHandler" />
    <result property="name"    column="name"     />
  </resultMap>

  <select id="selectUUID"
          parameterType="UUIDBean"
          resultMap="selectUUIDResultMap">
    select test_id,
           name
      from uuid_test
     where test_id = #{testId,javaType=UUID,jdbcType=OTHER,typeHandler=UUIDTypeHandler}
       and name = #{name}
  </select>

  <select id="selectNullUUID"
          parameterType="UUIDBean"
          resultMap="selectUUIDResultMap">
    select test_id,
           name
      from uuid_test
     where name = #{name}
  </select>

  <insert id="createIntArrayTestTable">
    create temporary table int_array_test (
        int_array  integer[]      null,
        name       text       not null)
  </insert>

  <insert id="insertIntArray" parameterType="IntegerArrayBean">
    insert into int_array_test (int_array, name) values (#{integerArray,javaType=int[],jdbcType=OTHER,typeHandler=IntegerArrayTypeHandler}, #{name})
  </insert>

  <resultMap id="selectIntArrayResultMap" type="IntegerArrayBean">
    <id     property="integerArray"  column="int_array"  typeHandler="IntegerArrayTypeHandler" />
    <result property="name"          column="name"     />
  </resultMap>

  <select id="selectIntArray"
          parameterType="IntegerArrayBean"
          resultMap="selectIntArrayResultMap">
    select int_array,
           name
      from int_array_test
     where int_array = #{integerArray,javaType=int[],jdbcType=OTHER,typeHandler=IntegerArrayTypeHandler}
       and name = #{name}
  </select>

  <select id="selectNullIntArray"
          parameterType="IntegerArrayBean"
          resultMap="selectIntArrayResultMap">
    select int_array,
           name
      from int_array_test
     where name = #{name}
  </select>
```

Yes, it's a little wordy, but it works great.


